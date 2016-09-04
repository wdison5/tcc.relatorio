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
public abstract class Formatador {

    public abstract String formata(String value);
    public static final Formato NUMERO = Formato.NUMERO;
    public static final Formato LETRA = Formato.LETRA;

    public String sub(String value, int indexDe, int indexAte) {
        String result = "";
        if (!Validador.isBlank(value)) {
            result = value.substring(indexDe, indexAte);
        }
        return result;
    }

    public String completa(String value, Integer tam, String itemCompletador, Direcao direcao) {
        if (value != null && itemCompletador != null) {
            while (value.length() < tam) {
                if (direcao.equals(Direcao.INICIO)) {
                    value = itemCompletador + value;
                } else if (direcao.equals(Direcao.FIM)) {
                    value = value + itemCompletador;
                }
            }
        }
        return value;
    }

    public String clearMask(String value, Formato formato) {
        switch (formato) {
            case NUMERO: {
                value = value.replaceAll("[^0-9]", "");
                break;
            }
            case LETRA: {
                value = value.replaceAll("[^A-Za-z]", "");
                break;
            }
        }
        return value;

    }

    public enum Formato {

        NUMERO, LETRA
    }

    public enum Direcao {

        INICIO, FIM
    }

}
