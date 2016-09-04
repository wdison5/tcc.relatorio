/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tcc.relatorio.negocio.validator;

/**
 *
 * @author jwsouza
 */
public enum TipoUnidade {
    SAUDE("US"), PAGADORA("UP");
    private String cod;

    private TipoUnidade(String cod) {
        this.cod = cod;
    }
    public String getCod(){
        return cod;
    }
}
