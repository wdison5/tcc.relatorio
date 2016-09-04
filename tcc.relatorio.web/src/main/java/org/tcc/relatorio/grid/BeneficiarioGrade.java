package org.tcc.relatorio.grid;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tcc.relatorio.dominio.PphUnidSaudeEntity;
import org.tcc.relatorio.dominio.PphAgenciaBancariaEntity;
import org.tcc.relatorio.dominio.PphBeneficiarioEntity;
import org.tcc.relatorio.dominio.PphEmpenhoEntity;
import org.tcc.relatorio.dominio.PphEnderecoEntity;
import org.tcc.relatorio.dominio.PphUnidadePagadoraEntity;
import org.tcc.relatorio.mbean.BeneficiarioMBean;
import org.tcc.relatorio.mbean.EmpenhoMBean;
import org.tcc.relatorio.negocio.BeneficiarioBC;
import org.tcc.relatorio.negocio.EmpenhoBC;
import org.tcc.relatorio.negocio.EnderecoBC;
import org.tcc.relatorio.enumeracao.Confirmacao;
import org.tcc.relatorio.util.Campo;
import org.tcc.relatorio.hammer.persistencia.exception.BCException;

/**
 *
 * @author Eloy
 */

@SessionScoped
@ManagedBean
public final class BeneficiarioGrade extends BaseGrade<BeneficiarioMBean, PphBeneficiarioEntity> implements Serializable{

    private static final Logger LOGGER = LoggerFactory.getLogger(BeneficiarioGrade.class);
    
    @EJB(name = "BeneficiarioBC")
    private BeneficiarioBC beneficiarioBC;

    @EJB(name = "EnderecoBC")
    private EnderecoBC enderecoBC;

    @EJB(name = "EmpenhoBC")
    private EmpenhoBC empenhoBC;

    public BeneficiarioGrade() {
        super.iniciar();
        
        Campo[] estrutura = {
            new Campo("nmBeneficiario"    , true, 30, "NOME"                , 1, "left"  , null                 , true, true ),
            new Campo("dtNascimento"      , true, 15, "NASCIMENTO"          , 0, "center", "dataNasc"           , true, true ),
            new Campo("nmMae"             , true, 30, "NOME DA MÃE"         , 0, "left"  , null                 , true, true ),
            new Campo("pphUnidadePagadora", true, 25, "UNIDADE PAGADORA"    , 0, "left"  , "unidadePagadora"    , true, true ),
            new Campo("pphUnidSaude"      , true, 25, "UNIDADE DE SAÚDE"    , 0, "left"  , "unidadeSaude"       , true, false),
            new Campo("pphUnidSaude"      , true, 25, "CNES"                , 0, "left"  , "cnes"               , true, false),
            new Campo("tpBeneficiario"    , true, 20, "TIPO"                , 0, "left"  , "tipoBeneficiario"   , true, false),
            new Campo("pphBeneficiario"   , true, 20, "BENEFICIARIO INICIAL", 0, "left"  , "beneficiarioInicial", true, false),
            new Campo("cdRgBeneficiario"  , true, 20, "RG"                  , 0, "left"  , null                 , true, false),
            new Campo("nrCpfBeneficiario" , true, 20, "CPF"                 , 0, "left"  , null                 , true, false),
            new Campo("pphAgenciaBancaria", true, 20, "AGENCIA BANCÁRIA"    , 0, "left"  , "agenciaBancaria"    , true, false),
            new Campo("nrContaCorrente"   , true, 20, "CONTA CORRENTE"      , 0, "left"  , null                 , true, false),
            new Campo("dtObito"           , true, 20, "DATA DO ÓBITO"       , 0, "left"  , "data"               , true, false),
            new Campo("id"                , true, 20, "ENDEREÇO"            , 0, "left"  , "endereco"           , true, false),
            
        };
        super.addColumns(estrutura);
        super.setNome("Beneficiario");
        super.setBotaoEditar(true);
        super.setBotaoDetalhar(true);
        super.setBotaoExcluir(true);
        super.setActionEditar("/view/beneficiario/manter.jsf?faces-redirect=true");
        super.setActionDetalhar("/view/beneficiario/detalhar.jsf?faces-redirect=true");
        
        super.getRelatorioPadrao().put("campos", "nmBeneficiario;dtNascimento;nmMae");
        super.getRelatorioPadrao().put("titulos", "Beneficiário;Nascimento;Nome da Mãe");
        super.getRelatorioPadrao().put("larguras", "100;30;100");
        super.getRelatorioPadrao().put("cabecalho1", "Pensão Hansenianos");
        super.getRelatorioPadrao().put("cabecalho2", "Listagem de Beneficiarios");
        super.getRelatorioPadrao().put("cabecalho3", "SIPPH");
        
        super.setTextoHintCsv("Arquivo CSV (Ctrl + Alt + S)");
        super.setTextoHintPdf("Arquivo PDF (Ctrl + Alt + F)");
        super.setTextoHintXls("Excel (Ctrl + Alt + H)");
        super.setTextoHintXml("Arquivo XML (Ctrl + Alt + M)");
    }

    public Object endereco(Object informacao, Tipo tipo){
        PphEnderecoEntity end = new PphEnderecoEntity();
        end.setPphBeneficiario(new PphBeneficiarioEntity((Long) informacao));
        List<PphEnderecoEntity> enders = new ArrayList<PphEnderecoEntity>();
        try {
            enders = enderecoBC.listar(end,end);
        } catch (BCException ex) {
            LOGGER.info("Erro ao carregar endereço na grade: {}", ex.toString());
        }

        String ender = "";
        if (enders.size() > 0) {
            String rua    = ((enders.get(0).getTpEndereco() == null ? "" : enders.get(0).getTpEndereco()) + " " + 
                             (enders.get(0).getDsEndereco() == null ? "" : enders.get(0).getDsEndereco() )).trim();
            String numero = (enders.get(0).getNrEndereco() == null ? "" : (enders.get(0).getNrEndereco().trim().equals("") ? "" : ", " + enders.get(0).getNrEndereco()));
            String compl  = (enders.get(0).getDsComplEndereco() == null ? "" : " " + enders.get(0).getDsComplEndereco());
            String bairro = (enders.get(0).getNmBairro() == null ? "" : " - " + enders.get(0).getNmBairro());
            String cep    = (enders.get(0).getCdCep() == null ? "" : (enders.get(0).getCdCep().trim().equals("") ? "" : " CEP: " + enders.get(0).getCdCep()));
            String cidade = "";
            if (enders.get(0).getPphMunicipio() != null) {
                cidade = " - " + ((enders.get(0).getPphMunicipio().getNmMunicipio() == null ? "" : enders.get(0).getPphMunicipio().getNmMunicipio()) + "/" + 
                               (enders.get(0).getPphMunicipio().getPphEstado() == null ? "" : enders.get(0).getPphMunicipio().getPphEstado().getCdUf())).trim();
            }
            ender = (((((rua + numero).trim() + compl).trim() + bairro).trim() + cep).trim() + cidade).trim();
        }
        switch (tipo) {
            case HINT:
                return ender;
            case SORT:
                return ender;
            case GRID:
                return ender;
        }
        return informacao;
    }
    
    public Object agenciaBancaria(Object informacao, Tipo tipo){
        String banco = ((PphAgenciaBancariaEntity) informacao).getPphBanco().getNmBanco() + 
                " - Agência: " + ((PphAgenciaBancariaEntity) informacao).getNrAgencia() +
                " / " + ((PphAgenciaBancariaEntity) informacao).getNmAgBancaria() +
                " - Município: " + ((PphAgenciaBancariaEntity) informacao).getPphMunicipio().getNmMunicipio();
        switch (tipo) {
            case HINT:
                return banco;
            case SORT:
                return banco;
            case GRID:
                return banco;
        }
        return informacao;
    }
    
    public Object beneficiarioInicial(Object informacao, Tipo tipo){
        switch (tipo) {
            case HINT:
                return ((PphBeneficiarioEntity) informacao).getNmBeneficiario();
            case SORT:
                return ((PphBeneficiarioEntity) informacao).getNmBeneficiario();
            case GRID:
                return ((PphBeneficiarioEntity) informacao).getNmBeneficiario();
        }
        return informacao;
    }
    
    public Object tipoBeneficiario(Object informacao, Tipo tipo){
        switch (tipo) {
            case HINT:
                return ((String) informacao).equals("1") ? "Inicial" : "Dependente";
            case SORT:
                return ((String) informacao).equals("1") ? "Inicial" : "Dependente";
            case GRID:
                return ((String) informacao).equals("1") ? "Inicial" : "Dependente";
        }
        return informacao;
    }
    
    public Object cnes(Object informacao, Tipo tipo){
        switch (tipo) {
            case HINT:
                return ((PphUnidSaudeEntity) informacao).getCnes();
            case SORT:
                return ((PphUnidSaudeEntity) informacao).getCnes();
            case GRID:
                return ((PphUnidSaudeEntity) informacao).getCnes();
        }
        return informacao;
    }
    
    public Object unidadeSaude(Object informacao, Tipo tipo){
        switch (tipo) {
            case HINT:
                return ((PphUnidSaudeEntity) informacao).getNome();
            case SORT:
                return ((PphUnidSaudeEntity) informacao).getNome();
            case GRID:
                return ((PphUnidSaudeEntity) informacao).getNome();
        }
        return informacao;
    }
    
    public Object unidadePagadora(Object informacao, Tipo tipo){
        switch (tipo) {
            case HINT:
                return ((PphUnidadePagadoraEntity) informacao).getNmUnidadePagadora();
            case SORT:
                return ((PphUnidadePagadoraEntity) informacao).getNmUnidadePagadora();
            case GRID:
                return ((PphUnidadePagadoraEntity) informacao).getNmUnidadePagadora();
        }
        return informacao;
    }
    
    @Override
    public void selecionar(Object mBean, Object entidade) throws BCException {
        LOGGER.debug(""+entidade);
        LOGGER.debug(""+mBean);
    }

    @Override
    public void detalhar(BeneficiarioMBean mBean, PphBeneficiarioEntity entidade, Object aux) throws BCException {
        LOGGER.debug(""+entidade);
        LOGGER.debug(""+mBean);
        mBean.gravaMemoria();
        mBean.setItem(entidade);
        
        if (aux != null) {
            PphEmpenhoEntity empenho = new PphEmpenhoEntity();
            empenho.setPphBeneficiario(new PphBeneficiarioEntity(mBean.getItem().getId()));
            ((EmpenhoMBean) aux).setLista(empenhoBC.listar(empenho, empenho));
            ((EmpenhoMBean) aux).setActionVoltar("/view/beneficiario/detalhar.jsf?faces-redirect=true");
        }
    }

    @Override
    public void editar(BeneficiarioMBean mBean, PphBeneficiarioEntity entidade, Object aux) throws BCException {
        LOGGER.debug(""+entidade);
        LOGGER.debug(""+mBean);
        mBean.gravaMemoria();
        mBean.setItem(entidade);
        
        if (aux != null) {
            PphEmpenhoEntity empenho = new PphEmpenhoEntity();
            PphEmpenhoEntity empenho2 = new PphEmpenhoEntity();
            empenho.setPphBeneficiario(new PphBeneficiarioEntity(mBean.getItem().getId()));
            empenho2.setPphBeneficiario(new PphBeneficiarioEntity(mBean.getItem().getId()));
            empenho2.setFlExclusao(Confirmacao.NAO.getId());
            ((EmpenhoMBean) aux).setLista(empenhoBC.listar(empenho, empenho2));
            ((EmpenhoMBean) aux).setActionVoltar("/view/beneficiario/manter.jsf?faces-redirect=true");
        }
    }
    
    public void mostraBotoesEditarExcluir(boolean mostraBotoesEditarExcluir) {
        setBotaoEditar(mostraBotoesEditarExcluir);
        setBotaoExcluir(mostraBotoesEditarExcluir);
    }
}