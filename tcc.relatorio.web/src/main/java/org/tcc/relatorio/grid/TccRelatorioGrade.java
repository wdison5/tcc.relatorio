package org.tcc.relatorio.grid;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.primefaces.context.RequestContext;
import org.tcc.relatorio.dominio.ProdutoEntity;
import org.tcc.relatorio.dominio.TipoProdutoEntity;
import org.tcc.relatorio.util.Campo;
import org.tcc.relatorio.hammer.persistencia.exception.BCException;
import org.tcc.relatorio.mbean.TccRelatorioMBean;

/**
 *
 * @author Jose Wdison de Souza<wdison@hotmail.com>
 */
@SessionScoped
@ManagedBean(name = "tccRelatorioGrade")
public final class TccRelatorioGrade extends BaseGrade<TccRelatorioMBean, ProdutoEntity> implements Serializable {
    
    public TccRelatorioGrade() {
        super.iniciar();

        Campo[] estrutura = {
//            new Campo("id"                      , true, 15, "Iden. Produto"    , 1, "left"  , "numero"         , true, true ),
            new Campo("descricao"               , true, 15, "Desc. Produto"    , 0, "center", "texto"          , true, true ),
            new Campo("tipoProduto"             , true, 15, "Categoria"        , 0, "center", "tipoProduto"    , true, true ),
            new Campo("dataReferencia"          , true, 15, "Data Movimento"   , 0, "center", "data"           , true, true ),
            new Campo("quantidade"              , true, 15, "Quantidade"       , 0, "right" , "numero"         , true, true ),
            new Campo("valorUnitario"           , true, 15, "Valor Unitário"   , 0, "right" , "moeda"          , true, true ),
            new Campo("valorCusto"              , true, 15, "Valor Unit. Custo", 0, "right" , "moeda"          , true, true ),
        };
        super.addColumns(estrutura);
        super.setNome("Produtos");
        super.setBotaoEditar(false);
        super.setBotaoDetalhar(false);
        super.setActionEditar("");
        super.setActionDetalhar("");
        super.setPossuiGrafico(true);
    }
    
    public Object tipoProduto(Object informacao, Tipo tipo){
        return informacao != null ? ((TipoProdutoEntity)informacao).getDescricao() : "";
    }

    @Override
    public void selecionar(Object mBean, Object entidade) throws BCException {
    }

    @Override
    public void detalhar(TccRelatorioMBean mBean, ProdutoEntity entidade, Object aux) throws BCException {
        mBean.setItem(entidade);
        atualizaMostraPainel();
    }

    private void atualizaMostraPainel() {
        RequestContext context = RequestContext.getCurrentInstance();
        context.update("painelPagamento");
        context.execute("PF('pagamento_dialog').show();");
    }

    @Override
    public void editar(TccRelatorioMBean mBean, ProdutoEntity entidade, Object aux) throws BCException {
        mBean.setItem(entidade);
        atualizaMostraPainel();
    }
}
