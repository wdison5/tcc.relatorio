/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tcc.relatorio.cap.web.mbean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.management.MBeanException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tcc.relatorio.cap.dominio.FuncionalidadeEntity;
import org.tcc.relatorio.cap.dominio.GrupoEntity;
import org.tcc.relatorio.cap.dominio.UsuarioEntity;
import org.tcc.relatorio.cap.negocio.FuncionalidadeBC;
import org.tcc.relatorio.cap.negocio.UsuarioBC;
import org.tcc.relatorio.mbean.exception.util.MBeanExceptionUtil;
import org.tcc.relatorio.negocio.exception.util.BCExceptionUtil;
import org.tcc.relatorio.util.Funcoes;
import org.tcc.relatorio.hammer.persistencia.exception.BCException;

/**
 *
 * @author Roger
 */
@SessionScoped
@ManagedBean(name = "grpFuncMBean")
public class GrpFuncMBean extends BaseMBean<GrupoEntity> implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 118231614034054149L;
    private static final Logger logger = LoggerFactory.getLogger(GrpFuncMBean.class);
    @EJB(name = "UsuarioBC")
    private UsuarioBC usr;
    @EJB(name = "FuncionalidadeBC")
    private FuncionalidadeBC funcionalidadeBC;
    private List<FuncionalidadeEntity> funcionalidades;
    private List<String> funcionalidadesSelecionadas;

    /**
     * Construtor default.
     */
    public GrpFuncMBean() {
        setParameterClass(GrupoEntity.class);
        super.initialize("grpFunc");
        logger.debug(getDescricaoMBean());

        funcionalidadesSelecionadas = new ArrayList<String>();
    }

    @Override
    public List<GrupoEntity> manter(ManterOp op) throws BCException {
        switch (op) {
            case ASSOCIAR:
                associarGrpFunc();
                break;

            case LISTAR:
                return usr.listar(getItem(), (UsuarioEntity)Funcoes.getSessionObject("usuarioLogado"));
        }
        return null;
    }

    private List<FuncionalidadeEntity> listarFuncionalidades() throws MBeanException {
        FuncionalidadeEntity func = new FuncionalidadeEntity();
        try {
            func.setNome("");
            funcionalidades = funcionalidadeBC.listar(func);
        } catch (Exception ex) {
            throw MBeanExceptionUtil.prepara(logger, ex);
        }
        return funcionalidades;
    }

    private void associarGrpFunc() throws BCException {
        Set<FuncionalidadeEntity> funcAssociar = new HashSet<FuncionalidadeEntity>();
        Set<FuncionalidadeEntity> funcDesassociar = new HashSet<FuncionalidadeEntity>();

        try {
            for (FuncionalidadeEntity f : getFuncionalidades()) {
                if (funcionalidadesSelecionadas.contains(f.getNome())) {
                    if (!getItem().getFuncionalidades().contains(f)) {
                        funcAssociar.add(f);
                    }
                } else {
                    if (getItem().getFuncionalidades().contains(f)) {
                        funcDesassociar.add(f);
                    }
                }
            }

            if (!funcDesassociar.isEmpty()) {
                usr.desassociar(getItem(), funcDesassociar);
            }

            if (!funcAssociar.isEmpty()) {
                usr.associar(getItem(), funcAssociar);
            }

            setLista(new ArrayList<GrupoEntity>(usr.listar(getItem(), (UsuarioEntity)Funcoes.getSessionObject("usuarioLogado"))));
            setId(getItem().getId());

        } catch (Exception ex) {
            throw BCExceptionUtil.prepara(logger, ex, "Erro ao associar grp/funcionalidade");
        }
    }

    @Override
    public void setId(Long id) {
        super.setId(id);

        try {
            super.setItem(usr.consultar(getItem()));
        } catch (BCException ex) {
            logger.error("Erro interno: {}", ex.getMessage());
            FacesContext.getCurrentInstance().addMessage(getDescricaoMBean() + "Form", new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Erro interno: " + getItem().getNome() + " : " + ex.getMessage(), ""));
        }
    }

    /**
     * @return the gruposSelecionados
     */
    public List<String> getFuncionalidadesSelecionadas() {

        try {
            funcionalidadesSelecionadas = new ArrayList<String>();
            for (FuncionalidadeEntity f2 : getItem().getFuncionalidades()) {
                funcionalidadesSelecionadas.add(f2.getNome());
            }
        } catch (Exception ex) {
            logger.error("Erro interno: {}", ex.getMessage());
            FacesContext.getCurrentInstance().addMessage(getDescricaoMBean() + "Form", new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Erro interno: " + getItem().getNome() + " : " + ex.getMessage(), ""));
        }
        return funcionalidadesSelecionadas;
    }

    /**
     * @param funcSelecionadas the gruposSelecionados to set
     */
    public void setFuncionalidadesSelecionadas(List<String> funcSelecionadas) {
        this.funcionalidadesSelecionadas = funcSelecionadas;
    }

    /**
     * @return the grupos
     */
    public List<FuncionalidadeEntity> getFuncionalidades() {
        try {
            funcionalidades = listarFuncionalidades();
        } catch (Exception ex) {
            logger.error("Erro interno: {}", ex.getMessage());
            FacesContext.getCurrentInstance().addMessage(getDescricaoMBean() + "Form", new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Erro interno: " + getItem().getNome() + " : " + ex.getMessage(), ""));
        }
        return funcionalidades;
    }

    /**
     * @param funcs the grupos to set
     */
    public void setFuncionalidades(List<FuncionalidadeEntity> funcs) {
        this.funcionalidades = funcs;
    }
}
