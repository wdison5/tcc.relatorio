package org.tcc.relatorio.persistencia;

import org.tcc.relatorio.hammer.persistencia.exception.DaoException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tcc.relatorio.cap.dominio.UsuarioEntity;
import org.tcc.relatorio.dominio.EmpresaEntity;
import org.tcc.relatorio.enumeracao.Confirmacao;
import org.tcc.relatorio.persistencia.exception.util.DaoExceptionUtil;

/**
 * @author Jose Wdison
 */
public class EmpresaRepo extends Repositorio {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmpresaRepo.class);

    public EmpresaEntity buscarPorId(long id) throws DaoException {

        try {
            CriteriaBuilder cb = entityManager().getCriteriaBuilder();
            CriteriaQuery<EmpresaEntity> c = cb.createQuery(EmpresaEntity.class);

            Root<EmpresaEntity> root = c.from(EmpresaEntity.class);
            c.select(root).
                    where(cb.equal(root.get("id"), id),
                            cb.equal(root.get("flExclusao"), Confirmacao.NAO.getId()));

            List<EmpresaEntity> result = entityManager().createQuery(c).getResultList();
            return result.isEmpty() ? null : result.get(0);

        } catch (Exception e) {
            throw DaoExceptionUtil.prepara(LOGGER, e);
        }
    }

    public List<EmpresaEntity> list() throws DaoException {

        try {
            CriteriaBuilder cb = entityManager().getCriteriaBuilder();
            CriteriaQuery<EmpresaEntity> cq = cb.createQuery(EmpresaEntity.class);
            Root<EmpresaEntity> root = cq.from(EmpresaEntity.class);
            cq.select(root).where(cb.equal(root.get("flExclusao"), Confirmacao.NAO.getId()));
            return entityManager().createQuery(cq).getResultList();
        } catch (Exception e) {
            throw DaoExceptionUtil.prepara(LOGGER, e);
        }
    }

    public List<EmpresaEntity> list(UsuarioEntity usuario) throws DaoException {
        try {
            EntityManager em = entityManager();
            TypedQuery<EmpresaEntity> query = em.createQuery("SELECT i FROM UsuarioEntity u JOIN u.empresas i WHERE u.userId = :userid and u.flExclusao = :flExclusao and i.flExclusao = :flExclusao",
                    EmpresaEntity.class);
            return query.setParameter("userid", usuario.getUserId()).setParameter("flExclusao", Confirmacao.NAO.getId()).getResultList();
        } catch (Exception e) {
            throw DaoExceptionUtil.prepara(LOGGER, e);
        }
    }

    public List<EmpresaEntity> listPorIds_NomeECdLike(List<Long> lstEmpresaId, String nomeEmpresa, String codigoEmpresa) throws DaoException {
        try {
            CriteriaBuilder cb = entityManager().getCriteriaBuilder();
            CriteriaQuery<EmpresaEntity> q = cb.createQuery(EmpresaEntity.class);
            Root<EmpresaEntity> root = q.from(EmpresaEntity.class);
            q.select(root).where(cb.and(root.get("id").in(lstEmpresaId),
                    cb.equal(root.get("flExclusao"), Confirmacao.NAO.getId()),
                    cb.like(root.<String>get("nmEmpresa"), nomeEmpresa),
                    cb.like(root.<String>get("cdEmpresa"), codigoEmpresa)
            ));
            return entityManager().createQuery(q).getResultList();
        } catch (Exception e) {
            throw DaoExceptionUtil.prepara(LOGGER, e);
        }
    }

    public Long isNrUsuariosAssociado(Long idEmpresa, Integer flExclusao) throws DaoException {
        try {
            CriteriaBuilder cb = entityManager().getCriteriaBuilder();
            CriteriaQuery<Long> cqCount = cb.createQuery(Long.class);
            Root<UsuarioEntity> root = cqCount.from(UsuarioEntity.class);
            List<Predicate> criteriaListAnd = new ArrayList();

            Join<UsuarioEntity, EmpresaEntity> joinUsuarioEmpresa = root.<UsuarioEntity, EmpresaEntity>join("empresas", JoinType.INNER);
            criteriaListAnd.add(cb.equal(joinUsuarioEmpresa.get("id"), idEmpresa));
            criteriaListAnd.add(cb.equal(joinUsuarioEmpresa.get("flExclusao"), flExclusao));
            criteriaListAnd.add(cb.equal(root.get("flExclusao"), flExclusao));

            cqCount.select(cb.count(root)).where(cb.and(criteriaListAnd.toArray(new Predicate[0])));
            return entityManager().createQuery(cqCount).getSingleResult();
        } catch (Exception e) {
            throw DaoExceptionUtil.prepara(LOGGER, e);
        }
    }
    
    
    
    
    
    
    public Set<EmpresaEntity> listarPorNome(String nome) throws DaoException {

        try {
            CriteriaBuilder cb = entityManager().getCriteriaBuilder();
            CriteriaQuery<EmpresaEntity> c = cb.createQuery(EmpresaEntity.class);

            Root<EmpresaEntity> root = c.from(EmpresaEntity.class);
            c.select(root).
                    where(cb.equal(root.get("nmEmpresa"), nome));

            return new HashSet<EmpresaEntity>(entityManager().createQuery(c).getResultList());

        } catch (Exception e) {
            throw DaoExceptionUtil.prepara(LOGGER, e);
        }
    }

    public List<EmpresaEntity> listarPorNomeLike(String nome) throws DaoException {

        try {
            CriteriaBuilder cb = entityManager().getCriteriaBuilder();
            CriteriaQuery<EmpresaEntity> c = cb.createQuery(EmpresaEntity.class);

            Root<EmpresaEntity> root = c.from(EmpresaEntity.class);
            Expression<String> path = root.get("nmEmpresa");
            c.select(root).
                    where(cb.like(path, nome + "%"));

            return entityManager().createQuery(c).getResultList();

        } catch (Exception e) {
            throw DaoExceptionUtil.prepara(LOGGER, e);
        }
    }

    public List<EmpresaEntity> listarPorNomeLista(List<String> nomes) throws DaoException {

        try {
            CriteriaBuilder cb = entityManager().getCriteriaBuilder();
            CriteriaQuery<EmpresaEntity> c = cb.createQuery(EmpresaEntity.class);

            Root<EmpresaEntity> root = c.from(EmpresaEntity.class);
            c.select(root).
                    where(root.get("nmEmpresa").in(nomes));

            return entityManager().createQuery(c).getResultList();

        } catch (Exception e) {
            throw DaoExceptionUtil.prepara(LOGGER, e);
        }
    }
    
    public List<EmpresaEntity> listar(List<Long> idsEmpresa, int flAtivo) throws DaoException {
        try {
            CriteriaBuilder cb = entityManager().getCriteriaBuilder();
            CriteriaQuery<EmpresaEntity> c = cb.createQuery(EmpresaEntity.class);
            Root<EmpresaEntity> root = c.from(EmpresaEntity.class);
            c.select(root);
            
            if (idsEmpresa.size() > 0) {
                List<Predicate> predicados = new ArrayList<Predicate>();

                predicados.add(root.get("id").in(idsEmpresa));
                c.where(cb.and(predicados.toArray(new Predicate[]{})));
                return entityManager().createQuery(c).getResultList();
            } else {
                return new ArrayList<EmpresaEntity>();
            }
        } catch (Exception e) {
            throw DaoExceptionUtil.prepara(LOGGER, e);
        }
    }
}
