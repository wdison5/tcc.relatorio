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
import org.tcc.relatorio.dominio.PphEnderecoEntity;
import org.tcc.relatorio.persistencia.exception.util.DaoExceptionUtil;

/**
 *
 * @author Jose Wdison de Souza
 */
public class EnderecoRepo extends Repositorio {

    private static final Logger log = LoggerFactory.getLogger(EnderecoRepo.class);

    public Set<PphEnderecoEntity> listarPorNome(String nome) throws DaoException {

        try {
            CriteriaBuilder cb = entityManager().getCriteriaBuilder();
            CriteriaQuery<PphEnderecoEntity> c = cb.createQuery(PphEnderecoEntity.class);

            Root<PphEnderecoEntity> Endereco = c.from(PphEnderecoEntity.class);
            Endereco.fetch("funcionalidades", JoinType.LEFT);
            c.select(Endereco).
                    where(cb.equal(Endereco.get("nome"), nome));

            return new HashSet<PphEnderecoEntity>(entityManager().createQuery(c).getResultList());

        } catch (Exception e) {
            throw DaoExceptionUtil.prepara(log, e);
        }
    }

    public List<PphEnderecoEntity> listarPorNomeLike(String nome) throws DaoException {

        try {
            CriteriaBuilder cb = entityManager().getCriteriaBuilder();
            CriteriaQuery<PphEnderecoEntity> c = cb.createQuery(PphEnderecoEntity.class);

            Root<PphEnderecoEntity> Endereco = c.from(PphEnderecoEntity.class);
            Expression<String> path = Endereco.get("nome");
            c.select(Endereco).
                    where(cb.like(path, nome + "%"));

            return entityManager().createQuery(c).getResultList();

        } catch (Exception e) {
            throw DaoExceptionUtil.prepara(log, e);
        }
    }

    public List<PphEnderecoEntity> listarPorNomeLista(List<String> nomes) throws DaoException {

        try {
            CriteriaBuilder cb = entityManager().getCriteriaBuilder();
            CriteriaQuery<PphEnderecoEntity> c = cb.createQuery(PphEnderecoEntity.class);

            Root<PphEnderecoEntity> Endereco = c.from(PphEnderecoEntity.class);
            c.select(Endereco).
                    where(Endereco.get("nome").in(nomes));

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
    public PphEnderecoEntity buscarPorId(Long id) throws DaoException {

        try {
            CriteriaBuilder cb = entityManager().getCriteriaBuilder();
            CriteriaQuery<PphEnderecoEntity> c = cb.createQuery(PphEnderecoEntity.class);

            Root<PphEnderecoEntity> Endereco = c.from(PphEnderecoEntity.class);

            c.select(Endereco).
                    where(cb.equal(Endereco.get("id"), id));

            return entityManager().createQuery(c).getSingleResult();

        } catch (Exception e) {
            throw DaoExceptionUtil.prepara(log, e);
        }
    }

    public List<PphEnderecoEntity> pesquisaPorNome(String nomeEndereco) throws DaoException {
        try {
            CriteriaBuilder cb = entityManager().getCriteriaBuilder();
            CriteriaQuery<PphEnderecoEntity> c = cb.createQuery(PphEnderecoEntity.class);
            Root<PphEnderecoEntity> root = c.from(PphEnderecoEntity.class);

            c.select(root).
                    where(cb.like(root.<String>get("nmEndereco"), "%" + nomeEndereco + "%"));
            return entityManager().createQuery(c).getResultList();

        } catch (Exception e) {
            throw DaoExceptionUtil.prepara(log, e);
        }
    }
}
