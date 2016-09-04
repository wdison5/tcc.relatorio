package org.tcc.relatorio.hammer.persistencia.exception;

import javax.ejb.ApplicationException;

/**
 * @author wdi_s
 */
@ApplicationException(
    rollback = true
)
public class BCException extends Exception {
    public BCException(String message) {
        super(message);
    }
    
    public BCException(String message, Exception e) {
        super(message, e);
    }

    public BCException(Exception e) {
        super(e);
    }
}
