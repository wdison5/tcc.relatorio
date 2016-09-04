/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tcc.relatorio.persistencia;

import org.tcc.relatorio.hammer.persistencia.exception.DaoException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tcc.relatorio.dominio.PphEstadoEntity;
import org.tcc.relatorio.persistencia.exception.util.DaoExceptionUtil;

/**
 *
 * @author Jose Wdison de Souza
 */
public class EstadoRepo extends Repositorio {

    private static final Logger log = LoggerFactory.getLogger(EstadoRepo.class);

    public Set<PphEstadoEntity> listarPorNome(String nome) throws DaoException {

        try {
            CriteriaBuilder cb = entityManager().getCriteriaBuilder();
            CriteriaQuery<PphEstadoEntity> c = cb.createQuery(PphEstadoEntity.class);

            Root<PphEstadoEntity> Estado = c.from(PphEstadoEntity.class);
            Estado.fetch("funcionalidades", JoinType.LEFT);
            c.select(Estado).
                where(cb.equal(Estado.get("nome"), nome));

            return new HashSet<PphEstadoEntity>(entityManager().createQuery(c).getResultList());

        } catch (Exception e) {
            throw DaoExceptionUtil.prepara(log, e);
        }
    }
    
    public List<PphEstadoEntity> listarPorNomeLike(String nome) throws DaoException {

        try {
            CriteriaBuilder cb = entityManager().getCriteriaBuilder();
            CriteriaQuery<PphEstadoEntity> c = cb.createQuery(PphEstadoEntity.class);

            Root<PphEstadoEntity> Estado = c.from(PphEstadoEntity.class);
            Expression<String> path = Estado.get("nome");
            c.select(Estado).
                where(cb.like(path, nome + "%"));

            return entityManager().createQuery(c).getResultList();

        } catch (Exception e) {
            throw DaoExceptionUtil.prepara(log, e);
        }
    }
    
    public List<PphEstadoEntity> listarPorNomeLista(List<String> nomes) throws DaoException {

        try {
            CriteriaBuilder cb = entityManager().getCriteriaBuilder();
            CriteriaQuery<PphEstadoEntity> c = cb.createQuery(PphEstadoEntity.class);

            Root<PphEstadoEntity> Estado = c.from(PphEstadoEntity.class);
            c.select(Estado).
                where(Estado.get("nome").in(nomes));

            return entityManager().createQuery(c).getResultList();

        } catch (Exception e) {
            throw DaoExceptionUtil.prepara(log, e);
        }
    }
    
    /**
    * 
    * @param nome
    * @return
    * @throws DaoException 
    */
    public PphEstadoEntity buscarPorId(Long id) throws DaoException {

        try {
            CriteriaBuilder cb = entityManager().getCriteriaBuilder();
            CriteriaQuery<PphEstadoEntity> c = cb.createQuery(PphEstadoEntity.class);

            Root<PphEstadoEntity> Estado = c.from(PphEstadoEntity.class);

            c.select(Estado).
                where(cb.equal(Estado.get("id"), id));

            return entityManager().createQuery(c).getSingleResult();

        } catch (Exception e) {
            throw DaoExceptionUtil.prepara(log, e);
        }
    }

    public List<PphEstadoEntity> pesquisaPorNome(String nomeEstado) throws DaoException {
        try {
            CriteriaBuilder cb = entityManager().getCriteriaBuilder();
            CriteriaQuery<PphEstadoEntity> c = cb.createQuery(PphEstadoEntity.class);
            Root<PphEstadoEntity> root = c.from(PphEstadoEntity.class);
            
            c.select(root).
                where(cb.like(root.<String>get("nmEstado"), "%"+nomeEstado+"%"));
            return entityManager().createQuery(c).getResultList();

        } catch (Exception e) {
            throw DaoExceptionUtil.prepara(log, e);
        }
    }

    public List<PphEstadoEntity> list() throws DaoException {
        try {
            CriteriaBuilder cb = entityManager().getCriteriaBuilder();
            CriteriaQuery<PphEstadoEntity> c = cb.createQuery(PphEstadoEntity.class);
            Root<PphEstadoEntity> root = c.from(PphEstadoEntity.class);
            c.select(root);
            return entityManager().createQuery(c).getResultList();

        } catch (Exception e) {
            throw DaoExceptionUtil.prepara(log, e);
        }
    }
}
