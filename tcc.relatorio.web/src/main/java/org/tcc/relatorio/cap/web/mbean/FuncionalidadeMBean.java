/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tcc.relatorio.cap.web.mbean;

import java.io.Serializable;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tcc.relatorio.cap.dominio.FuncionalidadeEntity;
import org.tcc.relatorio.cap.negocio.FuncionalidadeBC;
import org.tcc.relatorio.hammer.persistencia.exception.BCException;

/**
 *
 * @author Roger
 */
@SessionScoped
@ManagedBean(name = "funcionalidadeMBean")
public class FuncionalidadeMBean extends BaseMBean<FuncionalidadeEntity> implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 118231614034054149L;

	private static final Logger logger = LoggerFactory.getLogger(FuncionalidadeMBean.class);

    @EJB(name="FuncionalidadeBC") private FuncionalidadeBC funcionalidadeBC;

    /**
     * Construtor default.
     */
    public FuncionalidadeMBean() {
        setParameterClass(FuncionalidadeEntity.class);
        super.initialize("funcionalidade");
        logger.debug(getDescricaoMBean());
    }

    @Override
    public List<FuncionalidadeEntity> manter(ManterOp op) throws BCException {
        switch(op) {
            case INCLUIR:
                funcionalidadeBC.incluir(getItem());
                break;
                
            case ATUALIZAR:
                funcionalidadeBC.atualizar(getItem());
                break;
                
            case LISTAR:
                return funcionalidadeBC.listar(getItem());
        }
        return null;
    }
}
