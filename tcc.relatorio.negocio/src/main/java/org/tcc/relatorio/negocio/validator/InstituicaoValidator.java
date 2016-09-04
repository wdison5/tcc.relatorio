/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tcc.relatorio.negocio.validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tcc.relatorio.dominio.InstituicaoEntity;
import org.tcc.relatorio.negocio.exception.util.BCExceptionUtil;
import static org.tcc.relatorio.negocio.validator.Validador.isBlank;
import static org.tcc.relatorio.negocio.validator.Validador.isMaior;
import static org.tcc.relatorio.negocio.validator.Validador.isNull;
import org.tcc.relatorio.hammer.persistencia.exception.BCException;

/**
 *
 * @author jwsouza
 */
class InstituicaoValidator extends ValidadorEntity<InstituicaoEntity> {

    private static final Logger LOGGER = LoggerFactory.getLogger(InstituicaoValidator.class);

    @Override
    public boolean validaInsert(InstituicaoEntity entity) throws BCException {
        try {
            valida(entity);
            if (isMaior(entity.getId(), 0)) {
                entity.getMsg().add("A instituição já existe na base de dados.");
            }
        } catch (Exception e) {
            throw BCExceptionUtil.prepara(LOGGER, e);
        }
        return entity.isOk();
    }

    @Override
    public boolean validaUpdate(InstituicaoEntity entity) throws BCException {
        try {
            valida(entity);
        } catch (Exception e) {
            throw BCExceptionUtil.prepara(LOGGER, e);
        }
        return entity.isOk();
    }

    @Override
    public boolean valida(InstituicaoEntity entity) {
        if (isNull(entity)) {
            entity = new InstituicaoEntity();
            LOGGER.info("instituição não informada.");
            entity.getMsg().add("O objeto instituição está vazio.");
        }

        if (isBlank(entity.getTpUnid())) {
            entity.getMsg().add("Selecione um tipo de unidade.");
        }
        if (isEquals(entity.getTpUnid(), TipoUnidade.SAUDE.getCod()) && isNull(entity.getPphUnidSaude())) {
            entity.getMsg().add("Selecione uma unidade de saúde.");
        }
        if (isEquals(entity.getTpUnid(), TipoUnidade.PAGADORA.getCod()) && isNull(entity.getPphUnidadePagadora())) {
            entity.getMsg().add("Selecione uma unidade pagadora.");
        }

        return entity.isOk();
    }
}
