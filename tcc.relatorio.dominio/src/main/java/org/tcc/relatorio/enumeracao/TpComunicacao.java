/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tcc.relatorio.enumeracao;

/**
 *
 * @author jwsouza
 */
public enum TpComunicacao {
    FONE_PRIN("FP"), FONE_SEC("FS"), CELULAR("CE");

    private String cd;
    private TpComunicacao(String cd) {
       this.cd=cd; 
    }
    public String getCd() {
        return cd;
    }
}
