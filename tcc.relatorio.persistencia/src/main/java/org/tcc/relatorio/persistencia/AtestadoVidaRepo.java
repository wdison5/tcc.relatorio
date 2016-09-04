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
import org.tcc.relatorio.dominio.PphAtestadoVidaEntity;
import org.tcc.relatorio.dominio.PphBeneficiarioEntity;
import org.tcc.relatorio.dominio.PphEstadoEntity;
import org.tcc.relatorio.dominio.PphMunicipioEntity;
import org.tcc.relatorio.persistencia.exception.util.DaoExceptionUtil;

/**
 *
 * @author Jose Wdison de Souza
 */
public class AtestadoVidaRepo extends Repositorio {

    private static final Logger log = LoggerFactory.getLogger(AtestadoVidaRepo.class);

    public List<PphAtestadoVidaEntity> listarPorNomeLike(String nome) throws DaoException {

        try {
            CriteriaBuilder cb = entityManager().getCriteriaBuilder();
            CriteriaQuery<PphAtestadoVidaEntity> c = cb.createQuery(PphAtestadoVidaEntity.class);

            Root<PphAtestadoVidaEntity> AtestadoVida = c.from(PphAtestadoVidaEntity.class);
            Expression<String> path = AtestadoVida.get("nmMedicoResp");
            c.select(AtestadoVida).
                    where(cb.like(path, nome + "%"));

            return entityManager().createQuery(c).getResultList();

        } catch (Exception e) {
            throw DaoExceptionUtil.prepara(log, e);
        }
    }

    public List<PphAtestadoVidaEntity> listarPorNomeLista(List<String> nomes) throws DaoException {

        try {
            CriteriaBuilder cb = entityManager().getCriteriaBuilder();
            CriteriaQuery<PphAtestadoVidaEntity> c = cb.createQuery(PphAtestadoVidaEntity.class);

            Root<PphAtestadoVidaEntity> AtestadoVida = c.from(PphAtestadoVidaEntity.class);
            c.select(AtestadoVida).
                    where(AtestadoVida.get("nmMedicoResp").in(nomes));

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
    public PphAtestadoVidaEntity buscarPorId(Long id) throws DaoException {

        try {
            CriteriaBuilder cb = entityManager().getCriteriaBuilder();
            CriteriaQuery<PphAtestadoVidaEntity> c = cb.createQuery(PphAtestadoVidaEntity.class);

            Root<PphAtestadoVidaEntity> AtestadoVida = c.from(PphAtestadoVidaEntity.class);

            c.select(AtestadoVida).
                    where(cb.equal(AtestadoVida.get("id"), id));

            return entityManager().createQuery(c).getSingleResult();

        } catch (Exception e) {
            throw DaoExceptionUtil.prepara(log, e);
        }
    }

    public List<PphAtestadoVidaEntity> pesquisaPorNome(String nmMedicoResp) throws DaoException {
        try {
            CriteriaBuilder cb = entityManager().getCriteriaBuilder();
            CriteriaQuery<PphAtestadoVidaEntity> c = cb.createQuery(PphAtestadoVidaEntity.class);
            Root<PphAtestadoVidaEntity> root = c.from(PphAtestadoVidaEntity.class);

            c.select(root).
                    where(cb.like(root.<String>get("nmMedicoResp"), "%" + nmMedicoResp + "%"));
            return entityManager().createQuery(c).getResultList();

        } catch (Exception e) {
            throw DaoExceptionUtil.prepara(log, e);
        }
    }
    public List<PphAtestadoVidaEntity> pesquisaPorIdbeneficiario(Long idBeneficiario) throws DaoException {
        try {
            CriteriaBuilder cb = entityManager().getCriteriaBuilder();
            CriteriaQuery<PphAtestadoVidaEntity> c = cb.createQuery(PphAtestadoVidaEntity.class);
            Root<PphAtestadoVidaEntity> root = c.from(PphAtestadoVidaEntity.class);
            Join<PphAtestadoVidaEntity, PphBeneficiarioEntity> beneficiario = root.<PphAtestadoVidaEntity, PphBeneficiarioEntity>join("pphBeneficiario", JoinType.LEFT);
            c.select(root).
                    where(cb.and(cb.equal(beneficiario.get("id"), idBeneficiario), 
                            cb.equal(root.get("flExclusao"), 0)))
                    .orderBy(cb.desc(root.get("dtAtestado")));
            return entityManager().createQuery(c).getResultList();
        } catch (Exception e) {
            throw DaoExceptionUtil.prepara(log, e);
        }
    }
}
