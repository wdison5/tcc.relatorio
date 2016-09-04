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
public enum Confirmacao {

    SIM(1), NAO(0) ;
    private int id;

    private Confirmacao(int id) {
        this.id = id;
    }
    public int getId(){
        return this.id;
    }
}
