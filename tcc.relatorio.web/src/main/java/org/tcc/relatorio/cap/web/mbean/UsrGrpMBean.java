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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.tcc.relatorio.cap.dominio.GrupoEntity;
import org.tcc.relatorio.cap.dominio.UsuarioEntity;
import org.tcc.relatorio.cap.negocio.UsuarioBC;
import org.tcc.relatorio.util.Funcoes;
import org.tcc.relatorio.hammer.persistencia.exception.BCException;

/**
 *
 * @author Roger
 */
@SessionScoped
@ManagedBean(name = "usrGrpMBean")
public class UsrGrpMBean extends BaseMBean<UsuarioEntity> implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 118231614034054149L;
    private static final Logger logger = LoggerFactory.getLogger(UsrGrpMBean.class);
    @EJB(name = "UsuarioBC")
    private UsuarioBC usr;
    private List<GrupoEntity> grupos;
    private List<String> gruposSelecionados;

    /**
     * Construtor default.
     */
    public UsrGrpMBean() {
        setParameterClass(UsuarioEntity.class);
        super.initialize("usrGrp");
        logger.debug(getDescricaoMBean());

        gruposSelecionados = new ArrayList<String>();
    }

    @Override
    public List<UsuarioEntity> manter(ManterOp op) throws BCException {
        switch (op) {
            case ASSOCIAR:
                associarUsrGrp();
                break;

            case LISTAR:
                Set<UsuarioEntity> set = usr.listar(getItem(), (List<Long>)Funcoes.getSessionObject("lstInstituicaoId"), (UsuarioEntity)Funcoes.getSessionObject("usuarioLogado"));
                return new ArrayList<UsuarioEntity>(set);
        }
        return null;
    }

    private List<GrupoEntity> listarGrupos() {
        GrupoEntity grupo = new GrupoEntity();
        try {
            grupo.setNome("");
            grupos = usr.listar(grupo, (UsuarioEntity)Funcoes.getSessionObject("usuarioLogado"));
        } catch (BCException ex) {
            logger.error("Erro interno: {}", ex.getMessage());
        }
        return grupos;
    }

    private void associarUsrGrp() throws BCException {
        Set<GrupoEntity> gruposAssociar = new HashSet<GrupoEntity>();
        Set<GrupoEntity> gruposDesassociar = new HashSet<GrupoEntity>();

        try {
            for (GrupoEntity g : getGrupos()) {
                if (gruposSelecionados.contains(g.getNome())) {
                    if (!getItem().getGrupos().contains(g)) {
                        gruposAssociar.add(g);
                    }
                } else {
                    //if (getItem().getGrupos().contains(g)) {
                        gruposDesassociar.add(g);
                   // }
                }
            }

            if (!gruposDesassociar.isEmpty()) {
                usr.desassociar(getItem(), gruposDesassociar);
            }

            if (!gruposAssociar.isEmpty()) {
                usr.associar(getItem(), gruposAssociar);
            }

            setLista(new ArrayList<UsuarioEntity>(usr.listar(getItem(), (List<Long>)Funcoes.getSessionObject("lstInstituicaoId"), (UsuarioEntity)Funcoes.getSessionObject("usuarioLogado"))));
            setId(getItem().getId());

        } catch (BCException ex) {
            logger.error("Erro interno: {}", ex.getMessage());
            throw ex;
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
     * Auto Completar o input do nome.
     *
     * @param query Parte do nome para buscar.
     * @return List<String> Lista com os possivies nomes do auto complete.
     */
    @Override
    public List<String> completar(String query) {
        List<String> results = new ArrayList<String>();

        UsuarioEntity user = new UsuarioEntity();
        try {
            user.setUserId(query);
            Set<UsuarioEntity> list = usr.listar(user, (List<Long>)Funcoes.getSessionObject("lstInstituicaoId"), (UsuarioEntity)Funcoes.getSessionObject("usuarioLogado"));
            logger.debug("completar().size: {}", list.size());
            for (UsuarioEntity s : list) {
                results.add(s.getUserId());
            }
        } catch (Exception ex) {
            logger.error("Erro interno: {}-{}", query, ex);
            FacesContext.getCurrentInstance().addMessage(getDescricaoMBean() + "Form", new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Erro interno: " + getItem().getNome() + " : " + ex.getMessage(), ""));
        }
        return results;
    }

    /**
     * @return the gruposSelecionados
     */
    public List<String> getGruposSelecionados() {

        try {
            gruposSelecionados = new ArrayList<String>();
            for (GrupoEntity g2 : getItem().getGrupos()) {
                gruposSelecionados.add(g2.getNome());
            }
        } catch (Exception ex) {
            logger.error("Erro interno: {}", ex.getMessage());
            FacesContext.getCurrentInstance().addMessage(getDescricaoMBean() + "Form", new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Erro interno: " + getItem().getNome() + " : " + ex.getMessage(), ""));
        }
        return gruposSelecionados;
    }

    /**
     * @param gruposSelecionados the gruposSelecionados to set
     */
    public void setGruposSelecionados(List<String> gruposSelecionados) {
        this.gruposSelecionados = gruposSelecionados;
    }

    /**
     * @return the grupos
     */
    public List<GrupoEntity> getGrupos() {
        try {
            grupos = listarGrupos();
        } catch (Exception ex) {
            logger.error("Erro interno: {}", ex.getMessage());
            FacesContext.getCurrentInstance().addMessage(getDescricaoMBean() + "Form", new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Erro interno: " + getItem().getNome() + " : " + ex.getMessage(), ""));
        }
        return grupos;
    }

    /**
     * @param grupos the grupos to set
     */
    public void setGrupos(List<GrupoEntity> grupos) {
        this.grupos = grupos;
    }
}
