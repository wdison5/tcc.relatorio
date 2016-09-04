/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tcc.relatorio.cap.negocio.service;

import javax.ejb.Remote;
import org.tcc.relatorio.cap.dominio.UsuarioEntity;
import org.tcc.relatorio.hammer.persistencia.exception.BCException;

/**
 *
 * @author 140200
 */
@Remote
public interface AutenticadorSC {
    UsuarioEntity autenticar(UsuarioEntity usuario) throws BCException;
}
