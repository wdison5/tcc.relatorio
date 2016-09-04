/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tcc.relatorio.cap.persistencia;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
import org.tcc.relatorio.enumeracao.Confirmacao;
import org.tcc.relatorio.hammer.persistencia.exception.DaoException;

/**
 *
 * @author 140200
 */
public class UsuarioRepo extends Repositorio {

    private static final Logger log = LoggerFactory.getLogger(UsuarioRepo.class);

    public Set<UsuarioEntity> listarPorUserId(String id) throws DaoException {

        try {
            CriteriaBuilder cb = entityManager().getCriteriaBuilder();
            CriteriaQuery<UsuarioEntity> c = cb.createQuery(UsuarioEntity.class);

            Root<UsuarioEntity> root = c.from(UsuarioEntity.class);
            root.fetch("grupos", JoinType.LEFT).fetch("funcionalidades", JoinType.LEFT);
            c.select(root).
                    where(cb.and(cb.equal(root.get("userId"), id)), cb.equal(root.get("flExclusao"), Confirmacao.NAO.getId()));

            return new HashSet<UsuarioEntity>(entityManager().createQuery(c).getResultList());

        } catch (Exception e) {
            log.error("Erro: {}-{}", e.getMessage(), e);
            throw new DaoException(e.getMessage());
        }
    }

    public Set<UsuarioEntity> listarPorUserIdLike(String userId, List<Long> lstInstituicaoId, UsuarioEntity usuarioLogado) throws DaoException {
        try {
            CriteriaBuilder cb = entityManager().getCriteriaBuilder();
            CriteriaQuery<UsuarioEntity> c = cb.createQuery(UsuarioEntity.class);
            List<Predicate> criteriaListAnd = new ArrayList();

            Root<UsuarioEntity> root = c.from(UsuarioEntity.class);

            criteriaListAnd.add(cb.like(root.<String>get("userId"), userId + "%"));
            criteriaListAnd.add(cb.equal(root.get("flExclusao"), Confirmacao.NAO.getId()));

            if (usuarioLogado == null || !usuarioLogado.isAcessoGeral()) {
                if (lstInstituicaoId != null && !lstInstituicaoId.isEmpty()) {
                    Join<UsuarioEntity, InstituicaoEntity> joinUsuarioInstituicao = root.<UsuarioEntity, InstituicaoEntity>join("instituicoes", JoinType.LEFT);
                    criteriaListAnd.add(
                            cb.or(
                                cb.isNull(joinUsuarioInstituicao), 
                                cb.and(
                                    joinUsuarioInstituicao.get("id").in(lstInstituicaoId),
                                    cb.equal(joinUsuarioInstituicao.get("flExclusao"), Confirmacao.NAO.getId())
                            )));
                    criteriaListAnd.add(cb.equal(root.get("acessoGeral"), false));
                }
            }

            c.select(root).where(cb.and(criteriaListAnd.toArray(new Predicate[0])));

            return new HashSet<UsuarioEntity>(entityManager().createQuery(c).getResultList());

        } catch (Exception e) {
            log.error("Erro: {}-{}", e.getMessage(), e);
            throw new DaoException(e.getMessage());
        }
    }
    
    public List<UsuarioEntity> listarPorEmail(String email) throws DaoException {

        try {
            CriteriaBuilder cb = entityManager().getCriteriaBuilder();
            CriteriaQuery<UsuarioEntity> c = cb.createQuery(UsuarioEntity.class);

            Root<UsuarioEntity> root = c.from(UsuarioEntity.class);
            root.fetch("grupos", JoinType.LEFT).fetch("funcionalidades", JoinType.LEFT);
            c.select(root).where(cb.and(cb.equal(root.get("email"), email)));

            return entityManager().createQuery(c).getResultList();

        } catch (Exception e) {
            log.error("Erro: {}-{}", e.getMessage(), e);
            throw new DaoException(e);
        }
    }
}
