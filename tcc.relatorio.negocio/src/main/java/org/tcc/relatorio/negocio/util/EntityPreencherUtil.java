/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tcc.relatorio.negocio.util;

import java.util.Date;
import org.tcc.relatorio.cap.dominio.UsuarioEntity;
import org.tcc.relatorio.dominio.interfaces.IDadosData;
import org.tcc.relatorio.dominio.interfaces.IDadosUsuario;
import org.tcc.relatorio.dominio.interfaces.IEntityPreencher;
import org.tcc.relatorio.dominio.interfaces.IExclusao;
import org.tcc.relatorio.enumeracao.Confirmacao;

/**
 *
 * @author Jose Wdison
 */
public class EntityPreencherUtil {

    private UsuarioEntity usuario;

    public EntityPreencherUtil(UsuarioEntity usuario) {
        if (usuario == null) {
            throw new RuntimeException("Usuário invalido para configuração de entity. usuario: " + usuario);
        }
        this.usuario = usuario;
    }

    private EntityPreencherUtil usuario(IDadosUsuario entity) {
        if (entity != null) {
            entity.setIdUserAlt(this.usuario.getId());
            if (entity.getIdUserIncl() == null || entity.getIdUserIncl() < 1) {
                entity.setIdUserIncl(this.usuario.getId());
            }
        }
        return this;
    }

    private EntityPreencherUtil data(IDadosData entity) {
        if (entity != null) {
            entity.setDhAlteracao(new Date());
            if (entity.getDhInclusao() == null) {
                entity.setDhInclusao(new Date());
            }
        }
        return this;
    }

    private EntityPreencherUtil flExclusao(IExclusao entity) {
        if (entity.getFlExclusao()==null) {
            entity.setFlExclusao(Confirmacao.NAO.getId());
        }
        return this;
    }

    public EntityPreencherUtil set(IEntityPreencher entity) {
        boolean isDadosUsuario = entity instanceof IDadosUsuario,
                isDadosData = entity instanceof IDadosData,
                isFlExcluir = entity instanceof IExclusao;
        if (isDadosUsuario) {
            usuario((IDadosUsuario) entity);
        }
        if (isDadosData) {
            data((IDadosData) entity);
        }
        if (isFlExcluir) {
            flExclusao((IExclusao) entity);
        }
        if (!isDadosData && !isDadosUsuario && !isFlExcluir) {
            throw new RuntimeException("Não existe nada para preencher nessa entidade. Entity: " + entity);
        }
        return this;
    }
}
