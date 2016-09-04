/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tcc.relatorio.negocio.validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tcc.relatorio.dominio.PphProcuradorEntity;
import org.tcc.relatorio.negocio.exception.util.BCExceptionUtil;
import org.tcc.relatorio.hammer.persistencia.exception.BCException;

/**
 *
 * @author jwsouza
 */
public class ProcuradorValidator extends ValidadorEntity<PphProcuradorEntity> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProcuradorValidator.class);

    @Override
    public boolean validaInsert(PphProcuradorEntity entity) throws BCException {
        try {
            valida(entity);
            if (isMaior(entity.getId(), 0)) {
                entity.getMsg().add("O procurador já existe na base de dados.");
            }
        } catch (Exception e) {
            throw BCExceptionUtil.prepara(LOGGER, e);
        }
        return entity.isOk();
    }

    @Override
    public boolean validaUpdate(PphProcuradorEntity entity) throws BCException {
        try {
            valida(entity);
        } catch (Exception e) {
            throw BCExceptionUtil.prepara(LOGGER, e);
        }
        return entity.isOk();
    }

    @Override
    public boolean valida(PphProcuradorEntity entity) {
        if (isNull(entity)) {
            entity = new PphProcuradorEntity();
            entity.getMsg().add("O objeto procurador está vazio.");
        }
        if (isBlank(entity.getNmProcurador())) {
            entity.getMsg().add("Preencha nome do procurador.");
        }
        if (isBlank(entity.getNrContaCorrente())) {
            entity.getMsg().add("Preencher numero da conta corrente do procurador.");
        }
        if(!isColecao(entity.getPphEnderecoSet())){
            entity.getMsg().add("Preencher endreço do procurador.");
        }
        if(isNull(entity.getPphAgenciaBancaria())){
            entity.getMsg().add("Preencher dados bancários do procurador.");
        }
        
        return entity.isOk();
    }
}
