package org.tcc.relatorio.grid;

import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tcc.relatorio.dominio.InstituicaoEntity;
import org.tcc.relatorio.mbean.InstituicaoMBean;
import org.tcc.relatorio.negocio.validator.TipoUnidade;
import org.tcc.relatorio.negocio.validator.Validador;
import org.tcc.relatorio.util.Campo;
import org.tcc.relatorio.util.FacesUtil;
import org.tcc.relatorio.util.Funcoes;
import org.tcc.relatorio.hammer.persistencia.exception.BCException;

/**
 *
 * @author Eloy
 */

@SessionScoped
@ManagedBean
public final class InstituicaoGrade extends BaseGrade<InstituicaoMBean, InstituicaoEntity> implements Serializable{
    private static final Logger LOGGER = LoggerFactory.getLogger(InstituicaoGrade.class);

    private boolean adminInstituicoes = false;
    private boolean consultaInstituicoes = false;
    
    public InstituicaoGrade() {
        super.iniciar();
        
        Campo[] estrutura = {
            new Campo("tpUnid", true, 10, "TIPO DE INSTITUIÇÃO",    1, "left",      "tpUnid",   false, true ),
            new Campo(null,       true, 10, "CÓDIGO",                 0, "center",    "codigo",   false, true ),
            new Campo(null,       true, 50, "INSTITUIÇÃO",            0, "left",      "nome",     false, true ),
        };
        super.addColumns(estrutura);
        super.setNome("Instituição");
        super.setBotaoEditar(false);
        super.setBotaoDetalhar(true);
        super.setBotaoExcluir(true);
        super.setActionEditar("/view/instituicao/editar.jsf?faces-redirect=true");
        super.setActionDetalhar("/view/instituicao/detalhar.jsf?faces-redirect=true");
    }
    
    @PostConstruct
    private void posContruct() {
        adminInstituicoes = FacesUtil.isUserInRole("AdminInstituicoes");
        consultaInstituicoes = FacesUtil.isUserInRole("ConsultarInstituicoes");
    }

    public Object tpUnid(Object informacao, Tipo tipo){
        
        switch (tipo) {
            case HINT:
                return TipoUnidade.PAGADORA.getCod().equals(informacao)?"Unidade Pagadora":"Unidade Saúde";
            case SORT:
                return TipoUnidade.PAGADORA.getCod().equals(informacao)?"Unidade Pagadora":"Unidade Saúde";
            case GRID:
                return TipoUnidade.PAGADORA.getCod().equals(informacao)?"Unidade Pagadora":"Unidade Saúde";
        }
        return informacao;
    }
    
    public Object codigo(Object informacao, Tipo tipo){
        InstituicaoEntity entity = (InstituicaoEntity) informacao;
        
        switch (tipo) {
            case HINT:
                return labelCodigo(entity);
            case SORT:
                return Integer.valueOf(labelCodigo(entity));
            case GRID:
                return labelCodigo(entity);
        }
        return informacao;
    }
    
    public Object nome(Object informacao, Tipo tipo){
        InstituicaoEntity entity = (InstituicaoEntity) informacao;

        switch (tipo) {
            case HINT:
                return labelInsituicao(entity);
            case SORT:
                return labelInsituicao(entity);
            case GRID:
                return labelInsituicao(entity);
        }
        return informacao;
    }
    
    @Override
    public void selecionar(Object mBean, Object entidade) throws BCException {
        LOGGER.debug(""+entidade);
        LOGGER.debug(""+mBean);
    }

    @Override
    public void detalhar(InstituicaoMBean mBean, InstituicaoEntity entidade, Object aux) throws BCException {
        LOGGER.debug(""+entidade);
        LOGGER.debug(""+mBean);
        mBean.gravaMemoria();
        mBean.setItem(entidade);
    }

    @Override
    public void editar(InstituicaoMBean mBean, InstituicaoEntity entidade, Object aux) throws BCException {
        LOGGER.debug(""+entidade);
        LOGGER.debug(""+mBean);
        mBean.gravaMemoria();
        mBean.setItem(entidade);
    }
    
    public void mostraBotoesEditarExcluir(boolean mostraBotoesEditarExcluir) {
        setBotaoEditar(mostraBotoesEditarExcluir);
        setBotaoExcluir(mostraBotoesEditarExcluir);
    }

    private InstituicaoEntity getEntity(Long id) {
        InstituicaoEntity result = null;
        Object sessionObject = Funcoes.getSessionObject("instituicaoMBean");
        if(sessionObject!=null){
            InstituicaoMBean instituicaoMBean = (InstituicaoMBean) sessionObject;
            for(InstituicaoEntity entity: instituicaoMBean.getLista()){
                if(Validador.isEquals(entity.getId(), id)){
                    result = entity;
                    break;
                }
            }
        }
        return result;
    }
    
    public String labelInsituicao(InstituicaoEntity institu) {
        String result = "";
        if (institu != null) {
            if ("US".equals(institu.getTpUnid())) {
                result = institu.getPphUnidSaude().getNome();
            }
            if ("UP".equals(institu.getTpUnid())) {
                result = institu.getPphUnidadePagadora().getNmUnidadePagadora();
            }
        }
        return result;
    }
    
    public String labelCodigo(InstituicaoEntity institu) {
        String result = "";
        if (institu != null) {
            if ("US".equals(institu.getTpUnid())) {
                result = ""+institu.getPphUnidSaude().getCnes();
            }
            if ("UP".equals(institu.getTpUnid())) {
                result = ""+institu.getPphUnidadePagadora().getCdUnidadePagadora();
            }
        }
        return result;
    }

    @Override
    public Boolean getBotaoExcluir() {
        return super.getBotaoExcluir() && adminInstituicoes;
    }

    @Override
    public Boolean getBotaoDetalhar() {
        return super.getBotaoDetalhar() && (adminInstituicoes || consultaInstituicoes);
    }
    
}