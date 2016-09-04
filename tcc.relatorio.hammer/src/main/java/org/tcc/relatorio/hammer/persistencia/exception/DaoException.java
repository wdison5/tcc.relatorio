package org.tcc.relatorio.hammer.persistencia.exception;

/**
 * @author wdi_s
 */
public class DaoException extends Exception {
    private static final long serialVersionUID = -697770938711331173L;

    public DaoException(Exception e) {
        super(e);
    }
    
    public DaoException(String msg, Exception e) {
        super(msg, e);
    }

    public DaoException(String msg) {
        super(msg);
    }
}
