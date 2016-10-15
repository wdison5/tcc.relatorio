package org.tcc.relatorio.persistencia;

import org.tcc.relatorio.hammer.persistencia.exception.DaoException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tcc.relatorio.cap.dominio.UsuarioEntity;
import org.tcc.relatorio.dominio.PphEmpenhoEntity;
import org.tcc.relatorio.dominio.ProdutoEntity;
import org.tcc.relatorio.persistencia.exception.util.DaoExceptionUtil;

/**
 *
 * @author Eloy
 */
public class TccRelatorioRepo extends Repositorio {
    private static final Logger log = LoggerFactory.getLogger(TccRelatorioRepo.class);

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
    
    public List<ProdutoEntity> pesquisar(ProdutoEntity produtoInicial, ProdutoEntity produtoFinal, UsuarioEntity usuarioLogado, Object ... idsTpProd) throws DaoException {
        try {
            CriteriaBuilder cb = entityManager().getCriteriaBuilder();
            CriteriaQuery<ProdutoEntity> c = cb.createQuery(ProdutoEntity.class);
            Root<ProdutoEntity> root = c.from(ProdutoEntity.class);
//            root.fetch("tipoProduto");
            List<Predicate> predicados = new ArrayList<Predicate>();
            if (produtoInicial.getDataReferencia()!= null) predicados.add(cb.greaterThanOrEqualTo(root.<Date  >get("dataReferencia"), produtoInicial.getDataReferencia()));
            if (produtoFinal  .getDataReferencia()!= null) predicados.add(cb.lessThanOrEqualTo   (root.<Date  >get("dataReferencia"), produtoFinal  .getDataReferencia()));
            if (idsTpProd != null && idsTpProd.length > 0) predicados.add(root.get("tipoProduto").get("id").in(idsTpProd));
            predicados.add(cb.or(cb.equal(root.get("idUsuario"), usuarioLogado.getId()), root.get("idInstituicao").in(new Object[]{1})));//TODO deve ser os ids Corretos das instituicoes
            
            c.select(cb.construct(ProdutoEntity.class, root.get("descricao")
                            ,root.get("dataReferencia")
                            ,root.get("valorUnitario")
                            ,root.get("valorCusto")
                            ,root.get("tipoProduto").get("id")
                            ,root.get("tipoProduto").get("descricao")
                            ,cb.sumAsLong(root.<Integer>get("quantidade")).as(Integer.class).alias("quantidade")
                    ))
                    .where(cb.and(predicados.toArray(new Predicate[]{})))
                    .groupBy(root.get("descricao")
                            ,root.get("dataReferencia")
                            ,root.get("valorUnitario")
                            ,root.get("valorCusto")
                            ,root.get("tipoProduto").get("id")
                            ,root.get("tipoProduto").get("descricao")
                            )
                    .orderBy(cb.asc(root.get("dataReferencia")), 
                            cb.desc(cb.sumAsLong(root.<Integer>get("quantidade"))));
            return entityManager().createQuery(c)//.setMaxResults(5)
                    .getResultList();
        } catch (Exception e) {
            throw DaoExceptionUtil.prepara(log, e);
        }
    }
}
