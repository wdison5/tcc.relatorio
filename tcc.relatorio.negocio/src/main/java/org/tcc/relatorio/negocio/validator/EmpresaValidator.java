/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tcc.relatorio.negocio.validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tcc.relatorio.dominio.EmpresaEntity;
import org.tcc.relatorio.negocio.exception.util.BCExceptionUtil;
import static org.tcc.relatorio.negocio.validator.Validador.isMaior;
import static org.tcc.relatorio.negocio.validator.Validador.isNull;
import org.tcc.relatorio.hammer.persistencia.exception.BCException;

/**
 *
 * @author jwsouza
 */
class EmpresaValidator extends ValidadorEntity<EmpresaEntity> {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmpresaValidator.class);

    @Override
    public boolean validaInsert(EmpresaEntity entity) throws BCException {
        try {
            valida(entity);
            if (isMaior(entity.getId(), 0)) {
                entity.getMsg().add("A empresa já existe na base de dados.");
            }
        } catch (Exception e) {
            throw BCExceptionUtil.prepara(LOGGER, e);
        }
        return entity.isOk();
    }

    @Override
    public boolean validaUpdate(EmpresaEntity entity) throws BCException {
        try {
            valida(entity);
        } catch (Exception e) {
            throw BCExceptionUtil.prepara(LOGGER, e);
        }
        return entity.isOk();
    }

    @Override
    public boolean valida(EmpresaEntity entity) {
        if (isNull(entity)) {
            entity = new EmpresaEntity();
            LOGGER.info("empresa não informada.");
            entity.getMsg().add("O objeto empresa está vazio.");
        }
        
        if (isBlank(entity.getNmEmpresa())) {
            entity.getMsg().add("Preencha o nome da empresa.");
        }
        
        if (isBlank(entity.getCdEmpresa())) {
            entity.getMsg().add("Preencha o codigo da empresa.");
        }

        return entity.isOk();
    }
}
