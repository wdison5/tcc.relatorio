package org.tcc.relatorio.negocio;

import java.util.List;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import static javax.ejb.TransactionAttributeType.REQUIRED;
import javax.enterprise.inject.New;
import javax.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tcc.relatorio.cap.dominio.UsuarioEntity;
import org.tcc.relatorio.dominio.PphAgenciaBancariaEntity;
import org.tcc.relatorio.negocio.exception.util.BCExceptionUtil;
import org.tcc.relatorio.negocio.util.EntityPreencherUtil;
import org.tcc.relatorio.negocio.validator.ValidadorUtil;
import org.tcc.relatorio.persistencia.AgenciaBancariaRepo;
import org.tcc.relatorio.hammer.persistencia.exception.BCException;
import org.tcc.relatorio.hammer.persistencia.exception.DaoException;

/**
 *
 * @author Jose Wdison de Souza
 */
@Stateless
public class AgenciaBancariaBC {

    private static final Logger LOGGER = LoggerFactory.getLogger(AgenciaBancariaBC.class);

    @Inject
    @New
    private AgenciaBancariaRepo agenciaBancariaRepo;

    @Inject
    private UsuarioEntity usuario;

    private EntityPreencherUtil preencherEntity;
    @Inject
    private ValidadorUtil validador;

    /**
     * Construtor.
     */
    public AgenciaBancariaBC() {
        LOGGER.debug("__| " + this.getClass().getSimpleName());
    }

    @TransactionAttribute(REQUIRED)
    public void incluir(PphAgenciaBancariaEntity agenciaBancaria, UsuarioEntity usuarioLogado) throws BCException {
        try {
            setUsuario(usuarioLogado);
            process(agenciaBancaria, ValidadorUtil.INSERT);
            
            if (agenciaBancaria.isOk()) {
                List<PphAgenciaBancariaEntity> lstEntity = agenciaBancariaRepo.getByIdBancoCodAgencia(agenciaBancaria.getPphBanco().getId(), agenciaBancaria.getNrAgencia());
                if(lstEntity==null || lstEntity.isEmpty()){
                    agenciaBancariaRepo.persist(agenciaBancaria);
                }else{
                    agenciaBancaria.getMsg().add("Número de agência já existe para o banco.");
                }
            }
        } catch (DaoException ex) {
            throw BCExceptionUtil.prepara(LOGGER, ex);
        }
    }

    @TransactionAttribute(REQUIRED)
    public void atualizar(PphAgenciaBancariaEntity agenciaBancaria, UsuarioEntity usuarioLogado) throws BCException {
        try {
            setUsuario(usuarioLogado);
            process(agenciaBancaria, ValidadorUtil.UPDATE);
            if (agenciaBancaria.isOk()) {
                agenciaBancariaRepo.update(agenciaBancaria);
            }
        } catch (DaoException ex) {
            throw BCExceptionUtil.prepara(LOGGER, ex);
        }
    }

    private void process(PphAgenciaBancariaEntity entity, Integer tipo) throws DaoException, BCException {
        preencherEntity.set(entity);
        validador.valida(entity, tipo);
    }

    public List<PphAgenciaBancariaEntity> pesquisaPorNome(String nomeAgenciaBancaria) throws BCException {
        if (nomeAgenciaBancaria == null || "".equals(nomeAgenciaBancaria)) {
            throw BCExceptionUtil.prepara(LOGGER, null);
        }
        try {
            LOGGER.debug(usuario.toString());
            return agenciaBancariaRepo.pesquisaPorNome(nomeAgenciaBancaria);
        } catch (Exception ex) {
            throw BCExceptionUtil.prepara(LOGGER, ex);
        }
    }

    public List<PphAgenciaBancariaEntity> pesquisaPorIdBanco(Long idBanco) throws BCException {
        try {
            return agenciaBancariaRepo.pesquisaPorIdBanco(idBanco);
        } catch (Exception ex) {
            throw BCExceptionUtil.prepara(LOGGER, ex);
        }
    }

    public PphAgenciaBancariaEntity buscarPorId(Long id) throws BCException {
        try {
            PphAgenciaBancariaEntity agencia = agenciaBancariaRepo.buscarPorId(id);
            if (agencia == null) {
                throw new BCException("Agencia bancaria não encontrada.");
            }
            return agencia;
        } catch (Exception e) {
            LOGGER.error("BC/ error {}", e.getMessage());
            throw new BCException(e.getMessage());
        }
    }

    public PphAgenciaBancariaEntity getByIdComMunicipio(Long idAgencia) throws BCException {
        PphAgenciaBancariaEntity agenciaBancaria = buscarPorId(idAgencia);
        agenciaBancaria.getPphMunicipio().getNmMunicipio();
        agenciaBancaria.getPphMunicipio().getPphEstado().getNmEstado();
        return agenciaBancaria;
    }

    public void setUsuario(UsuarioEntity usuario) {
        this.usuario = usuario;
        preencherEntity = new EntityPreencherUtil(usuario);
    }
}
