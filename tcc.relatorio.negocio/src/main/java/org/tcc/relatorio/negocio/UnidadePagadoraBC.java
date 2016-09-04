package org.tcc.relatorio.negocio;

import org.tcc.relatorio.enumeracao.Confirmacao;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import static javax.ejb.TransactionAttributeType.REQUIRED;
import javax.enterprise.inject.New;
import javax.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tcc.relatorio.cap.dominio.UsuarioEntity;
import org.tcc.relatorio.dominio.InstituicaoEntity;
import org.tcc.relatorio.dominio.PphUnidadePagadoraEntity;
import org.tcc.relatorio.negocio.exception.util.BCExceptionUtil;
import org.tcc.relatorio.persistencia.InstituicaoRepo;
import org.tcc.relatorio.persistencia.UnidadePagadoraRepo;
import org.tcc.relatorio.hammer.persistencia.exception.BCException;
import org.tcc.relatorio.hammer.persistencia.exception.DaoException;

/**
 *
 * @author 140200
 */
@Stateless
public class UnidadePagadoraBC {

    private static final Logger LOGGER = LoggerFactory.getLogger(UnidadePagadoraBC.class);

    @Inject
    @New
    private UnidadePagadoraRepo unidadePagadoraRepo;

    @Inject
    @New
    private InstituicaoRepo instituicaoRepo;

    @Inject
    private UsuarioEntity usuario;

    /**
     * Construtor.
     */
    public UnidadePagadoraBC() {
        LOGGER.debug("__| " + this.getClass().getSimpleName());
    }

    public PphUnidadePagadoraEntity getById(long id) throws BCException {
        try {
            PphUnidadePagadoraEntity unidadePagadora = unidadePagadoraRepo.buscarPorId(id);
            if (unidadePagadora == null) {
                throw new BCException("Unidade Pagadora não encontrada.");
            }
            return unidadePagadora;
        } catch (DaoException e) {
            LOGGER.error("BC/ error {}", e.getMessage());
            throw new BCException(e.getMessage());
        }
    }

    /**
     * Incluir usuário.
     *
     * @param entity
     * @throws BCException Erro de processamento da solicitação (geralemnte
     * acesso ao banco).
     */
    @TransactionAttribute(REQUIRED)
    public void incluir(PphUnidadePagadoraEntity entity) throws BCException {
        try {
            unidadePagadoraRepo.persist(entity);
        } catch (Exception ex) {
            throw BCExceptionUtil.prepara(LOGGER, ex);
        }
    }

    /**
     * Atualiza UsuarioEntity.
     *
     * @param entity
     * @throws BCException Erro de processamento da solicitação (geralemnte
     * acesso ao banco).
     */
    @TransactionAttribute(REQUIRED)
    public void atualizar(PphUnidadePagadoraEntity entity) throws BCException {
        try {
            unidadePagadoraRepo.persist(entity);
        } catch (Exception ex) {
            entity.getMsg().add("Erro ao atualizar Unidade de Saúde");
            throw BCExceptionUtil.prepara(LOGGER, ex);
        }
    }

	public List<PphUnidadePagadoraEntity> list(UsuarioEntity usuario) throws BCException {
        try {
            return usuario.isAcessoGeral() ? unidadePagadoraRepo.list(): unidadePagadoraRepo.list(usuario);
        } catch (Exception e) {
            throw BCExceptionUtil.prepara(LOGGER, e);
        }
    }

    public List<PphUnidadePagadoraEntity> list(boolean isAdmin, List<Long> idsUnidPagadora) throws BCException {
        try {
            List<Long> ids = new ArrayList<Long>();
            if (isAdmin) {
                InstituicaoEntity inst = new InstituicaoEntity();
                inst.setTpUnid("UP");
                List<InstituicaoEntity> listaInstituicao = (List) instituicaoRepo.listar(inst, inst, new String[0]);
                for (InstituicaoEntity instituicao : listaInstituicao) {
                    ids.add(instituicao.getPphUnidadePagadora().getId());
                }
            } else {
                ids = idsUnidPagadora;
            }
            return unidadePagadoraRepo.listar(ids, Confirmacao.SIM.getId());
        } catch (Exception ex) {
            throw BCExceptionUtil.prepara(LOGGER, ex);
        }
    }
    
    /**
     * Consultar usuario atravez do UserId.
     *
     * @param id
     * @return PphUnidadeSaudeEntity
     * @throws BCException Erro de processamento da solicitação (geralemnte
     * acesso ao banco).
     */
    public PphUnidadePagadoraEntity consultar(Long id) throws BCException {
        PphUnidadePagadoraEntity entity = null;
        try {
            if (id == null) {
                LOGGER.info("Id da unidade de saúde não informado");
            }else{
                entity = unidadePagadoraRepo.buscarPorId(id);
                if (entity == null) {
                    LOGGER.info("Unidade Saúde {} não encontrado", id);
                }
            }
            return entity;
        } catch (Exception e) {
            throw BCExceptionUtil.prepara(LOGGER, e);
        }
    }

    public List<PphUnidadePagadoraEntity> listAllNoCadInstit(int flAtivo) throws BCException {
        try {
            return unidadePagadoraRepo.listAllNoCadInstit(flAtivo);
        } catch (Exception e) {
            throw BCExceptionUtil.prepara(LOGGER, e);
        }
    }
    
    public List<PphUnidadePagadoraEntity> list() throws BCException {
        try {
            return unidadePagadoraRepo.list(Confirmacao.SIM.getId());
        } catch (Exception e) {
            throw BCExceptionUtil.prepara(LOGGER, e);
        }
    }
}
