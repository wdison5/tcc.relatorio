/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tcc.relatorio.negocio.validator;

import org.tcc.relatorio.negocio.formatador.Cpf;
import org.tcc.relatorio.dominio.BaseEntity;
import org.tcc.relatorio.hammer.persistencia.exception.BCException;

/**
 * @author Jose Wdison
 * @param <T>
 */
public abstract class ValidadorEntity<T extends BaseEntity> extends Validador<T> {

    public abstract boolean validaInsert(T entity) throws BCException;
    public abstract boolean validaUpdate(T entity) throws BCException;
    
    public static boolean isCpf(String value) {
        return new Cpf().valida(value);
    }
}
