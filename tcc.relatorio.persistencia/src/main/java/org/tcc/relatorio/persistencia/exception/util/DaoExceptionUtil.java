/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tcc.relatorio.persistencia.exception.util;

import org.tcc.relatorio.hammer.persistencia.exception.DaoException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author jwsouza
 */
public class DaoExceptionUtil {

    public static DaoException prepara(Logger log, Exception e){
        log(log, e);
//        DaoException daoException = new DaoException(e.getMessage());
//        daoException.initCause(e);
        DaoException daoException = new DaoException(e);
        return daoException;
    }

    public static void log(Logger log, Exception e, String title) {
        if (e != null) {
            if (log == null) {
                log = LoggerFactory.getLogger(DaoExceptionUtil.class);
            }
            if (title != null && !"".equals(title)) {
                log.error(title + "\n{}", e);
            } else {
                log.error("Dao/ Erro: {}", e);
            }
        }
    }

    public static void log(Logger log, Exception e) {
        log(log, e, null);
    }
}
