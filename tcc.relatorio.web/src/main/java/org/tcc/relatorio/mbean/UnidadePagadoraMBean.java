/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tcc.relatorio.mbean;

import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tcc.relatorio.dominio.PphUnidadePagadoraEntity;
import org.tcc.relatorio.negocio.UnidadePagadoraBC;
import org.tcc.relatorio.hammer.persistencia.exception.BCException;

/**
 *
 * @author Roger
 */
@SessionScoped
@ManagedBean(name = "unidadePagadoraMBean")
public class UnidadePagadoraMBean extends BaseMBean<PphUnidadePagadoraEntity> {

    /**
     *
     */
    private static final long serialVersionUID = 118231614034054149L;

    private static final Logger LOGGER = LoggerFactory.getLogger(UnidadePagadoraMBean.class);
    
    @EJB(name = "UnidadePagadoraBC")
    private UnidadePagadoraBC unidadeSaudeBC;

    /**
     * Construtor default.
     */
    public UnidadePagadoraMBean() {

        setParameterClass(PphUnidadePagadoraEntity.class);
        super.initialize("usuario");
        LOGGER.debug(getDescricaoMBean());

    }

    @PostConstruct
    public void initialize() {


    }

    @Override
    public List<PphUnidadePagadoraEntity> manter(ManterOp op) throws BCException {
        switch (op) {
            case INCLUIR:

                unidadeSaudeBC.incluir(getItem());
                break;

            case ATUALIZAR:
//                unidadeSaudeBC.atualizar(getItem());
                break;

            case LISTAR:
        }
        return null;
    }

    @Override
    public String compoeFiltro() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String compoePeriodo() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void prepararTelaInclusao() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void processarPDF(Map relatorioPadrao) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
