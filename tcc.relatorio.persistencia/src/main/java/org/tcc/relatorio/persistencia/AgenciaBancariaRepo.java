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
import org.tcc.relatorio.dominio.PphAgenciaBancariaEntity;
import org.tcc.relatorio.dominio.PphBancoEntity;
import org.tcc.relatorio.persistencia.exception.util.DaoExceptionUtil;

/**
 *
 * @author Jose Wdison de Souza
 */
public class AgenciaBancariaRepo extends Repositorio {

    private static final Logger log = LoggerFactory.getLogger(AgenciaBancariaRepo.class);

    public Set<PphAgenciaBancariaEntity> listarPorNome(String nome) throws DaoException {

        try {
            CriteriaBuilder cb = entityManager().getCriteriaBuilder();
            CriteriaQuery<PphAgenciaBancariaEntity> c = cb.createQuery(PphAgenciaBancariaEntity.class);

            Root<PphAgenciaBancariaEntity> AgenciaBancaria = c.from(PphAgenciaBancariaEntity.class);
            AgenciaBancaria.fetch("funcionalidades", JoinType.LEFT);
            c.select(AgenciaBancaria).
                    where(cb.equal(AgenciaBancaria.get("nome"), nome));

            return new HashSet<PphAgenciaBancariaEntity>(entityManager().createQuery(c).getResultList());

        } catch (Exception e) {
            throw DaoExceptionUtil.prepara(log, e);
        }
    }

    public List<PphAgenciaBancariaEntity> listarPorNomeLike(String nome) throws DaoException {

        try {
            CriteriaBuilder cb = entityManager().getCriteriaBuilder();
            CriteriaQuery<PphAgenciaBancariaEntity> c = cb.createQuery(PphAgenciaBancariaEntity.class);

            Root<PphAgenciaBancariaEntity> AgenciaBancaria = c.from(PphAgenciaBancariaEntity.class);
            Expression<String> path = AgenciaBancaria.get("nome");
            c.select(AgenciaBancaria).
                    where(cb.like(path, nome + "%"));

            return entityManager().createQuery(c).getResultList();

        } catch (Exception e) {
            throw DaoExceptionUtil.prepara(log, e);
        }
    }

    public List<PphAgenciaBancariaEntity> listarPorNomeLista(List<String> nomes) throws DaoException {

        try {
            CriteriaBuilder cb = entityManager().getCriteriaBuilder();
            CriteriaQuery<PphAgenciaBancariaEntity> c = cb.createQuery(PphAgenciaBancariaEntity.class);

            Root<PphAgenciaBancariaEntity> AgenciaBancaria = c.from(PphAgenciaBancariaEntity.class);
            c.select(AgenciaBancaria).
                    where(AgenciaBancaria.get("nome").in(nomes));

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
    public PphAgenciaBancariaEntity buscarPorId(Long id) throws DaoException {

        try {
            CriteriaBuilder cb = entityManager().getCriteriaBuilder();
            CriteriaQuery<PphAgenciaBancariaEntity> c = cb.createQuery(PphAgenciaBancariaEntity.class);

            Root<PphAgenciaBancariaEntity> root = c.from(PphAgenciaBancariaEntity.class);

            c.select(root).
                    where(cb.equal(root.get("id"), id));

            return entityManager().createQuery(c).getSingleResult();

        } catch (Exception e) {
            throw DaoExceptionUtil.prepara(log, e);
        }
    }

    public List<PphAgenciaBancariaEntity> pesquisaPorNome(String nomeAgenciaBancaria) throws DaoException {
        try {
            CriteriaBuilder cb = entityManager().getCriteriaBuilder();
            CriteriaQuery<PphAgenciaBancariaEntity> c = cb.createQuery(PphAgenciaBancariaEntity.class);
            Root<PphAgenciaBancariaEntity> root = c.from(PphAgenciaBancariaEntity.class);

            c.select(root).
                    where(cb.like(root.<String>get("nmAgenciaBancaria"), "%" + nomeAgenciaBancaria + "%"));
            return entityManager().createQuery(c).getResultList();

        } catch (Exception e) {
            throw DaoExceptionUtil.prepara(log, e);
        }
    }
    
    public List<PphAgenciaBancariaEntity> pesquisaPorIdBanco(Long idBanco) throws DaoException {
        try {
            CriteriaBuilder cb = entityManager().getCriteriaBuilder();
            CriteriaQuery<PphAgenciaBancariaEntity> c = cb.createQuery(PphAgenciaBancariaEntity.class);
            Root<PphAgenciaBancariaEntity> root = c.from(PphAgenciaBancariaEntity.class);
            Join<PphAgenciaBancariaEntity, PphBancoEntity> banco = root.<PphAgenciaBancariaEntity, PphBancoEntity>join("pphBanco", JoinType.LEFT);
            c.select(root).where(cb.equal(banco.get("id"), idBanco));
            return entityManager().createQuery(c).getResultList();
        } catch (Exception e) {
            throw DaoExceptionUtil.prepara(log, e);
        }
    }

    public List<PphAgenciaBancariaEntity> getByIdBancoCodAgencia(Long idBanco, String nrAgencia) throws DaoException {
        try {
            CriteriaBuilder cb = entityManager().getCriteriaBuilder();
            CriteriaQuery<PphAgenciaBancariaEntity> c = cb.createQuery(PphAgenciaBancariaEntity.class);
            Root<PphAgenciaBancariaEntity> root = c.from(PphAgenciaBancariaEntity.class);
            Join<PphAgenciaBancariaEntity, PphBancoEntity> banco = root.<PphAgenciaBancariaEntity, PphBancoEntity>join("pphBanco", JoinType.LEFT);
            c.select(root).where(cb.equal(root.get("nrAgencia"), nrAgencia), cb.equal(banco.get("id"), idBanco));
            return entityManager().createQuery(c).getResultList();
        } catch (Exception e) {
            throw DaoExceptionUtil.prepara(log, e);
        }
    }
}
