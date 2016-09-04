package org.tcc.relatorio.negocio;

import org.tcc.relatorio.enumeracao.Confirmacao;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import static javax.ejb.TransactionAttributeType.REQUIRED;
import javax.enterprise.inject.New;
import javax.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tcc.relatorio.cap.dominio.UsuarioEntity;
import org.tcc.relatorio.cap.negocio.UsuarioBC;
import org.tcc.relatorio.cap.persistencia.UsuarioRepo;
import org.tcc.relatorio.dominio.InstituicaoEntity;
import org.tcc.relatorio.dominio.PphUnidSaudeEntity;
import org.tcc.relatorio.dominio.PphUnidadePagadoraEntity;
import org.tcc.relatorio.negocio.exception.util.BCExceptionUtil;
import org.tcc.relatorio.negocio.validator.TipoUnidade;
import org.tcc.relatorio.negocio.validator.Validador;
import org.tcc.relatorio.negocio.validator.ValidadorUtil;
import org.tcc.relatorio.persistencia.InstituicaoRepo;
import org.tcc.relatorio.hammer.persistencia.exception.BCException;

/**
 *
 * @author Jose Wdison
 */
@Stateless
public class InstituicaoBC {

    private static final Logger LOGGER = LoggerFactory.getLogger(InstituicaoBC.class);

    @Inject
    @New
    private InstituicaoRepo instituicaoRepo;
    @Inject
    private UsuarioRepo usuarioRepo;

    @Inject
    private UsuarioBC usuarioBC;
    @Inject
    private UnidadePagadoraBC unidadePagadoraBC;
    @Inject
    private UnidSaudeBC unidSaudeBC;

    @Inject
    private ValidadorUtil validador;

    /**
     * Construtor.
     */
    public InstituicaoBC() {
        LOGGER.debug("__| " + getClass().getSimpleName());
    }

    public InstituicaoEntity getById(long id) throws BCException {
        try {
            InstituicaoEntity entidade = instituicaoRepo.getById(id);
            if (entidade == null) {
                throw new BCException("Instituição não encontrada.");
            }
            return entidade;
        } catch (Exception e) {
            throw BCExceptionUtil.prepara(LOGGER, e);
        }
    }

    public List<InstituicaoEntity> list() throws BCException {
        try {
            return instituicaoRepo.list();
        } catch (Exception e) {
            throw BCExceptionUtil.prepara(LOGGER, e);
        }
    }

    public List<InstituicaoEntity> listComUSaudUPaga(String userId) throws BCException {
        List<InstituicaoEntity> lstInstitu = list(userId);
        if (Validador.isColecao(lstInstitu)) {
            for (InstituicaoEntity institu : lstInstitu) {
                PphUnidSaudeEntity unidSaude = institu.getPphUnidSaude();
                if (unidSaude != null && unidSaude.getId() != null) {
                    unidSaude.getNome();
                }
                PphUnidadePagadoraEntity unidadePagadora = institu.getPphUnidadePagadora();
                if (unidadePagadora != null && unidadePagadora.getId() != null) {
                    unidadePagadora.getNmUnidadePagadora();
                }
            }
        }
        return lstInstitu;
    }

    public List<InstituicaoEntity> list(String userId) throws BCException {
        try {
            // busca o usuário com base no userId
            UsuarioEntity usuario = new UsuarioEntity();
            usuario.setUserId(userId);
            usuario = usuarioBC.consultar(usuario);
            if (usuario == null || usuario.getId() == null) {
                throw new BCException("O usuário informado não foi encontrado.");
            }
            // busca as unidades saude do usuário
            return usuario.isAcessoGeral() ? instituicaoRepo.list() : instituicaoRepo.list(usuario);
        } catch (Exception e) {
            throw BCExceptionUtil.prepara(LOGGER, e);
        }
    }

    @TransactionAttribute(value = TransactionAttributeType.REQUIRED)
    public void incluir(InstituicaoEntity instituicao, UsuarioEntity usuarioLogado) throws BCException {
        try {
            validador.valida(instituicao, ValidadorUtil.INSERT);
            if (instituicao.isOk()) {
                if (Validador.isEquals(instituicao.getTpUnid(), TipoUnidade.SAUDE.getCod())) {
                    PphUnidSaudeEntity unidSaude = unidSaudeBC.getById(instituicao.getPphUnidSaude().getId());
                    if (unidSaude.getInstituicao() == null || unidSaude.getInstituicao().getId() == null) {
                        instituicao.setPphUnidSaude(unidSaude);
                    } else {
                        instituicao.getMsg().add("Unidade de saúde já cadastrada.");
                    }
                } else if (Validador.isEquals(instituicao.getTpUnid(), TipoUnidade.PAGADORA.getCod())) {
                    PphUnidadePagadoraEntity unidPagadora = unidadePagadoraBC.getById(instituicao.getPphUnidadePagadora().getId());
                    if (unidPagadora.getInstituicao() == null || unidPagadora.getInstituicao().getId() == null) {
                        instituicao.setPphUnidadePagadora(unidPagadora);
                    } else {
                        instituicao.getMsg().add("Unidade de pagadora já cadastrada.");
                    }
                }

                if (instituicao.isOk()) {
                    instituicao.setFlExclusao(Confirmacao.NAO.getId());
                    instituicaoRepo.persist(instituicao);
                    UsuarioEntity usuario = usuarioRepo.find(UsuarioEntity.class, usuarioLogado.getId());
                    usuario.getInstituicoes().add(instituicao);
                    usuarioRepo.update(usuario);
                }
            }
        } catch (Exception e) {
            throw BCExceptionUtil.prepara(LOGGER, e);
        }
    }

    @TransactionAttribute(REQUIRED)
    public void atualizar(InstituicaoEntity unidSaude) throws BCException {
        try {
            validador.valida(unidSaude, ValidadorUtil.UPDATE);
            if (unidSaude.isOk()) {
                instituicaoRepo.update(unidSaude);
            }
        } catch (Exception ex) {
            throw BCExceptionUtil.prepara(LOGGER, ex);
        }
    }

    @TransactionAttribute(REQUIRED)
    public void excluir(InstituicaoEntity instituicao, UsuarioEntity usuarioLogado, List<Long> lstInstituicaoId) throws BCException {
        try {
            Long nrUsuarios = instituicaoRepo.isNrUsuariosAssociado(instituicao.getId(), Confirmacao.NAO.getId());
            if (Validador.isEquals(nrUsuarios, 1)) {
                if(Validador.isColecao(lstInstituicaoId)){
                    boolean contem = false;
                    for (Long id : lstInstituicaoId) {
                        if(Validador.isEquals(id, instituicao.getId())){
                            contem = true;
                            break;
                        }
                    }
                    if(contem){
                        UsuarioEntity usuario = usuarioRepo.find(UsuarioEntity.class, usuarioLogado.getId());
                        usuario.getInstituicoes().remove(instituicao);
                        usuarioRepo.update(usuario);
                    }else{
                        instituicao.getMsg().add("Instituição contem um usuário ativo. Impossível excluir");
                    }
                }
            } else if (Validador.isMaior(nrUsuarios, 0)) {
                instituicao.getMsg().add("Instituição contem usuários ativos. Impossível excluir");
            }
            
            if (instituicao.isOk()) {
                InstituicaoEntity entity = instituicaoRepo.getById(instituicao.getId());
                entity.setFlExclusao(Confirmacao.SIM.getId());
                instituicaoRepo.update(entity);
                LOGGER.info("Instituição {} {} excluida", instituicao.getTpUnid(), instituicao.getId());
            }

        } catch (Exception ex) {
            throw BCExceptionUtil.prepara(LOGGER, ex);
        }
    }

    public List<InstituicaoEntity> listByUPaga(List<Long> lstUnidPagadoraId, String nomeUnidPagadora, String codigoUnidPagadora) throws BCException {
        if (Validador.isColecao(lstUnidPagadoraId)) {
            nomeUnidPagadora = "%" + (Validador.isBlank(nomeUnidPagadora) ? "" : nomeUnidPagadora) + "%";
            codigoUnidPagadora = "%" + (Validador.isBlank(codigoUnidPagadora) ? "" : codigoUnidPagadora) + "%";
        } else {
            lstUnidPagadoraId = new ArrayList<Long>() {
                {
                    add(0L);
                }
            };
        }
        try {
            List<InstituicaoEntity> lstInstitu = instituicaoRepo.listByUPaga(lstUnidPagadoraId, nomeUnidPagadora, codigoUnidPagadora);
            if (Validador.isColecao(lstInstitu)) {
                for (InstituicaoEntity institu : lstInstitu) {
                    PphUnidadePagadoraEntity unidadePagadora = institu.getPphUnidadePagadora();
                    if (unidadePagadora != null && unidadePagadora.getId() != null) {
                        unidadePagadora.getNmUnidadePagadora();
                    }
                }
            }
            return lstInstitu;
        } catch (Exception e) {
            throw BCExceptionUtil.prepara(LOGGER, e);
        }
    }

    public List<InstituicaoEntity> listByUSaud(List<Long> lstUnidSaudeId, String nomeUnidSaude, String codigoUnidSaude) throws BCException {
        if (Validador.isColecao(lstUnidSaudeId)) {
            nomeUnidSaude = "%" + (Validador.isBlank(nomeUnidSaude) ? "" : nomeUnidSaude) + "%";
            codigoUnidSaude = "%" + (Validador.isBlank(codigoUnidSaude) ? "" : codigoUnidSaude) + "%";
        } else {
            lstUnidSaudeId = new ArrayList<Long>() {
                {
                    add(0L);
                }
            };
        }
        try {
            List<InstituicaoEntity> lstInstitu = instituicaoRepo.listByUSaud(lstUnidSaudeId, nomeUnidSaude, codigoUnidSaude);
            if (Validador.isColecao(lstInstitu)) {
                for (InstituicaoEntity institu : lstInstitu) {
                    PphUnidSaudeEntity unidadeSaude = institu.getPphUnidSaude();
                    if (unidadeSaude != null && unidadeSaude.getId() != null) {
                        unidadeSaude.getNome();
                    }
                }
            }
            return lstInstitu;
        } catch (Exception e) {
            throw BCExceptionUtil.prepara(LOGGER, e);
        }
    }
}
