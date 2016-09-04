/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tcc.relatorio.cap.dominio.policy;

import org.tcc.relatorio.cap.dominio.BaseEntity;
import org.tcc.relatorio.cap.dominio.util.IPolicy;
import org.tcc.relatorio.cap.dominio.util.PolicyHelper;
import org.tcc.relatorio.cap.dominio.util.PolicyOp;
import static org.tcc.relatorio.cap.dominio.util.PolicyOp.INSERT;
import static org.tcc.relatorio.cap.dominio.util.PolicyOp.UPDATE;

/**
 *
 * @author roger
 */
public class BaseNotNull implements IPolicy {

    /**
     * Constutor.
     * @param name nome da policy.
     */
    public BaseNotNull(String name) {
    }
    
    @Override
    public <T extends BaseEntity> boolean apply(T entity, PolicyOp op) {
        if(op == INSERT || op == UPDATE) {
//            PolicyHelper.notNull(entity, entity.getDataInicio(),      "Data de Inicio deve ser fornecida");
            PolicyHelper.notNullNotEmpty(entity, entity.getNome(),    "Nome do grupo não pode ser nulo");
        }
        return true;
    }
}
