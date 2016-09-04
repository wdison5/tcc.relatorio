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
import org.tcc.relatorio.enumeracao.Confirmacao;
import org.tcc.relatorio.persistencia.exception.util.DaoExceptionUtil;

/**
 *
 * @author Vanderlei Garcia
 */
public class UnidSaudeRepo extends Repositorio {

    private static final Logger LOGGER = LoggerFactory.getLogger(UnidSaudeRepo.class);

    /**
     * Recupera Instituicao por id
     *
     * @param id
     * @return
     * @throws DaoException
     */
    public PphUnidSaudeEntity getById(long id) throws DaoException {

        try {
            CriteriaBuilder cb = entityManager().getCriteriaBuilder();
            CriteriaQuery<PphUnidSaudeEntity> c = cb.createQuery(PphUnidSaudeEntity.class);

            Root<PphUnidSaudeEntity> root = c.from(PphUnidSaudeEntity.class);
            c.select(root).
                    where(cb.equal(root.get("id"), id));

            List<PphUnidSaudeEntity> result = entityManager().createQuery(c).getResultList();
            return result.isEmpty() ? null : result.get(0);

        } catch (Exception e) {
            throw DaoExceptionUtil.prepara(LOGGER, e);
        }
    }

    public PphUnidSaudeEntity getByCNES(int cnes) throws DaoException {

        try {
            CriteriaBuilder cb = entityManager().getCriteriaBuilder();
            CriteriaQuery<PphUnidSaudeEntity> c = cb.createQuery(PphUnidSaudeEntity.class);

            Root<PphUnidSaudeEntity> root = c.from(PphUnidSaudeEntity.class);
            c.select(root).
                    where(cb.equal(root.get("cnes"), cnes));

            List<PphUnidSaudeEntity> result = entityManager().createQuery(c).getResultList();
            return result.isEmpty() ? null : result.get(0);

        } catch (Exception e) {
            throw DaoExceptionUtil.prepara(LOGGER, e);
        }
    }

    public List<PphUnidSaudeEntity> listar(List<Long> idsUnidSaude, int flAtivo) throws DaoException {
        try {
            CriteriaBuilder cb = entityManager().getCriteriaBuilder();
            CriteriaQuery<PphUnidSaudeEntity> c = cb.createQuery(PphUnidSaudeEntity.class);
            Root<PphUnidSaudeEntity> root = c.from(PphUnidSaudeEntity.class);
            c.select(root);
            
            if (idsUnidSaude.size() > 0) {
                List<Predicate> predicados = new ArrayList<Predicate>();
                predicados.add(cb.equal(root.get("flAtivo"), flAtivo));
                predicados.add(root.get("id").in(idsUnidSaude));
                c.where(cb.and(predicados.toArray(new Predicate[]{})));
                return entityManager().createQuery(c).getResultList();
            } else {
                return new ArrayList<PphUnidSaudeEntity>();
            }
        } catch (Exception e) {
            throw DaoExceptionUtil.prepara(LOGGER, e);
        }
    }

    public List<PphUnidSaudeEntity> list(UsuarioEntity usuario) throws DaoException {
        try {
            EntityManager em = entityManager();
            TypedQuery<PphUnidSaudeEntity> query = em.createQuery("SELECT us FROM UsuarioEntity u JOIN u.instituicoes i JOIN i.pphUnidSaude us WHERE us.flAtivo = :flAtivo and u.userId = :userid and u.flExclusao = :flExclusao and i.flExclusao = :flExclusao",
                    PphUnidSaudeEntity.class);
            return 
                    query.setParameter("userid", usuario.getUserId())
                    .setParameter("flAtivo", 1)
                    .setParameter("flExclusao", Confirmacao.NAO.getId())
                    .getResultList();
        } catch (Exception e) {
            throw DaoExceptionUtil.prepara(LOGGER, e);
        }
    }

    public List<PphUnidSaudeEntity> listAllNoCadInstit(int flAtivo) throws DaoException {    
        try {
            CriteriaBuilder cb = entityManager().getCriteriaBuilder();
            CriteriaQuery<PphUnidSaudeEntity> c = cb.createQuery(PphUnidSaudeEntity.class);
            Root<PphUnidSaudeEntity> root = c.from(PphUnidSaudeEntity.class);
            Join<PphUnidSaudeEntity, InstituicaoEntity> joinUnidSaudeInstituicao = root.<PphUnidSaudeEntity, InstituicaoEntity>join("instituicao", JoinType.LEFT);
            c.select(root)
                    .where(cb.and(cb.equal(root.get("flAtivo"), flAtivo), 
                            cb.or(cb.isNull(joinUnidSaudeInstituicao),
                                cb.equal(joinUnidSaudeInstituicao.get("flExclusao"), Confirmacao.SIM.getId())
                            )
                    ));
            return entityManager().createQuery(c).getResultList();
        } catch (Exception e) {
            throw DaoExceptionUtil.prepara(LOGGER, e);
        }
    }

    public List<PphUnidSaudeEntity> list(int flAtivo) throws DaoException {    
        try {
            CriteriaBuilder cb = entityManager().getCriteriaBuilder();
            CriteriaQuery<PphUnidSaudeEntity> c = cb.createQuery(PphUnidSaudeEntity.class);
            Root<PphUnidSaudeEntity> root = c.from(PphUnidSaudeEntity.class);
            c.select(root)
                    .where(cb.and(cb.equal(root.get("flAtivo"), flAtivo)));
            return entityManager().createQuery(c).getResultList();
        } catch (Exception e) {
            throw DaoExceptionUtil.prepara(LOGGER, e);
        }
    }
}
