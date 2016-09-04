package org.tcc.relatorio.negocio;

import java.util.List;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import static javax.ejb.TransactionAttributeType.REQUIRED;
import javax.enterprise.inject.New;
import javax.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tcc.relatorio.dominio.PphProcuradorEntity;
import org.tcc.relatorio.negocio.exception.util.BCExceptionUtil;
import org.tcc.relatorio.persistencia.ProcuradorRepo;
import org.tcc.relatorio.hammer.persistencia.exception.BCException;
import org.tcc.relatorio.hammer.persistencia.exception.DaoException;

/**
 *
 * @author Eloy
 */

@Stateless
public class ProcuradorBC {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProcuradorBC.class);

    @Inject
    @New
    private ProcuradorRepo procuradorRepo;

    /**
     * Construtor.
     */
    public ProcuradorBC() {
        LOGGER.debug("__| " + this.getClass().getSimpleName());
    }

    /**
     * Incluir usuário.
     *
     * @param procurador
     * @throws BCException Erro de processamento da solicitação (geralemnte
     * acesso ao banco).
     */
    @TransactionAttribute(REQUIRED)
    public void incluir(PphProcuradorEntity procurador) throws BCException {
        try {
            procuradorRepo.persist(procurador);
        } catch (DaoException e) {
            throw BCExceptionUtil.prepara(LOGGER, e);
        }
    }

    /**
     * Atualiza UsuarioEntity.
     *
     * @param procurador
     * @throws BCException Erro de processamento da solicitação (geralemnte
     * acesso ao banco).
     */
    @TransactionAttribute(REQUIRED)
    public void atualizar(PphProcuradorEntity procurador) throws BCException {
        try {
            procuradorRepo.persist(procurador);
        } catch (DaoException ex) {
            throw BCExceptionUtil.prepara(LOGGER, ex);
        }
    }

    public List<PphProcuradorEntity> pesquisaPorNome(String nomeBeneficiario) throws BCException {
        if (nomeBeneficiario == null || "".equals(nomeBeneficiario)) {
            throw BCExceptionUtil.prepara(LOGGER, null);
        }
        try {
            return procuradorRepo.listarPorNome(nomeBeneficiario);
        } catch (Exception ex) {
            throw BCExceptionUtil.prepara(LOGGER, ex);
        }
    }

    public List<PphProcuradorEntity> listar(PphProcuradorEntity procuradorInicial, PphProcuradorEntity procuradorFinal) throws BCException {
        try {
            return (List) procuradorRepo.listar(procuradorInicial, procuradorFinal, new String[0]);
        } catch (DaoException e) {
            LOGGER.error("BC/ error {}", e.getMessage());
            throw new BCException(e.getMessage());
        }
    }
}
