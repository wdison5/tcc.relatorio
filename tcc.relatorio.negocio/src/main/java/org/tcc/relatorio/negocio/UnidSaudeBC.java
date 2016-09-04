package org.tcc.relatorio.negocio;

import org.tcc.relatorio.enumeracao.Confirmacao;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.enterprise.inject.New;
import javax.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tcc.relatorio.cap.negocio.UsuarioBC;
import org.tcc.relatorio.dominio.InstituicaoEntity;
import org.tcc.relatorio.dominio.PphUnidSaudeEntity;
import org.tcc.relatorio.negocio.exception.util.BCExceptionUtil;
import org.tcc.relatorio.persistencia.InstituicaoRepo;
import org.tcc.relatorio.persistencia.UnidSaudeRepo;
import org.tcc.relatorio.hammer.persistencia.exception.BCException;
import org.tcc.relatorio.hammer.persistencia.exception.DaoException;

/**
 *
 * @author Vanderlei Garcia
 */
@Stateless
public class UnidSaudeBC {

    private static final Logger LOGGER = LoggerFactory.getLogger(UnidSaudeBC.class);

    @Inject
    @New
    private UnidSaudeRepo unidSaudeRepo;

    @Inject
    @New
    private InstituicaoRepo instituicaoRepo;

    @Inject
    private UsuarioBC usuarioBC;

    /**
     * Construtor.
     */
    public UnidSaudeBC() {
        LOGGER.debug("__| "+getClass().getSimpleName());
    }

    public PphUnidSaudeEntity getById(long id) throws BCException {
        try {
            PphUnidSaudeEntity entidade = unidSaudeRepo.getById(id);
            if (entidade == null) {
                throw new BCException("Unidade Saude não encontrada.");
            }
            return entidade;
        } catch (Exception e) {
            throw BCExceptionUtil.prepara(LOGGER, e);
        }
    }
    
    public PphUnidSaudeEntity getByCNES(int cnes) throws BCException {
        try {
            PphUnidSaudeEntity entidade = unidSaudeRepo.getByCNES(cnes);
            if (entidade == null) {
                throw new BCException("Unidade Saude não encontrada.");
            }
            return entidade;
        } catch (DaoException e) {
            throw BCExceptionUtil.prepara(LOGGER, e);
        }
    }

    public List<PphUnidSaudeEntity> list(boolean isAdmin, List<Long> idsUnidSaude) throws BCException {
        try {
            List<Long> ids = new ArrayList<Long>();
            if (isAdmin) {
                InstituicaoEntity inst = new InstituicaoEntity();
                inst.setTpUnid("US");
                List<InstituicaoEntity> listaInstituicao = (List) instituicaoRepo.listar(inst, inst, new String[0]);
                for (InstituicaoEntity instituicao : listaInstituicao) {
                    ids.add(instituicao.getPphUnidSaude().getId());
                }
            } else {
                ids = idsUnidSaude;
            }
            return unidSaudeRepo.listar(ids, Confirmacao.SIM.getId());
        } catch (Exception ex) {
            throw BCExceptionUtil.prepara(LOGGER, ex);
        }
    }

    @TransactionAttribute(value = TransactionAttributeType.REQUIRED)
    public PphUnidSaudeEntity persist(PphUnidSaudeEntity unidSaude) throws BCException {
        try {
            return unidSaudeRepo.persist(unidSaude);
        } catch (Exception e) {
            throw BCExceptionUtil.prepara(LOGGER, e);
        }
    }

    public List<PphUnidSaudeEntity> listAllNoCadInstit(int flAtivo) throws BCException {
        try {
            return unidSaudeRepo.listAllNoCadInstit(flAtivo);
        } catch (Exception e) {
            throw BCExceptionUtil.prepara(LOGGER, e);
        }
    }

    public List<PphUnidSaudeEntity> list() throws BCException {
        try {
            return unidSaudeRepo.list(Confirmacao.SIM.getId());
        } catch (Exception e) {
            throw BCExceptionUtil.prepara(LOGGER, e);
        }
    }

}
