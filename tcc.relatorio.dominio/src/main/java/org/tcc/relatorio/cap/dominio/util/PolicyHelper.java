/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tcc.relatorio.cap.dominio.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tcc.relatorio.cap.dominio.BaseEntity;

/**
 *
 * @author roger
 */
public final class PolicyHelper {

    private static final Logger logger = LoggerFactory.getLogger(PolicyHelper.class);
    private static Map<String, List<IPolicy>> staticPolicies = new HashMap<String, List<IPolicy>>();

    /**
     * Construtor privado, para utility class.
     */
    private PolicyHelper() {
    }
            
    /**
     * Lista as Plocies para uma determinada Entity.
     * @param <T> Template para Entity.
     * @param entity instancia da Entity.
     * @return Lista Policies.
     * @throws Exception Erro de acesso as propriedades da classe.
     */
    public static <T extends BaseEntity> List<IPolicy> getPolicies(T entity) throws Exception {
        if (!staticPolicies.containsKey(entity.getClass().getCanonicalName())) {
            staticPolicies.put(entity.getClass().getCanonicalName(), create(entity));
        }
        return staticPolicies.get(entity.getClass().getCanonicalName());
    }

    /**
     *
     * @param <T> Template da Entity.
     * @param entity Instancia da Entity.
     * @return Lista de Policies criadas.
     * @throws Exception Erro de Acesso ao conteudo das anotações.
     */
    public static <T extends BaseEntity> List<IPolicy> create(T entity) throws Exception {

        List<IPolicy> list = new ArrayList<IPolicy>();

        for (Annotation a : entity.getClass().getDeclaredAnnotations()) {
            if (a.annotationType().isAssignableFrom(Policies.class)) {
                Policies policies = (Policies) a;
                for (Policy p : policies.policies()) {
                    try {
                        Constructor<IPolicy> c = p.policy().getDeclaredConstructor(String.class);
                        list.add(c.newInstance(p.name()));
                        logger.info("..-> {}: {}", entity.getClass().getCanonicalName(), p.name());
                    } catch (Exception ex) {
                        throw new Exception(ex.getMessage());
                    }
                }
            }
        }
        return list;
    }

    /**
     * Teste para verificar se um objecto é null
     * @param <T> Template da Entity.
     * @param entity Instancia da Entity.
     * @param obj Objeto, field da entity, para teste.
     * @param msg Mensagem de erro caso o obj seja null.
     */
    public static <T extends BaseEntity> void notNull(T entity, Object obj, String msg) {
        if (obj == null) {
            logger.info("..-> {}: {}", entity.getClass().getCanonicalName(), msg);
            entity.getMsg().add(msg);
        }
    }

    /**
     * Teste para verificar se um objecto é null ou vazio
     * @param <T> Template da Entity.
     * @param entity Instancia da Entity.
     * @param str Objeto, field da entity, para teste.
     * @param msg Mensagem de erro caso o obj seja null ou vazio.
     */
    public static <T extends BaseEntity> void notNullNotEmpty(T entity, String str, String msg) {
        if (str == null || str.isEmpty()) {
            logger.info("..-> {}: {}", entity.getClass().getCanonicalName(), msg);
            entity.getMsg().add(msg);
        }
    }
}
