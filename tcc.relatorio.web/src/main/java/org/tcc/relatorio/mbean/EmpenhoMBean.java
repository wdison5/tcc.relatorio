package org.tcc.relatorio.mbean;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.primefaces.context.RequestContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tcc.relatorio.cap.dominio.UsuarioEntity;
import org.tcc.relatorio.dominio.PphUnidSaudeEntity;
import org.tcc.relatorio.dominio.PphBeneficiarioEntity;
import org.tcc.relatorio.dominio.PphEmpenhoEntity;
import org.tcc.relatorio.dominio.PphUnidadePagadoraEntity;
import org.tcc.relatorio.mbean.exception.util.MBeanExceptionUtil;
import org.tcc.relatorio.enumeracao.Confirmacao;
import org.tcc.relatorio.negocio.EmpenhoBC;
import org.tcc.relatorio.negocio.UnidSaudeBC;
import org.tcc.relatorio.negocio.UnidadePagadoraBC;
import org.tcc.relatorio.negocio.validator.Validador;
import org.tcc.relatorio.util.ExportarArquivo;
import org.tcc.relatorio.util.FacesUtil;
import org.tcc.relatorio.util.jasper.Reports;
import org.tcc.relatorio.hammer.persistencia.exception.BCException;

/**
 *
 * @author Eloy
 */
@SessionScoped
@ManagedBean(name = "empenhoMBean")
public class EmpenhoMBean extends BaseMBean<PphEmpenhoEntity> {

    private static final long serialVersionUID = 118231614034054148L;
    private static final Logger LOGGER = LoggerFactory.getLogger(EmpenhoMBean.class);

    private List<Long> idsUnidSaude;
    private List<Long> idsUnidPagadora;

    private List<PphUnidadePagadoraEntity> lstUnidadePagadora;
    private List<PphUnidSaudeEntity> lstUnidSaude;
    private Date dataPeriodoDe;
    private Date dataPeriodoAte;
    private Long unidadeSaudeSelecionada;
    private Long unidadePagadoraSelecionada;
    private UsuarioEntity usuarioLogado;

    private boolean detalhar = false;

    private String actionVoltar = "/view/empenho/consultar.jsf?faces-redirect=true";

    @EJB(name = "EmpenhoBC")
    private EmpenhoBC empenhoBC;

    @EJB(name = "UnidadePagadoraBC")
    private UnidadePagadoraBC unidadePagadoraBC;

    @EJB(name = "UnidSaudeBC")
    private UnidSaudeBC unidSaudeBC;

    public EmpenhoMBean() {
        setParameterClass(PphEmpenhoEntity.class);
        super.initialize("Empenho");
        LOGGER.debug(getDescricaoMBean());
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
            MBeanExceptionUtil.log(LOGGER, e, "Lista de Unidades de Saude não encontrada: " + e.getMessage());
            FacesUtil.addError("Lista de Unidades de Saude não encontrada.");
        }
    }

    public void inicializaRelatorio(String complemento) {
        this.setEstadoDefault();
        super.setComplementoRelatorio(complemento);
    }

    public void inicializaRelatorio(String complemento, Boolean periodoDefault) {
        this.setEstadoDefault();
        super.setComplementoRelatorio(complemento);
        if (periodoDefault) {
            Date date = new Date();
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            c.set(Calendar.DAY_OF_MONTH, 1);
            this.setDataPeriodoDe(c.getTime());
            this.setDataPeriodoAte(new Date());
        }
    }

    public void setEstadoDefault() {
        super.initialize("Empenho");
        super.setComplementoRelatorio("");
        this.setDataPeriodoDe(null);
        this.setDataPeriodoAte(null);
        this.setUnidadePagadoraSelecionada(null);
        this.setUnidadeSaudeSelecionada(null);
        this.getItem().setNrEmpenho(null);
        this.getItem().setId(null);
        this.setActionVoltar("/view/empenho/consultar.jsf?faces-redirect=true");
    }

    public String tituloTela() {
        if (this.getComplementoRelatorio().equals("Analitico")) {
            return "Relatório Analítico";
        }
        if (this.getComplementoRelatorio().equals("Sintetico")) {
            return "Relatório Sintético";
        }
        if (this.getComplementoRelatorio().equals("SQLLog")) {
            return "LOG - Pagamentos";
        }
        return "Consulta Pagamentos";
    }

    @Override
    public List<PphEmpenhoEntity> manter(ManterOp op) throws BCException {
        switch (op) {
            case INCLUIR:
                empenhoBC.incluir(getItem(), usuarioLogado);
                break;
            case EXCLUIR:
                //empenhoBC.excluir(getItem(), usuarioLogado);
                break;
            case ATUALIZAR:
                empenhoBC.atualizar(getItem(), usuarioLogado);
                break;

            case LISTAR:
                PphEmpenhoEntity empenhoInicial = new PphEmpenhoEntity();
                PphEmpenhoEntity empenhoFinal = new PphEmpenhoEntity();

                if (getItem().getNrEmpenho() != null) {
                    if (!getItem().getNrEmpenho().equals("")) {
                        empenhoInicial.setNrEmpenho(getItem().getNrEmpenho());
                        empenhoFinal.setNrEmpenho(getItem().getNrEmpenho());
                    }
                }
                empenhoInicial.setFlExclusao(Confirmacao.NAO.getId());
                empenhoFinal.setFlExclusao(Confirmacao.NAO.getId());

                if (dataPeriodoDe  != null) empenhoInicial.setDtEmpenho(dataPeriodoDe);
                if (dataPeriodoAte != null) empenhoFinal.setDtEmpenho(dataPeriodoAte);
                if (dataPeriodoDe  != null && dataPeriodoAte !=null && dataPeriodoDe.after(dataPeriodoAte)) {
                    FacesUtil.addMsg(FacesUtil.WARN, "A data inicial é maior que a data final.");
                    break;
                }

                PphBeneficiarioEntity beneficiario = new PphBeneficiarioEntity();
                if (Validador.isMaior(unidadeSaudeSelecionada, 0)) {
                    PphUnidSaudeEntity unidSaude = new PphUnidSaudeEntity(unidadeSaudeSelecionada);
                    beneficiario.setPphUnidSaude(unidSaude);
                }
                if (Validador.isMaior(unidadePagadoraSelecionada, 0)) {
                    PphUnidadePagadoraEntity unidPagadora = new PphUnidadePagadoraEntity(unidadePagadoraSelecionada);
                    beneficiario.setPphUnidadePagadora(unidPagadora);
                }
                empenhoInicial.setPphBeneficiario(beneficiario);
                empenhoFinal.setPphBeneficiario(beneficiario);

                List<PphEmpenhoEntity> set = empenhoBC.pesquisar(empenhoInicial, empenhoFinal, idsUnidSaude, idsUnidPagadora, usuarioLogado);
                if (set != null) {
                    return new ArrayList<PphEmpenhoEntity>(set);
                }
            break;
        }
        return null;
    }

    public Long getUnidadeSaudeSelecionada() {
//        if (getItem().getPphBeneficiario().getInstituicao() != null) {
//            this.unidadeSaudeSelecionada = getItem().getPphBeneficiario().getInstituicao().getId();
//        } else {
//            this.unidadeSaudeSelecionada = Long.valueOf(0);
//        }
        return unidadeSaudeSelecionada;
    }

    public void setUnidadeSaudeSelecionada(Long unidadeSaudeSelecionada) {
//        try {
//            if (unidadeSaudeSelecionada > 0) {
//                getItem().getPphBeneficiario().setInstituicao(instituicaoBC.getById(unidadeSaudeSelecionada));
//            } else if (unidadeSaudeSelecionada == 0) {
//                getItem().getPphBeneficiario().setInstituicao(null);
//            }
//        } catch (BCException ex) {
//            LOGGER.error("Unidade de Saude Selecionada: {} erro: {}", unidadeSaudeSelecionada, ex.getMessage());
//        }
        this.unidadeSaudeSelecionada = unidadeSaudeSelecionada;
    }

    public Long getUnidadePagadoraSelecionada() {
//        if (getItem().getPphBeneficiario().getPphUnidadePagadora() != null) {
//            this.unidadePagadoraSelecionada = getItem().getPphBeneficiario().getPphUnidadePagadora().getId();
//        } else {
//            this.unidadePagadoraSelecionada = Long.valueOf(0);
//        }
        return unidadePagadoraSelecionada;
    }

    public void setUnidadePagadoraSelecionada(Long unidadePagadoraSelecionada) {
//        try {
//            if (unidadePagadoraSelecionada > 0) {
//                getItem().getPphBeneficiario().setPphUnidadePagadora(unidadePagadoraBC.getById(unidadePagadoraSelecionada));
//            } else if (unidadePagadoraSelecionada == 0) {
//                getItem().getPphBeneficiario().setPphUnidadePagadora(null);
//            }
//        } catch (BCException ex) {
//            LOGGER.error("Unidade Pagadora Selecionada: {} erro: {}", unidadePagadoraSelecionada, ex.getMessage());
//        }
        this.unidadePagadoraSelecionada = unidadePagadoraSelecionada;
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

    public Date getDataPeriodoDe() {
        return dataPeriodoDe;
    }

    public void setDataPeriodoDe(Date dataPeriodoDe) {
        this.dataPeriodoDe = dataPeriodoDe;
    }

    public Date getDataPeriodoAte() {
        return dataPeriodoAte;
    }

    public void setDataPeriodoAte(Date dataPeriodoAte) {
        this.dataPeriodoAte = dataPeriodoAte;
    }

    public String getActionVoltar() {
        return actionVoltar;
    }

    public void setActionVoltar(String actionVoltar) {
        this.actionVoltar = actionVoltar;
    }

    @Override
    public String compoeFiltro() {
        List<String> filtros = new ArrayList<String>();

        if (this.getItem().getNrEmpenho() != null && !this.getItem().getNrEmpenho().trim().equals("")) {
            filtros.add("Número do Empenho igual a " + this.getItem().getNrEmpenho());
        }
        if (unidadeSaudeSelecionada > 0) {
            try {
                PphUnidSaudeEntity unidade = unidSaudeBC.getById(unidadeSaudeSelecionada);
                filtros.add("Unidade de Saúde igual a " + unidade.getNome());
            } catch (BCException ex) {
                LOGGER.info("Erro em compoeFiltro: {}", ex.toString());
            }
        }
        if (unidadePagadoraSelecionada > 0) {
            try {
                PphUnidadePagadoraEntity unidade = unidadePagadoraBC.getById(unidadePagadoraSelecionada);
                filtros.add("Unidade Pagadora igual a " + unidade.getNmUnidadePagadora());
            } catch (BCException ex) {
                LOGGER.info("Erro em compoeFiltro: {}", ex.toString());
            }
        }
//        SimpleDateFormat formatoData = new SimpleDateFormat("dd/MM/yyyy");
//        if (getDataPeriodoDe() != null && getDataPeriodoAte() != null) {
//            String dataDe = formatoData.format(getDataPeriodoDe());
//            String dataAte = formatoData.format(getDataPeriodoAte());
//            filtros.add("Período de " + dataDe + " a " + dataAte);
//        } else if (this.dataPeriodoDe != null && this.dataPeriodoAte == null) {
//            String dataDe = formatoData.format(getDataPeriodoDe());
//            filtros.add("Período de Pagamento maior ou igual a " + dataDe);
//        } else if (this.dataPeriodoDe == null && this.dataPeriodoAte != null) {
//            String dataAte = formatoData.format(getDataPeriodoAte());
//            filtros.add("Período de Pagamento menor ou igual a " + dataAte);
//        }

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

    public void prepararTelaInclusaoEmpenho(PphBeneficiarioEntity beneficiario) {
        this.prepararTelaInclusao();
        this.getItem().setPphBeneficiario(beneficiario);
    }

    @Override
    public String compoePeriodo() {
        SimpleDateFormat formatoData = new SimpleDateFormat("dd/MM/yyyy");
        if (getDataPeriodoDe() != null && getDataPeriodoAte() != null) {
            String dataDe = formatoData.format(getDataPeriodoDe());
            String dataAte = formatoData.format(getDataPeriodoAte());
            return "Período de " + dataDe + " a " + dataAte;
        } else if (this.dataPeriodoDe != null && this.dataPeriodoAte == null) {
            String dataDe = formatoData.format(getDataPeriodoDe());
            return "Período de Pagamento maior ou igual a " + dataDe;
        } else if (this.dataPeriodoDe == null && this.dataPeriodoAte != null) {
            String dataAte = formatoData.format(getDataPeriodoAte());
            return "Período de Pagamento menor ou igual a " + dataAte;
        } else {
            return "";
        }
    }

    @Override
    public void prepararTelaInclusao() {
        this.getItem().setDtEmpenho(null);
        this.getItem().setId(null);
        this.getItem().setNrEmpenho(null);
        this.getItem().setPphBeneficiario(null);
        this.getItem().setVlEmpenho(null);
    }

    @Override
    public void processarPDF(Map relatorioPadrao) throws Exception {
        Reports r = new Reports(this);
        r.setRegistros((ArrayList) this.getLista());
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
            if (unidadePagadoraSelecionada == 0) idsUP = idsUnidPagadora; else idsUP.add(unidadePagadoraSelecionada);
            if (unidadeSaudeSelecionada    == 0) idsUS = idsUnidSaude   ; else idsUS.add(unidadeSaudeSelecionada);
            
            r.getParametros().put("unidadePagadora", idsUP);
            r.getParametros().put("unidadeSaude"   , idsUS);
            r.getParametros().put("nrEmpenho"      , getItem().getNrEmpenho());
              
            Calendar calendar = new GregorianCalendar();
            if (dataPeriodoDe == null) {
                r.getParametros().put("periodoInicial", null);
            } else {
                calendar.setTime(dataPeriodoDe);
                int mes = calendar.get(Calendar.MONTH) + 1;
                int dia = calendar.get(Calendar.DAY_OF_MONTH);
                int ano = calendar.get(Calendar.YEAR);
                r.getParametros().put("periodoInicial", dataPeriodoDe == null ? null : String.format("%02d", dia) + "/" + String.format("%02d", mes) + "/" + ano);
            }

            if (dataPeriodoAte == null) {
                r.getParametros().put("periodoFinal", null);
            } else {
                calendar.setTime(dataPeriodoAte);
                int mes = calendar.get(Calendar.MONTH) + 1;
                int dia = calendar.get(Calendar.DAY_OF_MONTH);
                int ano = calendar.get(Calendar.YEAR);
                r.getParametros().put("periodoFinal", dataPeriodoAte == null ? null : String.format("%02d", dia) + "/" + String.format("%02d", mes) + "/" + ano);
            }
        }

        ExportarArquivo e = new ExportarArquivo();
        e.processaPDF(r, this.getComplementoRelatorio());

    }

    public boolean isDetalhar() {
        return detalhar;
    }

    public void setDetalhar(boolean detalhar) {
        this.detalhar = detalhar;
    }

    public void resetItem() {
        setItem(new PphEmpenhoEntity());
        setDetalhar(false);
    }

    public void manterPorDialog() {
        PphBeneficiarioEntity beneficiario = getItem().getPphBeneficiario();
        if (Validador.isMaior(getItem().getId(), 0)) {
            atualizar();
        } else {
            persistir();
        }

        if (getItem().isOk()) {
            PphEmpenhoEntity empenho = new PphEmpenhoEntity();
            empenho.setPphBeneficiario(new PphBeneficiarioEntity(beneficiario.getId()));
            empenho.setFlExclusao(Confirmacao.NAO.getId());
            try {
                setLista(empenhoBC.listar(empenho, empenho));
            } catch (Exception ex) {LOGGER.info("Erro ao consultar lista de empenhos.{}", ex);}

            RequestContext request = RequestContext.getCurrentInstance();
            request.execute("PF('pagamento_dialog').hide();");
            request.update("sanfona:grade");

        }
    }

}
