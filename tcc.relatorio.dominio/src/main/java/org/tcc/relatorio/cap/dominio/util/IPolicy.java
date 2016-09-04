/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tcc.relatorio.cap.dominio.util;

import org.tcc.relatorio.cap.dominio.BaseEntity;

/**
 *
 * @author roger
 */
public interface IPolicy {

    /**
     * Interface padrão para policy de enity.
     * 
     * @param <T> Template para Classe de entrada.
     * @param entity Entity para aplicação da policy.
     * @param op Tipo de operacao para aplicação da policy (INSERT,DELETE,UPDATE).
     * @return True se a policy foi aplicada.
     */
    <T extends BaseEntity> boolean apply(T entity, PolicyOp op);
}
