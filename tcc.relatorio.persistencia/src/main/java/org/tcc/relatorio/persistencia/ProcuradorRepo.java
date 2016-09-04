package org.tcc.relatorio.persistencia;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tcc.relatorio.dominio.PphProcuradorEntity;
import org.tcc.relatorio.persistencia.exception.util.DaoExceptionUtil;
import org.tcc.relatorio.hammer.persistencia.exception.DaoException;

/**
 *
 * @author Eloy
 */
public class ProcuradorRepo extends Repositorio {

    private static final Logger log = LoggerFactory.getLogger(ProcuradorRepo.class);

    public PphProcuradorEntity buscarPorId(Long id) throws DaoException {

        try {
            CriteriaBuilder cb = entityManager().getCriteriaBuilder();
            CriteriaQuery<PphProcuradorEntity> c = cb.createQuery(PphProcuradorEntity.class);
            Root<PphProcuradorEntity> beneficiario = c.from(PphProcuradorEntity.class);
            c.select(beneficiario).where(cb.equal(beneficiario.get("id"), id));

            return entityManager().createQuery(c).getSingleResult();
        } catch (Exception e) {
            throw DaoExceptionUtil.prepara(log, e);
        }
    }
    
    public List<PphProcuradorEntity> listarPorNome(String nome) throws DaoException {

        try {
            CriteriaBuilder cb = entityManager().getCriteriaBuilder();
            CriteriaQuery<PphProcuradorEntity> c = cb.createQuery(PphProcuradorEntity.class);

            Root<PphProcuradorEntity> procurador = c.from(PphProcuradorEntity.class);
            //beneficiario.fetch("funcionalidades", JoinType.LEFT);
            c.select(procurador);//.where(cb.equal(procurador.get("nmProcurador"), nome));

            return new ArrayList<PphProcuradorEntity>(entityManager().createQuery(c).getResultList());

        } catch (Exception e) {
            throw DaoExceptionUtil.prepara(log, e);
        }
    }


}
