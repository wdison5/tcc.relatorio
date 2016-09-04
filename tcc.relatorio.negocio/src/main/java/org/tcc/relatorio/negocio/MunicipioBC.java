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
import org.tcc.relatorio.dominio.PphMunicipioEntity;
import org.tcc.relatorio.negocio.exception.util.BCExceptionUtil;
import org.tcc.relatorio.persistencia.MunicipioRepo;
import org.tcc.relatorio.hammer.persistencia.exception.BCException;
import org.tcc.relatorio.hammer.persistencia.exception.DaoException;

/**
 *
 * @author Jose Wdison de Souza
 */
@Stateless
public class MunicipioBC {

    private static final Logger LOGGER = LoggerFactory.getLogger(MunicipioBC.class);

    @Inject
    @New
    private MunicipioRepo MunicipioRepo;

    @Inject
    private UsuarioEntity usuario;

    /**
     * Construtor.
     */
    public MunicipioBC() {
        LOGGER.debug("__| " + this.getClass().getSimpleName());
    }

    /**
     * Incluir usuário.
     *
     * @param Municipio
     * @throws BCException Erro de processamento da solicitação (geralemnte
     * acesso ao banco).
     */
    @TransactionAttribute(REQUIRED)
    public void incluir(PphMunicipioEntity Municipio) throws BCException {
        try {
            if (Municipio == null || Municipio.getId() != null) {

            }
            MunicipioRepo.persist(Municipio);
        } catch (DaoException ex) {

            throw BCExceptionUtil.prepara(LOGGER, ex);
        }
    }

    /**
     * Atualiza UsuarioEntity.
     *
     * @param Municipio
     * @throws BCException Erro de processamento da solicitação (geralemnte
     * acesso ao banco).
     */
    @TransactionAttribute(REQUIRED)
    public void atualizar(PphMunicipioEntity Municipio) throws BCException {
        try {
            MunicipioRepo.persist(Municipio);
        } catch (DaoException ex) {
            throw BCExceptionUtil.prepara(LOGGER, ex);
        }
    }

    public List<PphMunicipioEntity> pesquisaPorNome(String nomeMunicipio) throws BCException {
        if (nomeMunicipio == null || "".equals(nomeMunicipio)) {
            throw BCExceptionUtil.prepara(LOGGER, null);
        }
        try {
            LOGGER.debug(usuario.toString());
            return MunicipioRepo.pesquisaPorNome(nomeMunicipio);
        } catch (Exception ex) {
            throw BCExceptionUtil.prepara(LOGGER, ex);
        }
    }

    public List<PphMunicipioEntity> pesquisaPorIdEstado(Long idEstado) throws BCException {
        try {
            return MunicipioRepo.pesquisaPorIdEstado(idEstado);
        } catch (Exception ex) {
            throw BCExceptionUtil.prepara(LOGGER, ex);
        }
    }
}
