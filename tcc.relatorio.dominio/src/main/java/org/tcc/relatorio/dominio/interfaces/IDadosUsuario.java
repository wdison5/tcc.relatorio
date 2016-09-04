/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tcc.relatorio.dominio.interfaces;

/**
 *
 * @author Jose Wdison
 */
public interface IDadosUsuario extends IEntityPreencher{

    public Long getIdUserIncl();

    public void setIdUserIncl(Long idUserIncl);

    public Long getIdUserAlt();

    public void setIdUserAlt(Long idUserAlt);

}
