package org.tcc.relatorio.util.jasper;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tcc.relatorio.util.ExportarArquivo;

/**
 *
 * @author Eloy
 */
public class Reports {

    private static final Logger logger = LoggerFactory.getLogger(ExportarArquivo.class);
    private Map parametros = new HashMap();    
    private Collection<Object> registros;
    private final String nomeArquivo;
    private Object classe;
    
    public Reports(Object info) throws ParseException {
        nomeArquivo = info.getClass().getSimpleName().replace("MBean", "");
        registros = new ArrayList<Object>();
        classe = info;
    }

    public String getNomeArquivo() {
        return nomeArquivo;
    }
    
    public Map getParametros() {
        return parametros;
    }

    public void setParametros(Map parametros) {
        this.parametros = parametros;
    }

    public Collection<Object> getRegistros() {
        return registros;
    }

    public void setRegistros(Collection<Object> registros) {
        this.registros = registros;
    }

    /**
     * @return the classe
     */
    public Object getClasse() {
        return classe;
    }

    /**
     * @param classe the classe to set
     */
    public void setClasse(Object classe) {
        this.classe = classe;
    }
    
}