/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tcc.relatorio.negocio.validator;

import javax.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tcc.relatorio.dominio.BaseEntity;
import org.tcc.relatorio.dominio.EmpresaEntity;
import org.tcc.relatorio.negocio.exception.util.BCExceptionUtil;
import org.tcc.relatorio.hammer.persistencia.exception.BCException;

/**
 *
 * @author Jose Wdison
 */
public class ValidadorUtil {
    public static final Integer INSERT = 1;
    public static final Integer UPDATE = 2;
    
    private static final Logger LOGGER = LoggerFactory.getLogger(ValidadorUtil.class);
    
    @Inject private EmpresaValidator empresaValidator;

    public void valida(EmpresaEntity entity, Integer tipo) throws BCException {
        valida(empresaValidator, entity, tipo);
    }

    private void valida(ValidadorEntity validador, BaseEntity entity, Integer tipo) throws BCException {
        if(!Validador.isNull(entity)){
            entity.getMsg().clear();
        }
        
        if(Validador.isEquals(INSERT, tipo)){
            validador.validaInsert(entity);
        }else if(Validador.isEquals(UPDATE, tipo)){
            validador.validaUpdate(entity);
        }else{
            throw BCExceptionUtil.prepara(null, new RuntimeException("Preencha um tipo de operacao valido para validar um entity. insert = 1 e update = 2"));
        }
        
        if(entity.isDirty()){
            for (String msg : entity.getMsg()) {
                LOGGER.debug(msg);
            }
        }
    }
}
