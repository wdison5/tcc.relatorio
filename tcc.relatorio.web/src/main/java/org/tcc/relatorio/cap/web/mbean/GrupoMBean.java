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
import org.tcc.relatorio.cap.dominio.GrupoEntity;
import org.tcc.relatorio.cap.dominio.UsuarioEntity;
import org.tcc.relatorio.cap.negocio.UsuarioBC;
import org.tcc.relatorio.util.FacesUtil;
import org.tcc.relatorio.hammer.persistencia.exception.BCException;

/**
 *
 * @author Roger
 */
@SessionScoped
@ManagedBean(name = "grupoMBean")
public class GrupoMBean extends BaseMBean<GrupoEntity> implements Serializable {

    private static final long serialVersionUID = 118231614034054149L;

    private static final Logger LOGGER = LoggerFactory.getLogger(GrupoMBean.class);

    @EJB(name = "UsuarioBC")
    private UsuarioBC usr;

    /**
     * Construtor default.
     */
    public GrupoMBean() {
        setParameterClass(GrupoEntity.class);
        super.initialize("grupo");
        LOGGER.debug(getDescricaoMBean());
    }

    public void initialize() {
        super.initialize("grupo");
    }
    
    

    @Override
    public List<GrupoEntity> manter(ManterOp op) throws BCException {
        switch (op) {
            case INCLUIR:
                usr.incluir(getItem());
                break;

            case ATUALIZAR:
                usr.atualizar(getItem());
                break;
            case EXCLUIR:
                usr.excluir(getItem());
                break;

            case LISTAR:
                return usr.listar(getItem(), (UsuarioEntity) FacesUtil.getInSession("usuarioLogado"));
        }
        return null;
    }

    public void preparaExclusao(Long id) {
        setId(id);
    }
}
