/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tcc.relatorio.negocio.reflection;

/**
 *
 * @author jwsouza
 */
public class ReflectionException extends Exception {
    public ReflectionException(String msg, Exception e) {
        super(msg, e);
    }
    public ReflectionException(Exception e) {
        super(e);
    }
    public ReflectionException(String msg) {
        super(msg);
    }
    public ReflectionException() {
        super();
    }
}
