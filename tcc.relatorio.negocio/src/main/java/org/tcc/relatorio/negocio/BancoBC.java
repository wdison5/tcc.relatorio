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
import org.tcc.relatorio.dominio.PphBancoEntity;
import org.tcc.relatorio.negocio.exception.util.BCExceptionUtil;
import org.tcc.relatorio.persistencia.BancoRepo;
import org.tcc.relatorio.hammer.persistencia.exception.BCException;
import org.tcc.relatorio.hammer.persistencia.exception.DaoException;

/**
 *
 * @author Jose Wdison de Souza
 */
@Stateless
public class BancoBC {

    private static final Logger LOGGER = LoggerFactory.getLogger(BancoBC.class);

    @Inject
    @New
    private BancoRepo bancoRepo;

    @Inject
    private UsuarioEntity usuario;

    /**
     * Construtor.
     */
    public BancoBC() {
        LOGGER.debug("__| " + this.getClass().getSimpleName());
    }

    /**
     * Incluir usuário.
     *
     * @param Banco
     * @throws BCException Erro de processamento da solicitação (geralemnte
     * acesso ao banco).
     */
    @TransactionAttribute(REQUIRED)
    public void incluir(PphBancoEntity Banco) throws BCException {
        try {
            if (Banco == null || Banco.getId() != null) {

            }
            bancoRepo.persist(Banco);
        } catch (DaoException ex) {

            throw BCExceptionUtil.prepara(LOGGER, ex);
        }
    }

    /**
     * Atualiza UsuarioEntity.
     *
     * @param Banco
     * @throws BCException Erro de processamento da solicitação (geralemnte
     * acesso ao banco).
     */
    @TransactionAttribute(REQUIRED)
    public void atualizar(PphBancoEntity Banco) throws BCException {
        try {
            bancoRepo.persist(Banco);
        } catch (DaoException ex) {
            throw BCExceptionUtil.prepara(LOGGER, ex);
        }
    }

    public List<PphBancoEntity> pesquisaPorNome(String nomeBanco) throws BCException {
        if (nomeBanco == null || "".equals(nomeBanco)) {
            throw BCExceptionUtil.prepara(LOGGER, null);
        }
        try {
            LOGGER.debug(usuario.toString());
            return bancoRepo.pesquisaPorNome(nomeBanco);
        } catch (Exception ex) {
            throw BCExceptionUtil.prepara(LOGGER, ex);
        }
    }
    
    public List<PphBancoEntity> list() throws BCException {
        try {
            return bancoRepo.list();
        } catch (Exception ex) {
            throw BCExceptionUtil.prepara(LOGGER, ex);
        }
    }
}
