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
import org.tcc.relatorio.dominio.PphComunicacaoEntity;
import org.tcc.relatorio.negocio.exception.util.BCExceptionUtil;
import org.tcc.relatorio.persistencia.ComunicacaoRepo;
import org.tcc.relatorio.hammer.persistencia.exception.BCException;
import org.tcc.relatorio.hammer.persistencia.exception.DaoException;

/**
 *
 * @author Jose Wdison de Souza
 */
@Stateless
public class ComunicacaoBC {

    private static final Logger LOGGER = LoggerFactory.getLogger(ComunicacaoBC.class);

    @Inject
    @New
    private ComunicacaoRepo comunicacaoRepo;

    @Inject
    private UsuarioEntity usuario;

    /**
     * Construtor.
     */
    public ComunicacaoBC() {
        LOGGER.debug("__| " + this.getClass().getSimpleName());
    }

    /**
     * Incluir usuário.
     *
     * @param comunicacao
     * @throws BCException Erro de processamento da solicitação (geralemnte
     * acesso ao banco).
     */
    @TransactionAttribute(REQUIRED)
    public void incluir(PphComunicacaoEntity comunicacao) throws BCException {
        try {
            comunicacaoRepo.persist(comunicacao);
        } catch (DaoException ex) {

            throw BCExceptionUtil.prepara(LOGGER, ex);
        }
    }

    /**
     * Atualiza UsuarioEntity.
     *
     * @param comunicacao
     * @throws BCException Erro de processamento da solicitação (geralemnte
     * acesso ao banco).
     */
    @TransactionAttribute(REQUIRED)
    public void atualizar(PphComunicacaoEntity comunicacao) throws BCException {
        try {
            comunicacaoRepo.persist(comunicacao);
        } catch (DaoException ex) {
            throw BCExceptionUtil.prepara(LOGGER, ex);
        }
    }

    public List<PphComunicacaoEntity> pesquisaPorNome(String nomeComunicacao) throws BCException {
        if (nomeComunicacao == null || "".equals(nomeComunicacao)) {
            throw BCExceptionUtil.prepara(LOGGER, null);
        }
        try {
            LOGGER.debug(usuario.toString());
            return comunicacaoRepo.pesquisaPorNome(nomeComunicacao);
        } catch (Exception ex) {
            throw BCExceptionUtil.prepara(LOGGER, ex);
        }
    }
    
    public List<PphComunicacaoEntity> listar(PphComunicacaoEntity comunicacaoInicial, PphComunicacaoEntity comunicacaoFinal) throws BCException {
        try {
            return (List) comunicacaoRepo.listar(comunicacaoInicial, comunicacaoFinal, new String[0]);
        } catch (DaoException e) {
            LOGGER.error("BC/ error {}", e.getMessage());
            throw new BCException(e.getMessage());
        }
    }

}
