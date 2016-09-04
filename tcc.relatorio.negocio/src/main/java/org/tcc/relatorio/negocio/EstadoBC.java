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
import org.tcc.relatorio.dominio.PphEstadoEntity;
import org.tcc.relatorio.negocio.exception.util.BCExceptionUtil;
import org.tcc.relatorio.persistencia.EstadoRepo;
import org.tcc.relatorio.hammer.persistencia.exception.BCException;
import org.tcc.relatorio.hammer.persistencia.exception.DaoException;

/**
 *
 * @author Jose Wdison de Souza
 */
@Stateless
public class EstadoBC {

    private static final Logger LOGGER = LoggerFactory.getLogger(EstadoBC.class);

    @Inject
    @New
    private EstadoRepo estadoRepo;

    @Inject
    private UsuarioEntity usuario;

    /**
    * Construtor.
    */
    public EstadoBC() {
        LOGGER.debug("__| " + this.getClass().getSimpleName());
    }

    /**
    * Incluir usuário.
    *
    * @param Estado
    * @throws BCException Erro de processamento da solicitação (geralemnte
    * acesso ao banco).
    */
    @TransactionAttribute(REQUIRED)
    public void incluir(PphEstadoEntity Estado) throws BCException {
        try {
            if (Estado == null || Estado.getId() != null) {

            }
            estadoRepo.persist(Estado);
        } catch (DaoException ex) {

            throw BCExceptionUtil.prepara(LOGGER, ex);
        }
    }

    /**
    * Atualiza UsuarioEntity.
    *
    * @param Estado
    * @throws BCException Erro de processamento da solicitação (geralemnte
    * acesso ao banco).
    */
    @TransactionAttribute(REQUIRED)
    public void atualizar(PphEstadoEntity Estado) throws BCException {
        try {
            estadoRepo.persist(Estado);
        } catch (DaoException ex) {
            throw BCExceptionUtil.prepara(LOGGER, ex);
        }
    }

    public List<PphEstadoEntity> pesquisaPorNome(String nomeEstado) throws BCException {
        if (nomeEstado == null || "".equals(nomeEstado)) {
            throw BCExceptionUtil.prepara(LOGGER, null);
        }
        try {
            LOGGER.debug(usuario.toString());
            return estadoRepo.pesquisaPorNome(nomeEstado);
        } catch (Exception ex) {
            throw BCExceptionUtil.prepara(LOGGER, ex);
        }
    }

    public List<PphEstadoEntity> list() throws BCException {
        try {
            return estadoRepo.list();
        } catch (Exception ex) {
            throw BCExceptionUtil.prepara(LOGGER, ex);
        }
    }
}
