/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tcc.relatorio.negocio.formatador;

import org.tcc.relatorio.negocio.validator.Validador;

/**
 *
 * @author Jose Wdison de Souza
 */
public class Maiusculas extends Formatador {
    @Override
    public String formata(String value) {
        if (!Validador.isNull(value)) {
            value = value.toUpperCase();
        }
        return value;
    }
}
