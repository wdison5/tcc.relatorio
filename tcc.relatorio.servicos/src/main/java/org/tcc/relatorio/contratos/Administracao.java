package org.tcc.relatorio.contratos;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

/**
 *
 * @author jfascio
 */
@WebService
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT, use=SOAPBinding.Use.LITERAL) //optional
public interface Administracao {
    
    @WebMethod(operationName="executaCargaSI3ToS4SP")       
    public String executaCargaSI3ToS4SP() throws Exception;
    
}
