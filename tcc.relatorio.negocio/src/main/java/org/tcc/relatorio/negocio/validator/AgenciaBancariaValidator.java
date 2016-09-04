/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tcc.relatorio.negocio.validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tcc.relatorio.dominio.PphAgenciaBancariaEntity;
import org.tcc.relatorio.negocio.exception.util.BCExceptionUtil;
import static org.tcc.relatorio.negocio.validator.Validador.isBlank;
import static org.tcc.relatorio.negocio.validator.Validador.isMaior;
import static org.tcc.relatorio.negocio.validator.Validador.isNull;
import org.tcc.relatorio.hammer.persistencia.exception.BCException;

/**
 *
 * @author jwsouza
 */
class AgenciaBancariaValidator  extends ValidadorEntity<PphAgenciaBancariaEntity> {

    private static final Logger LOGGER = LoggerFactory.getLogger(EnderecoValidator.class);

    @Override
    public boolean validaInsert(PphAgenciaBancariaEntity entity) throws BCException {
        try {
            valida(entity);
            if (isMaior(entity.getId(), 0)) {
                entity.getMsg().add("O Agencia Bancaria já existe na base de dados.");
            }
        } catch (Exception e) {
            throw BCExceptionUtil.prepara(LOGGER, e);
        }
        return entity.isOk();
    }

    @Override
    public boolean validaUpdate(PphAgenciaBancariaEntity entity) throws BCException {
        valida(entity);
        return entity.isOk();
    }

    @Override
    public boolean valida(PphAgenciaBancariaEntity entity) {
        if (isNull(entity)) {
            entity = new PphAgenciaBancariaEntity();
            entity.getMsg().add("O objeto atestado de vida está vazio.");
        }
        if (isBlank(entity.getNmAgBancaria())) {
            entity.getMsg().add("Preencha um nome/descrição para a agência bancária.");
        }
        if (isBlank(entity.getNrAgencia())) {
            entity.getMsg().add("Preencha o numero da agência bancária.");
        }

        if (isNull(entity.getPphBanco())) {
            entity.getMsg().add("Selecione um banco.");
        }
        if (isNull(entity.getPphMunicipio())) {
            entity.getMsg().add("Selecione um município.");
        }
        return entity.isOk();
    }

}
