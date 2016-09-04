package org.tcc.relatorio.mbean;

import java.util.ArrayList;
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
import org.tcc.relatorio.dominio.PphProcuradorEntity;
import org.tcc.relatorio.mbean.exception.util.MBeanExceptionUtil;
import org.tcc.relatorio.negocio.ProcuradorBC;
import org.tcc.relatorio.hammer.persistencia.exception.BCException;

/**
 *
 * @author Eloy
 */
@SessionScoped
@ManagedBean(name = "procuradorMBean")
public class ProcuradorMBean extends BaseMBean<PphProcuradorEntity> {

    private static final long serialVersionUID = 118231614034054148L;

    private static final Logger LOGGER = LoggerFactory.getLogger(ProcuradorMBean.class);

    @EJB(name = "ProcuradorBC")
    private ProcuradorBC procuradorBC;

    private UsuarioEntity usuarioLogado;


    public ProcuradorMBean() {
        setParameterClass(PphProcuradorEntity.class);
        super.initialize("Procurador");
        LOGGER.debug(getDescricaoMBean());
    }

    @PostConstruct
    public void initialize() {

        try {
            HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
            this.usuarioLogado = (UsuarioEntity)request.getSession().getAttribute("usuarioLogado");
        } catch (Exception e) {
            MBeanExceptionUtil.log(LOGGER, e, "Erro ao buscar unidades de saude: " + e.getMessage());
        }

    }

    public void setEstadoDefault(){
        super.initialize("Procurador");
        getItem().setId(null);
    }

    @Override
    public List<PphProcuradorEntity> manter(ManterOp op) throws BCException {
        switch (op) {
            case INCLUIR:
                break;
            case ATUALIZAR:
                break;
            case LISTAR:
                List<PphProcuradorEntity> set = procuradorBC.pesquisaPorNome(getItem().getNmProcurador());
                return new ArrayList<PphProcuradorEntity>(set);
        }
        return null;
    }

//    public List<PphProcuradorEntity> pesquisaProcuradorPorNome(String nomeProcurador) {
//        try {
//            Object session = FacesContext.getCurrentInstance().getExternalContext().getSession(false);
//            LOGGER.debug("" + session);
//
//            return procuradorBC.pesquisaPorNome(nomeProcurador);
//        } catch (Exception ex) {
//            MBeanExceptionUtil.log(LOGGER, ex);
//        }
//        return null;
//    }

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
