package org.tcc.relatorio.persistencia;

import org.tcc.relatorio.hammer.persistencia.exception.DaoException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tcc.relatorio.dominio.PphContatoEntity;
import org.tcc.relatorio.persistencia.exception.util.DaoExceptionUtil;

/**
 *
 * @author Eloy
 */
public class ContatoRepo extends Repositorio {

    private static final Logger log = LoggerFactory.getLogger(ContatoRepo.class);

    public PphContatoEntity buscarPorId(Long id) throws DaoException {
        try {
            CriteriaBuilder cb = entityManager().getCriteriaBuilder();
            CriteriaQuery<PphContatoEntity> c = cb.createQuery(PphContatoEntity.class);
            Root<PphContatoEntity> Contato = c.from(PphContatoEntity.class);
            c.select(Contato).where(cb.equal(Contato.get("id"), id));
            return entityManager().createQuery(c).getSingleResult();
        } catch (Exception e) {
            throw DaoExceptionUtil.prepara(log, e);
        }
    }
}