package org.tcc.relatorio.negocio;

import org.tcc.relatorio.enumeracao.Confirmacao;
import java.util.List;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import static javax.ejb.TransactionAttributeType.REQUIRED;
import javax.enterprise.inject.New;
import javax.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tcc.relatorio.cap.dominio.UsuarioEntity;
import org.tcc.relatorio.dominio.PphAtestadoVidaEntity;
import org.tcc.relatorio.negocio.exception.util.BCExceptionUtil;
import org.tcc.relatorio.negocio.util.EntityPreencherUtil;
import org.tcc.relatorio.negocio.validator.Validador;
import org.tcc.relatorio.negocio.validator.ValidadorUtil;
import org.tcc.relatorio.persistencia.AtestadoVidaRepo;
import org.tcc.relatorio.persistencia.BeneficiarioRepo;
import org.tcc.relatorio.hammer.persistencia.exception.BCException;
import org.tcc.relatorio.hammer.persistencia.exception.DaoException;

/**
 *
 * @author Jose Wdison de Souza
 */
@Stateless
public class AtestadoVidaBC {

    private static final Logger LOGGER = LoggerFactory.getLogger(AtestadoVidaBC.class);

    @Inject
    @New
    private AtestadoVidaRepo atestadoVidaRepo;
    @Inject
    @New
    private BeneficiarioRepo beneficiarioRepo;

    @Inject
    private UsuarioEntity usuario;

    private EntityPreencherUtil preencherEntity;
    @Inject
    private ValidadorUtil validador;

    /**
     * Construtor.
     */
    public AtestadoVidaBC() {
        LOGGER.debug("__| " + this.getClass().getSimpleName());
    }

    /**
     * Incluir usuário.
     *
     * @param atestadoVida
     * @param usuarioLogado
     * @throws BCException Erro de processamento da solicitação (geralemnte
     * acesso ao banco).
     */
    @TransactionAttribute(REQUIRED)
    public void incluir(PphAtestadoVidaEntity atestadoVida, UsuarioEntity usuarioLogado) throws BCException {
        try {
            setUsuario(usuarioLogado);
            process(atestadoVida, ValidadorUtil.INSERT);
            if (atestadoVida.isOk()) {
                atestadoVida.setFlExclusao(Confirmacao.NAO.getId());
                atestadoVida.setPphBeneficiario(beneficiarioRepo.buscarPorId(atestadoVida.getPphBeneficiario().getId()));
                atestadoVida.getPphBeneficiario().setDtObito(atestadoVida.getDtObito());
                atestadoVidaRepo.persist(atestadoVida);
            }
        } catch (Exception ex) {
            throw BCExceptionUtil.prepara(LOGGER, ex);
        }
    }

    private void process(PphAtestadoVidaEntity atestadoVida, Integer tipo) throws DaoException, BCException {
        preencherEntity.set(atestadoVida);
        validador.valida(atestadoVida, tipo);
    }

    @TransactionAttribute(REQUIRED)
    public void excluir(PphAtestadoVidaEntity atestadoVida, UsuarioEntity usuarioLogado) throws BCException {
        try{
            setUsuario(usuarioLogado);
            PphAtestadoVidaEntity entity = atestadoVidaRepo.buscarPorId(atestadoVida.getId());
            preencherEntity.set(entity);
            entity.setFlExclusao(Confirmacao.SIM.getId());
            atestadoVidaRepo.update(entity);
        }catch(Exception e){
            atestadoVida.getMsg().add("Erro ao exluir atestado de vida!");
            throw BCExceptionUtil.prepara(LOGGER, e);
        }
    }
    @TransactionAttribute(REQUIRED)
    public void atualizar(PphAtestadoVidaEntity atestadoVida, UsuarioEntity usuarioLogado) throws BCException {
        try {
            setUsuario(usuarioLogado);
            process(atestadoVida, ValidadorUtil.UPDATE);
            if (atestadoVida.isOk()) {
                if(!Validador.isNull(atestadoVida.getDtObito())){
                    atestadoVida.setPphBeneficiario(beneficiarioRepo.buscarPorId(atestadoVida.getPphBeneficiario().getId()));
                    atestadoVida.getPphBeneficiario().setDtObito(atestadoVida.getDtObito());
                }
                atestadoVidaRepo.update(atestadoVida);
            }
        } catch (Exception ex) {
            throw BCExceptionUtil.prepara(LOGGER, ex);
        }
    }

    public List<PphAtestadoVidaEntity> pesquisaPorNome(String nomeAtestadoVida) throws BCException {
        if (Validador.isBlank(nomeAtestadoVida)) {
            throw BCExceptionUtil.prepara(LOGGER, null);
        }
        try {
            return atestadoVidaRepo.pesquisaPorNome(nomeAtestadoVida);
        } catch (Exception ex) {
            throw BCExceptionUtil.prepara(LOGGER, ex);
        }
    }

    public List<PphAtestadoVidaEntity> pesquisaPorIdbeneficiario(Long idBeneficiario) throws BCException {
        List<PphAtestadoVidaEntity> list = null;
        try {
            list = atestadoVidaRepo.pesquisaPorIdbeneficiario(idBeneficiario);
        } catch (Exception ex) {
            throw BCExceptionUtil.prepara(LOGGER, ex);
        }
        return list;
    }

    public void setUsuario(UsuarioEntity usuario) {
        this.usuario = usuario;
        preencherEntity = new EntityPreencherUtil(usuario);
    }
}
