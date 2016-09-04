/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tcc.relatorio.dominio.interfaces;

import java.util.Date;

/**
 *
 * @author Jose Wdison
 */
public interface IDadosData extends IEntityPreencher{

    public Date getDhAlteracao();

    public void setDhAlteracao(Date dhAlteracao);

    public Date getDhInclusao();

    public void setDhInclusao(Date dhInclusao);
}
