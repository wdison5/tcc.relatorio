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
import org.tcc.relatorio.dominio.PphBancoEntity;
import org.tcc.relatorio.dominio.PphEstadoEntity;
import org.tcc.relatorio.persistencia.exception.util.DaoExceptionUtil;

/**
 *
 * @author Jose Wdison de Souza
 */
public class BancoRepo extends Repositorio {

    private static final Logger log = LoggerFactory.getLogger(BancoRepo.class);

    public Set<PphBancoEntity> listarPorNome(String nome) throws DaoException {

        try {
            CriteriaBuilder cb = entityManager().getCriteriaBuilder();
            CriteriaQuery<PphBancoEntity> c = cb.createQuery(PphBancoEntity.class);

            Root<PphBancoEntity> Banco = c.from(PphBancoEntity.class);
            Banco.fetch("funcionalidades", JoinType.LEFT);
            c.select(Banco).
                    where(cb.equal(Banco.get("nome"), nome));

            return new HashSet<PphBancoEntity>(entityManager().createQuery(c).getResultList());

        } catch (Exception e) {
            throw DaoExceptionUtil.prepara(log, e);
        }
    }

    public List<PphBancoEntity> listarPorNomeLike(String nome) throws DaoException {

        try {
            CriteriaBuilder cb = entityManager().getCriteriaBuilder();
            CriteriaQuery<PphBancoEntity> c = cb.createQuery(PphBancoEntity.class);

            Root<PphBancoEntity> root = c.from(PphBancoEntity.class);
            Expression<String> path = root.get("nome");
            c.select(root).
                    where(cb.like(path, nome + "%"));

            return entityManager().createQuery(c).getResultList();

        } catch (Exception e) {
            throw DaoExceptionUtil.prepara(log, e);
        }
    }

    public List<PphBancoEntity> listarPorNomeLista(List<String> nomes) throws DaoException {

        try {
            CriteriaBuilder cb = entityManager().getCriteriaBuilder();
            CriteriaQuery<PphBancoEntity> c = cb.createQuery(PphBancoEntity.class);

            Root<PphBancoEntity> root = c.from(PphBancoEntity.class);
            c.select(root).
                    where(root.get("nome").in(nomes));

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
    public PphBancoEntity buscarPorId(Long id) throws DaoException {

        try {
            CriteriaBuilder cb = entityManager().getCriteriaBuilder();
            CriteriaQuery<PphBancoEntity> c = cb.createQuery(PphBancoEntity.class);

            Root<PphBancoEntity> Banco = c.from(PphBancoEntity.class);

            c.select(Banco).
                    where(cb.equal(Banco.get("id"), id));

            return entityManager().createQuery(c).getSingleResult();

        } catch (Exception e) {
            throw DaoExceptionUtil.prepara(log, e);
        }
    }

    public List<PphBancoEntity> pesquisaPorNome(String nomeBanco) throws DaoException {
        try {
            CriteriaBuilder cb = entityManager().getCriteriaBuilder();
            CriteriaQuery<PphBancoEntity> c = cb.createQuery(PphBancoEntity.class);
            Root<PphBancoEntity> root = c.from(PphBancoEntity.class);

            c.select(root).
                    where(cb.like(root.<String>get("nmBanco"), "%" + nomeBanco + "%"));
            return entityManager().createQuery(c).getResultList();

        } catch (Exception e) {
            throw DaoExceptionUtil.prepara(log, e);
        }
    }
    
    public List<PphBancoEntity> list() throws DaoException {
        try {
            CriteriaBuilder cb = entityManager().getCriteriaBuilder();
            CriteriaQuery<PphBancoEntity> c = cb.createQuery(PphBancoEntity.class);
            Root<PphBancoEntity> root = c.from(PphBancoEntity.class);
            c.select(root);
            return entityManager().createQuery(c).getResultList();

        } catch (Exception e) {
            throw DaoExceptionUtil.prepara(log, e);
        }
    }
}
