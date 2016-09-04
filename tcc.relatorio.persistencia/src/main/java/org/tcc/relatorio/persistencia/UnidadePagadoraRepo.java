/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tcc.relatorio.persistencia;

import org.tcc.relatorio.hammer.persistencia.exception.DaoException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tcc.relatorio.cap.dominio.UsuarioEntity;
import org.tcc.relatorio.dominio.InstituicaoEntity;
import org.tcc.relatorio.dominio.PphUnidadePagadoraEntity;
import org.tcc.relatorio.enumeracao.Confirmacao;
import org.tcc.relatorio.persistencia.exception.util.DaoExceptionUtil;

/**
 *
 * @author 140200
 */
public class UnidadePagadoraRepo extends Repositorio {

    private static final Logger LOGGER = LoggerFactory.getLogger(UnidadePagadoraRepo.class);

    public Set<PphUnidadePagadoraEntity> listarPorNome(String nome) throws DaoException {

        try {
            CriteriaBuilder cb = entityManager().getCriteriaBuilder();
            CriteriaQuery<PphUnidadePagadoraEntity> c = cb.createQuery(PphUnidadePagadoraEntity.class);

            Root<PphUnidadePagadoraEntity> root = c.from(PphUnidadePagadoraEntity.class);
            root.fetch("funcionalidades", JoinType.LEFT);
            c.select(root).
                    where(cb.equal(root.get("nome"), nome));

            return new HashSet<PphUnidadePagadoraEntity>(entityManager().createQuery(c).getResultList());

        } catch (Exception e) {
            throw DaoExceptionUtil.prepara(LOGGER, e);
        }
    }

    public List<PphUnidadePagadoraEntity> listarPorNomeLike(String nome) throws DaoException {

        try {
            CriteriaBuilder cb = entityManager().getCriteriaBuilder();
            CriteriaQuery<PphUnidadePagadoraEntity> c = cb.createQuery(PphUnidadePagadoraEntity.class);

            Root<PphUnidadePagadoraEntity> root = c.from(PphUnidadePagadoraEntity.class);
            Expression<String> path = root.get("nome");
            c.select(root).
                    where(cb.like(path, nome + "%"));

            return entityManager().createQuery(c).getResultList();

        } catch (Exception e) {
            throw DaoExceptionUtil.prepara(LOGGER, e);
        }
    }

    public List<PphUnidadePagadoraEntity> listarPorNomeLista(List<String> nomes) throws DaoException {

        try {
            CriteriaBuilder cb = entityManager().getCriteriaBuilder();
            CriteriaQuery<PphUnidadePagadoraEntity> c = cb.createQuery(PphUnidadePagadoraEntity.class);

            Root<PphUnidadePagadoraEntity> root = c.from(PphUnidadePagadoraEntity.class);
            c.select(root).
                    where(root.get("nome").in(nomes));

            return entityManager().createQuery(c).getResultList();

        } catch (Exception e) {
            throw DaoExceptionUtil.prepara(LOGGER, e);
        }
    }

    /**
     *
     * @param id
     * @return
     * @throws DaoException
     */
    public PphUnidadePagadoraEntity buscarPorId(Long id) throws DaoException {

        try {
            CriteriaBuilder cb = entityManager().getCriteriaBuilder();
            CriteriaQuery<PphUnidadePagadoraEntity> c = cb.createQuery(PphUnidadePagadoraEntity.class);

            Root<PphUnidadePagadoraEntity> root = c.from(PphUnidadePagadoraEntity.class);

            Join<PphUnidadePagadoraEntity, InstituicaoEntity> joinUnidPagadoraInstituicao = root.<PphUnidadePagadoraEntity, InstituicaoEntity>join("instituicao", JoinType.LEFT);
            c.select(root).where(cb.and(
                cb.equal(root.get("id"), id),
                cb.or(cb.isNull(joinUnidPagadoraInstituicao),
                    cb.equal(joinUnidPagadoraInstituicao.get("flExclusao"), Confirmacao.NAO.getId()))));

            return entityManager().createQuery(c).getSingleResult();

        } catch (Exception e) {
            throw DaoExceptionUtil.prepara(LOGGER, e);
        }
    }

    public List<PphUnidadePagadoraEntity> listar(List<Long> idsUnidPagadora, int flAtivo) throws DaoException {
        try {
            CriteriaBuilder cb = entityManager().getCriteriaBuilder();
            CriteriaQuery<PphUnidadePagadoraEntity> c = cb.createQuery(PphUnidadePagadoraEntity.class);
            Root<PphUnidadePagadoraEntity> root = c.from(PphUnidadePagadoraEntity.class);
            c.select(root);
            
            if (idsUnidPagadora.size() > 0) {
                List<Predicate> predicados = new ArrayList<Predicate>();

                predicados.add(root.get("id").in(idsUnidPagadora));
                c.where(cb.and(predicados.toArray(new Predicate[]{})));
                return entityManager().createQuery(c).getResultList();
            } else {
                return new ArrayList<PphUnidadePagadoraEntity>();
            }
        } catch (Exception e) {
            throw DaoExceptionUtil.prepara(LOGGER, e);
        }
    }

    public List<PphUnidadePagadoraEntity> list() throws DaoException {
        try {
            CriteriaBuilder cb = entityManager().getCriteriaBuilder();
            CriteriaQuery<PphUnidadePagadoraEntity> c = cb.createQuery(PphUnidadePagadoraEntity.class);
            Root<PphUnidadePagadoraEntity> root = c.from(PphUnidadePagadoraEntity.class);
            c.select(root);
            return entityManager().createQuery(c).getResultList();
        } catch (Exception e) {
            throw DaoExceptionUtil.prepara(LOGGER, e);
        }
    }
    
    public List<PphUnidadePagadoraEntity> list(UsuarioEntity usuario) throws DaoException {
        try {
            EntityManager em = entityManager();
            TypedQuery<PphUnidadePagadoraEntity> query = em.createQuery("SELECT up FROM UsuarioEntity u JOIN u.instituicoes i JOIN i.pphUnidadePagadora up WHERE up.flAtivo = :flAtivo and u.userId = :userid and u.flExclusao = :flExclusao and i.flExclusao = :flExclusao",
                    PphUnidadePagadoraEntity.class);
            return 
                    query.setParameter("userid", usuario.getUserId())
                    .setParameter("flAtivo", 1)
                    .setParameter("flExclusao", Confirmacao.NAO.getId())
                    .getResultList();
        } catch (Exception e) {
            throw DaoExceptionUtil.prepara(LOGGER, e);
        }
    }

    public List<PphUnidadePagadoraEntity> listAllNoCadInstit(int flAtivo) throws DaoException {
        try {
            CriteriaBuilder cb = entityManager().getCriteriaBuilder();
            CriteriaQuery<PphUnidadePagadoraEntity> c = cb.createQuery(PphUnidadePagadoraEntity.class);
            Root<PphUnidadePagadoraEntity> root = c.from(PphUnidadePagadoraEntity.class);
            Join<PphUnidadePagadoraEntity, InstituicaoEntity> joinUnidPagadoraInstituicao = root.<PphUnidadePagadoraEntity, InstituicaoEntity>join("instituicao", JoinType.LEFT);
            c.select(root)
                    .where(cb.and(cb.equal(root.get("flAtivo"), flAtivo), 
                            cb.or(cb.isNull(joinUnidPagadoraInstituicao),
                                cb.equal(joinUnidPagadoraInstituicao.get("flExclusao"), Confirmacao.SIM.getId())
                            )));
            return entityManager().createQuery(c).getResultList();
        } catch (Exception e) {
            throw DaoExceptionUtil.prepara(LOGGER, e);
        }
    }

    public List<PphUnidadePagadoraEntity> list(int flAtivo) throws DaoException {
        try {
            CriteriaBuilder cb = entityManager().getCriteriaBuilder();
            CriteriaQuery<PphUnidadePagadoraEntity> c = cb.createQuery(PphUnidadePagadoraEntity.class);
            Root<PphUnidadePagadoraEntity> root = c.from(PphUnidadePagadoraEntity.class);
            c.select(root).where(cb.and(cb.equal(root.get("flAtivo"), flAtivo)));
            return entityManager().createQuery(c).getResultList();
        } catch (Exception e) {
            throw DaoExceptionUtil.prepara(LOGGER, e);
        }
    }
}
