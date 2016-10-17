package org.tcc.relatorio.grid;

import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tcc.relatorio.dominio.EmpresaEntity;
import org.tcc.relatorio.mbean.EmpresaMBean;
import org.tcc.relatorio.negocio.validator.TipoUnidade;
import org.tcc.relatorio.negocio.validator.Validador;
import org.tcc.relatorio.util.Campo;
import org.tcc.relatorio.util.FacesUtil;
import org.tcc.relatorio.util.Funcoes;
import org.tcc.relatorio.hammer.persistencia.exception.BCException;

/**
 *
 * @author Jose Wdison
 */

@SessionScoped
@ManagedBean
public final class EmpresaGrade extends BaseGrade<EmpresaMBean, EmpresaEntity> implements Serializable{
    private static final Logger LOGGER = LoggerFactory.getLogger(EmpresaGrade.class);

    private boolean adminEmpresas = false;
    private boolean consultaEmpresas = false;
    
    public EmpresaGrade() {
        super.iniciar();
        
        Campo[] estrutura = {
            new Campo("cdEmpresa",       true, 10, "CÓDIGO",        0, "center",    "numero",   false, true ),
            new Campo("nmEmpresa",       true, 50, "Nome",          0, "left",      "texto",    false, true ),
        };
        super.addColumns(estrutura);
        super.setNome("Empresa");
        super.setBotaoEditar(false);
        super.setBotaoDetalhar(true);
        super.setBotaoExcluir(true);
        super.setActionEditar("/view/empresa/editar.jsf?faces-redirect=true");
        super.setActionDetalhar("/view/empresa/detalhar.jsf?faces-redirect=true");
    }
    
    @PostConstruct
    private void posContruct() {
        adminEmpresas = FacesUtil.isUserInRole("AdminEmpresas");
        consultaEmpresas = FacesUtil.isUserInRole("ConsultarEmpresas");
    } 
   
    @Override
    public void selecionar(Object mBean, Object entidade) throws BCException {
        LOGGER.debug(""+entidade);
        LOGGER.debug(""+mBean);
    }

    @Override
    public void detalhar(EmpresaMBean mBean, EmpresaEntity entidade, Object aux) throws BCException {
        LOGGER.debug(""+entidade);
        LOGGER.debug(""+mBean);
        mBean.gravaMemoria();
        mBean.setItem(entidade);
    }

    @Override
    public void editar(EmpresaMBean mBean, EmpresaEntity entidade, Object aux) throws BCException {
        LOGGER.debug(""+entidade);
        LOGGER.debug(""+mBean);
        mBean.gravaMemoria();
        mBean.setItem(entidade);
    }
    
    public void mostraBotoesEditarExcluir(boolean mostraBotoesEditarExcluir) {
        setBotaoEditar(mostraBotoesEditarExcluir);
        setBotaoExcluir(mostraBotoesEditarExcluir);
    }

    @Override
    public Boolean getBotaoExcluir() {
        return super.getBotaoExcluir() && adminEmpresas;
    }

    @Override
    public Boolean getBotaoDetalhar() {
        return super.getBotaoDetalhar() && (adminEmpresas || consultaEmpresas);
    }
    
}