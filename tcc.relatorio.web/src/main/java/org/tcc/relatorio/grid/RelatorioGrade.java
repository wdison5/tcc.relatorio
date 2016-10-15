package org.tcc.relatorio.grid;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.primefaces.context.RequestContext;
import org.tcc.relatorio.dominio.PphBeneficiarioEntity;
import org.tcc.relatorio.dominio.PphEmpenhoEntity;
import org.tcc.relatorio.mbean.EmpenhoMBean;
import org.tcc.relatorio.util.Campo;
import org.tcc.relatorio.hammer.persistencia.exception.BCException;

/**
 *
 * @author Jose Wdison de Souza<wdison@hotmail.com>
 */
@SessionScoped
@ManagedBean
public final class RelatorioGrade extends BaseGrade<EmpenhoMBean, PphEmpenhoEntity> implements Serializable {

    public RelatorioGrade() {
        super.iniciar();

        Campo[] estrutura = {
            new Campo("nrEmpenho"      , true, 15, "NÚMERO DO EMPENHO", 1, "left"  , "numero"  , true, true ),
            new Campo("dtEmpenho"      , true, 15, "DATA"             , 0, "center", "data"           , true, true ),
            new Campo("pphBeneficiario", true, 15, "UNIDADE PAGADORA" , 0, "left"  , "unidadePagadora", true, false),
            new Campo("vlEmpenho"      , true, 15, "VALOR PAGO"       , 0, "right" , "moeda"          , true, true ),
        };
        super.addColumns(estrutura);
        super.setNome("Empenho");
        super.setBotaoEditar(false);
        super.setBotaoDetalhar(true);
        super.setActionEditar("");
        super.setActionDetalhar("");

        super.getRelatorioPadrao().put("campos", "nrEmpenho;dtEmpenho;pphBeneficiario->pphUnidadePagadora->nmUnidadePagadora;vlEmpenho");
        super.getRelatorioPadrao().put("titulos", "Empenho;Data;Unidade Pagadora;Valor");
        super.getRelatorioPadrao().put("larguras", "40;40;120;50");
        super.getRelatorioPadrao().put("cabecalho1", "Pensão Hansenianos");
        super.getRelatorioPadrao().put("cabecalho2", "Listagem de Pagamentos");
        super.getRelatorioPadrao().put("cabecalho3", "SIPPH");
    }

    public Object unidadePagadora(Object informacao, Tipo tipo) {
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

    public Object unidadeSaude(Object informacao, Tipo tipo) {
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
    public void selecionar(Object mBean, Object entidade) throws BCException {
    }

    @Override
    public void detalhar(EmpenhoMBean mBean, PphEmpenhoEntity entidade, Object aux) throws BCException {
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
        mBean.setItem(entidade);
        mBean.setDetalhar(false);
        atualizaMostraPainel();
    }

}
