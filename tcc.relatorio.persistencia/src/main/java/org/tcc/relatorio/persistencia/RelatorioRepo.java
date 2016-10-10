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
import org.tcc.relatorio.dominio.ProdutoEntity;
import org.tcc.relatorio.persistencia.exception.util.DaoExceptionUtil;

/**
 *
 * @author Eloy
 */
public class RelatorioRepo extends Repositorio {
    private static final Logger log = LoggerFactory.getLogger(RelatorioRepo.class);

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
    
    public List<ProdutoEntity> pesquisar(ProdutoEntity produtoInicial, ProdutoEntity produtoFinal) throws DaoException {
        try {
            CriteriaBuilder cb = entityManager().getCriteriaBuilder();
            CriteriaQuery<ProdutoEntity> c = cb.createQuery(ProdutoEntity.class);
            Root<ProdutoEntity> root = c.from(ProdutoEntity.class);
            c.select(root);
            List<Predicate> predicados = new ArrayList<Predicate>();
            if (produtoInicial.getDataReferencia()!= null) predicados.add(cb.greaterThanOrEqualTo(root.<Date  >get("dtEmpenho"), produtoInicial.getDataReferencia()));
            if (produtoFinal  .getDataReferencia()!= null) predicados.add(cb.lessThanOrEqualTo   (root.<Date  >get("dtEmpenho"), produtoFinal  .getDataReferencia()));
            
            c.where(cb.and(predicados.toArray(new Predicate[]{})));
            return entityManager().createQuery(c).getResultList();
        } catch (Exception e) {
            throw DaoExceptionUtil.prepara(log, e);
        }
    }
}
