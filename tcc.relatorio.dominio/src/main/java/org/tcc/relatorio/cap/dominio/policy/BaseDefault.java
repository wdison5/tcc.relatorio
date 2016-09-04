/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tcc.relatorio.cap.dominio.policy;

import org.tcc.relatorio.cap.dominio.BaseEntity;
import org.tcc.relatorio.cap.dominio.util.IPolicy;
import org.tcc.relatorio.cap.dominio.util.PolicyOp;


/**
 *
 * @author 140200
 */
public class BaseDefault implements IPolicy {
    
    /**
     * Construtor.
     * @param name nome da policy.
     */
    public BaseDefault(String name) {
    }

    @Override
    public <T extends BaseEntity> boolean apply(T entity, PolicyOp op) {
//        if(op == INSERT) {
//            entity.setDataTermino(null);
//        }
        return true;
    }
}
