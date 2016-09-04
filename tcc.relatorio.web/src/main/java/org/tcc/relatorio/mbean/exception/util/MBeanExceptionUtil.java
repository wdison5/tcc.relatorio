/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tcc.relatorio.mbean.exception.util;

import javax.management.MBeanException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tcc.relatorio.negocio.validator.Validador;

/**
 *
 * @author jwsouza
 */
public class MBeanExceptionUtil {

    public static MBeanException prepara(Logger log, Exception e, String msg) {
        MBeanException bcException = null;
        log(log, e, msg);
        if(Validador.isBlank(msg)){
            bcException = new MBeanException(e);
        }else{
            bcException = new MBeanException(e, msg);
        }
        return bcException;
    }
    
    public static MBeanException prepara(Logger log, Exception e) {
        return prepara(log, e, null);
    }

    public static void log(Logger log, Exception e, String title) {
        if (e != null) {
            if (log == null) {
                log = LoggerFactory.getLogger(MBeanExceptionUtil.class);
            }
            if (title != null && !"".equals(title)) {
                log.error(title + "\n{}", e);
            } else {
                log.error("MBean/ Erro: {}-{}", e.getMessage(), e);
            }
        }
    }

    public static void log(Logger log, Exception e) {
        log(log, e, null);
    }
}
