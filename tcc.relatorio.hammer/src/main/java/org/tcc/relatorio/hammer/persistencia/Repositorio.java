package org.tcc.relatorio.hammer.persistencia;
import org.tcc.relatorio.hammer.persistencia.exception.DaoException;
import org.tcc.relatorio.hammer.persistencia.dominio.DomainObject;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.persistence.EntityManager;
import javax.persistence.Parameter;
import javax.persistence.TypedQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * @author wdi_s
 */
public abstract class Repositorio implements IDao {
    private static final Logger log = LoggerFactory.getLogger(Repositorio.class);
    private int count = 0;
    protected static final int FLUSH_COUNT = 5000;
    protected static final int CLEAN_COUNT = 50000;

    public abstract EntityManager entityManager();

    public Repositorio() {
        log.debug("__¢ DaoJpa");
    }

    public <T extends DomainObject<?>> T persist(T entity) throws DaoException {
        try {
            if(++this.count % FLUSH_COUNT == 0) {
                this.entityManager().flush();
                if(this.count % CLEAN_COUNT == 0) {
                    this.entityManager().clear();
                }
            }

            this.entityManager().persist(entity);
            return entity;
        } catch (Exception var3) {
            log.error("Error: {}", entity, var3);
            throw new DaoException(var3);
        }
    }

    public <T extends DomainObject<?>> void delete(T entity) throws DaoException {
        try {
            this.entityManager().remove(entity);
        } catch (Exception var3) {
            log.error("Error: {}", entity, var3);
            throw new DaoException(var3);
        }
    }

    public <T extends DomainObject<?>> T update(T entity) throws DaoException {
        try {
            return (T)this.entityManager().merge(entity);
        } catch (Exception var3) {
            log.error("Error: {}", entity, var3);
            throw new DaoException(var3);
        }
    }

    public void flushAndClean() throws DaoException {
        try {
            this.entityManager().flush();
            this.entityManager().clear();
        } catch (Exception var2) {
            log.error("Error: {}", var2);
            throw new DaoException(var2);
        }
    }

    public void close() throws DaoException {
        if(this.entityManager() != null) {
            try {
                this.entityManager().close();
            } catch (Exception var2) {
                log.error("Error: {}", var2);
                throw new DaoException(var2.getMessage());
            }
        }

    }

    public <T extends DomainObject<?>> List<T> select(Class<T> clazz, String namedQuery, Map<String, Object> map) throws DaoException {
        return this.buildTypedQuery(clazz, namedQuery, map).getResultList();
    }

    public <T extends DomainObject<?>> List<T> select(Class<T> clazz, String namedQuery, Map<String, Object> map, int pageNumber, int pageSize) throws DaoException {
        return this.buildTypedQuery(clazz, namedQuery, map).setFirstResult(pageNumber * pageSize).setMaxResults(pageSize).getResultList();
    }

    public <T extends DomainObject<?>> T find(Class<T> clazz, String namedQuery, Map<String, Object> map) throws DaoException {
        return (T)this.buildTypedQuery(clazz, namedQuery, map).getSingleResult();
    }

    public <T extends DomainObject<?>> T find(Class<T> clazz, Long pk) throws DaoException {
        try {
            return (T)this.entityManager().find(clazz, pk);
        } catch (Exception var4) {
            log.error("Error: {} : {} - {}", new Object[]{clazz.getName(), pk, var4});
            throw new DaoException(var4);
        }
    }

    protected <T extends DomainObject<?>> TypedQuery<T> buildTypedQuery(Class<T> clazz, String namedQuery, Map<String, Object> map) throws DaoException {
        TypedQuery typed = this.entityManager().createNamedQuery(namedQuery, clazz);
        if(typed == null) {
            log.error("__¢ query not found: {}", namedQuery);
            throw new DaoException("query not found: " + namedQuery);
        } else {
            Set paramSet = typed.getParameters();
            if(paramSet.size() > 0 && map == null) {
                log.error("__¢ no room (map) for paramSet.size: {}", Integer.valueOf(paramSet.size()));
                throw new DaoException("no room (map) for paramSet.size: " + paramSet.size());
            } else {
                Iterator iter = paramSet.iterator();

                while(iter.hasNext()) {
                    Parameter param = (Parameter)iter.next();
                    String paramName = param.getName();
                    log.debug("___¢ paramName: {}", paramName);
                    if(!map.containsKey(paramName)) {
                        log.error("__¢ parameter {} not found in Map.", paramName);
                        throw new DaoException("parameter \'" + paramName + "\' not found in Map.");
                    }

                    Object obj = map.get(paramName);
                    if(!obj.getClass().getName().equals(param.getParameterType().getName())) {
                        log.error("__¢ paramName \'{}\' is \'{}\' but i need \'{}\'", paramName, new Object[]{obj.getClass().getName(), param.getClass().getName()});
                        throw new DaoException("paramName \'" + paramName + "\' has wrong type \'" + param.getClass().getName() + "\'.");
                    }

                    typed.setParameter(paramName, map.get(paramName));
                }

                log.debug("__¢ typedQuery: {}", typed);
                return typed;
            }
        }
    }
}
