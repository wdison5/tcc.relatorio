/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tcc.relatorio.negocio.validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tcc.relatorio.dominio.PphEnderecoEntity;
import org.tcc.relatorio.negocio.exception.util.BCExceptionUtil;
import org.tcc.relatorio.hammer.persistencia.exception.BCException;

/**
 *
 * @author jwsouza
 */
public class EnderecoValidator extends ValidadorEntity<PphEnderecoEntity> {

    private static final Logger LOGGER = LoggerFactory.getLogger(EnderecoValidator.class);

    @Override
    public boolean validaInsert(PphEnderecoEntity entity) throws BCException {
        try {
            valida(entity);
            if (isMaior(entity.getId(), 0)) {
                entity.getMsg().add("O Endereco já existe na base de dados.");
            }
        } catch (Exception e) {
            throw BCExceptionUtil.prepara(LOGGER, e);
        }
        return entity.isOk();
    }

    @Override
    public boolean validaUpdate(PphEnderecoEntity entity) throws BCException {
        valida(entity);
        return entity.isOk();
    }

    @Override
    public boolean valida(PphEnderecoEntity entity) {
        if (isNull(entity)) {
            entity = new PphEnderecoEntity();
            entity.getMsg().add("O objeto endereço está vazio.");
        }
        if (isBlank(entity.getCdCep())) {
            entity.getMsg().add("Preencha o CEP.");
        }
        if (isBlank(entity.getTpLogradouro())) {
            entity.getMsg().add("Selecione ou Preencha o Tipo de Logradouro.");
        }
        if (isBlank(entity.getNrEndereco())) {
            entity.getMsg().add("Preencha o Numero.");
        }
        if (isBlank(entity.getDsEndereco())) {
            entity.getMsg().add("Preencha o Logradouro.");
        }
        if (isBlank(entity.getNmBairro())) {
            entity.getMsg().add("Preencha o Bairro.");
        }
        if (isNull(entity.getPphMunicipio())) {
            entity.getMsg().add("Selecione o Município.");
        }
        return entity.isOk();
    }
}
