package org.tcc.relatorio.servicos;

import javax.jws.WebService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 *
 * @author jfascio
 */
@WebService(endpointInterface = "prodesp.s4sp.indicadores.contratos.Administracao", serviceName = "AdministracaoService")
public class Administracao implements org.tcc.relatorio.contratos.Administracao {

    private static final Logger logger = LoggerFactory.getLogger(Administracao.class);
        
    /**
     * Construtor.
     */
    public Administracao() {
        logger.info("__| Administracao");
    }

//    @EJB(name="IndicadoresBC") 
//    private IndicadoresBC bc;
//
//    @Override
//    public String executaCargaSI3ToS4SP() throws Exception {
//        return bc.executaCargaSI3ToS4SP();
//    }

    @Override
    public String executaCargaSI3ToS4SP() throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
