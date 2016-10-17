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
import org.tcc.relatorio.dominio.EmpresaEntity;
import org.tcc.relatorio.negocio.exception.util.BCExceptionUtil;
import org.tcc.relatorio.negocio.validator.Validador;
import org.tcc.relatorio.negocio.validator.ValidadorUtil;
import org.tcc.relatorio.persistencia.EmpresaRepo;
import org.tcc.relatorio.hammer.persistencia.exception.BCException;

/**
 *
 * @author Jose Wdison
 */
@Stateless
public class EmpresaBC {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmpresaBC.class);

    @Inject
    @New
    private EmpresaRepo empresaRepo;
    @Inject
    private UsuarioRepo usuarioRepo;

    @Inject
    private UsuarioBC usuarioBC;

    @Inject
    private ValidadorUtil validador;

    /**
     * Construtor.
     */
    public EmpresaBC() {
        LOGGER.debug("__| " + getClass().getSimpleName());
    }

    public EmpresaEntity getById(long id) throws BCException {
        try {
            EmpresaEntity entidade = empresaRepo.buscarPorId(id);
            if (entidade == null) {
                throw new BCException("Empresa não encontrada.");
            }
            return entidade;
        } catch (Exception e) {
            throw BCExceptionUtil.prepara(LOGGER, e);
        }
    }

    public List<EmpresaEntity> list() throws BCException {
        try {
            return empresaRepo.list();
        } catch (Exception e) {
            throw BCExceptionUtil.prepara(LOGGER, e);
        }
    }

    public List<EmpresaEntity> list(String userId) throws BCException {
        try {
            // busca o usuário com base no userId
            UsuarioEntity usuario = new UsuarioEntity();
            usuario.setUserId(userId);
            usuario = usuarioBC.consultar(usuario);
            if (usuario == null || usuario.getId() == null) {
                throw new BCException("O usuário informado não foi encontrado.");
            }
            // busca as empresas do usuário
            return usuario.isAcessoGeral() ? empresaRepo.list() : empresaRepo.list(usuario);
        } catch (Exception e) {
            throw BCExceptionUtil.prepara(LOGGER, e);
        }
    }

    @TransactionAttribute(value = TransactionAttributeType.REQUIRED)
    public void incluir(EmpresaEntity empresa, UsuarioEntity usuarioLogado) throws BCException {
        try {
            validador.valida(empresa, ValidadorUtil.INSERT);
            if (empresa.isOk()) {
                empresa.setFlExclusao(Confirmacao.NAO.getId());
                empresaRepo.persist(empresa);
                UsuarioEntity usuario = usuarioRepo.find(UsuarioEntity.class, usuarioLogado.getId());
                usuario.getEmpresas().add(empresa);
                usuarioRepo.update(usuario);
            }
        } catch (Exception e) {
            throw BCExceptionUtil.prepara(LOGGER, e);
        }
    }

    @TransactionAttribute(REQUIRED)
    public void atualizar(EmpresaEntity empresa) throws BCException {
        try {
            validador.valida(empresa, ValidadorUtil.UPDATE);
            if (empresa.isOk()) {
                empresaRepo.update(empresa);
            }
        } catch (Exception ex) {
            throw BCExceptionUtil.prepara(LOGGER, ex);
        }
    }

    @TransactionAttribute(REQUIRED)
    public void excluir(EmpresaEntity empresa, UsuarioEntity usuarioLogado, List<Long> lstEmpresaId) throws BCException {
        try {
            Long nrUsuarios = empresaRepo.isNrUsuariosAssociado(empresa.getId(), Confirmacao.NAO.getId());
            if (Validador.isEquals(nrUsuarios, 1)) {
                if (Validador.isColecao(lstEmpresaId)) {
                    boolean contem = false;
                    for (Long id : lstEmpresaId) {
                        if (Validador.isEquals(id, empresa.getId())) {
                            contem = true;
                            break;
                        }
                    }
                    if (contem) {
                        UsuarioEntity usuario = usuarioRepo.find(UsuarioEntity.class, usuarioLogado.getId());
                        usuario.getEmpresas().remove(empresa);
                        usuarioRepo.update(usuario);
                    } else {
                        empresa.getMsg().add("Empresa contem um usuário ativo. Impossível excluir");
                    }
                }
            } else if (Validador.isMaior(nrUsuarios, 0)) {
                empresa.getMsg().add("Empresa contem usuários ativos. Impossível excluir");
            }

            if (empresa.isOk()) {
                EmpresaEntity entity = empresaRepo.buscarPorId(empresa.getId());
                entity.setFlExclusao(Confirmacao.SIM.getId());
                empresaRepo.update(entity);
                LOGGER.info("Empresa {} {} excluida", empresa.getNmEmpresa(), empresa.getId());
            }

        } catch (Exception ex) {
            throw BCExceptionUtil.prepara(LOGGER, ex);
        }
    }

    public List<EmpresaEntity> listPorIds_NomeECdLike(List<Long> lstEmpresaId, String nomeEmpresa, String codigoEmpresa) throws BCException {
        if (Validador.isColecao(lstEmpresaId)) {
            nomeEmpresa = "%" + (Validador.isBlank(nomeEmpresa) ? "" : nomeEmpresa) + "%";
            codigoEmpresa = "%" + (Validador.isBlank(codigoEmpresa) ? "" : codigoEmpresa) + "%";
        } else {
            lstEmpresaId = new ArrayList<Long>() {
                {
                    add(0L);
                }
            };
        }
        try {
            List<EmpresaEntity> lstInstitu = empresaRepo.listPorIds_NomeECdLike(lstEmpresaId, nomeEmpresa, codigoEmpresa);
            return lstInstitu;
        } catch (Exception e) {
            throw BCExceptionUtil.prepara(LOGGER, e);
        }
    }
}
