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
public interface IValidador<T> {
    public abstract boolean valida(T value);
}
