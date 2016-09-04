package org.tcc.relatorio.negocio;

import java.util.List;
import javax.ejb.Stateless;
import javax.enterprise.inject.New;
import javax.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tcc.relatorio.dominio.PphContatoEntity;
import org.tcc.relatorio.persistencia.ContatoRepo;
import org.tcc.relatorio.hammer.persistencia.exception.BCException;
import org.tcc.relatorio.hammer.persistencia.exception.DaoException;

/**
 *
 * @author Eloy
 */
@Stateless
public class ContatoBC {

    private static final Logger LOGGER = LoggerFactory.getLogger(ContatoBC.class);

    @Inject
    @New
    private ContatoRepo contatoRepo;
    
    public ContatoBC() {
        LOGGER.debug("__| " + this.getClass().getSimpleName());
    }
    
    public List<PphContatoEntity> listar(PphContatoEntity contatoInicial, PphContatoEntity contatoFinal) throws BCException {
        try {
            return (List) contatoRepo.listar(contatoInicial, contatoFinal, new String[0]);
        } catch (DaoException e) {
            LOGGER.error("BC/ error {}", e.getMessage());
            throw new BCException(e.getMessage());
        }
    }
}