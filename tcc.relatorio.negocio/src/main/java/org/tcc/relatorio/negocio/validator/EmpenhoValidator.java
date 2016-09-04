/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tcc.relatorio.negocio.validator;

import java.math.BigDecimal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tcc.relatorio.dominio.PphEmpenhoEntity;
import org.tcc.relatorio.negocio.exception.util.BCExceptionUtil;
import static org.tcc.relatorio.negocio.validator.Validador.isBlank;
import static org.tcc.relatorio.negocio.validator.Validador.isMaior;
import static org.tcc.relatorio.negocio.validator.Validador.isMenor;
import static org.tcc.relatorio.negocio.validator.Validador.isNull;
import org.tcc.relatorio.hammer.persistencia.exception.BCException;

/**
 *
 * @author jwsouza
 */
class EmpenhoValidator extends ValidadorEntity<PphEmpenhoEntity> {

    private static final Logger LOGGER = LoggerFactory.getLogger(EnderecoValidator.class);

    @Override
    public boolean validaInsert(PphEmpenhoEntity entity) throws BCException {
        try {
            valida(entity);
            if (isMaior(entity.getId(), 0)) {
                entity.getMsg().add("O empenho já existe na base de dados.");
            }
        } catch (Exception e) {
            throw BCExceptionUtil.prepara(LOGGER, e);
        }
        return entity.isOk();
    }

    @Override
    public boolean validaUpdate(PphEmpenhoEntity entity) throws BCException {
        valida(entity);
        return entity.isOk();
    }

    @Override
    public boolean valida(PphEmpenhoEntity entity) {
        if (isNull(entity)) {
            entity = new PphEmpenhoEntity();
            LOGGER.info("Empenho não informado");
            entity.getMsg().add("O objeto empenho está vazio.");
        }

        if (isBlank(entity.getNrEmpenho())) {
            entity.getMsg().add("Preencha o número do empenho.");
        }
        if (isNull(entity.getVlEmpenho())) {
            LOGGER.info("Preencha o valor do Pagamento.");
            entity.getMsg().add("Preencha o valor do Pagamento.");
        }
        if (isMenor(entity.getVlEmpenho(), new BigDecimal("0.01"))) {
            LOGGER.info("Valor do Pagamento deve ser maior que zero.");
            entity.getMsg().add("Valor do pagamento deve ser maior que zero.");
        }
        if (isNull(entity.getDtEmpenho())) {
            LOGGER.info("Preencha a Data do Empenho.");
            entity.getMsg().add("Preencha a Data do Empenho.");
        }

        if (isNull(entity.getPphBeneficiario()) || isMenor(entity.getPphBeneficiario().getId(), 1)) {
            entity.getMsg().add("O atestado não está associado a nenhum beneficiário.");
        }
        return entity.isOk();
    }

}
