/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tcc.relatorio.persistencia;

import org.tcc.relatorio.hammer.persistencia.exception.DaoException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tcc.relatorio.dominio.PphUnidSaudeEntity;
import org.tcc.relatorio.dominio.PphBeneficiarioEntity;
import org.tcc.relatorio.dominio.PphUnidadePagadoraEntity;
import org.tcc.relatorio.persistencia.exception.util.DaoExceptionUtil;

/**
 *
 * @author Jose Wdison
 */
public class BeneficiarioRepo extends Repositorio {

    private static final Logger log = LoggerFactory.getLogger(BeneficiarioRepo.class);

    public Set<PphBeneficiarioEntity> listarPorNome(String nome) throws DaoException {

        try {
            CriteriaBuilder cb = entityManager().getCriteriaBuilder();
            CriteriaQuery<PphBeneficiarioEntity> c = cb.createQuery(PphBeneficiarioEntity.class);

            Root<PphBeneficiarioEntity> beneficiario = c.from(PphBeneficiarioEntity.class);
            beneficiario.fetch("funcionalidades", JoinType.LEFT);
            c.select(beneficiario).
               where(cb.equal(beneficiario.get("nome"), nome));

            return new HashSet<PphBeneficiarioEntity>(entityManager().createQuery(c).getResultList());

        } catch (Exception e) {
            throw DaoExceptionUtil.prepara(log, e);
        }
    }
    
    public List<PphBeneficiarioEntity> listarPorNomeLike(String nome) throws DaoException {

        try {
            CriteriaBuilder cb = entityManager().getCriteriaBuilder();
            CriteriaQuery<PphBeneficiarioEntity> c = cb.createQuery(PphBeneficiarioEntity.class);

            Root<PphBeneficiarioEntity> beneficiario = c.from(PphBeneficiarioEntity.class);
            Expression<String> path = beneficiario.get("nome");
            c.select(beneficiario).
               where(cb.like(path, nome + "%"));

            return entityManager().createQuery(c).getResultList();

        } catch (Exception e) {
            throw DaoExceptionUtil.prepara(log, e);
        }
    }
    
    public List<PphBeneficiarioEntity> listarPorNomeLista(List<String> nomes) throws DaoException {

        try {
            CriteriaBuilder cb = entityManager().getCriteriaBuilder();
            CriteriaQuery<PphBeneficiarioEntity> c = cb.createQuery(PphBeneficiarioEntity.class);

            Root<PphBeneficiarioEntity> beneficiario = c.from(PphBeneficiarioEntity.class);
            c.select(beneficiario).
               where(beneficiario.get("nome").in(nomes));

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
    public PphBeneficiarioEntity buscarPorId(Long id) throws DaoException {

        try {
            CriteriaBuilder cb = entityManager().getCriteriaBuilder();
            CriteriaQuery<PphBeneficiarioEntity> c = cb.createQuery(PphBeneficiarioEntity.class);

            Root<PphBeneficiarioEntity> beneficiario = c.from(PphBeneficiarioEntity.class);

            c.select(beneficiario).
               where(cb.equal(beneficiario.get("id"), id));

            return entityManager().createQuery(c).getSingleResult();

        } catch (Exception e) {
            throw DaoExceptionUtil.prepara(log, e);
        }
    }

    public List<PphBeneficiarioEntity> pesquisar(PphBeneficiarioEntity beneficiarioEntity, List<Long> idsUnidSaude, List<Long> idsUnidPagadora, boolean isAcessoGeral) throws DaoException {
        try {
            CriteriaBuilder cb = entityManager().getCriteriaBuilder();
            CriteriaQuery<PphBeneficiarioEntity> c = cb.createQuery(PphBeneficiarioEntity.class);
            Root<PphBeneficiarioEntity> root = c.from(PphBeneficiarioEntity.class);
            Join<PphBeneficiarioEntity, PphUnidSaudeEntity      > rootUS = root.<PphBeneficiarioEntity, PphUnidSaudeEntity      >join("pphUnidSaude"      , JoinType.LEFT);
            Join<PphBeneficiarioEntity, PphUnidadePagadoraEntity> rootUP = root.<PphBeneficiarioEntity, PphUnidadePagadoraEntity>join("pphUnidadePagadora", JoinType.LEFT);

            c.select(root);
            List<Predicate> predicados = new ArrayList<Predicate>();
            predicados.add(cb.like(root.<String>get("nmBeneficiario"), "%"+beneficiarioEntity.getNmBeneficiario().toUpperCase()+"%"));
            predicados.add(cb.equal(root.get("flExclusao"), beneficiarioEntity.getFlExclusao()));
            
            if(!isAcessoGeral){
                if (idsUnidSaude   .size() > 0                    ) predicados.add(rootUS.get("id").in(idsUnidSaude));
                if (idsUnidPagadora.size() > 0                    ) predicados.add(rootUP.get("id").in(idsUnidPagadora));
            }
            if (beneficiarioEntity.getPphUnidSaude()       != null) predicados.add(cb.equal(rootUS.get("id"), beneficiarioEntity.getPphUnidSaude().getId()));
            if (beneficiarioEntity.getPphUnidadePagadora() != null) predicados.add(cb.equal(rootUP.get("id"), beneficiarioEntity.getPphUnidadePagadora().getId()));
            if (beneficiarioEntity.getTpBeneficiario()     != null) predicados.add(cb.equal(root.get("tpBeneficiario"), beneficiarioEntity.getTpBeneficiario()));

            c.where(cb.and(predicados.toArray(new Predicate[]{})));

            return entityManager().createQuery(c).getResultList();
        } catch (Exception e) {
            throw DaoExceptionUtil.prepara(log, e);
        }
    }
    
    public List<PphBeneficiarioEntity> pesquisaPorNome(String nomeBeneficiario, PphUnidSaudeEntity unidSaude) throws DaoException {
        try {
            nomeBeneficiario = nomeBeneficiario!=null?nomeBeneficiario : "";
            
            CriteriaBuilder cb = entityManager().getCriteriaBuilder();
            CriteriaQuery<PphBeneficiarioEntity> c = cb.createQuery(PphBeneficiarioEntity.class);
            Root<PphBeneficiarioEntity> root = c.from(PphBeneficiarioEntity.class);
            Join<PphBeneficiarioEntity, PphUnidSaudeEntity> rootInsti = root.<PphBeneficiarioEntity, PphUnidSaudeEntity>join("pphUnidSaude", JoinType.LEFT);
            
            c.select(root)
                    .where(
                            cb.and(cb.like(root.<String>get("nmBeneficiario"), "%"+nomeBeneficiario+"%"), 
                                    cb.equal(rootInsti.get("id"), unidSaude.getId())));
            
            return entityManager().createQuery(c).getResultList();

        } catch (Exception e) {
            throw DaoExceptionUtil.prepara(log, e);
        }
    }
    
    public List<PphBeneficiarioEntity> pesquisaPorNome(String nomeBeneficiario, PphUnidadePagadoraEntity unidPagadora) throws DaoException {
        try {
            nomeBeneficiario = nomeBeneficiario!=null?nomeBeneficiario : "";
            
            CriteriaBuilder cb = entityManager().getCriteriaBuilder();
            CriteriaQuery<PphBeneficiarioEntity> c = cb.createQuery(PphBeneficiarioEntity.class);
            Root<PphBeneficiarioEntity> root = c.from(PphBeneficiarioEntity.class);
            Join<PphBeneficiarioEntity, PphUnidadePagadoraEntity> rootInsti = root.<PphBeneficiarioEntity, PphUnidadePagadoraEntity>join("pphUnidadePagadora", JoinType.LEFT);
            
            c.select(root).where(cb.and(cb.like(root.<String>get("nmBeneficiario"), "%"+nomeBeneficiario+"%"), cb.equal(rootInsti.get("id"), unidPagadora.getId())));
            
            return entityManager().createQuery(c).getResultList();

        } catch (Exception e) {
            throw DaoExceptionUtil.prepara(log, e);
        }
    }

    public PphBeneficiarioEntity buscarPorCPF(String nrCpfBeneficiario) throws DaoException {
            PphBeneficiarioEntity result = null;
        try {
            CriteriaBuilder cb = entityManager().getCriteriaBuilder();
            CriteriaQuery<PphBeneficiarioEntity> c = cb.createQuery(PphBeneficiarioEntity.class);
            Root<PphBeneficiarioEntity> root = c.from(PphBeneficiarioEntity.class);
            
            c.select(root).
               where(cb.equal(root.get("nrCpfBeneficiario"), nrCpfBeneficiario));
            List<PphBeneficiarioEntity> resultList = entityManager().createQuery(c).getResultList();
            
            if(resultList!=null&&resultList.size()>0){
                result = resultList.get(resultList.size()-1);
            }
            
            return result;
        } catch (Exception e) {
            throw DaoExceptionUtil.prepara(log, e);
        }
    }
}
