package org.tcc.relatorio.mbean;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tcc.relatorio.cap.dominio.UsuarioEntity;

import org.tcc.relatorio.dominio.PphAtestadoVidaEntity;
import org.tcc.relatorio.dominio.PphBeneficiarioEntity;
import org.tcc.relatorio.mbean.exception.util.MBeanExceptionUtil;
import org.tcc.relatorio.negocio.AtestadoVidaBC;
import org.tcc.relatorio.negocio.validator.Validador;
import org.tcc.relatorio.util.FacesUtil;
import org.tcc.relatorio.util.Funcoes;
import org.tcc.relatorio.hammer.persistencia.exception.BCException;

/**
 *
 * @author Jose Wdison de Souza
 */
@SessionScoped
@ManagedBean(name = "atestadoVidaMBean")
public class AtestadoVidaMBean extends BaseMBean<PphAtestadoVidaEntity> {

    private static final long serialVersionUID = 118231614034054148L;

    private static final Logger LOGGER = LoggerFactory.getLogger(AtestadoVidaMBean.class);

    @EJB(name = "AtestadoVidaBC")
    private AtestadoVidaBC atestadoVidaBC;

    private UsuarioEntity usuarioLogado;
    private PphBeneficiarioEntity beneficiario;

    private Date dtMax;
    private Date dtMin;
    private boolean detalhar = false;

    /**
     * Construtor default.
     */
    public AtestadoVidaMBean() {

        setParameterClass(PphAtestadoVidaEntity.class);
        super.initialize("Atestado de Vida");
        LOGGER.debug(getDescricaoMBean());
    }

    @PostConstruct
    public void initialize() {
        try {
            HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
            this.usuarioLogado = (UsuarioEntity) request.getSession().getAttribute("usuarioLogado");

            atestadoVidaBC.setUsuario(usuarioLogado);
        } catch (Exception e) {
            MBeanExceptionUtil.log(LOGGER, e, "Erro ao inicializar o atestado de vida: " + e.getMessage());
        }

        Calendar calendar = Calendar.getInstance();
        dtMax = calendar.getTime();
        calendar.add(Calendar.YEAR, -10);
        dtMin = calendar.getTime();
    }

    @Override
    public List<PphAtestadoVidaEntity> manter(ManterOp op) throws BCException {
        List<PphAtestadoVidaEntity> list = null;
        BeneficiarioMBean beneficiarioMBean = (BeneficiarioMBean) FacesUtil.getInSession("beneficiarioMBean");
        switch (op) {
            case INCLUIR:
                if(beneficiarioMBean!=null&&beneficiarioMBean.getItem().getDtObito()!=null){
                    getItem().getMsg().add("Beneficiário \""+beneficiarioMBean.getItem().getNmBeneficiario()+"\" com data de óbito.");
                }else{
                    atestadoVidaBC.incluir(getItem(), (UsuarioEntity) Funcoes.getSessionObject("usuarioLogado"));
                }
                break;
            case EXCLUIR:
                atestadoVidaBC.excluir(getItem(), (UsuarioEntity) Funcoes.getSessionObject("usuarioLogado"));
                break;
            case ATUALIZAR:
                atestadoVidaBC.atualizar(getItem(), (UsuarioEntity) Funcoes.getSessionObject("usuarioLogado"));
                break;
            case LISTAR:
                break;
        }
        if (beneficiarioMBean != null && getItem().getPphBeneficiario() != null && getItem().getPphBeneficiario().getId() != null) {
            List<PphAtestadoVidaEntity> lstAtestadoVida = atestadoVidaBC.pesquisaPorIdbeneficiario(getItem().getPphBeneficiario().getId());
            beneficiarioMBean.setLstAtestadoVida(lstAtestadoVida);
            if(getItem().getDtObito()!=null){
                beneficiarioMBean.getItem().setDtObito(getItem().getDtObito());
            }
        }
        return list;
    }

    public List<PphAtestadoVidaEntity> pesquisaAtestadoVidaPorNome(String nomeAtestadoVida) {
        try {
            Object session = FacesContext.getCurrentInstance().getExternalContext().getSession(false);
            LOGGER.debug("" + session);

            return atestadoVidaBC.pesquisaPorNome(nomeAtestadoVida);
        } catch (Exception ex) {
            MBeanExceptionUtil.log(LOGGER, ex);
        }
        return null;
    }

    public PphBeneficiarioEntity getBeneficiario() {
        return beneficiario;
    }

    public void setBeneficiario(PphBeneficiarioEntity beneficiario) {
        if (!Validador.isNull(beneficiario)) {
            getItem().setPphBeneficiario(beneficiario);
        }
        this.beneficiario = beneficiario;
    }

    public void beneficiario(PphBeneficiarioEntity beneficiario) {
        setBeneficiario(beneficiario);
    }

    public Date getDtMax() {
        return dtMax;
    }

    public Date getDtMin() {
        return dtMin;
    }

    public void preparaAtestado(PphBeneficiarioEntity beneficiario) {
        setBeneficiario(beneficiario);
    }

    @Override
    public String compoeFiltro() {
        return "";
    }

    @Override
    public String compoePeriodo() {
        return "";
    }

    @Override
    public void prepararTelaInclusao() {
    }

    public boolean isDetalhar() {
        return detalhar;
    }

    public void setDetalhar(boolean detalhar) {
        this.detalhar = detalhar;
    }

    public void preparaItem(PphAtestadoVidaEntity atestado, PphBeneficiarioEntity beneficiario, boolean detalhar) {
        if (!Validador.isNull(atestado)) {
            setItem(atestado);
        } else {
            setItem(new PphAtestadoVidaEntity());
        }
        if (!Validador.isNull(beneficiario)) {
            getItem().setPphBeneficiario(beneficiario);
            getItem().setDtObito(beneficiario.getDtObito());
        }
        setDetalhar(detalhar);
    }
    
    public void manterPorDialog() {
        if (Validador.isMaior(getItem().getId(), 0)) {
            atualizar();
        } else {
            persistir();
        }

        if (getItem().isOk()) {
            FacesUtil.exec("PF('atestado_incluir').hide();");
            FacesUtil.update("sanfona:panel_atestado");
        }
    }
}
