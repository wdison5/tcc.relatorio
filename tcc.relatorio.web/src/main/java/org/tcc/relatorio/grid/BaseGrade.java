package org.tcc.relatorio.grid;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import org.primefaces.component.api.UIColumn;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.model.SortMeta;
import org.primefaces.model.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tcc.relatorio.cap.dominio.UsuarioEntity;
import org.tcc.relatorio.cap.negocio.UsuarioBC;
import org.tcc.relatorio.dominio.BaseEntity;
import org.tcc.relatorio.mbean.BaseMBean;
import org.tcc.relatorio.util.Campo;
import static org.tcc.relatorio.util.Funcoes.dataPorExtenso;
import org.tcc.relatorio.util.Registro;
import org.tcc.relatorio.hammer.persistencia.exception.BCException;

/**
 *
 * @author eloy
 * 22/01/15 - alteração na classe, usando generics...
 * @param <MBean>
 * @param <Entidade>
 */

@SessionScoped
public abstract class BaseGrade<MBean extends BaseMBean, Entidade extends BaseEntity> {
    
    @EJB(name = "UsuarioBC")
    private UsuarioBC usuarioBC;
    
    private static final Logger logger = LoggerFactory.getLogger(BaseGrade.class);
    private List<Registro> registros;
    private List<ColumnModel> columns = new ArrayList<ColumnModel>();
    private String nome;
    
    private Boolean possuiGrafico = false;
    
    private Boolean botaoEditar = false;
    private Boolean botaoExcluir = false;
    private Boolean botaoDetalhar = false;
    private Boolean botaoSelecionar = false;

    private String actionEditar;
    private String actionDetalhar;

    private final Tipo grid = Tipo.GRID;
    private final Tipo hint = Tipo.HINT;
    private final Tipo sort = Tipo.SORT;
    
    private String textoHintPdf = "Ctrl + Alt + F";
    private String textoHintXls = "Ctrl + Alt + H";
    private String textoHintXml = "Ctrl + Alt + M";
    private String textoHintCsv = "Ctrl + Alt + S";
    
    public Map relatorioPadrao = new HashMap();
    private List<SortMeta> ordenacaoInicial = null;
    
    private int mensagemExclusao = 0;
    
    public abstract void selecionar(Object mBean, Object entidade) throws BCException;
    public abstract void detalhar(MBean mBean, Entidade entidade, Object aux) throws BCException;
    public abstract void editar(MBean mBean, Entidade entidade, Object aux) throws BCException;
    public void excluir(MBean mBean, Entidade entidade) throws BCException{
        mBean.setItem(entidade);
        mBean.excluir();
    };
    
    public void iniciar() {
        registros = new ArrayList<Registro>();
    }
    
    private List<Integer> criaOrdem() {
        List<Ordenamento> lista = new ArrayList<Ordenamento>();

        for (int i = 0; i < this.columns.size(); i++) {
            if (this.columns.get(i).getVisivel() && this.columns.get(i).getOrdem() > 0) {
                lista.add( new Ordenamento(i, this.columns.get(i).getOrdem()) );
            }
        }
        
        Collections.sort(lista);
        
        List<Integer> retorno = new ArrayList<Integer>();
        for (Ordenamento l : lista) {
            retorno.add(l.index);
        }
        
        return retorno;
    }
    
    static public class Ordenamento implements Comparable<Ordenamento> {
        private int index;
        private int ordem;
        public Ordenamento(int index, int ordem) { this.index = index; this.ordem = ordem; }
        public int  getIndex() { return index; }
        public int  getOrdem() { return ordem; }
        public void setIndex(int index) { this.index = index; }
        public void setOrdem(int ordem) { this.ordem = ordem; }
        @Override
        public int compareTo(Ordenamento ordenamento) {
            if      (ordenamento.getOrdem() < this.ordem) { return  1; }
            else if (ordenamento.getOrdem() > this.ordem) { return -1; }
            else { return 0; }
        }
    }

    public List<SortMeta> getOrdenacaoInicial() {
        return this.ordenacaoInicial;
    }
    
    @PostConstruct
    private void iniciaOrdenacao() {
        this.ordenacaoInicial = new ArrayList<SortMeta>();
        String componente = getNomeComponente();
        UIViewRoot view = FacesContext.getCurrentInstance().getViewRoot();
        DataTable tabler = (DataTable) view.findComponent(componente);
        List<UIColumn> coler = tabler.getColumns();
        if (coler.size() > 1) {
            for (int criaOrdem : criaOrdem()) {
                SortMeta sm1 = new SortMeta();
                sm1.setSortBy((UIColumn)coler.get(criaOrdem));
                //sm1.setSortField("id");
                sm1.setSortOrder(SortOrder.ASCENDING);
                ordenacaoInicial.add(sm1);
            }
        }
    }
    
    protected String getNomeComponente() {
        return ":formulario:grade";
    }

    static public class ColumnModel {
 
        private final String header;
        private final String property;
        private final int largura;
        private final int ordem;
        private final String alinhamento;
        private final String funcao;
        private final boolean visivel;
        private final boolean exportavel;
 
        public ColumnModel(String header, String property, int largura, int ordem, String alinhamento, String funcao, boolean visivel, boolean exportavel) {
            this.header = header;
            this.property = property;
            this.largura = largura;
            this.ordem = ordem;
            this.alinhamento = alinhamento;
            this.funcao = funcao;
            this.visivel = visivel;
            this.exportavel = exportavel;
        }
 
        public String getHeader() {
            return header;
        }
 
        public String getProperty() {
            return property;
        }

        public int getLargura() {
            return largura;
        }

        public int getOrdem() {
            return ordem;
        }

        public String getAlinhamento() {
            return alinhamento;
        }

        public String getFuncao() {
            return funcao;
        }
        
        public boolean getVisivel() {
            return visivel;
        }

        public boolean getExportavel() {
            return exportavel;
        }
    }

    public List<ColumnModel> getColumns() {
        return columns;
    }

    public void setColumns(List<ColumnModel> columns) {
        this.columns = columns;
    }
    
    public void addColumns(Campo[] campos){
        for (Campo campo : campos) {
            this.getColumns().add(
                    new ColumnModel(
                            campo.getTitulo(),
                            campo.getNome(),
                            campo.getLargura(),
                            campo.getOrdem(),
                            campo.getAlinhamento(),
                            campo.getFuncao(),
                            campo.getVisivel(),
                            campo.getExportavel()
                    )
            );
        }
    }

    public Object funcao(Object classe, String metodo, Object informacao, Tipo tipo, Object reg) throws ClassNotFoundException {
        Object retobj = informacao;
        if (metodo != null && !metodo.equals("")) {
            try {
                informacao = informacao !=null ? informacao : reg;
                
                Class partypes[] = new Class[2];  
                partypes[0] = Object.class;
                partypes[1] = Tipo.class;  

                String ent = classe.getClass().getName();
                Class cls = Class.forName(ent);  
                Method meth = cls.getMethod(metodo, partypes);  

                Object arglist[] = new Object[2];  
                arglist[0] = informacao;
                arglist[1] = tipo;

                if (informacao == null) {
                    retobj = null;
                } else {
                    retobj = meth.invoke(classe, arglist);
                }

            } catch (IllegalAccessException ex) {
                logger.info("Erro: {} - {}", ex.toString(), ex.getMessage());
            } catch (IllegalArgumentException ex) {
                logger.info("Erro: {} - {}", ex.toString(), ex.getMessage());
            } catch (NoSuchMethodException ex) {
                logger.info("Erro: {} - {}", ex.toString(), ex.getMessage());
            } catch (SecurityException ex) {
                logger.info("Erro: {} - {}", ex.toString(), ex.getMessage());
            } catch (InvocationTargetException ex) {
                logger.info("Erro: {} - {}", ex.toString(), ex.getMessage());
            }
        }
        return retobj;
    }
    
    public String mensagemCabecalho(List<Entidade> lista) {
        return !lista.isEmpty() ? 
                    lista.size() == 1 ? 
                        "Um Registro Encontrado" : 
                        "Quantidade de Registros: " + String.valueOf(lista.size()) : "";
    }

    public String mensagemVazio(MBean mbean) {
        return mbean.getFiltroVazio();
    }

    /*********************************************************************/
    /************************FUNÇÕES PARA COLUNAS DA GRADE****************/
    /*********************************************************************/
    public Object dataHora(Object informacao, Tipo tipo){
        switch (tipo) {
            case HINT:
                return dataPorExtenso((Date) informacao);
            case SORT:
                return (Date) informacao;
            case GRID:
                SimpleDateFormat formatoData = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                return formatoData.format(informacao);
        }
        return informacao;
    }
    
    public Object data(Object informacao, Tipo tipo){
        switch (tipo) {
            case HINT:
                return dataPorExtenso((Date) informacao);
            case GRID:
                SimpleDateFormat formatoData = new SimpleDateFormat("dd/MM/yyyy");
                return formatoData.format(informacao);
        }
        return informacao;
    }
    
    public Object moeda(Object informacao, Tipo tipo) {
        Locale BRAZIL = new Locale("pt", "BR");
        DecimalFormatSymbols REAL = new DecimalFormatSymbols(BRAZIL);
        DecimalFormat DINHEIRO_REAL = new DecimalFormat("¤ ###,###,##0.00", REAL);

        switch (tipo) {
            case HINT:
                return DINHEIRO_REAL.format(informacao);
            case SORT:
                return (BigDecimal) informacao;
            case GRID:
                return DINHEIRO_REAL.format(informacao);
        }
        return informacao;
    }
    
    public Object texto(Object informacao, Tipo tipo) {
        return informacao;
    }
    
    public Object numero(Object informacao, Tipo tipo){
        switch (tipo) {
            case SORT:
                return Integer.valueOf((String) informacao);
        }
        return informacao;
    }
    
    public Object dataNasc(Object informacao, Tipo tipo){
        switch (tipo) {
            case HINT:
                Calendar dateOfBirth = new GregorianCalendar();
                dateOfBirth.setTime((Date) informacao);
                Calendar today = Calendar.getInstance();
                int age = today.get(Calendar.YEAR) - dateOfBirth.get(Calendar.YEAR);
                dateOfBirth.add(Calendar.YEAR, age);
                if (today.before(dateOfBirth)) age--;
                return "O beneficiário tem " + age + (age <= 1 ? " ano" : " anos");
            case SORT:
                return (Date) informacao;
            case GRID:
                SimpleDateFormat formatoData = new SimpleDateFormat("dd/MM/yyyy");
                return formatoData.format(informacao);
        }
        return informacao;
    }
    
    public Object funcionario(Object informacao, Tipo tipo){
        UsuarioEntity usuarioEntity = new UsuarioEntity();
        usuarioEntity.setUserId((String) informacao);
        String funcionario;
        try {
            funcionario = usuarioBC.consultar(usuarioEntity).getNome().toUpperCase();
        } catch (BCException ex) {
            funcionario = "";
        }
        switch (tipo) {
            case HINT:
                return funcionario;
            case SORT:
                return funcionario;
            case GRID:
                return funcionario;
        }
        return informacao;
    }
    
    public Object login(Object informacao, Tipo tipo){
        String funcionario = informacao.toString().toUpperCase();
        switch (tipo) {
            case HINT:
                return funcionario;
            case SORT:
                return funcionario;
            case GRID:
                return funcionario;
        }
        return informacao;
    }
    
    public Object situacao(Object informacao, Tipo tipo){
        if ((Boolean) informacao) { return "ATIVO"; } else { return "INATIVO"; }
    }
    

    /*********************************************************************/
    /************************GETTER´S AND SETTER´S************************/
    /*********************************************************************/
    
    /**
     * Metodo para obter o tipo para Grid
     * @return Retorna um ENUM do tipo grid
     */
    public Tipo getGrid() {
        return grid;
    }

    public Tipo getHint() {
        return hint;
    }

    public Tipo getSort() {
        return sort;
    }

    public List<Registro> getRegistros() {
        return registros;
    }

    public void setRegistros(List<Registro> registros) {
        this.registros = registros;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Boolean getPossuiGrafico() {
        return possuiGrafico;
    }

    public void setPossuiGrafico(Boolean possuiGrafico) {
        this.possuiGrafico = possuiGrafico;
    }

    public Boolean getBotaoEditar() {
        return botaoEditar;
    }

    public void setBotaoEditar(Boolean botaoEditar) {
        this.botaoEditar = botaoEditar;
    }

    public Boolean getBotaoExcluir() {
        return botaoExcluir;
    }

    public void setBotaoExcluir(Boolean botaoExcluir) {
        this.botaoExcluir = botaoExcluir;
    }

    public Boolean getBotaoSelecionar() {
        return botaoSelecionar;
    }

    public void setBotaoSelecionar(Boolean botaoSelecionar) {
        this.botaoSelecionar = botaoSelecionar;
    }

    public String getActionEditar() {
        return actionEditar;
    }

    public void setActionEditar(String actionEditar) {
        this.actionEditar = actionEditar;
    }
    
    public Boolean getBotaoDetalhar() {
        return botaoDetalhar;
    }
    
    public void setBotaoDetalhar(Boolean botaoDetalhar) {
        this.botaoDetalhar = botaoDetalhar;
    }
    
    public String getActionDetalhar() {
        return actionDetalhar;
    }
    
    public void setActionDetalhar(String actionDetalhar) {
        this.actionDetalhar = actionDetalhar;
    }

    public int getMensagemExclusao() {
        return mensagemExclusao;
    }

    public void setMensagemExclusao(int mensagemExclusao) {
        this.mensagemExclusao = mensagemExclusao;
    }

    public Map getRelatorioPadrao() {
        return relatorioPadrao;
    }

    public void setRelatorioPadrao(Map relatorioPadrao) {
        this.relatorioPadrao = relatorioPadrao;
    }

    public String getTextoHintPdf() {
        return textoHintPdf;
    }

    public void setTextoHintPdf(String textoHintPdf) {
        this.textoHintPdf = textoHintPdf;
    }

    public String getTextoHintXls() {
        return textoHintXls;
    }

    public void setTextoHintXls(String textoHintXls) {
        this.textoHintXls = textoHintXls;
    }

    public String getTextoHintXml() {
        return textoHintXml;
    }

    public void setTextoHintXml(String textoHintXml) {
        this.textoHintXml = textoHintXml;
    }

    public String getTextoHintCsv() {
        return textoHintCsv;
    }

    public void setTextoHintCsv(String textoHintCsv) {
        this.textoHintCsv = textoHintCsv;
    }

}