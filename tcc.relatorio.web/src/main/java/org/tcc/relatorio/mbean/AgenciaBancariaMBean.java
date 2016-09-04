package org.tcc.relatorio.mbean;

import java.util.List;
import javax.annotation.PostConstruct;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tcc.relatorio.cap.dominio.UsuarioEntity;
import org.tcc.relatorio.dominio.PphAgenciaBancariaEntity;
import org.tcc.relatorio.dominio.PphBancoEntity;
import org.tcc.relatorio.dominio.PphEstadoEntity;
import org.tcc.relatorio.dominio.PphMunicipioEntity;
import org.tcc.relatorio.mbean.exception.util.MBeanExceptionUtil;
import org.tcc.relatorio.negocio.AgenciaBancariaBC;
import org.tcc.relatorio.negocio.MunicipioBC;
import org.tcc.relatorio.negocio.validator.Validador;
import org.tcc.relatorio.util.FacesUtil;
import org.tcc.relatorio.hammer.persistencia.exception.BCException;

/**
 *
 * @author Jose Wdison de Souza
 */
@SessionScoped
@ManagedBean(name = "agenciaBancariaMBean")
public class AgenciaBancariaMBean extends BaseMBean<PphAgenciaBancariaEntity> {

    private static final long serialVersionUID = 118231614034054148L;

    private static final Logger LOGGER = LoggerFactory.getLogger(AgenciaBancariaMBean.class);

    @EJB(name = "AgenciaBancariaBC")
    private AgenciaBancariaBC agenciaBancariaBC;
    @EJB(name = "MunicipioBC")
    private MunicipioBC municipioBC;

    private List<PphBancoEntity> lstBanco;
    private List<PphEstadoEntity> lstEstado;
    private List<PphMunicipioEntity> lstMunicipio;

    private PphMunicipioEntity municipio;
    private PphEstadoEntity estado;
    private PphBancoEntity banco;

    public AgenciaBancariaMBean() {

        setParameterClass(PphAgenciaBancariaEntity.class);
        super.initialize("Agencia Bancária");
        LOGGER.debug(getDescricaoMBean());
    }

    @PostConstruct
    public void initialize() {

    }

    @Override
    public List<PphAgenciaBancariaEntity> manter(ManterOp op) throws BCException {
        List<PphAgenciaBancariaEntity> list = null;
        switch (op) {
            case INCLUIR:
                setAll();
                agenciaBancariaBC.incluir(getItem(), (UsuarioEntity) FacesUtil.getInSession("usuarioLogado"));
                if(getItem().isOk()){
                    PphAgenciaBancariaEntity item = getItem();
                    gravaMemoria();
                    setItem(item);
                }
                break;
            case ATUALIZAR:
                setAll();
                agenciaBancariaBC.atualizar(getItem(), (UsuarioEntity) FacesUtil.getInSession("usuarioLogado"));
                break;
            case LISTAR:
                break;
        }
        return list;
    }

    private void setAll() {
        setBanco(getBanco());
        setMunicipio(getMunicipio());
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

    public void preparaItem(PphAgenciaBancariaEntity entity, List<PphBancoEntity> lstBanco, List<PphEstadoEntity> lstEstado, PphBancoEntity banco) {
        if (!Validador.isNull(entity)) {
            setItem(entity);
        } else {
            setItem(new PphAgenciaBancariaEntity());
            setBanco(null);
            setEstado(null);
            setMunicipio(null);
        }
        if (Validador.isColecao(lstBanco)) {
            setLstBanco(lstBanco);
        }
        if (Validador.isColecao(lstEstado)) {
            setLstEstado(lstEstado);
        }
        if (!Validador.isNull(banco) && !Validador.isNull(banco.getId())) {
            setBanco(banco);
        }
    }

    public List<PphBancoEntity> getLstBanco() {
        return lstBanco;
    }

    public void setLstBanco(List<PphBancoEntity> lstBanco) {
        this.lstBanco = lstBanco;
    }

    public List<PphEstadoEntity> getLstEstado() {
        return lstEstado;
    }

    public void setLstEstado(List<PphEstadoEntity> lstEstado) {
        this.lstEstado = lstEstado;
    }

    public List<PphMunicipioEntity> getLstMunicipio() {
        return lstMunicipio;
    }

    public void setLstMunicipio(List<PphMunicipioEntity> lstMunicipio) {
        this.lstMunicipio = lstMunicipio;
    }

    public PphMunicipioEntity getMunicipio() {
        if (municipio == null) {
            municipio = new PphMunicipioEntity();
        }
        return municipio;
    }

    public void setMunicipio(PphMunicipioEntity municipio) {
        if (municipio != null && Validador.isMaior(municipio.getId(), 0)) {
            if (Validador.isColecao(lstMunicipio)) {
                for (PphMunicipioEntity munic : lstMunicipio) {
                    if (Validador.isEquals(munic.getId(), municipio.getId())) {
                        municipio = munic;
                        break;
                    }
                }
            }
            getItem().setPphMunicipio(municipio);
        }
        this.municipio = municipio;
    }

    public PphEstadoEntity getEstado() {
        if (estado == null) {
            estado = new PphEstadoEntity();
        }
        return estado;
    }

    public void setEstado(PphEstadoEntity estado) {
        this.estado = estado;
    }

    public PphBancoEntity getBanco() {
        if (banco == null) {
            banco = new PphBancoEntity();
        }
        return banco;
    }

    public void setBanco(PphBancoEntity banco) {
        if (banco != null && Validador.isMaior(banco.getId(), 0)) {
            if (Validador.isColecao(lstBanco)) {
                for (PphBancoEntity bancoTmp : lstBanco) {
                    if (Validador.isEquals(bancoTmp.getId(), banco.getId())) {
                        banco = bancoTmp;
                        break;
                    }
                }
            }
            getItem().setPphBanco(banco);
        }
        this.banco = banco;
    }

    public void onSelectEstadoChange(Long idEstado) {
        try {
            if (idEstado != null) {
                lstMunicipio = municipioBC.pesquisaPorIdEstado(idEstado);
            }
        } catch (Exception ex) {
            MBeanExceptionUtil.log(LOGGER, ex);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Lista de municipios não encontrado. IdEstado: " + idEstado));
        }
    }

    public void manterPorDialog() {
        if (Validador.isMaior(getItem().getId(), 0)) {
            atualizar();
        } else {
            persistir();
        }

        if (getItem().isOk()) {
            atualizaAgenciasBeneficiario();
            FacesUtil.exec("PF('agencia_incluir').hide();");
            FacesUtil.update("panel_agencia_incluir");
        }
    }

    private void atualizaAgenciasBeneficiario() {
        if (getItem().isOk()) {
            BeneficiarioMBean bean = (BeneficiarioMBean) FacesUtil.getInSession("beneficiarioMBean");
            if (bean != null) {
                if(bean.getBanco()!=null && Validador.isEquals(bean.getBanco().getId(), getBanco().getId())){
                    bean.getLstAgenciaBancaria().add(getItem());
//                    bean.setAgenciaBancaria(getItem());
                    FacesUtil.update("sanfona:beneficiarioBancoAgencia");
//                    FacesUtil.update("sanfona:beneficiarioEstadoAgencia");
//                    FacesUtil.update("sanfona:beneficiarioMunicipioAgencia");
                }
                if(bean.getBancoProcurador()!=null && Validador.isEquals(bean.getBancoProcurador().getId(), getBanco().getId())){
                    bean.getLstAgenciaBancariaProcurador().add(getItem());
//                    bean.setAgenciaBancariaProcurador(getItem());
                    FacesUtil.update("sanfona:procuradorBancoAgencia");
//                    FacesUtil.update("sanfona:procuradorEstadoAgencia");
//                    FacesUtil.update("sanfona:procuradorMunicipioAgencia");
                }
            }
        }
    }
}
