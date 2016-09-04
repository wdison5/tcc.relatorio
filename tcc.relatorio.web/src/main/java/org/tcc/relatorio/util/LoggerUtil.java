package org.tcc.relatorio.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tcc.relatorio.negocio.validator.Validador;

/**
 * @author Jose Wdison
 */
public class LoggerUtil {

    private static final int INFO = 1;
    private static final int ERROR = 2;
    private static final int WARN = 3;
    private static final int DEBUG = 4;

    private static void logger(String msg, Exception ex, Logger log, int prioridade) {
        msg = Validador.isBlank(msg) ? "" : msg + ".";
        log = log == null ? LoggerFactory.getLogger(LoggerUtil.class) : log;
        Object[] objArray = null;
        String estruturaMsg = "{} {}: {}";

        if (ex != null) {
            FacesUtil.put(ex);
            objArray = new Object[]{msg, ex.getClass().getSimpleName(), ex};
        } else {
            objArray = new Object[]{msg};
            estruturaMsg = "{}";
        }

        switch (prioridade) {
            case INFO:
                log.info(estruturaMsg, objArray);
                break;
            case ERROR:
                log.error(estruturaMsg, objArray);
                FacesUtil.addError(msg);
                break;
            case WARN:
                log.warn(estruturaMsg, objArray);
                break;
            case DEBUG:
                log.debug(estruturaMsg, objArray);
                break;
        }
    }

    public static void info(String msg, Exception ex, Logger LOGGER) {
        logger(msg, ex, LOGGER, INFO);
    }

    public static void info(Exception ex, Logger LOGGER) {
        info(null, ex, LOGGER);
    }

    public static void info(String msg, Exception ex, Logger LOGGER, boolean sendMsgToClient) {
        if (sendMsgToClient) {
            FacesUtil.addInfo(msg);
        }
        info(msg, ex, LOGGER);
    }

    public static void error(String msg, Exception ex, Logger LOGGER, boolean sendMsgToClient) {
        if (sendMsgToClient) {
            msg = msg +". Entre em contato com a área de tecnologia.";
            FacesUtil.addError(msg);
        }
        error(msg, ex, LOGGER);
    }

    public static void error(String msg, Exception ex, Logger LOGGER) {
        logger(msg, ex, LOGGER, ERROR);
    }

    public static void error(Exception ex, Logger LOGGER) {
        error(null, ex, LOGGER);
    }

    public static void warn(String msg, Exception ex, Logger LOGGER) {
        logger(msg, ex, LOGGER, WARN);
    }

    public static void warn(Exception ex, Logger LOGGER) {
        warn(null, ex, LOGGER);
    }

    public static void debug(String msg, Exception ex, Logger LOGGER) {
        logger(msg, ex, LOGGER, DEBUG);
    }

    public static void debug(Exception ex, Logger LOGGER) {
        debug(null, ex, LOGGER);
    }

}
