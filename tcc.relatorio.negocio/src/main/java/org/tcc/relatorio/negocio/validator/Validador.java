/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tcc.relatorio.negocio.validator;

import java.util.Collection;

/**
 *
 * @author jwsouza
 */
public abstract class Validador<T> implements IValidador<T> {

    public static boolean isEquals(String value, String value2) {
        return !isBlank(value) && !isBlank(value2) && value.equals(value2);
    }

    public static boolean isEquals(Number value, Number value2) {
        return !isNull(value) && !isNull(value2) && Double.doubleToRawLongBits(value.doubleValue()) == Double.doubleToRawLongBits(value2.doubleValue());
    }

    public static boolean isEmpty(String value) {
        return isNull(value) || "".equals(value);
    }

    public static boolean isBlank(String value) {
        return isNull(value) || "".equals(value.trim());
    }

    public static boolean isNull(Object value) {
        return value == null;
    }

    public static boolean isMaior(Number value, Number value2) {
        return !isNull(value) && !isNull(value2) && value.doubleValue() > value2.doubleValue();
    }

    public static boolean isMenor(Number value, Number value2) {
        return !isNull(value) && !isNull(value2) && value.doubleValue() < value2.doubleValue();
    }

    public static boolean isColecao(Collection colecao) {
        return !isNull(colecao) && !colecao.isEmpty();
    }

    public static boolean isColecao(Object obj) {
        return obj instanceof Collection && isColecao((Collection)obj);
    }
    
    public static boolean isArray(Object[] array) {
        return !isNull(array) && array.length>0;
    }
}
