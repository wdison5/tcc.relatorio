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
import org.tcc.relatorio.dominio.PphComunicacaoEntity;
import org.tcc.relatorio.persistencia.exception.util.DaoExceptionUtil;

/**
 *
 * @author Jose Wdison de Souza
 */
public class ComunicacaoRepo extends Repositorio {

    private static final Logger log = LoggerFactory.getLogger(ComunicacaoRepo.class);

    public Set<PphComunicacaoEntity> listarPorNome(String nome) throws DaoException {

        try {
            CriteriaBuilder cb = entityManager().getCriteriaBuilder();
            CriteriaQuery<PphComunicacaoEntity> c = cb.createQuery(PphComunicacaoEntity.class);

            Root<PphComunicacaoEntity> Comunicacao = c.from(PphComunicacaoEntity.class);
            Comunicacao.fetch("funcionalidades", JoinType.LEFT);
            c.select(Comunicacao).
                    where(cb.equal(Comunicacao.get("nome"), nome));

            return new HashSet<PphComunicacaoEntity>(entityManager().createQuery(c).getResultList());

        } catch (Exception e) {
            throw DaoExceptionUtil.prepara(log, e);
        }
    }

    public List<PphComunicacaoEntity> listarPorNomeLike(String nome) throws DaoException {

        try {
            CriteriaBuilder cb = entityManager().getCriteriaBuilder();
            CriteriaQuery<PphComunicacaoEntity> c = cb.createQuery(PphComunicacaoEntity.class);

            Root<PphComunicacaoEntity> Comunicacao = c.from(PphComunicacaoEntity.class);
            Expression<String> path = Comunicacao.get("nome");
            c.select(Comunicacao).
                    where(cb.like(path, nome + "%"));

            return entityManager().createQuery(c).getResultList();

        } catch (Exception e) {
            throw DaoExceptionUtil.prepara(log, e);
        }
    }

    public List<PphComunicacaoEntity> listarPorNomeLista(List<String> nomes) throws DaoException {

        try {
            CriteriaBuilder cb = entityManager().getCriteriaBuilder();
            CriteriaQuery<PphComunicacaoEntity> c = cb.createQuery(PphComunicacaoEntity.class);

            Root<PphComunicacaoEntity> Comunicacao = c.from(PphComunicacaoEntity.class);
            c.select(Comunicacao).
                    where(Comunicacao.get("nome").in(nomes));

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
    public PphComunicacaoEntity buscarPorId(Long id) throws DaoException {

        try {
            CriteriaBuilder cb = entityManager().getCriteriaBuilder();
            CriteriaQuery<PphComunicacaoEntity> c = cb.createQuery(PphComunicacaoEntity.class);

            Root<PphComunicacaoEntity> Comunicacao = c.from(PphComunicacaoEntity.class);

            c.select(Comunicacao).
                    where(cb.equal(Comunicacao.get("id"), id));

            return entityManager().createQuery(c).getSingleResult();

        } catch (Exception e) {
            throw DaoExceptionUtil.prepara(log, e);
        }
    }

    public List<PphComunicacaoEntity> pesquisaPorNome(String nomeComunicacao) throws DaoException {
        try {
            CriteriaBuilder cb = entityManager().getCriteriaBuilder();
            CriteriaQuery<PphComunicacaoEntity> c = cb.createQuery(PphComunicacaoEntity.class);
            Root<PphComunicacaoEntity> root = c.from(PphComunicacaoEntity.class);

            c.select(root).
                    where(cb.like(root.<String>get("nmComunicacao"), "%" + nomeComunicacao + "%"));
            return entityManager().createQuery(c).getResultList();

        } catch (Exception e) {
            throw DaoExceptionUtil.prepara(log, e);
        }
    }
}
