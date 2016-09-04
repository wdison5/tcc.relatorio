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
import org.tcc.relatorio.cap.dominio.GrupoEntity;
import org.tcc.relatorio.cap.dominio.UsuarioEntity;
import org.tcc.relatorio.enumeracao.Confirmacao;
import org.tcc.relatorio.persistencia.exception.util.DaoExceptionUtil;
import org.tcc.relatorio.hammer.persistencia.exception.DaoException;

/**
 *
 * @author 140200
 */
public class GrupoRepo extends Repositorio {

    private static final Logger log = LoggerFactory.getLogger(UsuarioRepo.class);

    public Set<GrupoEntity> listarPorNome(String nome) throws DaoException {

        try {
            CriteriaBuilder cb = entityManager().getCriteriaBuilder();
            CriteriaQuery<GrupoEntity> c = cb.createQuery(GrupoEntity.class);

            Root<GrupoEntity> root = c.from(GrupoEntity.class);
            root.fetch("funcionalidades", JoinType.LEFT);
            c.select(root).
                    where(cb.and(cb.equal(root.get("nome"), nome), cb.equal(root.get("flExclusao"), Confirmacao.NAO.getId())));

            return new HashSet<GrupoEntity>(entityManager().createQuery(c).getResultList());

        } catch (Exception e) {
            throw DaoExceptionUtil.prepara(log, e);
        }
    }

    public List<GrupoEntity> listarPorNomeLike(String nome) throws DaoException {
        return listarPorNomeLike(nome, null);
    }

    public List<GrupoEntity> listarPorNomeLike(String nome, UsuarioEntity usuarioLogado) throws DaoException {

        try {
            CriteriaBuilder cb = entityManager().getCriteriaBuilder();
            CriteriaQuery<GrupoEntity> c = cb.createQuery(GrupoEntity.class);
            List<Predicate> criteriaListAnd = new ArrayList();

            Root<GrupoEntity> root = c.from(GrupoEntity.class);
            criteriaListAnd.add(cb.like(root.<String>get("nome"), nome + "%"));
            criteriaListAnd.add(cb.equal(root.get("flExclusao"), Confirmacao.NAO.getId()));
            if (usuarioLogado != null && usuarioLogado.getId() != null && !usuarioLogado.isAcessoGeral()) {
                Join<GrupoEntity, UsuarioEntity> joinGrupoUsuario = root.<GrupoEntity, UsuarioEntity>join("usuarios", JoinType.LEFT);
                criteriaListAnd.add(cb.or(
                        cb.isNull(joinGrupoUsuario), cb.and(
                            cb.equal(joinGrupoUsuario.get("id"), usuarioLogado.getId()),
                            cb.equal(joinGrupoUsuario.get("flExclusao"), Confirmacao.NAO.getId())
                        )
                    )
                );
            }

            c.select(root).where(cb.and(criteriaListAnd.toArray(new Predicate[0])));

            return entityManager().createQuery(c).getResultList();
        } catch (Exception e) {
            throw DaoExceptionUtil.prepara(log, e);
        }
    }

    public List<GrupoEntity> listarPorNomeLista(List<String> nomes) throws DaoException {

        try {
            CriteriaBuilder cb = entityManager().getCriteriaBuilder();
            CriteriaQuery<GrupoEntity> c = cb.createQuery(GrupoEntity.class);

            Root<GrupoEntity> root = c.from(GrupoEntity.class);
            c.select(root).
                    where(cb.and(root.get("nome").in(nomes), cb.equal(root.get("flExclusao"), Confirmacao.NAO.getId())));

            return entityManager().createQuery(c).getResultList();

        } catch (Exception e) {
            log.error("Erro: {}-{}", e.getMessage(), e);
            throw new DaoException(e.getMessage());
        }
    }

    public GrupoEntity buscarPorId(Long id) throws DaoException {

        try {

            CriteriaBuilder cb = entityManager().getCriteriaBuilder();
            CriteriaQuery<GrupoEntity> c = cb.createQuery(GrupoEntity.class);

            Root<GrupoEntity> root = c.from(GrupoEntity.class);

            c.select(root).where(cb.and(cb.equal(root.get("id"), id), cb.equal(root.get("flExclusao"), Confirmacao.NAO.getId())));

            return entityManager().createQuery(c).getSingleResult();

        } catch (Exception e) {
            throw DaoExceptionUtil.prepara(log, e);
        }
    }
}
