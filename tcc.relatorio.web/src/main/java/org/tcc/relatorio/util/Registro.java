package org.tcc.relatorio.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

/**
 *
 * @author eloy
 */
public class Registro {

    private UUID id;
    private Object[] dados;
    private String[] campos;
    private String[] header;

    public Registro(UUID id) {
        this.id = id;
    }

    public Registro(UUID id, Object[] dados, String[] campos, String[] header) {
        this.id = id;
        this.dados = dados;
        this.campos = campos;
        this.header = header;
    }
    
    public Object info(String campo) {
        ArrayList<String> list = new ArrayList<String>(Arrays.asList(this.campos));
        int index = list.indexOf(campo);
        return dados[index];
    }

    public Object infoColuna1() {
        return dados[0];
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Object[] getDados() {
        return dados;
    }

    public void setDados(String[] dados) {
        this.dados = dados;
    }

    public String[] getCampos() {
        return campos;
    }

    public void setTitulos(String[] campos) {
        this.campos = campos;
    }

    public String[] getHeader() {
        return header;
    }

    public void setHeader(String[] header) {
        this.header = header;
    }
}
