package org.tcc.relatorio.hammer.persistencia;

import java.util.TreeMap;

/**
 * @author wdi_s
 */
public class BCMap extends TreeMap<String, Object> {
    private Object value;
    
    public BCMap() {
    }

    public BCMap add(String k, Object v) {
        value = this.put(k, v);
        return this;
    }

    public Object getValue() {
        return value;
    }
}
