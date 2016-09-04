/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tcc.relatorio.negocio.formatador;

import java.util.ArrayList;
import java.util.List;
import org.tcc.relatorio.negocio.validator.IValidador;
import org.tcc.relatorio.negocio.validator.Validador;

/**
 *
 * @author jwsouza
 */
public class Cpf extends Formatador implements IValidador {
    private static List<String> lstInvalidCpf = new ArrayList<String>(){{
        add("00000000000");
        add("11111111111");
        add("22222222222");
        add("33333333333");
        add("44444444444");
        add("55555555555");
        add("66666666666");
        add("77777777777");
        add("88888888888");
        add("99999999999");
    }};
    
    public static String geraCPF() {
        String iniciais = "";
        Integer numero;
        for (int i = 0; i < 9; i++) {
            numero = (int) (Math.random() * 10);
            iniciais += numero.toString();
        }
        return iniciais + calculaDigitoVerificador(iniciais);
    }

    @Override
    public boolean valida(Object cpf) {
        String cpfString = (String) cpf;
        if (!Validador.isBlank(cpfString)) {
            cpfString = clearMask(cpfString, NUMERO);
            if (!Validador.isBlank(cpfString) && cpfString.length() == 11) {
                String numDig = cpfString.substring(0, 9);
                return !lstInvalidCpf.contains(cpfString) && calculaDigitoVerificador(numDig).equals(cpfString.substring(9, 11));
            }
        }
        return false;
    }

    private static String calculaDigitoVerificador(String num) {
        String primDig, segDig;

        primDig = calculaModulo11(num);
        segDig = calculaModulo11(num + primDig);

        return primDig + segDig;
    }

    private static String calculaModulo11(String num) {
        int soma = 0;
        for (int i = 0; i < num.length(); i++) {

            String numero = num.substring(num.length() - 1 - i, num.length() - i);
            soma += Integer.valueOf(numero) * (i + 2);
        }
        int digitoVerificador = (soma * 10) % 11;
        if (digitoVerificador == 10) {
            digitoVerificador = 0;
        }
        return String.valueOf(digitoVerificador);
    }

    @Override
    public String formata(String cpf) {
        if (!Validador.isBlank(cpf)) {
            cpf = super.clearMask(cpf, Formatador.NUMERO);
            if (cpf.length() > 9) {
                cpf = completa(cpf, 11, "0", Direcao.INICIO);
                cpf = sub(cpf, 0, 3)+"."+sub(cpf, 3, 6)+"."+sub(cpf, 6, 9)+"-"+sub(cpf, 9, 11);
            }
        }
        return cpf;
    }
    
}
