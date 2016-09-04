/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tcc.relatorio.negocio.formatador;

import org.tcc.relatorio.negocio.validator.IValidador;
import org.tcc.relatorio.negocio.validator.Validador;

/**
 *
 * @author jwsouza
 */
public class Cep extends Formatador implements IValidador {

    @Override
    public boolean valida(Object cep) {
        String cepString = (String) cep;
        boolean result = false;
        if (!Validador.isBlank(cepString)) {
            cepString = formata(cepString);
            result = cepString.matches("\\d{5}-\\d{3}") || cepString.matches("\\d{8}");
        }
        return result;
    }

    @Override
    public String formata(String cep) {
        if (!Validador.isBlank(cep)) {
            cep = super.clearMask(cep, Formatador.NUMERO);
            if (cep.length() > 5) {
                cep = completa(cep, 8, "0", Direcao.INICIO);
                cep = cep.substring(0, 5)+"-" + cep.substring(5, 8);
            }
        }
        return cep;
    }
    
}
