package org.tcc.relatorio.persistencia;

import org.tcc.relatorio.hammer.persistencia.exception.DaoException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tcc.relatorio.dominio.PphBeneficiarioEntity;
import org.tcc.relatorio.dominio.PphEmpenhoEntity;
import org.tcc.relatorio.dominio.PphUnidSaudeEntity;
import org.tcc.relatorio.dominio.PphUnidadePagadoraEntity;
import org.tcc.relatorio.persistencia.exception.util.DaoExceptionUtil;

/**
 *
 * @author Eloy
 */
public class EmpenhoRepo extends Repositorio {
    private static final Logger log = LoggerFactory.getLogger(EmpenhoRepo.class);

    public PphEmpenhoEntity buscarPorId(Long id) throws DaoException {

        try {
            CriteriaBuilder cb = entityManager().getCriteriaBuilder();
            CriteriaQuery<PphEmpenhoEntity> c = cb.createQuery(PphEmpenhoEntity.class);

            Root<PphEmpenhoEntity> beneficiario = c.from(PphEmpenhoEntity.class);

            c.select(beneficiario).
               where(cb.equal(beneficiario.get("id"), id));

            return entityManager().createQuery(c).getSingleResult();

        } catch (Exception e) {
            throw DaoExceptionUtil.prepara(log, e);
        }
    }
    
    public List<PphEmpenhoEntity> pesquisar(PphEmpenhoEntity empenhoInicial, PphEmpenhoEntity empenhoFinal, List<Long> idsUnidSaude, List<Long> idsUnidPagadora, boolean isAcessoGeral) throws DaoException {
        try {
            CriteriaBuilder cb = entityManager().getCriteriaBuilder();
            CriteriaQuery<PphEmpenhoEntity> c = cb.createQuery(PphEmpenhoEntity.class);
            Root<PphEmpenhoEntity> root = c.from(PphEmpenhoEntity.class);
            Join<PphEmpenhoEntity, PphBeneficiarioEntity   > rootBE = root  .<PphEmpenhoEntity, PphBeneficiarioEntity   >join("pphBeneficiario"   , JoinType.LEFT);
            Join<PphEmpenhoEntity, PphUnidSaudeEntity      > rootUS = rootBE.<PphEmpenhoEntity, PphUnidSaudeEntity      >join("pphUnidSaude"      , JoinType.LEFT);
            Join<PphEmpenhoEntity, PphUnidadePagadoraEntity> rootUP = rootBE.<PphEmpenhoEntity, PphUnidadePagadoraEntity>join("pphUnidadePagadora", JoinType.LEFT);

            c.select(root);
            List<Predicate> predicados = new ArrayList<Predicate>();
            if (empenhoInicial.getNrEmpenho() != null) predicados.add(cb.greaterThanOrEqualTo(root.<String>get("nrEmpenho"), empenhoInicial.getNrEmpenho()));
            if (empenhoFinal  .getNrEmpenho() != null) predicados.add(cb.lessThanOrEqualTo   (root.<String>get("nrEmpenho"), empenhoFinal  .getNrEmpenho()));
            if (empenhoInicial.getDtEmpenho() != null) predicados.add(cb.greaterThanOrEqualTo(root.<Date  >get("dtEmpenho"), empenhoInicial.getDtEmpenho()));
            if (empenhoFinal  .getDtEmpenho() != null) predicados.add(cb.lessThanOrEqualTo   (root.<Date  >get("dtEmpenho"), empenhoFinal  .getDtEmpenho()));

            if (empenhoInicial.getPphBeneficiario().getPphUnidSaude()      != null) predicados.add(cb.greaterThanOrEqualTo(rootUS.<Long>get("id"), empenhoInicial.getPphBeneficiario().getPphUnidSaude()      .getId()));
            if (empenhoFinal  .getPphBeneficiario().getPphUnidSaude()      != null) predicados.add(cb.lessThanOrEqualTo   (rootUS.<Long>get("id"), empenhoFinal  .getPphBeneficiario().getPphUnidSaude()      .getId()));
            if (empenhoInicial.getPphBeneficiario().getPphUnidadePagadora()!= null) predicados.add(cb.greaterThanOrEqualTo(rootUP.<Long>get("id"), empenhoInicial.getPphBeneficiario().getPphUnidadePagadora().getId()));
            if (empenhoFinal  .getPphBeneficiario().getPphUnidadePagadora()!= null) predicados.add(cb.lessThanOrEqualTo   (rootUP.<Long>get("id"), empenhoFinal  .getPphBeneficiario().getPphUnidadePagadora().getId()));
            
            predicados.add(cb.equal(root.get("flExclusao"), empenhoInicial.getFlExclusao()));
            
            if(!isAcessoGeral){
                if (idsUnidSaude   .size() > 0) predicados.add(rootUS.get("id").in(idsUnidSaude));
                if (idsUnidPagadora.size() > 0) predicados.add(rootUP.get("id").in(idsUnidPagadora));
            }

            c.where(cb.and(predicados.toArray(new Predicate[]{})));

            return entityManager().createQuery(c).getResultList();
        } catch (Exception e) {
            throw DaoExceptionUtil.prepara(log, e);
        }
    }

}
