/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tcc.relatorio.cap.persistencia;

import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Root;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tcc.relatorio.cap.dominio.FuncionalidadeEntity;
import org.tcc.relatorio.hammer.persistencia.exception.DaoException;

/**
 *
 * @author 140200
 */
public class FuncionalidadeRepo extends Repositorio {

    private static final Logger log = LoggerFactory.getLogger(FuncionalidadeRepo.class);

    public List<FuncionalidadeEntity> listarFuncPorNome(String nome) throws DaoException {

        try {
            CriteriaBuilder cb = entityManager().getCriteriaBuilder();
            CriteriaQuery<FuncionalidadeEntity> c = cb.createQuery(FuncionalidadeEntity.class);

            Root<FuncionalidadeEntity> func = c.from(FuncionalidadeEntity.class);
            c.select(func).
                    where(cb.equal(func.get("nome"), nome));

            return entityManager().createQuery(c).getResultList();

        } catch (Exception e) {
            log.error("Erro: {}-{}", e.getMessage(), e);
            throw new DaoException(e.getMessage());
        }
    }

    public List<FuncionalidadeEntity> listarFuncPorNomeLista(List<String> listaNomes) throws DaoException {
        try {
            CriteriaBuilder cb = entityManager().getCriteriaBuilder();
            CriteriaQuery<FuncionalidadeEntity> c = cb.createQuery(FuncionalidadeEntity.class);

            Root<FuncionalidadeEntity> func = c.from(FuncionalidadeEntity.class);
            c.select(func).
                    where(func.get("nome").in(listaNomes));

            return entityManager().createQuery(c).getResultList();

        } catch (Exception e) {
            log.error("Erro: {}-{}", e.getMessage(), e);
            throw new DaoException(e.getMessage());
        }
    }

    public List<FuncionalidadeEntity> listarFuncPorNomeLike(String nome) throws DaoException {

        try {
            CriteriaBuilder cb = entityManager().getCriteriaBuilder();
            CriteriaQuery<FuncionalidadeEntity> c = cb.createQuery(FuncionalidadeEntity.class);

            Root<FuncionalidadeEntity> funcionalidade = c.from(FuncionalidadeEntity.class);

            Expression<String> path = funcionalidade.get("nome");
            c.select(funcionalidade).
                    where(cb.like(path, nome + "%"));

            return entityManager().createQuery(c).getResultList();

        } catch (Exception e) {
            log.error("Erro: {}-{}", e.getMessage(), e);
            throw new DaoException(e.getMessage());
        }
    }
}
