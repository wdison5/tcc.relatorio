package org.tcc.relatorio.mbean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.PostConstruct;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.management.MBeanException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tcc.relatorio.cap.dominio.UsuarioEntity;
import org.tcc.relatorio.dominio.PphUnidSaudeEntity;
import org.tcc.relatorio.dominio.PphAgenciaBancariaEntity;
import org.tcc.relatorio.dominio.PphAtestadoVidaEntity;
import org.tcc.relatorio.dominio.PphBancoEntity;
import org.tcc.relatorio.dominio.PphBeneficiarioEntity;
import org.tcc.relatorio.dominio.PphComunicacaoEntity;
import org.tcc.relatorio.dominio.PphContatoEntity;
import org.tcc.relatorio.dominio.PphEnderecoEntity;
import org.tcc.relatorio.dominio.PphEstadoEntity;
import org.tcc.relatorio.dominio.PphMunicipioEntity;
import org.tcc.relatorio.dominio.PphProcuradorEntity;
import org.tcc.relatorio.dominio.PphUnidadePagadoraEntity;
import org.tcc.relatorio.enumeracao.Confirmacao;
import org.tcc.relatorio.mbean.exception.util.MBeanExceptionUtil;
import org.tcc.relatorio.negocio.AgenciaBancariaBC;
import org.tcc.relatorio.negocio.BancoBC;
import org.tcc.relatorio.negocio.BeneficiarioBC;
import org.tcc.relatorio.negocio.ContatoBC;
import org.tcc.relatorio.negocio.EmpenhoBC;
import org.tcc.relatorio.negocio.EnderecoBC;
import org.tcc.relatorio.negocio.EstadoBC;
import org.tcc.relatorio.negocio.UnidSaudeBC;
import org.tcc.relatorio.negocio.MunicipioBC;
import org.tcc.relatorio.negocio.ProcuradorBC;
import org.tcc.relatorio.negocio.UnidadePagadoraBC;
import org.tcc.relatorio.enumeracao.TpComunicacao;
import org.tcc.relatorio.enumeracao.TpLogradouro;
import org.tcc.relatorio.enumeracao.TpParentesco;
import org.tcc.relatorio.negocio.ComunicacaoBC;
import org.tcc.relatorio.negocio.formatador.Maiusculas;
import org.tcc.relatorio.negocio.validator.Validador;
import org.tcc.relatorio.util.BuscaCep;
import org.tcc.relatorio.util.Cep;
import org.tcc.relatorio.util.ExportarArquivo;
import org.tcc.relatorio.util.FacesUtil;
import org.tcc.relatorio.util.LoggerUtil;
import org.tcc.relatorio.util.jasper.Reports;
import org.tcc.relatorio.hammer.persistencia.exception.BCException;

@SessionScoped
@ManagedBean(name = "beneficiarioMBean")
public class BeneficiarioMBean extends BaseMBean<PphBeneficiarioEntity> {

    private static final long serialVersionUID = 118231614034054148L;

    private static final Logger LOGGER = LoggerFactory.getLogger(BeneficiarioMBean.class);

    @EJB(name = "BeneficiarioBC")
    private BeneficiarioBC beneficiarioBC;

    @EJB(name = "EnderecoBC")
    private EnderecoBC enderecoBC;

    @EJB(name = "ContatoBC")
    private ContatoBC contatoBC;

    @EJB(name = "ComunicacaoBC")
    private ComunicacaoBC comunicacaoBC;

    @EJB(name = "ProcuradorBC")
    private ProcuradorBC procuradorBC;

    @EJB(name = "UnidSaudeBC")
    private UnidSaudeBC unidSaudeBC;

    @EJB(name = "UnidadePagadoraBC")
    private UnidadePagadoraBC unidadePagadoraBC;
    @EJB(name = "EstadoBC")
    private EstadoBC estadoBC;
    @EJB(name = "MunicipioBC")
    private MunicipioBC municipioBC;
    @EJB(name = "BancoBC")
    private BancoBC bancoBC;
    @EJB(name = "AgenciaBancariaBC")
    private AgenciaBancariaBC agenciaBancariaBC;

    @EJB(name = "EmpenhoBC")
    private EmpenhoBC empenhoBC;

    private UsuarioEntity usuarioLogado;
    
    private List<Long> idsUnidSaude;
    private List<Long> idsUnidPagadora;

    private List<PphUnidadePagadoraEntity> lstUnidadePagadora;
    private List<PphUnidSaudeEntity> lstUnidSaude;
    private TpLogradouro[] lstTpLogradouro;
    private TpParentesco[] lstTpParentesco;
    private List<PphEstadoEntity> lstEstado;
    private List<PphMunicipioEntity> lstMunicipio;
    private List<PphMunicipioEntity> lstMunicipioProcurador;
    private List<PphBancoEntity> lstBanco;
    private List<PphAgenciaBancariaEntity> lstAgenciaBancaria;
    private List<PphAgenciaBancariaEntity> lstAgenciaBancariaProcurador;
    private List<PphAtestadoVidaEntity> lstAtestadoVida;

    private PphUnidSaudeEntity unidSaude;
    private PphUnidadePagadoraEntity unidadePagadora;
    private PphMunicipioEntity municipioBeneficiario;
    private PphMunicipioEntity municipioAgencia;
    private PphEstadoEntity estadoBeneficiario;
    private PphEstadoEntity estadoAgencia;
    private PphEnderecoEntity endereco;
    private PphBeneficiarioEntity beneficiarioInicial;
    private PphAgenciaBancariaEntity agenciaBancaria;
    private PphBancoEntity banco;
    private PphComunicacaoEntity[] comuniBenArray;
    private PphComunicacaoEntity[] comuniContArray;
    private PphContatoEntity contato;
    private PphProcuradorEntity procurador;
    private PphEstadoEntity estadoProcurador;
    private PphMunicipioEntity municipioProcurador;
    private PphEnderecoEntity enderecoProcurador;
    private PphComunicacaoEntity[] comuniProcArray;
    private PphBancoEntity bancoProcurador;
    private PphAgenciaBancariaEntity agenciaBancariaProcurador;

    private PphUnidSaudeEntity[] unidadeSaudeArray;
    private Long unidadeSaudeSelecionada;

    private PphUnidadePagadoraEntity[] unidadePagadoraArray;
    private Long unidadePagadoraSelecionada;

    private Date dtMax;
    private Date dtMin;
    private boolean mostraBotoesEditarExcluir;

    public BeneficiarioMBean() {
        setParameterClass(PphBeneficiarioEntity.class);
        super.initialize("BeneficiÃ¡rio");

        LOGGER.debug(getDescricaoMBean());
    }

    public void inicializaRelatorio(String complemento) {
        this.setEstadoDefault();
        super.setComplementoRelatorio(complemento);
        setMostraBotoesEditarExcluir(true);
    }

    public void inicializaRelatorio(String complemento, boolean mostraBotoesEditarExcluir) {
        inicializaRelatorio(complemento);
        setMostraBotoesEditarExcluir(mostraBotoesEditarExcluir);
    }

    public void setMostraBotoesEditarExcluir(boolean mostraBotoesEditarExcluir) {
        this.mostraBotoesEditarExcluir = mostraBotoesEditarExcluir;
    }
    
    public boolean isMostraBotoesEditarExcluir() {
        return mostraBotoesEditarExcluir;
    }

    public void setEstadoDefault() {
        super.initialize("Beneficiario");
        super.setComplementoRelatorio("");
        this.getItem().setTpBeneficiario("3");
        getItem().setId(null);
    }

    public String tituloTela() {
        String titulo = "Cadastro de BeneficiÃ¡rios";
        if (this.getComplementoRelatorio().equals("Listagem")){ 
            titulo = "Listagem de BeneficiÃ¡rios";
        } else if (this.getComplementoRelatorio().equals("SQLLog")){
            titulo = "LOG - BeneficiÃ¡rios";
        }
        return titulo;
    }

    @PostConstruct
    public void initialize() {
        this.usuarioLogado = (UsuarioEntity) FacesUtil.getInSession("usuarioLogado");
        
        try {
            idsUnidSaude    = (List<Long>) FacesUtil.getInSession("lstUnidSaudeId");
            idsUnidPagadora = (List<Long>) FacesUtil.getInSession("lstUnidPagadoraId");
            boolean islstIdsUnidPagadora = Validador.isColecao(idsUnidPagadora);
            boolean islstIdsUnidSaude = Validador.isColecao(idsUnidSaude);
            
            if(islstIdsUnidPagadora || islstIdsUnidSaude){
                if(islstIdsUnidPagadora){
                    lstUnidadePagadora = unidadePagadoraBC.list(this.usuarioLogado.isAcessoGeral(), idsUnidPagadora);
                }else{
                    lstUnidadePagadora = unidadePagadoraBC.list();
                }
                if(islstIdsUnidSaude){
                    lstUnidSaude       = unidSaudeBC      .list(this.usuarioLogado.isAcessoGeral(), idsUnidSaude);
                }else{
                    lstUnidSaude       = unidSaudeBC      .list();
                }
            }else{
                lstUnidadePagadora = new ArrayList<PphUnidadePagadoraEntity>();
                lstUnidSaude       = new ArrayList<PphUnidSaudeEntity>();
            }
            
            Collections.sort(lstUnidSaude);
            Collections.sort(lstUnidadePagadora);
            
        } catch (Exception e) {
            MBeanExceptionUtil.log(LOGGER, e, "Lista de Unidades Pagadora nÃ£o encontrado: " + e.getMessage());
            FacesUtil.addMsg("Lista de Unidades Pagadora nÃ£o encontrado.");
            FacesUtil.put(e);
        }
        try {
            lstEstado = estadoBC.list();
        } catch (Exception e) {
            MBeanExceptionUtil.log(LOGGER, e, "Lista de bancos nÃ£o encontrado: " + e.getMessage());
            FacesUtil.addMsg("Lista de instituiÃ§Ã£o bancÃ¡ria nÃ£o encontrado.");
            FacesUtil.put(e);
        }
        try {
            lstBanco = bancoBC.list();
        } catch (Exception e) {
            MBeanExceptionUtil.log(LOGGER, e, "Lista de estados nÃ£o encontrado: " + e.getMessage());
            FacesUtil.addMsg("Lista de estados nÃ£o encontrado.");
            FacesUtil.put(e);
        }

        dtMax = Calendar.getInstance().getTime();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, -130);
        dtMin = calendar.getTime();
    }

    @Override
    public List<PphBeneficiarioEntity> manter(ManterOp op) throws BCException {
        switch (op) {
            case INCLUIR:
                setTodosParametros();
                beneficiarioBC.incluir(getItem(), usuarioLogado);
                if (getItem().isOk()) {
                    PphBeneficiarioEntity novo = new PphBeneficiarioEntity();
                    super.clonar(novo, getItem());
                    super.gravaMemoria();
                    super.setItem(novo);
                }
                break;
            case EXCLUIR:
                beneficiarioBC.excluir(getItem(), usuarioLogado);
                break;
            case ATUALIZAR:
                setTodosParametros();
                beneficiarioBC.atualizar(getItem(), usuarioLogado);
                if (getItem().isOk()) {
                    setId(getItem().getId());
                }
                break;
            case LISTAR:
                PphBeneficiarioEntity beneficiario = new PphBeneficiarioEntity();
                beneficiario.setNmBeneficiario("%" + getItem().getNmBeneficiario() + "%");
                beneficiario.setPphUnidadePagadora(getItem().getPphUnidadePagadora());
                beneficiario.setPphUnidSaude(getItem().getPphUnidSaude());
                beneficiario.setFlExclusao(Confirmacao.NAO.getId());
                if (!Validador.isBlank(getItem().getTpBeneficiario()) && "1-2".contains(getItem().getTpBeneficiario())) {
                    beneficiario.setTpBeneficiario(getItem().getTpBeneficiario());
                }
                
                List<PphBeneficiarioEntity> set = beneficiarioBC.pesquisar(beneficiario, idsUnidSaude, idsUnidPagadora, usuarioLogado);
                if (set != null) {
                    return new ArrayList<PphBeneficiarioEntity>(set);
                }
            break;
        }
        return null;
    }

    private void setTodosParametros() {
        setUnidSaude(getUnidSaude());
        setUnidadePagadora(getUnidadePagadora());
        setEndereco(getEndereco());
        setBeneficiarioInicial(getBeneficiarioInicial());
        setContato(getContato());
        setMunicipioBeneficiario(getMunicipioBeneficiario());
        setEstadoBeneficiario(getEstadoBeneficiario());
        setAgenciaBancaria(getAgenciaBancaria());
        setComuniBenArray(getComuniBenArray());
        setComuniContArray(getComuniContArray());
        setProcurador(getProcurador());
        setEnderecoProcurador(getEnderecoProcurador());
        setMunicipioProcurador(getMunicipioProcurador());
        setComuniProcArray(getComuniProcArray());
        setAgenciaBancariaProcurador(getAgenciaBancariaProcurador());
    }

    @Override
    public void persistir() {
        super.persistir();
    }

    @Override
    public void reset() {
        super.reset();
        limparCamposExtras();
    }

    public void onSelectUnidadePagadoraChange() {
        try {
            if(beneficiarioInicial!=null){
                beneficiarioInicial = null;
                getItem().setPphBeneficiario(null);
            }
        } catch (Exception ex) {
            MBeanExceptionUtil.log(LOGGER, ex);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Unidade Pagadora nÃ£o encontrado. Id: " + getItem().getPphUnidadePagadora().getId()));
        }
    }

    public void onSelectEstadoChange(Long idEstado, String to) {
        try {
            if (idEstado != null) {
                if ("beneficiario".equals(to)) {
                    lstMunicipio = municipioBC.pesquisaPorIdEstado(idEstado);
                } else if ("procurador".equals(to)) {
                    lstMunicipioProcurador = municipioBC.pesquisaPorIdEstado(idEstado);
                }
            }
        } catch (Exception ex) {
            MBeanExceptionUtil.log(LOGGER, ex);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Lista de municipios nÃ£o encontrado. Id: " + getItem().getPphUnidadePagadora().getId()));
        }
    }

    public void onSelectUnidSaudeChange() {
        try {
            unidSaude = unidSaudeBC.getById(unidSaude.getId());
            if (unidSaude != null) {
                getItem().setPphUnidSaude(unidSaude);
            }
        } catch (Exception ex) {
            MBeanExceptionUtil.log(LOGGER, ex);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Unidade SaÃºde nÃ£o encontrado."));
        }
    }

    public void onSelectBancoChange(Long idBanco, String to) {
        try {
            if ("beneficiario".equals(to)) {
                lstAgenciaBancaria = agenciaBancariaBC.pesquisaPorIdBanco(idBanco);
            } else if ("procurador".equals(to)) {
                lstAgenciaBancariaProcurador = agenciaBancariaBC.pesquisaPorIdBanco(idBanco);
            }
        } catch (Exception ex) {
            MBeanExceptionUtil.log(LOGGER, ex);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Lista de agencias bancarias nÃ£o encontrada."));
        }
    }

    public void onSelectAgenciaChange(Long idAgencia, String to) {
        try {
            if ("beneficiario".equals(to)) {
                agenciaBancaria = agenciaBancariaBC.getByIdComMunicipio(idAgencia);
            } else if ("procurador".equals(to)) {
                agenciaBancariaProcurador = agenciaBancariaBC.getByIdComMunicipio(idAgencia);
            }

        } catch (Exception ex) {
            MBeanExceptionUtil.log(LOGGER, ex);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Agencia agencias bancarias nÃ£o encontrada."));
        }
    }

    public void onRadioTpBeneficiarioChange() {
        try {
            String tpBeneficiario = getItem().getTpBeneficiario();
            if ("1".equals(tpBeneficiario)) {
                beneficiarioInicial = null;
            }
        } catch (Exception ex) {
            MBeanExceptionUtil.log(LOGGER, ex);
        }
    }

    public List<PphBeneficiarioEntity> pesquisaBeneficiarioPorNome(String nomeBeneficiario) {
        List<PphBeneficiarioEntity> lstBeneficiario = null;
        try {
            String tpBeneficiario = getItem().getTpBeneficiario();
//            if (unidSaude == null || unidSaude.getId() == null || unidSaude.getId() == 0) {
            if (unidadePagadora == null || unidadePagadora.getId() == null || unidadePagadora.getId() == 0) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Selecione uma unidade pagadora."));
            } else {
                if ("2".equals(tpBeneficiario)) {
                    lstBeneficiario = beneficiarioBC.pesquisaPorNome(nomeBeneficiario, unidadePagadora);
                } else {
                    FacesUtil.addError("Click em dependente para realizar essa pesquisa.");
                }

            }

        } catch (Exception ex) {
            LoggerUtil.error("Erro na pesquisa de beneficiÃ¡rio. nome: " + nomeBeneficiario, ex, LOGGER, true);
        }
        return lstBeneficiario;
    }

    public void buscaCep(String cep, String to) {
        if (cep != null) {
            String buscaCep = cep.replaceAll("-", "");
            Maiusculas upper = new Maiusculas();

            BuscaCep chamaBuscaCep = new BuscaCep();
            Cep cepRetorno = null;
            try {
                cepRetorno = chamaBuscaCep.cep(buscaCep);

                if (cepRetorno == null || cepRetorno.getEndereco() == null || cepRetorno.getEndereco().isEmpty()) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Cep nÃ£o localizado", ""));
                } else {

                    LOGGER.debug("endereco " + upper.formata(cepRetorno.getEndereco()));
                    if ("beneficiario".equals(to)) {
                        endereco.setDsEndereco(upper.formata(cepRetorno.getEndereco()));
                        endereco.setNmBairro(upper.formata(cepRetorno.getBairro()));
                        endereco.setDsComplEndereco(upper.formata(cepRetorno.getComplemento()));
                        String tipoLogradouro = cepRetorno.getTipoLogradouro();
                        endereco.setTpLogradouro(TpLogradouro.getByDescricao(tipoLogradouro).getDescricao());

                        if (estadoBeneficiario == null || !cepRetorno.getUf().equals(estadoBeneficiario.getCdUf())) {
                            for (PphEstadoEntity estado : lstEstado) {
                                if (estado.getCdUf().equals(cepRetorno.getUf())) {
                                    estadoBeneficiario = estado;
                                    break;
                                }
                            }
                        }
                        if (lstMunicipio == null || lstMunicipio.size() < 1 || lstMunicipio.get(0).getPphEstado().getId().longValue() != estadoBeneficiario.getId()) {
                            onSelectEstadoChange(estadoBeneficiario.getId(), to);
                        }
                        String numeroIBGE = cepRetorno.getNumeroIBGE().substring(0, 6);

                        if (municipioBeneficiario == null || !numeroIBGE.equals("" + municipioBeneficiario.getCdIbge())) {
                            for (PphMunicipioEntity municipio : lstMunicipio) {
                                if (numeroIBGE.equals(municipio.getCdIbge().toString())) {
                                    municipioBeneficiario = municipio;
                                    break;
                                }
                            }
                        }
                    } else if ("procurador".equals(to)) {
                        enderecoProcurador.setDsEndereco(upper.formata(cepRetorno.getEndereco()));
                        enderecoProcurador.setNmBairro(upper.formata(cepRetorno.getBairro()));
                        enderecoProcurador.setDsComplEndereco(upper.formata(cepRetorno.getComplemento()));
                        String tipoLogradouro = cepRetorno.getTipoLogradouro();
                        enderecoProcurador.setTpLogradouro(TpLogradouro.getByDescricao(tipoLogradouro).getDescricao());

                        if (estadoProcurador == null || !cepRetorno.getUf().equals(estadoProcurador.getCdUf())) {
                            for (PphEstadoEntity estado : lstEstado) {
                                if (estado.getCdUf().equals(cepRetorno.getUf())) {
                                    estadoProcurador = estado;
                                    break;
                                }
                            }
                        }
                        if (lstMunicipioProcurador == null || lstMunicipioProcurador.size() < 1 || lstMunicipioProcurador.get(0).getPphEstado().getId().longValue() != estadoProcurador.getId()) {
                            onSelectEstadoChange(estadoProcurador.getId(), to);
                        }
                        String numeroIBGE = cepRetorno.getNumeroIBGE().substring(0, 6);

                        if (municipioProcurador == null || !numeroIBGE.equals("" + municipioProcurador.getCdIbge())) {
                            for (PphMunicipioEntity municipio : lstMunicipioProcurador) {
                                if (numeroIBGE.equals(municipio.getCdIbge().toString())) {
                                    municipioProcurador = municipio;
                                    break;
                                }
                            }
                        }
                    }
                    LOGGER.debug("Encontrou CEP");
                }
            } catch (MBeanException ex) {
                MBeanExceptionUtil.log(LOGGER, ex);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("EndereÃ§o do cep nÃ£o encontrado. cep: " + endereco.getCdCep()));
            }
        }

    }

    public List<PphUnidadePagadoraEntity> getLstUnidadePagadora() {
        return lstUnidadePagadora;
    }

    public void setLstUnidadePagadora(List<PphUnidadePagadoraEntity> lstUnidadePagadora) {
        this.lstUnidadePagadora = lstUnidadePagadora;
    }

    public List<PphUnidSaudeEntity> getLstUnidSaude() {
        return lstUnidSaude;
    }

    public void setLstUnidSaude(List<PphUnidSaudeEntity> lstUnidSaude) {
        this.lstUnidSaude = lstUnidSaude;
    }

    public TpLogradouro[] getLstTpLogradouro() {
        if (lstTpLogradouro == null || lstTpLogradouro.length < 1) {
            lstTpLogradouro = TpLogradouro.values();
        }
        return lstTpLogradouro;
    }

    public TpParentesco[] getLstTpParentesco() {
        if (!Validador.isArray(lstTpParentesco)) {
            lstTpParentesco = TpParentesco.values();
        }
        return lstTpParentesco;
    }

    public List<PphEstadoEntity> getLstEstado() {
        return lstEstado;
    }

    public List<PphMunicipioEntity> getLstMunicipio() {
        return lstMunicipio;
    }

    public List<PphMunicipioEntity> getLstMunicipioProcurador() {
        return lstMunicipioProcurador;
    }

    public List<PphBancoEntity> getLstBanco() {
        return lstBanco;
    }

    public List<PphAgenciaBancariaEntity> getLstAgenciaBancaria() {
        if (!Validador.isColecao(lstAgenciaBancaria)) {
            lstAgenciaBancaria = new ArrayList<PphAgenciaBancariaEntity>();
        }
        return lstAgenciaBancaria;
    }

    public List<PphAgenciaBancariaEntity> getLstAgenciaBancariaProcurador() {
        if (!Validador.isColecao(lstAgenciaBancariaProcurador)) {
            lstAgenciaBancariaProcurador = new ArrayList<PphAgenciaBancariaEntity>();
        }
        return lstAgenciaBancariaProcurador;
    }

    public PphUnidSaudeEntity getUnidSaude() {
        if (unidSaude == null) {
            unidSaude = new PphUnidSaudeEntity();
        }
        return unidSaude;
    }

    public void setUnidSaude(PphUnidSaudeEntity unidSaude) {
        if (unidSaude != null) {
            getItem().setPphUnidSaude(unidSaude);
        }
        this.unidSaude = unidSaude;
    }

    public PphUnidadePagadoraEntity getUnidadePagadora() {
        if (unidadePagadora == null) {
            unidadePagadora = new PphUnidadePagadoraEntity();
        }
        return unidadePagadora;
    }

    public void setUnidadePagadora(PphUnidadePagadoraEntity unidadePagadora) {
        if (unidadePagadora != null) {
            getItem().setPphUnidadePagadora(unidadePagadora);
        }
        this.unidadePagadora = unidadePagadora;
    }

    public PphMunicipioEntity getMunicipioBeneficiario() {
        if (municipioBeneficiario == null) {
            municipioBeneficiario = new PphMunicipioEntity();
        }
        return municipioBeneficiario;
    }

    public void setMunicipioBeneficiario(PphMunicipioEntity municipioBeneficiario) {
        if (municipioBeneficiario != null) {
            Set<PphEnderecoEntity> enderecoSet = getItem().getPphEnderecoSet();
            if (Validador.isColecao(enderecoSet)) {
                enderecoSet.iterator().next().setPphMunicipio(municipioBeneficiario);
            }
        }
        this.municipioBeneficiario = municipioBeneficiario;
    }

    public PphMunicipioEntity getMunicipioAgencia() {
        if (municipioAgencia == null) {
            municipioAgencia = new PphMunicipioEntity();
        }
        return municipioAgencia;
    }

    public void setMunicipioAgencia(PphMunicipioEntity municipioAgencia) {
        if (municipioAgencia != null) {
            PphAgenciaBancariaEntity agenciaBancaria = getItem().getPphAgenciaBancaria();
            if (agenciaBancaria != null) {
                agenciaBancaria.setPphMunicipio(municipioAgencia);
            }
        }
        this.municipioAgencia = municipioAgencia;
    }

    public PphEstadoEntity getEstadoBeneficiario() {
        if (estadoBeneficiario == null) {
            estadoBeneficiario = new PphEstadoEntity();
        }
        return estadoBeneficiario;
    }

    public void setEstadoBeneficiario(PphEstadoEntity estadoBeneficiario) {
        this.estadoBeneficiario = estadoBeneficiario;
    }

    public PphEstadoEntity getEstadoAgencia() {
        if (estadoAgencia == null) {
            estadoAgencia = new PphEstadoEntity();
        }
        return estadoAgencia;
    }

    public void setEstadoAgencia(PphEstadoEntity estadoAgencia) {
        this.estadoAgencia = estadoAgencia;
    }

    public PphEnderecoEntity getEndereco() {
        if (endereco == null) {
            endereco = new PphEnderecoEntity();
        }
        return endereco;
    }

    public void setEndereco(final PphEnderecoEntity endereco) {
        if (endereco != null) {
            HashSet<PphEnderecoEntity> enderecoSet = new HashSet<PphEnderecoEntity>();
            enderecoSet.add(endereco);
            getItem().setPphEnderecoSet(enderecoSet);
        }
        this.endereco = endereco;
    }

    public PphBeneficiarioEntity getBeneficiarioInicial() {
        if (beneficiarioInicial == null) {
            beneficiarioInicial = new PphBeneficiarioEntity();
        }
        return beneficiarioInicial;
    }

    public void setBeneficiarioInicial(PphBeneficiarioEntity beneficiarioInicial) {
        if (beneficiarioInicial != null) {
            getItem().setPphBeneficiario(beneficiarioInicial);
        }
        this.beneficiarioInicial = beneficiarioInicial;
    }

    public PphAgenciaBancariaEntity getAgenciaBancaria() {
        if (agenciaBancaria == null) {
            agenciaBancaria = new PphAgenciaBancariaEntity();
        }
        return agenciaBancaria;
    }

    public void setAgenciaBancaria(PphAgenciaBancariaEntity agenciaBancaria) {
        if (agenciaBancaria != null) {
            getItem().setPphAgenciaBancaria(agenciaBancaria);
        }
        this.agenciaBancaria = agenciaBancaria;
    }

    public PphBancoEntity getBanco() {
        if (banco == null) {
            banco = new PphBancoEntity();
        }
        return banco;
    }

    public void setBanco(PphBancoEntity banco) {
        if (banco != null) {
            PphAgenciaBancariaEntity agenciaBancaria = getItem().getPphAgenciaBancaria();
            if (agenciaBancaria != null) {
                agenciaBancaria.setPphBanco(banco);
            }
        }
        this.banco = banco;
    }

    public PphComunicacaoEntity[] getComuniBenArray() {
        if (comuniBenArray == null) {
            comuniBenArray = new PphComunicacaoEntity[3];
            final PphComunicacaoEntity telefonePrin = new PphComunicacaoEntity();
            telefonePrin.setTpComunic(TpComunicacao.FONE_PRIN.getCd());
            telefonePrin.setFlAtivo((short) 1);
            comuniBenArray[0] = telefonePrin;
            final PphComunicacaoEntity telefoneSec = new PphComunicacaoEntity();
            telefoneSec.setTpComunic(TpComunicacao.FONE_SEC.getCd());
            telefoneSec.setFlAtivo((short) 1);
            comuniBenArray[1] = telefoneSec;
            final PphComunicacaoEntity celular = new PphComunicacaoEntity();
            celular.setTpComunic(TpComunicacao.CELULAR.getCd());
            celular.setFlAtivo((short) 1);
            comuniBenArray[2] = celular;
        }
        return comuniBenArray;
    }

    public void setComuniBenArray(PphComunicacaoEntity[] comuniBenArray) {
        if (comuniBenArray != null) {
            getItem().setPphComunicacaoSet(new HashSet(Arrays.asList(comuniBenArray)));
        }
        this.comuniBenArray = comuniBenArray;
    }

    public PphComunicacaoEntity[] getComuniContArray() {
        if (comuniContArray == null) {
            comuniContArray = new PphComunicacaoEntity[3];
            PphComunicacaoEntity telefonePrin = new PphComunicacaoEntity();
            telefonePrin.setTpComunic(TpComunicacao.FONE_PRIN.getCd());
            telefonePrin.setFlAtivo((short) 1);
            comuniContArray[0] = telefonePrin;
            PphComunicacaoEntity telefoneSec = new PphComunicacaoEntity();
            telefoneSec.setTpComunic(TpComunicacao.FONE_SEC.getCd());
            telefoneSec.setFlAtivo((short) 1);
            comuniContArray[1] = telefoneSec;
            PphComunicacaoEntity celular = new PphComunicacaoEntity();
            celular.setTpComunic(TpComunicacao.CELULAR.getCd());
            celular.setFlAtivo((short) 1);
            comuniContArray[2] = celular;
        }
        return comuniContArray;
    }

    public void setComuniContArray(PphComunicacaoEntity[] comuniContArray) {
        if (comuniContArray != null) {
            Set<PphContatoEntity> contatoSet = getItem().getPphContatoSet();
            if (Validador.isColecao(contatoSet)) {
                PphContatoEntity contato = contatoSet.iterator().next();
                contato.setPphComunicacaoSet(new HashSet(Arrays.asList(comuniContArray)));
            }
        }
        this.comuniContArray = comuniContArray;
    }

    public PphContatoEntity getContato() {
        if (contato == null) {
            contato = new PphContatoEntity();
        }
        return contato;
    }

    public void setContato(PphContatoEntity contato) {
        HashSet contatoHash = new HashSet<PphContatoEntity>();
        contatoHash.add(contato);
        getItem().setPphContatoSet(contatoHash);
        this.contato = contato;
    }

    public PphProcuradorEntity getProcurador() {
        if (procurador == null) {
            procurador = new PphProcuradorEntity();
        }
        return procurador;
    }

    public PphEnderecoEntity getEnderecoProcurador() {
        if (enderecoProcurador == null) {
            enderecoProcurador = new PphEnderecoEntity();
        }
        return enderecoProcurador;
    }

    public void setEnderecoProcurador(PphEnderecoEntity enderecoProcurador) {
        if (enderecoProcurador != null) {
            Set<PphProcuradorEntity> pphProcuradorSet = getItem().getPphProcuradorSet();
            if (pphProcuradorSet != null && pphProcuradorSet.size() > 0) {
                Set<PphEnderecoEntity> enderecoSet = new HashSet<PphEnderecoEntity>();
                enderecoSet.add(enderecoProcurador);
                pphProcuradorSet.iterator().next().setPphEnderecoSet(enderecoSet);
            }
        }
        this.enderecoProcurador = enderecoProcurador;
    }

    public PphComunicacaoEntity[] getComuniProcArray() {
        if (comuniProcArray == null) {
            comuniProcArray = new PphComunicacaoEntity[3];
            PphComunicacaoEntity telefonePrin = new PphComunicacaoEntity();
            telefonePrin.setTpComunic(TpComunicacao.FONE_PRIN.getCd());
            telefonePrin.setFlAtivo((short) 1);
            comuniProcArray[0] = telefonePrin;
            PphComunicacaoEntity telefoneSec = new PphComunicacaoEntity();
            telefoneSec.setTpComunic(TpComunicacao.FONE_SEC.getCd());
            telefoneSec.setFlAtivo((short) 1);
            comuniProcArray[1] = telefoneSec;
            PphComunicacaoEntity celular = new PphComunicacaoEntity();
            celular.setTpComunic(TpComunicacao.CELULAR.getCd());
            celular.setFlAtivo((short) 1);
            comuniProcArray[2] = celular;
        }
        return comuniProcArray;
    }

    public void setComuniProcArray(PphComunicacaoEntity[] comuniProcArray) {
        if (comuniProcArray != null) {
            Set<PphProcuradorEntity> procuradorSet = getItem().getPphProcuradorSet();
            if (procuradorSet != null && procuradorSet.size() > 0) {
                Set<PphComunicacaoEntity> comunicacaoSet = new HashSet<PphComunicacaoEntity>();
                comunicacaoSet.add(comuniProcArray[0]);
                comunicacaoSet.add(comuniProcArray[1]);
                comunicacaoSet.add(comuniProcArray[2]);
                procuradorSet.iterator().next().setPphComunicacaoSet(comunicacaoSet);
            }
        }

        this.comuniProcArray = comuniProcArray;
    }

    public PphEstadoEntity getEstadoProcurador() {
        if (estadoProcurador == null) {
            estadoProcurador = new PphEstadoEntity();
        }
        return estadoProcurador;
    }

    public void setEstadoProcurador(PphEstadoEntity estadoProcurador) {
        this.estadoProcurador = estadoProcurador;
    }

    public PphMunicipioEntity getMunicipioProcurador() {
        if (municipioProcurador == null) {
            municipioProcurador = new PphMunicipioEntity();
        }
        return municipioProcurador;
    }

    public void setMunicipioProcurador(PphMunicipioEntity municipioProcurador) {
        if (municipioProcurador != null) {
            getProcurador().getPphEnderecoSet().iterator().next().setPphMunicipio(municipioProcurador);
        }
        this.municipioProcurador = municipioProcurador;
    }

    public void setProcurador(PphProcuradorEntity procurador) {
        if (procurador != null) {
            HashSet<PphProcuradorEntity> procuradorSet = new HashSet<PphProcuradorEntity>();
            procuradorSet.add(procurador);
            getItem().setPphProcuradorSet(procuradorSet);
        }
        this.procurador = procurador;
    }

    public PphBancoEntity getBancoProcurador() {
        if (bancoProcurador == null) {
            bancoProcurador = new PphBancoEntity();
        }
        return bancoProcurador;
    }

    public void setBancoProcurador(PphBancoEntity bancoProcurador) {
        this.bancoProcurador = bancoProcurador;
    }

    public PphAgenciaBancariaEntity getAgenciaBancariaProcurador() {
        if (agenciaBancariaProcurador == null) {
            agenciaBancariaProcurador = new PphAgenciaBancariaEntity();
        }
        return agenciaBancariaProcurador;
    }

    public void setAgenciaBancariaProcurador(PphAgenciaBancariaEntity agenciaBancariaProcurador) {
        if (agenciaBancariaProcurador != null) {
            Set<PphProcuradorEntity> procuradorSet = getItem().getPphProcuradorSet();
            if (procuradorSet != null && procuradorSet.size() > 0) {
                procuradorSet.iterator().next().setPphAgenciaBancaria(agenciaBancariaProcurador);
            }
        }
        this.agenciaBancariaProcurador = agenciaBancariaProcurador;
    }

    public Date getDtMin() {
        return dtMin;
    }

    public Date getDtMax() {
        return dtMax;
    }

    public PphUnidSaudeEntity[] getUnidadeSaudeArray() {
        return unidadeSaudeArray;
    }

    public void setUnidadeSaudeArray(PphUnidSaudeEntity[] unidadeSaudeArray) {
        this.unidadeSaudeArray = unidadeSaudeArray;
    }

    public Long getUnidadeSaudeSelecionada() {
        if (getItem().getPphUnidSaude() != null) {
            this.unidadeSaudeSelecionada = getItem().getPphUnidSaude().getId();
        } else {
            this.unidadeSaudeSelecionada = Long.valueOf(0);
        }
        return unidadeSaudeSelecionada;
    }

    public void setUnidadeSaudeSelecionada(Long unidadeSaudeSelecionada) {
        try {
            if (unidadeSaudeSelecionada > 0) {
                getItem().setPphUnidSaude(unidSaudeBC.getById(unidadeSaudeSelecionada));
            } else if (unidadeSaudeSelecionada == 0) {
                getItem().setPphUnidSaude(null);
            }
        } catch (BCException ex) {
            LOGGER.error("Unidade de Saude Selecionada: {} erro: {}", unidadeSaudeSelecionada, ex.getMessage());
        }
        this.unidadeSaudeSelecionada = unidadeSaudeSelecionada;
    }

    public PphUnidadePagadoraEntity[] getUnidadePagadoraArray() {
        return unidadePagadoraArray;
    }

    public void setUnidadePagadoraArray(PphUnidadePagadoraEntity[] unidadePagadoraArray) {
        this.unidadePagadoraArray = unidadePagadoraArray;
    }

    public Long getUnidadePagadoraSelecionada() {
        if (getItem().getPphUnidadePagadora() != null) {
            this.unidadePagadoraSelecionada = getItem().getPphUnidadePagadora().getId();
        } else {
            this.unidadePagadoraSelecionada = Long.valueOf(0);
        }
        return unidadePagadoraSelecionada;
    }

    public void setUnidadePagadoraSelecionada(Long unidadePagadoraSelecionada) {
        try {
            if (unidadePagadoraSelecionada > 0) {
                getItem().setPphUnidadePagadora(unidadePagadoraBC.getById(unidadePagadoraSelecionada));
            } else if (unidadePagadoraSelecionada == 0) {
                getItem().setPphUnidadePagadora(null);
            }
        } catch (BCException ex) {
            LOGGER.error("Unidade Pagadora Selecionada: {} erro: {}", unidadePagadoraSelecionada, ex.getMessage());
        }
        this.unidadePagadoraSelecionada = unidadePagadoraSelecionada;
    }

    public List<PphAtestadoVidaEntity> getLstAtestadoVida() {
        if (lstAtestadoVida == null) {
            lstAtestadoVida = new ArrayList<PphAtestadoVidaEntity>();
        }
        return lstAtestadoVida;
    }

    public void setLstAtestadoVida(List<PphAtestadoVidaEntity> lstAtestadoVida) {
        this.lstAtestadoVida = lstAtestadoVida;
    }

    public void setLstAtestadoVida(Set<PphAtestadoVidaEntity> atestadoVidaSet) {
        this.lstAtestadoVida = null;
        if (Validador.isColecao(atestadoVidaSet)) {
            this.lstAtestadoVida = new ArrayList<PphAtestadoVidaEntity>(atestadoVidaSet);
        }
    }

    @Override
    public String compoeFiltro() {
        List<String> filtros = new ArrayList<String>();

        if (this.getItem().getNmBeneficiario() != null
                && !this.getItem().getNmBeneficiario().trim().equals("")) {
            filtros.add("Nome do Beneficiario contenha '" + this.getItem().getNmBeneficiario() + "'");
        }
        if (this.getItem().getPphUnidSaude() != null) {
            filtros.add("Unidade de SaÃºde igual a " + this.getItem().getPphUnidSaude().getNome());
        }
        if (this.getItem().getPphUnidadePagadora() != null) {
            filtros.add("Unidade Pagadora igual a " + this.getItem().getPphUnidadePagadora().getNmUnidadePagadora());
        }
        if (this.getItem().getTpBeneficiario() != null && !this.getItem().getTpBeneficiario().trim().equals("") && !this.getItem().getTpBeneficiario().equals("3")) {
            filtros.add("Tipo de BeneficiÃ¡rio igual a " + (this.getItem().getTpBeneficiario().equals("1") ? "Inicial" : (this.getItem().getTpBeneficiario().equals("2") ? "Dependente" : "Ambos")));
        }

        String retorno = "";
        if (filtros.size() > 1) {
            retorno = "Filtros: ";
        } else if (filtros.size() == 1) {
            retorno = "Filtro: ";
        }
        for (int i = 0; i < filtros.size(); i++) {
            String conexao = "";
            if (i > 0) {
                if (i == filtros.size() - 1) {
                    conexao = " e ";
                } else {
                    conexao = ", ";
                }
            }
            retorno += conexao + filtros.get(i);
        }
        return retorno;
    }

    @Override
    public String compoePeriodo() {
//        SimpleDateFormat formatoData = new SimpleDateFormat("dd/MM/yyyy");
//        if (getDataPeriodoDe() != null && getDataPeriodoAte() != null) {
//            String dataDe = formatoData.format(getDataPeriodoDe());
//            String dataAte = formatoData.format(getDataPeriodoAte());
//            return "PerÃ­odo de " + dataDe + " a " + dataAte;
//        } else if (this.dataPeriodoDe != null && this.dataPeriodoAte == null) {
//            String dataDe = formatoData.format(getDataPeriodoDe());
//            return "PerÃ­odo de Pagamento maior ou igual a " + dataDe;
//        } else if (this.dataPeriodoDe == null && this.dataPeriodoAte != null) {
//            String dataAte = formatoData.format(getDataPeriodoAte());
//            return "PerÃ­odo de Pagamento menor ou igual a " + dataAte;
//        } else {
            return "";
//        }
    }

    @Override
    public void prepararTelaInclusao() {
        this.gravaMemoria();
        limparCamposExtras();
    }

    @Override
    public PphBeneficiarioEntity getItem() {
        return super.getItem();
    }

    @Override
    public void setItem(PphBeneficiarioEntity item) {
        super.setItem(item);
        if (item != null) {
            try {
                limparCamposExtras();
                getComuniBenArray();
                getComuniContArray();
                getComuniProcArray();

                item = beneficiarioBC.buscarPorIdParaDetalhar(item.getId());
                this.unidSaude = item.getPphUnidSaude();
                this.unidadePagadora = item.getPphUnidadePagadora();
                {
                    if (Validador.isColecao(item.getPphEnderecoSet())) {
                        for (PphEnderecoEntity enderecoTmp : item.getPphEnderecoSet()) {
                            if (enderecoTmp.getPphProcurador() == null) {
                                this.endereco = enderecoTmp;
                                this.municipioBeneficiario = this.endereco.getPphMunicipio();
                                this.estadoBeneficiario = this.municipioBeneficiario.getPphEstado();
                                onSelectEstadoChange(this.estadoBeneficiario.getId(), "beneficiario");
                            }
                        }
                    }
                }
                this.agenciaBancaria = item.getPphAgenciaBancaria();
                if (this.agenciaBancaria != null) {
                    this.banco = this.agenciaBancaria.getPphBanco();
                    this.municipioAgencia = this.agenciaBancaria.getPphMunicipio();
                    this.estadoAgencia = this.municipioAgencia.getPphEstado();
                    onSelectBancoChange(this.banco.getId(), "beneficiario");
                }
                this.beneficiarioInicial = item.getPphBeneficiario();
                {
                    int i = 0;
                    Iterator<PphComunicacaoEntity> comuniItera = item.getPphComunicacaoSet().iterator();
                    while (comuniItera.hasNext()) {
                        PphComunicacaoEntity comunicacaoBeneficiario = comuniItera.next();
                        if (comunicacaoBeneficiario.getPphProcurador() == null && comunicacaoBeneficiario.getPphContato() == null) {
                            if (Validador.isEquals(comunicacaoBeneficiario.getTpComunic(), TpComunicacao.FONE_PRIN.getCd())) {
                                comuniBenArray[0] = comunicacaoBeneficiario;
                            }
                            if (Validador.isEquals(comunicacaoBeneficiario.getTpComunic(), TpComunicacao.FONE_SEC.getCd())) {
                                comuniBenArray[1] = comunicacaoBeneficiario;
                            }
                            if (Validador.isEquals(comunicacaoBeneficiario.getTpComunic(), TpComunicacao.CELULAR.getCd())) {
                                comuniBenArray[2] = comunicacaoBeneficiario;
                            }
                        }
                    }
                }
//                this.comuniContArray = null;

                if (item.getPphContatoSet().iterator().hasNext()) {
                    int i = 0;
                    this.contato = item.getPphContatoSet().iterator().next();
                    Iterator<PphComunicacaoEntity> comunContatoIterator = contato.getPphComunicacaoSet().iterator();
                    while (comunContatoIterator.hasNext()) {
                        PphComunicacaoEntity comunicacaoContato = comunContatoIterator.next();
                        if (Validador.isEquals(comunicacaoContato.getTpComunic(), TpComunicacao.FONE_PRIN.getCd())) {
                            comuniContArray[0] = comunicacaoContato;
                        }
                        if (Validador.isEquals(comunicacaoContato.getTpComunic(), TpComunicacao.FONE_SEC.getCd())) {
                            comuniContArray[1] = comunicacaoContato;
                        }
                        if (Validador.isEquals(comunicacaoContato.getTpComunic(), TpComunicacao.CELULAR.getCd())) {
                            comuniContArray[2] = comunicacaoContato;
                        }
                    }
                }
                Iterator<PphProcuradorEntity> procuradorIterator = item.getPphProcuradorSet().iterator();
                if (procuradorIterator.hasNext()) {
                    this.procurador = procuradorIterator.next();
                    Iterator<PphEnderecoEntity> enderecoProcuradorIterator = this.procurador.getPphEnderecoSet().iterator();
                    if (enderecoProcuradorIterator.hasNext()) {
                        this.enderecoProcurador = enderecoProcuradorIterator.next();
                        this.municipioProcurador = this.enderecoProcurador.getPphMunicipio();
                        this.estadoProcurador = this.municipioProcurador.getPphEstado();
                        onSelectEstadoChange(this.estadoProcurador.getId(), "procurador");
                    }
                    this.agenciaBancariaProcurador = this.procurador.getPphAgenciaBancaria();
                    if (this.agenciaBancariaProcurador != null) {
                        this.bancoProcurador = this.agenciaBancariaProcurador.getPphBanco();
                        onSelectBancoChange(this.bancoProcurador.getId(), "procurador");
                    }
                    {
                        int i = 0;
                        Iterator<PphComunicacaoEntity> comuniItera = this.procurador.getPphComunicacaoSet().iterator();

                        while (comuniItera.hasNext()) {
                            PphComunicacaoEntity comunicacaoProcurador = comuniItera.next();
//                            this.comuniProcArray[i++] = comunicacaoProcurador;
                            if (Validador.isEquals(comunicacaoProcurador.getTpComunic(), TpComunicacao.FONE_PRIN.getCd())) {
                                comuniProcArray[0] = comunicacaoProcurador;
                            }
                            if (Validador.isEquals(comunicacaoProcurador.getTpComunic(), TpComunicacao.FONE_SEC.getCd())) {
                                comuniProcArray[1] = comunicacaoProcurador;
                            }
                            if (Validador.isEquals(comunicacaoProcurador.getTpComunic(), TpComunicacao.CELULAR.getCd())) {
                                comuniProcArray[2] = comunicacaoProcurador;
                            }
                        }
                    }
                }
                setLstAtestadoVida(item.getPphAtestadoVidaSet());
                List<PphAtestadoVidaEntity> atestados = getLstAtestadoVida();
                for (int i = 0; i < atestados.size(); i++) {
                    PphAtestadoVidaEntity atestado = atestados.get(i);
                    if (Validador.isEquals(atestado.getFlExclusao(), Confirmacao.SIM.getId())) {
                        getLstAtestadoVida().remove(atestado);
                        item.getPphAtestadoVidaSet().remove(atestado);
                        i--;
                    }
                }

                super.setItem(item);
            } catch (BCException ex) {
                MBeanExceptionUtil.prepara(LOGGER, ex);
            }
        }
    }

    @Override
    public void setId(Long id) {
        super.setId(id);
        setItem(getItem());
    }

    @Override
    public Long getId() {
        return super.getId();
    }
    
    @Override
    public void processarPDF(Map relatorioPadrao) throws Exception {
        Reports r = new Reports(this);

        List<ListagemBeneficiarios> listaDeBeneficiarios = new ArrayList<ListagemBeneficiarios>();
        for (PphBeneficiarioEntity beneficiario : this.getLista()) {
            ListagemBeneficiarios dado = new ListagemBeneficiarios();

            //IDENTIFICACAO
            dado.setIdentNome      (beneficiario.getNmBeneficiario());
            dado.setIdentMae       (beneficiario.getNmMae());
            dado.setIdentRg        (beneficiario.getCdRgBeneficiario());
            dado.setIdentCpf       (beneficiario.getNrCpfBeneficiario());
            dado.setIdentNascimento(beneficiario.getDtNascimento());
            dado.setCategoria      (beneficiario.getTpBeneficiario().equals("1") ? "INICIAL" : "DEPENDENTE");
            dado.setUnidadeSaude   (beneficiario.getPphUnidSaude()       != null ? beneficiario.getPphUnidSaude().getNome() : "");
            dado.setUnidadePagadora(beneficiario.getPphUnidadePagadora() != null ? beneficiario.getPphUnidadePagadora().getNmUnidadePagadora() : "");
            
            //ENDERECO
            PphEnderecoEntity end = new PphEnderecoEntity();
            end.setPphBeneficiario(new PphBeneficiarioEntity(beneficiario.getId()));
            List<PphEnderecoEntity> ends = enderecoBC.listar(end, end);
            if (ends.size() > 0) {
                for (PphEnderecoEntity endere : ends) {
                    if (endere.getPphProcurador() != null) {
                        dado.setProcuradorEnderBairro     (endere.getNmBairro());
                        dado.setProcuradorEnderCep        (endere.getCdCep());
                        dado.setProcuradorEnderComplemento(endere.getDsComplEndereco());
                        dado.setProcuradorEnderLogradouro (endere.getDsEndereco());
                        dado.setProcuradorEnderMunicipio  (endere.getPphMunicipio() != null ? endere.getPphMunicipio().getNmMunicipio() : "");
                        dado.setProcuradorEnderNumero     (endere.getNrEndereco());
                        dado.setProcuradorEnderTipo       (endere.getTpLogradouro());
                        dado.setProcuradorEnderUf         (endere.getPphMunicipio() != null ? endere.getPphMunicipio().getPphEstado().getCdUf() : "");
                    } else {
                        dado.setEnderCep        (endere.getCdCep());
                        dado.setEnderTipo       (endere.getTpLogradouro());
                        dado.setEnderLogradouro (endere.getDsEndereco());
                        dado.setEnderNumero     (endere.getNrEndereco());
                        dado.setEnderComplemento(endere.getDsComplEndereco());
                        dado.setEnderBairro     (endere.getNmBairro());
                        dado.setEnderMunicipio  (endere.getPphMunicipio() != null ? endere.getPphMunicipio().getNmMunicipio() : "");
                        dado.setEnderUf         (endere.getPphMunicipio() != null ? endere.getPphMunicipio().getPphEstado().getCdUf() : "");
                    }
                }
            }
            
            //COMUNICACAO
            PphComunicacaoEntity com = new PphComunicacaoEntity();
            com.setPphBeneficiario(new PphBeneficiarioEntity(beneficiario.getId()));
            List<PphComunicacaoEntity> coms = comunicacaoBC.listar(com, com);
            if (coms.size() > 0) {
                for (PphComunicacaoEntity comunica : coms) {
                    if (comunica.getPphProcurador() == null && comunica.getPphContato() == null) {
                        if (comunica.getTpComunic().equals("FP")) dado.setEnderFone1  (comunica.getDsComunic());
                        if (comunica.getTpComunic().equals("FS")) dado.setEnderFone2  (comunica.getDsComunic());
                        if (comunica.getTpComunic().equals("CE")) dado.setEnderCelular(comunica.getDsComunic());
                    } else if (comunica.getPphProcurador() != null) {
                        if (comunica.getTpComunic().equals("FP")) dado.setProcuradorEnderFone1  (comunica.getDsComunic());
                        if (comunica.getTpComunic().equals("FS")) dado.setProcuradorEnderFone2  (comunica.getDsComunic());
                        if (comunica.getTpComunic().equals("CE")) dado.setProcuradorEnderCelular(comunica.getDsComunic());
                    } else if (comunica.getPphContato() != null) {
                        if (comunica.getTpComunic().equals("FP")) dado.setContatoFP(comunica.getDsComunic());
                        if (comunica.getTpComunic().equals("FS")) dado.setContatoFS(comunica.getDsComunic());
                        if (comunica.getTpComunic().equals("CE")) dado.setContatoCE(comunica.getDsComunic());
                    }
                }
            }
            
            //CONTATO
            PphContatoEntity con = new PphContatoEntity();
            con.setPphBeneficiario(new PphBeneficiarioEntity(beneficiario.getId()));
            List<PphContatoEntity> cons = contatoBC.listar(con, con);
            if (cons.size() > 0) {
                dado.setContatoNome   (cons.get(0).getNmContato());
                dado.setContatoRelacao(cons.get(0).getDsGrauObs().toUpperCase());
            }
            
            //DADOS BANCARIOS
            dado.setBancoCC(beneficiario.getNrContaCorrente());
            if (beneficiario.getPphAgenciaBancaria() != null) {
                dado.setBancoAgencia    (beneficiario.getPphAgenciaBancaria().getNrAgencia() + "-" + beneficiario.getPphAgenciaBancaria().getNmAgBancaria());
                dado.setBancoInstituicao(beneficiario.getPphAgenciaBancaria().getPphBanco().getNmBanco());
                dado.setBancoMunicipio  (beneficiario.getPphAgenciaBancaria().getPphMunicipio().getNmMunicipio());
                dado.setBancoUf         (beneficiario.getPphAgenciaBancaria().getPphMunicipio().getPphEstado().getCdUf());
            }
            
            //PROCURADOR
            PphProcuradorEntity pro = new PphProcuradorEntity();
            pro.setPphBeneficiario(new PphBeneficiarioEntity(beneficiario.getId()));
            List<PphProcuradorEntity> pros = procuradorBC.listar(pro, pro);
            if (pros.size() > 0) {
                dado.setProcuradorNome(pros.get(0).getNmProcurador());
                
                dado.setProcuradorbancoCC(pros.get(0).getNrContaCorrente());
                if (pros.get(0).getPphAgenciaBancaria() != null) {
                    dado.setProcuradorbancoAgencia    (pros.get(0).getPphAgenciaBancaria().getNrAgencia() + "-" + pros.get(0).getPphAgenciaBancaria().getNmAgBancaria());
                    dado.setProcuradorbancoInstituicao(pros.get(0).getPphAgenciaBancaria().getPphBanco().getNmBanco());
                    dado.setProcuradorbancoMunicipio  (pros.get(0).getPphAgenciaBancaria().getPphMunicipio().getNmMunicipio());
                    dado.setProcuradorbancoUf         (pros.get(0).getPphAgenciaBancaria().getPphMunicipio().getPphEstado().getCdUf());
                }
            }
            
            listaDeBeneficiarios.add(dado);
        }
        
        //List<PphEnderecoEntity> xxx = ((List<PphEnderecoEntity>) listaDeBeneficiarios.get(0).getPphEnderecoSet()).get(0).getDsEndereco();
        
        r.setRegistros((ArrayList) listaDeBeneficiarios);
        r.getParametros().put("filtro", compoeFiltro());
        r.getParametros().put("periodo", compoePeriodo());

        r.getParametros().put("campos", relatorioPadrao.get("campos"));
        r.getParametros().put("titulos", relatorioPadrao.get("titulos"));
        r.getParametros().put("larguras", relatorioPadrao.get("larguras"));
        r.getParametros().put("cabecalho1", relatorioPadrao.get("cabecalho1"));
        r.getParametros().put("cabecalho2", relatorioPadrao.get("cabecalho2"));
        r.getParametros().put("cabecalho3", relatorioPadrao.get("cabecalho3"));

        if (this.getComplementoRelatorio().contains("SQL")) {
            List<Long> idsUP = new ArrayList<Long>();
            List<Long> idsUS = new ArrayList<Long>();
            if (getItem().getPphUnidadePagadora() == null) idsUP = idsUnidPagadora; else idsUP.add(getItem().getPphUnidadePagadora().getId());
            if (getItem().getPphUnidSaude()       == null) idsUS = idsUnidSaude   ; else idsUS.add(getItem().getPphUnidSaude()      .getId());
                    
            r.getParametros().put("nome"            , getItem().getNmBeneficiario());
            r.getParametros().put("unidadePagadora" , idsUP);
            r.getParametros().put("unidadeSaude"    , idsUS);
            r.getParametros().put("tipoBeneficiario", getItem().getTpBeneficiario());
        }

        ExportarArquivo e = new ExportarArquivo();
        e.processaPDF(r, this.getComplementoRelatorio());

    }

    private void limparCamposExtras() {
        this.unidSaude = null;
        this.unidadePagadora = null;
        this.municipioBeneficiario = null;
        this.municipioAgencia = null;
        this.estadoBeneficiario = null;
        this.estadoAgencia = null;
        this.endereco = null;
        this.beneficiarioInicial = null;
        this.agenciaBancaria = null;
        this.banco = null;
        this.comuniBenArray = null;
        this.comuniContArray = null;
        this.contato = null;
        this.procurador = null;
        this.estadoProcurador = null;
        this.municipioProcurador = null;
        this.enderecoProcurador = null;
        this.comuniProcArray = null;
        this.bancoProcurador = null;
        this.agenciaBancariaProcurador = null;
        this.lstAtestadoVida = null;
        EmpenhoMBean empenhoMBean = (EmpenhoMBean) FacesUtil.getInSession("empenhoMBean");
        if(empenhoMBean!=null){
            empenhoMBean.reset();
        }
    }
}
