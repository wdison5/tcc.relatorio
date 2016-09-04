package org.tcc.relatorio.grid;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.primefaces.context.RequestContext;
import org.tcc.relatorio.dominio.PphBeneficiarioEntity;
import org.tcc.relatorio.dominio.PphEmpenhoEntity;
import org.tcc.relatorio.mbean.EmpenhoMBean;
import org.tcc.relatorio.negocio.EmpenhoBC;
import org.tcc.relatorio.util.Campo;
import org.tcc.relatorio.util.FacesUtil;
import org.tcc.relatorio.hammer.persistencia.exception.BCException;

/**
 *
 * @author Eloy
 */
@SessionScoped
@ManagedBean
public final class EmpenhoGrade extends BaseGrade<EmpenhoMBean, PphEmpenhoEntity> implements Serializable {

    @EJB(name = "EmpenhoBC")
    private EmpenhoBC empenhoBC;
    private boolean adminEmpenhos = false;
    private boolean consultaEmpenhos = false;

    public EmpenhoGrade() {
        super.iniciar();

        Campo[] estrutura = {
            new Campo("nrEmpenho"      , true, 15, "NÚMERO DO EMPENHO", 1, "left"  , "numeroEmpenho"  , true, true ),
            new Campo("dtEmpenho"      , true, 15, "DATA"             , 0, "center", "data"           , true, true ),
            new Campo("pphBeneficiario", true, 15, "UNIDADE PAGADORA" , 0, "left"  , "unidadePagadora", true, false),
            new Campo("vlEmpenho"      , true, 15, "VALOR PAGO"       , 0, "right" , "moeda"          , true, true ),
        };
        super.addColumns(estrutura);
        super.setNome("Empenho");
        super.setBotaoEditar(true);
        super.setBotaoDetalhar(true);
        super.setBotaoExcluir(true);
        super.setActionEditar("");
        super.setActionDetalhar("");

        super.getRelatorioPadrao().put("campos", "nrEmpenho;dtEmpenho;pphBeneficiario->pphUnidadePagadora->nmUnidadePagadora;vlEmpenho");
        super.getRelatorioPadrao().put("titulos", "Empenho;Data;Unidade Pagadora;Valor");
        super.getRelatorioPadrao().put("larguras", "40;40;120;50");
        super.getRelatorioPadrao().put("cabecalho1", "Pensão Hansenianos");
        super.getRelatorioPadrao().put("cabecalho2", "Listagem de Pagamentos");
        super.getRelatorioPadrao().put("cabecalho3", "SIPPH");

        super.setTextoHintCsv("Arquivo CSV (Ctrl + Alt + S)");
        super.setTextoHintPdf("Arquivo PDF (Ctrl + Alt + F)");
        super.setTextoHintXls("Excel (Ctrl + Alt + H)");
        super.setTextoHintXml("Arquivo XML (Ctrl + Alt + M)");
    }

    @PostConstruct
    private void posContruct() {
        adminEmpenhos = FacesUtil.isUserInRole("AdminEmpenhos");
        consultaEmpenhos = FacesUtil.isUserInRole("ConsultarEmpenhos");
    }
    
    public Object moeda(Object informacao, Tipo tipo){
        Locale BRAZIL = new Locale("pt","BR");
        DecimalFormatSymbols REAL = new DecimalFormatSymbols(BRAZIL);
        DecimalFormat DINHEIRO_REAL = new DecimalFormat("¤ ###,###,##0.00",REAL);

        switch (tipo) {
            case SORT:
                return (BigDecimal) informacao;
            case HINT:
            case GRID:
                return DINHEIRO_REAL.format(informacao);
        }
        return informacao;
    }

    public Object numeroEmpenho(Object informacao, Tipo tipo){
        switch (tipo) {
            case SORT:
                try{informacao = Integer.valueOf((String) informacao);}catch(Exception e){};
                break;
            case HINT:
            case GRID:
        }
        return informacao;
    }

    public Object unidadePagadora(Object informacao, Tipo tipo){
        switch (tipo) {
            case HINT:
                return ((PphBeneficiarioEntity) informacao).getPphUnidadePagadora().getNmUnidadePagadora();
            case SORT:
                return ((PphBeneficiarioEntity) informacao).getPphUnidadePagadora().getNmUnidadePagadora();
            case GRID:
                return ((PphBeneficiarioEntity) informacao).getPphUnidadePagadora().getNmUnidadePagadora();
        }
        return informacao;
    }
    
    public Object unidadeSaude(Object informacao, Tipo tipo){
        switch (tipo) {
            case HINT:
                return ((PphBeneficiarioEntity) informacao).getPphUnidSaude().getNome();
            case SORT:
                return ((PphBeneficiarioEntity) informacao).getPphUnidSaude().getNome();
            case GRID:
                return ((PphBeneficiarioEntity) informacao).getPphUnidSaude().getNome();
        }
        return informacao;
    }

    @Override
    public void selecionar(Object mBean, Object entidade) throws BCException {}

    @Override
    public void detalhar(EmpenhoMBean mBean, PphEmpenhoEntity entidade, Object aux) throws BCException {
        mBean.gravaMemoria();
        mBean.setItem(entidade);
        mBean.setDetalhar(true);
        atualizaMostraPainel();
    }

    private void atualizaMostraPainel() {
        RequestContext context = RequestContext.getCurrentInstance();
        context.update("painelPagamento");
        context.execute("PF('pagamento_dialog').show();");
    }

    @Override
    public void editar(EmpenhoMBean mBean, PphEmpenhoEntity entidade, Object aux) throws BCException {
        mBean.gravaMemoria();
        mBean.setItem(entidade);
        mBean.setDetalhar(false);
        atualizaMostraPainel();
    }

    @Override
    protected String getNomeComponente() {
        return ":formulario:sanfona:grade";
    }

    @Override
    public Boolean getBotaoEditar() {
        return super.getBotaoEditar() && adminEmpenhos;
    }

    @Override
    public Boolean getBotaoDetalhar() {
        return super.getBotaoDetalhar() && (consultaEmpenhos||adminEmpenhos);
    }

    @Override
    public Boolean getBotaoExcluir() {
        return super.getBotaoExcluir() && adminEmpenhos;
    }
}
