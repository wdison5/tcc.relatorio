package org.tcc.relatorio.mbean;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tcc.relatorio.cap.dominio.UsuarioEntity;
import org.tcc.relatorio.dominio.ProdutoEntity;
import org.tcc.relatorio.util.ExportarArquivo;
import org.tcc.relatorio.util.FacesUtil;
import org.tcc.relatorio.util.jasper.Reports;
import org.tcc.relatorio.hammer.persistencia.exception.BCException;
import org.tcc.relatorio.negocio.RelatorioBC;

/**
 *
 * @author Eloy
 */
@SessionScoped
@ManagedBean(name = "relatorioMBean")
public class RelatorioMBean extends BaseMBean<ProdutoEntity> {

    private static final long serialVersionUID = 118231614034054148L;
    private static final Logger LOGGER = LoggerFactory.getLogger(RelatorioMBean.class);
    
    private Date dataPeriodoDe;
    private Date dataPeriodoAte;
    private UsuarioEntity usuarioLogado;

    private String actionVoltar = "/view/relatorio/consultar.jsf?faces-redirect=true";

    @EJB(name = "RelatorioBC")
    private RelatorioBC relatorioBC;

    public RelatorioMBean() {
        setParameterClass(ProdutoEntity.class);
        super.initialize("Produto");
        LOGGER.debug(getDescricaoMBean());
    }

    @PostConstruct
    public void initialize() {
        this.usuarioLogado = (UsuarioEntity) FacesUtil.getInSession("usuarioLogado");
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
        super.initialize("Produto");
        super.setComplementoRelatorio("");
        this.setDataPeriodoDe(null);
        this.setDataPeriodoAte(null);
        this.setActionVoltar("/view/relatorio/consultar.jsf?faces-redirect=true");//TODO
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
    public List<ProdutoEntity> manter(ManterOp op) throws BCException {
        switch (op) {
            case INCLUIR:
                break;
            case EXCLUIR:
                break;
            case ATUALIZAR:
                break;
            case LISTAR:
                ProdutoEntity produtoInicial = new ProdutoEntity();
                ProdutoEntity produtoFinal = new ProdutoEntity();

                if (dataPeriodoDe  != null) produtoInicial.setDataReferencia(dataPeriodoDe);
                if (dataPeriodoAte != null) produtoFinal.setDataReferencia(dataPeriodoAte);
                if (dataPeriodoDe  != null && dataPeriodoAte !=null && dataPeriodoDe.after(dataPeriodoAte)) {
                    FacesUtil.addMsg(FacesUtil.WARN, "A data inicial é maior que a data final.");
                    break;
                }


                List<ProdutoEntity> set = relatorioBC.pesquisar(produtoInicial, produtoFinal, usuarioLogado);
                if (set != null) {
                    return new ArrayList<ProdutoEntity>(set);
                }
            break;
        }
        return null;
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
    public void prepararTelaInclusao() {}

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
                 
            r.getParametros().put("unidadePagadora", idsUP);
            r.getParametros().put("unidadeSaude"   , idsUS);
              
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
    
}
