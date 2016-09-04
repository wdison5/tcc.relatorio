package org.tcc.relatorio.persistencia;

import org.tcc.relatorio.hammer.persistencia.exception.DaoException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Repositorio extends org.tcc.relatorio.hammer.persistencia.Repositorio {

    private static final Logger log = LoggerFactory.getLogger(Repositorio.class);
    private final int nivel = 2;

    @PersistenceContext(unitName = "org.tcc.relatorio.cap.persistenciaPU")
    protected EntityManager eManager;
    
    @Override
    public EntityManager entityManager() {
        return eManager;
    }
    
    /**
     * Método para listar dados de uma entidade de acordo com os critérios dos parâmetros,
     * para compor o filtro, esses parâmetros: <b>criterioInicial</b> e <b>criterioFinal</b>,
     * devem ser uma <b>entidade</b> do mesmo tipo, e suas propriedades devem estar preenchidas
     * com os dados a serem filtrados. As condições serão criadas de acordo com as seguintes regras:
     * 1) se uma determinada propriedade dessa entidade, tanto do critério inicial quanto do
     * critério final, for igual a <i>null</i>, a condição desse campo será ignorada;
     * 2) se a informação de uma propriedade do critério inicial for exatamente igual ao mesmo 
     * parâmetro do critério final, então a condição usada na filtragem será <b>igual a</b>;
     * 3) se a informação de uma propriedade do critério inicial for igual a <i>null</i>, mas
     * a informação do critério final não, então a condição usada será <b>menor que</b> a 
     * informação do critério final;
     * 4) se a informação de uma propriedade do critério final for igual a <i>null</i>, mas
     * a informação do critério inicial não, então a condição usada será <b>maior que</b> a 
     * informação do critério inicial;
     * 5) se ambas informações forem diferente de <i>null</i>, mas não forem idênticas, então
     * a condição usada será <b>between</b>, resultando os registros que estiver nessa
     * faixa de valores;
     * 6) para o uso do critério <b>like</b>, a informação tanto do critério inicial quanto
     * do final devem ser idênticas, porém o sinal de percentual <b>%</b> deve anteceder e/ou
     * suceder a expressão.
     * @param criterioInicial entidade inicial, com as informações para compor o filtro.
     * @param criterioFinal entidade final, com as informações para compor o filtro.
     * @param orderBy ordem da lista.
     * @return retorna uma lista de entidades que se encaixam nas condições dos critérios
     * iniciais e finais.
     * @throws DaoException 
     */
    public List<Object> listar(Object criterioInicial, Object criterioFinal, String[] orderBy) throws DaoException {
        return this.listar(criterioInicial, criterioFinal, orderBy, this.nivel);
    }
    
    private List<Object> listar(Object criterioInicial, Object criterioFinal, String[] orderBy, int nivel) throws DaoException {
        try {
            CriteriaBuilder cb = entityManager().getCriteriaBuilder();
            CriteriaQuery c = cb.createQuery();
            Root entidade = c.from(criterioInicial.getClass());
            List<Predicate> criteriaListAnd = new ArrayList();
            List<Predicate> criteriaListOr  = new ArrayList();

            boolean cntx = criterioInicial.getClass().getSuperclass().getName().toUpperCase().contains("PPH");
            int i = 1;
            if (nivel == this.nivel) {
                if (!cntx) i++;
            }
                
            Field[] campos = new Field[criterioInicial.getClass().getDeclaredFields().length + i];
            campos[0] = criterioInicial.getClass().getSuperclass().getDeclaredField("id");
            if (nivel == this.nivel) {
                if (!cntx) {
                    campos[1] = criterioInicial.getClass().getSuperclass().getDeclaredField("ver");
                }
            }
            
            for (Field declaredField : criterioInicial.getClass().getDeclaredFields()) {
                campos[i++] = declaredField;
            }
            for (Field campo : campos) {
                Object retObjI = invoca(criterioInicial, campo);
                Object retObjF = invoca(criterioFinal  , campo);

                if (!campo.getDeclaredAnnotations()[0].toString().toUpperCase().contains("TRANSIENT")) {
                    if ("DATE,INT,INTEGER,BOOLEAN,STRING,LONG".contains(campo.getType().getSimpleName().toUpperCase())) {
                        if (retObjI != null || retObjF != null) {
                            List<Predicate> criteriaList = new ArrayList();
                            criteriaList.add(condicao(entidade, campo, retObjI, retObjF));
                            if ((retObjI != null || retObjF != null) &&
                                (retObjI == null || retObjF == null) &&
                                (campo.getType().getSimpleName().equals("Date"))) {
                                criteriaList.add(cb.isNull(entidade.get(campo.getName())));
                            }
                            criteriaListAnd.add(cb.or(criteriaList.toArray(new Predicate[0])));
                        }
                    } else if ("SET,LIST".contains(campo.getType().getSimpleName().toUpperCase())) {
                        //log.info("FALTA IMPLEMENTARRRRR!");
                    } else {
                        if (retObjI != null && nivel > 0 && !"PersistentBag,PersistentSet".contains(retObjI.getClass().getSimpleName())) {
                            List<Object> subSelect = new ArrayList(this.listar(retObjI, retObjF, orderBy, nivel-1));

                            List<Predicate> criteriaList = new ArrayList();
                            for (Object subSelect1 : subSelect) {
                                Object id = invoca(subSelect1, subSelect1.getClass().getSuperclass().getDeclaredField("id"));
                                criteriaList.add(condicao(entidade, campo, id, id));
                            }
                            criteriaListOr.add( cb.or(criteriaList.toArray(new Predicate[0])) );
                        }
                    }
                }
            }

            if (criteriaListOr.size() > 0){
                c.select(entidade);
                c.where( cb.and(criteriaListAnd.toArray(new Predicate[0])), cb.and(criteriaListOr .toArray(new Predicate[0])) );
            } else {
                c.select(entidade);
                c.where( cb.and(criteriaListAnd.toArray(new Predicate[0])) );
            }
            if (orderBy.length > 0) {
                List<Order> orderList = new ArrayList();
                for (String o : orderBy) { orderList.add(cb.asc(entidade.get(o))); }
                c.orderBy(orderList);
            }
            
            return entityManager().createQuery(c).getResultList();

        } catch (SecurityException e) {
            log.error("Erro: {}-{}", e.getMessage(), e);
            throw new DaoException(e.getMessage());
        } catch (NoSuchMethodException e) {
            log.error("Erro: {}-{}", e.getMessage(), e);
            throw new DaoException(e.getMessage());
        } catch (IllegalAccessException e) {
            log.error("Erro: {}-{}", e.getMessage(), e);
            throw new DaoException(e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("Erro: {}-{}", e.getMessage(), e);
            throw new DaoException(e.getMessage());
        } catch (ClassNotFoundException e) {
            log.error("Erro: {}-{}", e.getMessage(), e);
            throw new DaoException(e.getMessage());
        } catch (InvocationTargetException e) {
            log.error("Erro: {}-{}", e.getMessage(), e);
            throw new DaoException(e.getMessage());
        } catch (NoSuchFieldException e) {
            log.error("Erro: {}-{}", e.getMessage(), e);
            throw new DaoException(e.getMessage());
        }
    }

    private Object invoca(Object entidade, Field campo) throws NoSuchMethodException, IllegalAccessException, IllegalArgumentException, ClassNotFoundException, InvocationTargetException, NoSuchFieldException {
        String atributo = campo.getName();
        Object retobj = null;
        String metodoGet = "get" + atributo.substring(0,1).toUpperCase() + atributo.substring(1);
        Class cls = entidade.getClass();
        Method meth = cls.getMethod(metodoGet);
        try {
            retobj = meth.invoke(entidade);
        } catch (IllegalAccessException e) {
            //log.info("Repositorio.java: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            //log.info("Repositorio.java: {}", e.getMessage());
        } catch (InvocationTargetException e) {
            //log.info("Repositorio.java: {}", e.getMessage());
        }
        return retobj;
    }
    
    private Predicate condicao(Root entidade, Field campo, Object retObjI, Object retObjF) {
        Predicate predicate;
        
        CriteriaBuilder cb = entityManager().getCriteriaBuilder();
        Expression path = entidade.get(campo.getName());
        String tipo = campo.getType().getSimpleName();
        
        if (retObjI != null && retObjF == null) {
            if (tipo.equals("Date"))           {
                //List<Predicate> condicao = new ArrayList();
                //condicao.add(cb.isEmpty(path));
                //condicao.add(cb.greaterThanOrEqualTo(path, (Date   )retObjI));
                predicate = cb.greaterThanOrEqualTo(path, (Date   )retObjI);
            } else if (tipo.toUpperCase().contains("INT"))   { predicate = cb.greaterThanOrEqualTo(path, (Integer)retObjI);
            } else if (tipo.toUpperCase().equals("BOOLEAN")) { predicate = cb.greaterThanOrEqualTo(path, (Boolean)retObjI);
            } else if (tipo.equals("String"))  { predicate = cb.greaterThanOrEqualTo(path, (String )retObjI);
            } else                             { predicate = cb.greaterThanOrEqualTo(path, (Long   )retObjI);
            }
        } else if (retObjI == null && retObjF != null) {
            if (tipo.equals("Date"))           {
                //List<Predicate> condicao = new ArrayList();
                //condicao.add(cb.isEmpty(path));
                //condicao.add(cb.lessThanOrEqualTo(path, (Date   )retObjI));
                predicate = cb.lessThanOrEqualTo(path, (Date   )retObjF);
            } else if (tipo.toUpperCase().contains("INT"))   { predicate = cb.lessThanOrEqualTo(path, (Integer)retObjF);
            } else if (tipo.toUpperCase().equals("BOOLEAN")) { predicate = cb.lessThanOrEqualTo(path, (Boolean)retObjF);
            } else if (tipo.equals("String"))  { predicate = cb.lessThanOrEqualTo(path, (String )retObjF);
            } else                             { predicate = cb.lessThanOrEqualTo(path, (Long   )retObjF);
            }
        } else {
            if (retObjI != null && retObjI.equals(retObjF)) {
                if (tipo.equals("Date"))           { predicate = cb.equal(path, (Date   )retObjI);
                } else if (tipo.toUpperCase().contains("INT"))   { predicate = cb.equal(path, (Integer)retObjI);
                } else if (tipo.toUpperCase().equals("BOOLEAN")) { predicate = cb.equal(path, (Boolean)retObjI);
                } else if (tipo.equals("String"))  { path = cb.upper(path);
                                                     predicate = cb.like (path, ((String)retObjI).toUpperCase());
                } else                             { predicate = cb.equal(path, (Long   )retObjI);
                }
            } else {
                if (tipo.equals("Date"))           { predicate = cb.between(path, (Date   )retObjI, (Date   )retObjF);
                } else if (tipo.toUpperCase().contains("INT"))   { predicate = cb.between(path, (Integer)retObjI, (Integer)retObjF);
                } else if (tipo.toUpperCase().equals("BOOLEAN")) { predicate = cb.between(path, (Boolean)retObjI, (Boolean)retObjF);
                } else if (tipo.equals("String"))  { predicate = cb.between(path, (String )retObjI, (String )retObjF);
                } else                             { predicate = cb.between(path, (Long   )retObjI, (Long   )retObjF);
                }
            }
        }
        return predicate;
    }
}