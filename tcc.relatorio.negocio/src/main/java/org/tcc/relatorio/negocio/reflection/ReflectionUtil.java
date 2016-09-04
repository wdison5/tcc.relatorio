/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tcc.relatorio.negocio.reflection;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tcc.relatorio.negocio.validator.Validador;

/**
 * @author José Wdison
 */
public class ReflectionUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(ReflectionUtil.class);

    public Object invocaGet(Object entidade, String campo) throws ReflectionException {

        Object retobj = null;
        Class cls = entidade.getClass();
        String metodoGet = "get" + campo.substring(0, 1).toUpperCase() + campo.substring(1);
        try {
            Method meth = cls.getMethod(metodoGet);
            retobj = meth.invoke(entidade);
        } catch (Exception e) {
            throw new ReflectionException("Erro ao invocar metodo " + metodoGet, e);
        }
        return retobj;
    }

    public Object invocaSet(Object entidade, String campo, Object valor) throws ReflectionException {
        Object retobj = null;
        Class cls = entidade.getClass();
        Class clsFieldType = getFieldType(cls, campo);
        String metodoGet = "set" + campo.substring(0, 1).toUpperCase() + campo.substring(1);
        try {
            Method meth = cls.getMethod(metodoGet, clsFieldType);
            retobj = meth.invoke(entidade, valor);
        } catch (Exception e) {
            throw new ReflectionException("Erro ao invocar metodo " + metodoGet + (valor != null ? ". args: " + valor : ""), e);
        }
        return retobj;
    }

    private Class getFieldType(Class cls, String campo) throws ReflectionException {
        Class clazz = Object.class;
        try {
            Field field = getField(cls, campo);
            if (!Validador.isNull(field)) {
                clazz = field.getType();
            } else {
                throw new RuntimeException("campo " + campo + " da classe " + cls.getName() + ", não encontrado.");
            }
        } catch (Exception ex) {
            throw new ReflectionException("Erro ao recuperar tipo de campo \"" + campo + "\" da classe " + cls.getName(), ex);
        }
        return clazz;
    }

    public Field getField(Class cls, String campo) throws ReflectionException {
        Field field = null;
        try {
            field = cls.getDeclaredField(campo);
        } catch (Exception ex) {
            throw new ReflectionException("Erro ao recuperar campo " + campo + " da classe " + cls.getName(), ex);
        }
        return field;
    }

    public void clonar(Object origem, Object clone) throws ReflectionException {
        boolean isOrigem = origem != null;
                String objNome = null, objClasseNome = null, metodoNome = null;
        try {
            if (isOrigem) {
                Class objClass = origem.getClass();
                objNome = objClass.getName();
                Method m[];
                do {
                    objClasseNome = objClass.getName();
                    m = objClass.getDeclaredMethods();
                    for (Method m1 : m) {
                        if (m1.getName().startsWith("set")) {
                            metodoNome = m1.getName();
                            Class partypes[] = new Class[0];
                            Method meth = null;
                            try {
                                meth = objClass.getMethod(m1.getName().replace("set", "get"), partypes);
                            } catch (Exception e) {
                                meth = objClass.getMethod(m1.getName().replace("set", "is"), partypes);
                            }
                            Object arg = meth.invoke(origem);
                            m1.invoke(clone, arg);
                        }
                    }

                    objClass = objClass.getSuperclass();
                } while (objClass != null && !objClass.getName().equals(Object.class.getName()));
            }
        } catch (Exception ex) {
            LOGGER.debug("Erro ao clonar o objeto \"" + objNome + "\". "+objClasseNome+" -> "+metodoNome+" {}", ex);
            throw new ReflectionException("Erro ao clonar o objeto \"" + objNome + "\". "+objClasseNome+" -> "+metodoNome, ex);
        }
    }
}
