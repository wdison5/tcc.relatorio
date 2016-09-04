/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tcc.relatorio.negocio.validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tcc.relatorio.dominio.PphAtestadoVidaEntity;
import org.tcc.relatorio.negocio.exception.util.BCExceptionUtil;
import static org.tcc.relatorio.negocio.validator.Validador.isBlank;
import static org.tcc.relatorio.negocio.validator.Validador.isMaior;
import static org.tcc.relatorio.negocio.validator.Validador.isNull;
import org.tcc.relatorio.hammer.persistencia.exception.BCException;

/**
 *
 * @author jwsouza
 */
class AtestadoVidaValidator extends ValidadorEntity<PphAtestadoVidaEntity> {

    private static final Logger LOGGER = LoggerFactory.getLogger(EnderecoValidator.class);

    @Override
    public boolean validaInsert(PphAtestadoVidaEntity entity) throws BCException {
        try {
            valida(entity);
            if (isMaior(entity.getId(), 0)) {
                entity.getMsg().add("O Atestado de vida já existe na base de dados.");
            }
        } catch (Exception e) {
            throw BCExceptionUtil.prepara(LOGGER, e);
        }
        return entity.isOk();
    }

    @Override
    public boolean validaUpdate(PphAtestadoVidaEntity entity) throws BCException {
        valida(entity);
        return entity.isOk();
    }

    @Override
    public boolean valida(PphAtestadoVidaEntity entity) {
        if (isNull(entity)) {
            entity = new PphAtestadoVidaEntity();
            entity.getMsg().add("O objeto atestado de vida está vazio.");
        }
        if (isBlank(entity.getCdCrm())) {
            entity.getMsg().add("Preencha o CRM.");
        }
        if (isBlank(entity.getNmMedicoResp())) {
            entity.getMsg().add("Preencha o nome do responsável pela atestação.");
        }

        if (isNull(entity.getDtAtestado())) {
            entity.getMsg().add("Preencha data da atestação.");
        }
        
        if (isNull(entity.getPphBeneficiario())||isMenor(entity.getPphBeneficiario().getId(), 1)) {
            entity.getMsg().add("O atestado não está associado a nenhum beneficiário.");
        }
        return entity.isOk();
    }

}
