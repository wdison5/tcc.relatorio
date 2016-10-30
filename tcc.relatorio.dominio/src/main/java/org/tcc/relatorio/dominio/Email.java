package org.tcc.relatorio.dominio;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jose Wdison
 */
public class Email {
    private String destino;
    private String nome;
    
    private List<byte[]> anexos = new ArrayList<>();

    public Email(String destico, String nome) {
        this.destino = destico;
        this.nome = nome;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void add(byte[] b) {
        anexos.add(b);
    }

    public List<byte[]> getAnexos() {
        return anexos;
    }
}
