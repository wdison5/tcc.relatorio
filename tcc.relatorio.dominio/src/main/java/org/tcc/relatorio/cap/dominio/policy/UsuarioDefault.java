/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tcc.relatorio.cap.dominio.policy;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.Date;
import org.tcc.relatorio.cap.dominio.BaseEntity;
import org.tcc.relatorio.cap.dominio.UsuarioEntity;
import org.tcc.relatorio.cap.dominio.util.IPolicy;
import org.tcc.relatorio.cap.dominio.util.PolicyOp;
import static org.tcc.relatorio.cap.dominio.util.PolicyOp.*;
import org.tcc.relatorio.cap.dominio.util.Sha1;

/**
 *
 * @author 140200
 */
public class UsuarioDefault implements IPolicy {
    
    private Calendar cal;

    /**
     * Construtor.
     * @param name Nome da policy. 
     */
    public UsuarioDefault(String name) {
        cal = Calendar.getInstance();
    }

    @Override
    public <T extends BaseEntity> boolean apply(T entity, PolicyOp op) {
        UsuarioEntity usuario = (UsuarioEntity) entity;

        if(op == INSERT) {
//            usuario.setDataTermino(entity.getDataTermino());
            usuario.setDataCriacao(new Date());
           // usuario.setTentativas(0);
            
            // senha expirada ...
            cal.setTime(new Date());
            cal.add(Calendar.DATE, -1);
            usuario.setDataExpSenha(cal.getTime());
        } else if(op == RESET) {
            try {
                usuario.setSenha(Sha1.digest(usuario.getSenha()));
            } catch (UnsupportedEncodingException ex) {
            } catch (NoSuchAlgorithmException ex) {
            }
            usuario.setDataExpSenha(null);
        }
        return true;
    }
}
