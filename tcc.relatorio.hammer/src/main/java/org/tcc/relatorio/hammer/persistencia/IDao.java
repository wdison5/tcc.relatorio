package org.tcc.relatorio.hammer.persistencia;

import org.tcc.relatorio.hammer.persistencia.exception.DaoException;
import java.util.List;
import java.util.Map;
import org.tcc.relatorio.hammer.persistencia.dominio.DomainObject;

/**
 * @author wdi_s
 */
public interface IDao {
    <T extends DomainObject<?>> T persist(T var1) throws DaoException;

    <T extends DomainObject<?>> void delete(T var1) throws DaoException;

    <T extends DomainObject<?>> T update(T var1) throws DaoException;

    void flushAndClean() throws DaoException;

    void close() throws DaoException;

    <T extends DomainObject<?>> List<T> select(Class<T> var1, String var2, Map<String, Object> var3) throws DaoException;

    <T extends DomainObject<?>> List<T> select(Class<T> var1, String var2, Map<String, Object> var3, int var4, int var5) throws DaoException;

    <T extends DomainObject<?>> T find(Class<T> var1, String var2, Map<String, Object> var3) throws DaoException;

    <T extends DomainObject<?>> T find(Class<T> var1, Long var2) throws DaoException;
}
