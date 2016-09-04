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
import org.tcc.relatorio.dominio.InstituicaoEntity;
import org.tcc.relatorio.dominio.PphAgenciaBancariaEntity;
import org.tcc.relatorio.dominio.PphAtestadoVidaEntity;
import org.tcc.relatorio.dominio.PphBeneficiarioEntity;
import org.tcc.relatorio.dominio.PphEmpenhoEntity;
import org.tcc.relatorio.dominio.PphEnderecoEntity;
import org.tcc.relatorio.dominio.PphProcuradorEntity;
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
    
    @Inject private BeneficiarioValidator beneficiarioValidator;
    @Inject private ProcuradorValidator procuradorValidator;
    @Inject private EnderecoValidator enderecoValidator;
    @Inject private AtestadoVidaValidator atestadoVidaValidator;
    @Inject private AgenciaBancariaValidator agenciaBancariaValidator;
    @Inject private EmpenhoValidator empenhoValidator;
    @Inject private InstituicaoValidator instituicaoValidator;
    
    public void valida(PphEnderecoEntity entity, Integer tipo) throws BCException {
        valida(enderecoValidator, entity, tipo);
    }

    public void valida(PphProcuradorEntity entity, Integer tipo) throws BCException {
        valida(procuradorValidator, entity, tipo);
    }

    public void valida(PphBeneficiarioEntity entity, Integer tipo) throws BCException {
        valida(beneficiarioValidator, entity, tipo);
    }

    public void valida(PphAtestadoVidaEntity entity, Integer tipo) throws BCException {
        valida(atestadoVidaValidator, entity, tipo);
    }

    public void valida(PphAgenciaBancariaEntity entity, Integer tipo) throws BCException {
        valida(agenciaBancariaValidator, entity, tipo);
    }

    public void valida(PphEmpenhoEntity entity, Integer tipo) throws BCException {
        valida(empenhoValidator, entity, tipo);
    }

    public void valida(InstituicaoEntity entity, Integer tipo) throws BCException {
        valida(instituicaoValidator, entity, tipo);
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
