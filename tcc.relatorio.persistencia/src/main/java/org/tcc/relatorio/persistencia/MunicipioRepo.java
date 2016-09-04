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
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tcc.relatorio.dominio.PphEstadoEntity;
import org.tcc.relatorio.dominio.PphMunicipioEntity;
import org.tcc.relatorio.persistencia.exception.util.DaoExceptionUtil;

/**
 *
 * @author Jose Wdison de Souza
 */
public class MunicipioRepo extends Repositorio {

    private static final Logger log = LoggerFactory.getLogger(MunicipioRepo.class);

    public Set<PphMunicipioEntity> listarPorNome(String nome) throws DaoException {

        try {
            CriteriaBuilder cb = entityManager().getCriteriaBuilder();
            CriteriaQuery<PphMunicipioEntity> c = cb.createQuery(PphMunicipioEntity.class);

            Root<PphMunicipioEntity> Municipio = c.from(PphMunicipioEntity.class);
            Municipio.fetch("funcionalidades", JoinType.LEFT);
            c.select(Municipio).
                    where(cb.equal(Municipio.get("nome"), nome));

            return new HashSet<PphMunicipioEntity>(entityManager().createQuery(c).getResultList());

        } catch (Exception e) {
            throw DaoExceptionUtil.prepara(log, e);
        }
    }

    public List<PphMunicipioEntity> listarPorNomeLike(String nome) throws DaoException {

        try {
            CriteriaBuilder cb = entityManager().getCriteriaBuilder();
            CriteriaQuery<PphMunicipioEntity> c = cb.createQuery(PphMunicipioEntity.class);

            Root<PphMunicipioEntity> Municipio = c.from(PphMunicipioEntity.class);
            Expression<String> path = Municipio.get("nome");
            c.select(Municipio).
                    where(cb.like(path, nome + "%"));

            return entityManager().createQuery(c).getResultList();

        } catch (Exception e) {
            throw DaoExceptionUtil.prepara(log, e);
        }
    }

    public List<PphMunicipioEntity> listarPorNomeLista(List<String> nomes) throws DaoException {

        try {
            CriteriaBuilder cb = entityManager().getCriteriaBuilder();
            CriteriaQuery<PphMunicipioEntity> c = cb.createQuery(PphMunicipioEntity.class);

            Root<PphMunicipioEntity> Municipio = c.from(PphMunicipioEntity.class);
            c.select(Municipio).
                    where(Municipio.get("nome").in(nomes));

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
    public PphMunicipioEntity buscarPorId(Long id) throws DaoException {

        try {
            CriteriaBuilder cb = entityManager().getCriteriaBuilder();
            CriteriaQuery<PphMunicipioEntity> c = cb.createQuery(PphMunicipioEntity.class);

            Root<PphMunicipioEntity> Municipio = c.from(PphMunicipioEntity.class);

            c.select(Municipio).
                    where(cb.equal(Municipio.get("id"), id));

            return entityManager().createQuery(c).getSingleResult();

        } catch (Exception e) {
            throw DaoExceptionUtil.prepara(log, e);
        }
    }

    public List<PphMunicipioEntity> pesquisaPorNome(String nomeMunicipio) throws DaoException {
        try {
            CriteriaBuilder cb = entityManager().getCriteriaBuilder();
            CriteriaQuery<PphMunicipioEntity> c = cb.createQuery(PphMunicipioEntity.class);
            Root<PphMunicipioEntity> root = c.from(PphMunicipioEntity.class);

            c.select(root).
                    where(cb.like(root.<String>get("nmMunicipio"), "%" + nomeMunicipio + "%"));
            return entityManager().createQuery(c).getResultList();

        } catch (Exception e) {
            throw DaoExceptionUtil.prepara(log, e);
        }
    }

    public List<PphMunicipioEntity> pesquisaPorIdEstado(Long idEstado) throws DaoException {
        try {
            CriteriaBuilder cb = entityManager().getCriteriaBuilder();
            CriteriaQuery<PphMunicipioEntity> c = cb.createQuery(PphMunicipioEntity.class);
            Root<PphMunicipioEntity> root = c.from(PphMunicipioEntity.class);
            Join<PphMunicipioEntity, PphEstadoEntity> estado = root.<PphMunicipioEntity, PphEstadoEntity>join("pphEstado", JoinType.LEFT);
            c.select(root).
                    where(cb.equal(estado.get("id"), idEstado))
                    .orderBy(cb.asc(root.get("nmMunicipio")));
            return entityManager().createQuery(c).getResultList();
        } catch (Exception e) {
            throw DaoExceptionUtil.prepara(log, e);
        }
    }
}
