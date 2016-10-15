package org.tcc.relatorio.util;

import java.util.Calendar;
import java.util.List;
import java.util.Map;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.primefaces.context.RequestContext;
import org.tcc.relatorio.negocio.util.CalendarUtil;

/**
 *
 * @author Jose Wdison de Souza
 */
public class FacesUtil {

    public static final FacesMessage.Severity ERROR = FacesMessage.SEVERITY_ERROR;
    public static final FacesMessage.Severity FATAL = FacesMessage.SEVERITY_FATAL;
    public static final FacesMessage.Severity INFO = FacesMessage.SEVERITY_INFO;
    public static final FacesMessage.Severity WARN = FacesMessage.SEVERITY_WARN;
    public static final String ID_COMPONENTE_PADRAO = "mensagem";
    public static final String ID_COMPONENTE_CUSTOMIZADO = "mensagem_customizada";

    public static final String ERROR_CONSOLE = "error";
    public static final String INFO_CONSOLE = "info";
    public static final String LOG_CONSOLE = "log";
    public static final String WARN_CONSOLE = "warn";
    public static final String DEBUG_CONSOLE = "debug";

    public static void addError(String msg) {
        addError(ID_COMPONENTE_PADRAO, msg);
    }
    
    public static void addError(List<String> msgs) {
        addMsg(ERROR, msgs);
    }
    
    public static void addError(String idComponente, List<String> msgs) {
        addMsg(idComponente, ERROR, msgs);
    }

    public static void addError(String idComponente, String msg) {
        addMsg(idComponente, ERROR, msg);
    }

    public static void addInfo(String msg) {
        addInfo(ID_COMPONENTE_PADRAO, msg);
    }

    public static void addInfo(String idComponente, String msg) {
        addMsg(idComponente, INFO, msg);
    }

    public static void addWarn(String msg) {
        addWarn(ID_COMPONENTE_PADRAO, msg);
    }

    public static void addWarn(String idComponente, String msg) {
        addMsg(idComponente, WARN, msg);
    }

    public static void addFatal(String msg) {
        addFatal(ID_COMPONENTE_PADRAO, msg);
    }

    public static void addFatal(String idComponente, String msg) {
        addMsg(idComponente, FATAL, msg);
    }

    public static void addMsg(FacesMessage.Severity prioridade, List<String> msgs) {
        addMsg(ID_COMPONENTE_PADRAO, prioridade, msgs);
    }

    public static void addMsg(List<String> msgs) {
        addMsg(ID_COMPONENTE_PADRAO, msgs);
    }

    public static void addMsg(FacesMessage.Severity prioridade, String msgs) {
        addMsg(ID_COMPONENTE_PADRAO, prioridade, msgs);
    }
    
    public static void addMsg(String msgs) {
        addMsg(INFO, msgs);
    }

    public static void addMsg(String idComponente, List<String> msgs) {
        addMsg(idComponente, INFO, msgs);
    }

    public static void addMsg(String idComponente, FacesMessage.Severity prioridade, List<String> msgs) {
        for (String msg : msgs) {
            addMsg(idComponente, prioridade, msg);
        }
    }

    public static void addMsg(String idComponente, FacesMessage.Severity prioridade, String msg) {
        addMsg(idComponente, prioridade, msg, "");
    }

    public static void addMsg(String idComponente, FacesMessage.Severity prioridade, String msg, String detalheFaces) {
        facesContext().addMessage(idComponente, new FacesMessage(prioridade, msg, detalheFaces));
        update(idComponente);
    }

    /*
     * Aciciona mensagem ao console do cliente
     */
    public static void addCsl(List<String> msgs) {
        addCsl(LOG_CONSOLE, msgs);
    }
    /*
     * Aciciona mensagem ao console do cliente
     */
    public static void put(List<String> consoleMsgs) {
        addCsl(consoleMsgs);
    }
    public static void put(Exception e) {
        addCsl(e);
    }
    public static void addCsl(Exception e) {
        addCsl(LOG_CONSOLE, e);
    }
    public static void addCsl(String prioridade, Exception e) {
        StringBuilder buildScript = new StringBuilder();
        StringBuilder build = new StringBuilder();
        if (e != null) {
            build.append(CalendarUtil.formata(Calendar.getInstance(), "yyyy-MM-dd HH:mm:ss:SSS => "))
                    .append(e.getClass().getName())
                    .append(": ")
                    .append(e.getMessage());

            buildScript.append(prepareCallConsole(prioridade, build.toString()));
            buildScript.append(preparaStackTrace(prioridade, e));
            exec(buildScript.toString());
        }
    }

    private static String preparaStackTrace(String prioridade, Exception e) {
        StringBuilder buildResult = new StringBuilder();
        StringBuilder build = new StringBuilder();
        StackTraceElement[] stackTrace = e.getStackTrace();
        if (stackTrace != null) {
            int i = 0, tam = 15;
            for (StackTraceElement str : stackTrace) {
                build.delete(0, build.length());
                build.append(".       em ")
                        .append(str.getClassName())
                        .append(".")
                        .append(str.getMethodName())
                        .append("() ")
                        .append(str.getLineNumber());
                buildResult.append(prepareCallConsole(prioridade, build.toString()));

                if ((i++) >= tam) {
                    build.delete(0, build.length());
                    buildResult.append(prepareCallConsole(prioridade, ".       mais: " + (stackTrace.length - tam) + " linas."));
                    break;
                }
            }
        }
        if (e.getCause() != null) {
            buildResult.append(prepareCallConsole(prioridade, "Causado por: " + e.getCause().getClass().getName() + ": " + e.getCause().getMessage()));
            buildResult.append(preparaStackTrace(prioridade, (Exception) e.getCause()));
        }
        return buildResult.toString();
    }
    /*
     * Aciciona mensagem ao console do cliente
     */
    public static void addCsl(String prioridade, List<String> msgs) {
        StringBuilder build = new StringBuilder();
        for (String msg : msgs) {
            build.append(prepareCallConsole(prioridade, msg));
        }
        exec(build.toString());
    }

    public static void addCsl(String msg) {
        addCsl(LOG_CONSOLE, msg);
    }

    public static void addCslError(String msg) {
        addCsl(ERROR_CONSOLE, msg);
    }

    public static void addCslError(List<String> msgs) {
        addCsl(ERROR_CONSOLE, msgs);
    }

    public static void addCslInfo(String msg) {
        addCsl(INFO_CONSOLE, msg);
    }

    public static void addCslInfo(List<String> msgs) {
        addCsl(INFO_CONSOLE, msgs);
    }

    public static void addCslWarn(String msg) {
        addCsl(WARN_CONSOLE, msg);
    }

    public static void addCslWarn(List<String> msgs) {
        addCsl(WARN_CONSOLE, msgs);
    }

    public static void addCslDebug(String msg) {
        addCsl(DEBUG_CONSOLE, msg);
    }

    public static void addCslDebug(List<String> msgs) {
        addCsl(DEBUG_CONSOLE, msgs);
    }

    public static void addCslLog(String msg) {
        addCsl(LOG_CONSOLE, msg);
    }

    public static void addCslLog(List<String> msgs) {
        addCsl(LOG_CONSOLE, msgs);
    }

    public static void addCsl(String prioridade, String msg) {
        exec(prepareCallConsole(prioridade, msg));
    }

    private static String prepareCallConsole(String prioridade, String msg) {
        return "console." + prioridade + "('" + msg + "');";
    }

    public static void exec(String functionJavaScript) {
        requestContext().execute(functionJavaScript);
    }

    public static void update(String idComponente) {
        requestContext().update(idComponente);
    }

    public static HttpServletRequest request() {
        return (HttpServletRequest) externalContext().getRequest();
    }

    public static ExternalContext externalContext() {
        return facesContext().getExternalContext();
    }

    public static FacesContext facesContext() {
        return FacesContext.getCurrentInstance();
    }

    public static RequestContext requestContext() {
        return RequestContext.getCurrentInstance();
    }
    public static HttpSession session() {
        return request().getSession();
    }
    
    public static void addInSession(String chave, Object valor) {
        session().setAttribute(chave, valor);
        sessionMap().put(chave, valor);
    }
    
    public static Object getInSession(String chave) {
        Map<String, Object> sessionMap = sessionMap();
        String[] valueNames = session().getValueNames();
        return sessionMap.get(chave);
    }
    
    public static boolean isUserInRole(String role) {
        return request().isUserInRole(role);
    }

    private static Map<String, Object> sessionMap() {
        return externalContext().getSessionMap();
    }
    
}
