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
import org.tcc.relatorio.dominio.PphEnderecoEntity;
import org.tcc.relatorio.negocio.exception.util.BCExceptionUtil;
import org.tcc.relatorio.persistencia.EnderecoRepo;
import org.tcc.relatorio.hammer.persistencia.exception.BCException;
import org.tcc.relatorio.hammer.persistencia.exception.DaoException;

/**
 *
 * @author Jose Wdison de Souza
 */
@Stateless
public class EnderecoBC {

    private static final Logger LOGGER = LoggerFactory.getLogger(EnderecoBC.class);

    @Inject
    @New
    private EnderecoRepo enderecoRepo;

    @Inject
    private UsuarioEntity usuario;

    /**
     * Construtor.
     */
    public EnderecoBC() {
        LOGGER.debug("__| " + this.getClass().getSimpleName());
    }

    /**
     * Incluir usuário.
     *
     * @param Endereco
     * @throws BCException Erro de processamento da solicitação (geralemnte
     * acesso ao banco).
     */
    @TransactionAttribute(REQUIRED)
    public void incluir(PphEnderecoEntity Endereco) throws BCException {
        try {
            enderecoRepo.persist(Endereco);
        } catch (DaoException ex) {

            throw BCExceptionUtil.prepara(LOGGER, ex);
        }
    }

    /**
     * Atualiza UsuarioEntity.
     *
     * @param Endereco
     * @throws BCException Erro de processamento da solicitação (geralemnte
     * acesso ao banco).
     */
    @TransactionAttribute(REQUIRED)
    public void atualizar(PphEnderecoEntity Endereco) throws BCException {
        try {
            enderecoRepo.persist(Endereco);
        } catch (DaoException ex) {
            throw BCExceptionUtil.prepara(LOGGER, ex);
        }
    }

    public List<PphEnderecoEntity> pesquisaPorNome(String nomeEndereco) throws BCException {
        if (nomeEndereco == null || "".equals(nomeEndereco)) {
            throw BCExceptionUtil.prepara(LOGGER, null);
        }
        try {
            LOGGER.debug(usuario.toString());
            return enderecoRepo.pesquisaPorNome(nomeEndereco);
        } catch (Exception ex) {
            throw BCExceptionUtil.prepara(LOGGER, ex);
        }
    }
    
    public List<PphEnderecoEntity> listar(PphEnderecoEntity enderecoInicial, PphEnderecoEntity enderecoFinal) throws BCException {
        try {
            return (List) enderecoRepo.listar(enderecoInicial, enderecoFinal, new String[0]);
        } catch (DaoException e) {
            LOGGER.error("BC/ error {}", e.getMessage());
            throw new BCException(e.getMessage());
        }
    }

}
