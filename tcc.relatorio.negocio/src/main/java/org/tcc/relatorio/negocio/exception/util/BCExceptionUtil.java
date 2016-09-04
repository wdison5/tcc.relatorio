/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tcc.relatorio.negocio.exception.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tcc.relatorio.negocio.validator.Validador;
import org.tcc.relatorio.hammer.persistencia.exception.BCException;

/**
 *
 * @author jwsouza
 */
public class BCExceptionUtil {

    
    public static BCException prepara(Logger log, Exception e, String msg) {
        BCException bcException = null;
        log(log, e, msg);
        if(Validador.isBlank(msg)){
            bcException = new BCException(e);
        }else{
            bcException = new BCException(msg);
            bcException.initCause(e);
        }
        return bcException;
    }
    
    public static BCException prepara(Logger log, Exception e) {
        return prepara(log, e, null);
    }
    
    public static void log(Logger log, Exception e, String title) {
        if (e != null) {
            if (log == null) {
                log = LoggerFactory.getLogger(BCExceptionUtil.class);
            }
            if (!Validador.isBlank(title)) {
                log.error(title + "\n{}", e);
            } else {
                log.error("BC/ Erro: {}-{}", e.getMessage(), e);
            }
        }
    }

    public static void log(Logger log, Exception e) {
        log(log, e, null);
    }
}
