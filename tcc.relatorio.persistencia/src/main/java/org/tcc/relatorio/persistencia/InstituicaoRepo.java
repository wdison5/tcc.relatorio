/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tcc.relatorio.persistencia;

import org.tcc.relatorio.hammer.persistencia.exception.DaoException;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tcc.relatorio.cap.dominio.UsuarioEntity;
import org.tcc.relatorio.dominio.InstituicaoEntity;
import org.tcc.relatorio.dominio.PphUnidSaudeEntity;
import org.tcc.relatorio.dominio.PphUnidadePagadoraEntity;
import org.tcc.relatorio.enumeracao.Confirmacao;
import org.tcc.relatorio.persistencia.exception.util.DaoExceptionUtil;

/**
 *
 * @author Jose Wdison
 */
public class InstituicaoRepo extends Repositorio {

    private static final Logger LOGGER = LoggerFactory.getLogger(InstituicaoRepo.class);

    public InstituicaoEntity getById(long id) throws DaoException {

        try {
            CriteriaBuilder cb = entityManager().getCriteriaBuilder();
            CriteriaQuery<InstituicaoEntity> c = cb.createQuery(InstituicaoEntity.class);

            Root<InstituicaoEntity> root = c.from(InstituicaoEntity.class);
            c.select(root).
                    where(cb.equal(root.get("id"), id), 
                            cb.equal(root.get("flExclusao"), Confirmacao.NAO.getId()));

            List<InstituicaoEntity> result = entityManager().createQuery(c).getResultList();
            return result.isEmpty() ? null : result.get(0);

        } catch (Exception e) {
            throw DaoExceptionUtil.prepara(LOGGER, e);
        }
    }

    public List<InstituicaoEntity> list() throws DaoException {

        try {
            CriteriaBuilder cb = entityManager().getCriteriaBuilder();
            CriteriaQuery<InstituicaoEntity> cq = cb.createQuery(InstituicaoEntity.class);
            Root<InstituicaoEntity> root = cq.from(InstituicaoEntity.class);
            cq.select(root).where(cb.equal(root.get("flExclusao"), Confirmacao.NAO.getId()));
            return entityManager().createQuery(cq).getResultList();
        } catch (Exception e) {
            throw DaoExceptionUtil.prepara(LOGGER, e);
        }
    }

    public List<InstituicaoEntity> list(UsuarioEntity usuario) throws DaoException {
        try {
            EntityManager em = entityManager();
            TypedQuery<InstituicaoEntity> query = em.createQuery("SELECT i FROM UsuarioEntity u JOIN u.instituicoes i WHERE u.userId = :userid and u.flExclusao = :flExclusao and i.flExclusao = :flExclusao",
                    InstituicaoEntity.class);
            return query.setParameter("userid", usuario.getUserId()).setParameter("flExclusao", Confirmacao.NAO.getId()).getResultList();
        } catch (Exception e) {
            throw DaoExceptionUtil.prepara(LOGGER, e);
        }
    }

    public List<InstituicaoEntity> listByUPaga(List<Long> lstUnidPagadoraId, String nomeUnidPagadora, String codigoUnidPagadora) throws DaoException {
        try {
            CriteriaBuilder cb = entityManager().getCriteriaBuilder();
            CriteriaQuery<InstituicaoEntity> q = cb.createQuery(InstituicaoEntity.class);
            Root<InstituicaoEntity> root = q.from(InstituicaoEntity.class);
            Join<InstituicaoEntity, PphUnidadePagadoraEntity> joinUnidPagadoraInstituicao = root.<InstituicaoEntity, PphUnidadePagadoraEntity>join("pphUnidadePagadora", JoinType.INNER);
            q.select(root).where(cb.and(joinUnidPagadoraInstituicao.get("id").in(lstUnidPagadoraId),
                    cb.equal(root.get("flExclusao"), Confirmacao.NAO.getId()),
                    cb.like(joinUnidPagadoraInstituicao.<String>get("nmUnidadePagadora"), nomeUnidPagadora),
                    cb.like(joinUnidPagadoraInstituicao.<String>get("cdUnidadePagadora"), codigoUnidPagadora)
            ));
            return entityManager().createQuery(q).getResultList();
        } catch (Exception e) {
            throw DaoExceptionUtil.prepara(LOGGER, e);
        }
    }

    public List<InstituicaoEntity> listByUSaud(List<Long> lstUnidSaudeId, String nomeUnidSaude, String codigoUnidSaude) throws DaoException {
        try {
            CriteriaBuilder cb = entityManager().getCriteriaBuilder();
            CriteriaQuery<InstituicaoEntity> q = cb.createQuery(InstituicaoEntity.class);
            Root<InstituicaoEntity> root = q.from(InstituicaoEntity.class);
            Join<InstituicaoEntity, PphUnidSaudeEntity> joinInstituicaoUnidSaude = root.<InstituicaoEntity, PphUnidSaudeEntity>join("pphUnidSaude", JoinType.INNER);
            q.select(root).where(cb.and(joinInstituicaoUnidSaude.get("id").in(lstUnidSaudeId),
                    cb.like(joinInstituicaoUnidSaude.<String>get("nome"), nomeUnidSaude),
                    cb.equal(root.get("flExclusao"), Confirmacao.NAO.getId()),
                    cb.like(cb.function("TO_CHAR", String.class, joinInstituicaoUnidSaude.<String>get("cnes")), codigoUnidSaude)
            ));
            return entityManager().createQuery(q).getResultList();
        } catch (Exception e) {
            throw DaoExceptionUtil.prepara(LOGGER, e);
        }
    }

    public Long isNrUsuariosAssociado(Long idInstituicao, Integer flExclusao) throws DaoException {
        try {
            CriteriaBuilder cb = entityManager().getCriteriaBuilder();
            CriteriaQuery<Long> cqCount = cb.createQuery(Long.class);
            Root<UsuarioEntity> root = cqCount.from(UsuarioEntity.class);
            List<Predicate> criteriaListAnd = new ArrayList();

            Join<UsuarioEntity, InstituicaoEntity> joinUsuarioInstituicao = root.<UsuarioEntity, InstituicaoEntity>join("instituicoes", JoinType.INNER);
            criteriaListAnd.add(cb.equal(joinUsuarioInstituicao.get("id"), idInstituicao));
            criteriaListAnd.add(cb.equal(joinUsuarioInstituicao.get("flExclusao"), flExclusao));
            criteriaListAnd.add(cb.equal(root.get("flExclusao"), flExclusao));

            cqCount.select(cb.count(root)).where(cb.and(criteriaListAnd.toArray(new Predicate[0])));
            return entityManager().createQuery(cqCount).getSingleResult();
        } catch (Exception e) {
            throw DaoExceptionUtil.prepara(LOGGER, e);
        }
    }
}
