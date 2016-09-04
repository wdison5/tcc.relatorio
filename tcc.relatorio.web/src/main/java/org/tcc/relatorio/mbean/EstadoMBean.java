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
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tcc.relatorio.cap.dominio.UsuarioEntity;
import org.tcc.relatorio.dominio.PphUnidSaudeEntity;
import org.tcc.relatorio.dominio.PphEstadoEntity;
import org.tcc.relatorio.dominio.PphUnidadePagadoraEntity;
import org.tcc.relatorio.mbean.exception.util.MBeanExceptionUtil;
import org.tcc.relatorio.negocio.EstadoBC;
import org.tcc.relatorio.negocio.UnidSaudeBC;
import org.tcc.relatorio.negocio.UnidadePagadoraBC;
import org.tcc.relatorio.hammer.persistencia.exception.BCException;

/**
 *
 * @author Jose Wdison de Souza
 */
@SessionScoped
@ManagedBean(name = "EstadoMBean")
public class EstadoMBean extends BaseMBean<PphEstadoEntity> {

    private static final long serialVersionUID = 118231614034054148L;

    private static final Logger LOGGER = LoggerFactory.getLogger(EstadoMBean.class);

    @EJB(name = "EstadoBC")
    private EstadoBC EstadoBC;

    @EJB(name = "UnidSaudeBC")
    private UnidSaudeBC unidSaudeBC;
    @EJB(name = "UnidadePagadoraBC")
    private UnidadePagadoraBC unidadePagadoraBC;

    private UsuarioEntity usuarioLogado;

    private List<PphUnidadePagadoraEntity> lstUnidadePagadora;
    private List<PphUnidSaudeEntity> lstInstituicao;

    private PphUnidSaudeEntity instituicao;
    private PphUnidadePagadoraEntity unidadePagadora;

    /**
     * Construtor default.
     */
    public EstadoMBean() {

        setParameterClass(PphEstadoEntity.class);
        super.initialize("usuario");
        LOGGER.debug(getDescricaoMBean());
    }

    @PostConstruct
    public void initialize() {

        try {
            HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
            this.usuarioLogado = (UsuarioEntity) request.getSession().getAttribute("usuarioLogado");

            //lstInstituicao = unidSaudeBC.list(this.usuarioLogado.getUserId());
            //lstUnidadePagadora = unidadePagadoraBC.list(this.usuarioLogado.isAcessoGeral());
        } catch (Exception e) {
            MBeanExceptionUtil.log(LOGGER, e, "Erro ao buscar unidades de saude: " + e.getMessage());
        }

    }

    @Override
    public List<PphEstadoEntity> manter(ManterOp op) throws BCException {
        switch (op) {
            case INCLUIR:

            case ATUALIZAR:

            case LISTAR:
        }
        return null;
    }

    public void onSelectUnidadePagadoraChange() {
        try {
            unidadePagadora = unidadePagadoraBC.consultar(unidadePagadora.getId());
            if (unidadePagadora != null) {
//                getItem().setPphUnidadePagadora(unidadePagadora);
            }
        } catch (Exception ex) {
            MBeanExceptionUtil.log(LOGGER, ex);
//            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Unidade Pagadora não encontrado. Id: " + getItem().getPphUnidadePagadora().getId()));
        }
    }

    public void onSelectInstituicaoChange() {
        try {
            instituicao = unidSaudeBC.getById(instituicao.getId());
            if (instituicao != null) {
//                getItem().setInstituicao(instituicao);
            }
        } catch (Exception ex) {
            MBeanExceptionUtil.log(LOGGER, ex);
//            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Unidade Saúde não encontrado. Id: " + getItem().getPphUnidadePagadora().getId()));
        }
    }

    public List<PphEstadoEntity> pesquisaEstadoPorNome(String nomeEstado) {
        try {
            Object session = FacesContext.getCurrentInstance().getExternalContext().getSession(false);
            LOGGER.debug("" + session);

            return EstadoBC.pesquisaPorNome(nomeEstado);
        } catch (Exception ex) {
            MBeanExceptionUtil.log(LOGGER, ex);
        }
        return null;
    }

    public List<PphUnidadePagadoraEntity> getLstUnidadePagadora() {
        return lstUnidadePagadora;
    }

    public void setLstUnidadePagadora(List<PphUnidadePagadoraEntity> lstUnidadePagadora) {
        this.lstUnidadePagadora = lstUnidadePagadora;
    }

    public List<PphUnidSaudeEntity> getLstInstituicao() {
        return lstInstituicao;
    }

    public void setLstInstituicao(List<PphUnidSaudeEntity> lstInstituicao) {
        this.lstInstituicao = lstInstituicao;
    }

    public PphUnidSaudeEntity getInstituicao() {
        if (instituicao == null) {
            instituicao = new PphUnidSaudeEntity();
        }
        return instituicao;
    }

    public void setInstituicao(PphUnidSaudeEntity instituicao) {
        this.instituicao = instituicao;
    }

    @Override
    public void processarPDF(Map relatorioPadrao) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
}
