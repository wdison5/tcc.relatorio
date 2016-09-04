package org.tcc.relatorio.mbean;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tcc.relatorio.cap.dominio.UsuarioEntity;
import org.tcc.relatorio.dominio.InstituicaoEntity;
import org.tcc.relatorio.dominio.PphUnidSaudeEntity;
import org.tcc.relatorio.dominio.PphUnidadePagadoraEntity;
import org.tcc.relatorio.enumeracao.Confirmacao;
import org.tcc.relatorio.negocio.InstituicaoBC;
import org.tcc.relatorio.negocio.UnidSaudeBC;
import org.tcc.relatorio.negocio.UnidadePagadoraBC;
import org.tcc.relatorio.negocio.validator.TipoUnidade;
import org.tcc.relatorio.negocio.validator.Validador;
import org.tcc.relatorio.util.FacesUtil;
import org.tcc.relatorio.util.Funcoes;
import org.tcc.relatorio.util.LoggerUtil;
import org.tcc.relatorio.hammer.persistencia.exception.BCException;

/**
 *
 * @author Roger
 */
@SessionScoped
@ManagedBean(name = "instituicaoMBean")
public class InstituicaoMBean extends BaseMBean<InstituicaoEntity> implements Serializable {

    private static final long serialVersionUID = 118231614034054149L;

    private static final Logger LOGGER = LoggerFactory.getLogger(InstituicaoMBean.class);

    @EJB(name = "UnidSaudeBC")
    private UnidSaudeBC unidSaudeBC;

    @EJB(name = "UnidadePagadoraBC")
    private UnidadePagadoraBC unidadePagadoraBC;

    @EJB(name = "InstituicaoBC")
    private InstituicaoBC instituicaoBC;

    private Long unidSaude;
    private Long unidPagadora;
    private String nomeUnidSaudeSelecionada;
    private String nomeUnidPagadoraSelecionada;
    private String codigoUnidSaude;
    private String codigoUnidPagadora;

    private List<PphUnidSaudeEntity> unidSaudes;
    private List<PphUnidadePagadoraEntity> unidPagadoras;
    private UsuarioEntity usuarioLogado;
    private long lastTimeTpUnidChange;

    /**
     * Construtor default.
     */
    public InstituicaoMBean() {
        setParameterClass(InstituicaoEntity.class);
        super.initialize("Instituição");
        LOGGER.debug(getDescricaoMBean());
    }

    @PostConstruct
    public void initialize() {
        try {
            usuarioLogado = (UsuarioEntity) FacesUtil.getInSession("usuarioLogado");

            lastTimeTpUnidChange = System.currentTimeMillis();
        } catch (Exception e) {
            LOGGER.error("Erro: {}", e);
            FacesUtil.put(e);
        }
    }

    @Override
    public List<InstituicaoEntity> manter(ManterOp op) throws BCException {
        List<InstituicaoEntity> result = null;
        switch (op) {
            case INCLUIR:
                getItem().setIdUserIncl(usuarioLogado.getId());
                getItem().setDhInclusao(new Date());
                if (Validador.isEquals(getItem().getTpUnid(), TipoUnidade.SAUDE.getCod())) {
                    for (PphUnidSaudeEntity unidSaudeTmp : unidSaudes) {
                        if (unidSaudeTmp.getId().longValue() == unidSaude) {
                            getItem().setPphUnidSaude(unidSaudeTmp);
                            break;
                        }
                    }
                } else if (Validador.isEquals(getItem().getTpUnid(), TipoUnidade.PAGADORA.getCod())) {
                    for (PphUnidadePagadoraEntity unidPagadoraTmp : unidPagadoras) {
                        if (unidPagadoraTmp.getId().longValue() == unidPagadora) {
                            getItem().setPphUnidadePagadora(unidPagadoraTmp);
                            break;
                        }
                    }
                }
                instituicaoBC.incluir(getItem(), usuarioLogado);
                if (getItem().isOk()) {
                    ((List<Long>) Funcoes.getSessionObject("lstInstituicaoId")).add(getItem().getId());
                    if (Validador.isEquals(getItem().getTpUnid(), TipoUnidade.SAUDE.getCod())) {
                        ((List<Long>) Funcoes.getSessionObject("lstUnidSaudeId")).add(getItem().getPphUnidSaude().getId());
                    } else if (Validador.isEquals(getItem().getTpUnid(), TipoUnidade.PAGADORA.getCod())) {
                        ((List<Long>) Funcoes.getSessionObject("lstUnidPagadoraId")).add(getItem().getPphUnidadePagadora().getId());
                    }
                }
                break;

            case EXCLUIR:
                instituicaoBC.excluir(getItem(), usuarioLogado, (List<Long>) FacesUtil.getInSession("lstInstituicaoId"));
                break;
            case ATUALIZAR:
                instituicaoBC.atualizar(getItem());
                break;

            case LISTAR:
                if (Validador.isEmpty(getItem().getTpUnid()) || "99".equals(getItem().getTpUnid())) {
                    result = instituicaoBC.listComUSaudUPaga(usuarioLogado.getUserId());
                } else if(TipoUnidade.PAGADORA.getCod().equals(getItem().getTpUnid())){
                    result = instituicaoBC.listByUPaga((List<Long>) Funcoes.getSessionObject("lstUnidPagadoraId"), getNomeUnidPagadoraSelecionada(), getCodigoUnidPagadora());
                } else if(TipoUnidade.SAUDE.getCod().equals(getItem().getTpUnid())){
                    result = instituicaoBC.listByUSaud((List<Long>) Funcoes.getSessionObject("lstUnidSaudeId"), getNomeUnidSaudeSelecionada(), getCodigoUnidSaude());
                }
        }
        return result;
    }

    public Long getUnidSaude() {
        return unidSaude;
    }

    public void setUnidSaude(Long unidSaude) {
        this.unidSaude = unidSaude;
    }

    public Long getUnidPagadora() {
        return unidPagadora;
    }

    public void setUnidPagadora(Long unidPagadora) {
        this.unidPagadora = unidPagadora;
    }

    public List<PphUnidSaudeEntity> getUnidSaudes() {
        return unidSaudes;
    }

    public List<PphUnidadePagadoraEntity> getUnidPagadoras() {
        return unidPagadoras;
    }

    public String getNomeUnidSaudeSelecionada() {
        return nomeUnidSaudeSelecionada;
    }

    public void setNomeUnidSaudeSelecionada(String nomeUnidSaudeSelecionada) {
        this.nomeUnidSaudeSelecionada = nomeUnidSaudeSelecionada;
    }

    public String getNomeUnidPagadoraSelecionada() {
        return nomeUnidPagadoraSelecionada;
    }

    public void setNomeUnidPagadoraSelecionada(String nomeUnidPagadoraSelecionada) {
        this.nomeUnidPagadoraSelecionada = nomeUnidPagadoraSelecionada;
    }

    public String getCodigoUnidSaude() {
        return codigoUnidSaude;
    }

    public void setCodigoUnidSaude(String codigoUnidSaude) {
        this.codigoUnidSaude = codigoUnidSaude;
    }

    public String getCodigoUnidPagadora() {
        return codigoUnidPagadora;
    }

    public void setCodigoUnidPagadora(String codigoUnidPagadora) {
        this.codigoUnidPagadora = codigoUnidPagadora;
    }

    public String labelInsituicao(InstituicaoEntity institu) {
        String result = "";
        if (institu != null) {
            if ("US".equals(institu.getTpUnid())) {
                result = "US(" + institu.getPphUnidSaude().getCnes() + ") - " + institu.getPphUnidSaude().getNome();
            }
            if ("UP".equals(institu.getTpUnid())) {
                result = "UP(" + institu.getPphUnidadePagadora().getCdUnidadePagadora() + ") - " + institu.getPphUnidadePagadora().getNmUnidadePagadora();
            }
        }
        return result;
    }

    public void onRadioTpUnidChange() {
        long minTime = 10 * 1000;//10 Segundos
        try {
            if (TipoUnidade.SAUDE.getCod().equals(getItem().getTpUnid())) {
                getItem().setPphUnidadePagadora(null);
                setUnidPagadora(null);

                if (!Validador.isColecao(unidSaudes) || (System.currentTimeMillis() - lastTimeTpUnidChange > minTime)) {
                    unidSaudes = unidSaudeBC.listAllNoCadInstit(Confirmacao.SIM.getId());
                    lastTimeTpUnidChange = System.currentTimeMillis();
                }

            } else if (TipoUnidade.PAGADORA.getCod().equals(getItem().getTpUnid())) {
                getItem().setPphUnidSaude(null);
                setUnidSaude(null);

                if (!Validador.isColecao(unidPagadoras) || (System.currentTimeMillis() - lastTimeTpUnidChange > minTime)) {
                    unidPagadoras = unidadePagadoraBC.listAllNoCadInstit(Confirmacao.SIM.getId());
                    lastTimeTpUnidChange = System.currentTimeMillis();
                }
            }
        } catch (Exception ex) {
            LoggerUtil.error("Erro ao mudar o tipo de unidade", ex, LOGGER, true);
        }
    }

    public String tituloTela() {
        if (this.getComplementoRelatorio().equals("Analitico")) {
            return "Relatório Analítico";
        }
        if (this.getComplementoRelatorio().equals("Sintetico")) {
            return "Relatório Sintético";
        }
        if (this.getComplementoRelatorio().equals("SQLLog")) {
            return "LOG - Instituição";
        }
        return "Consulta Instituição";
    }

    @Override
    public String compoeFiltro() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String compoePeriodo() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void prepararTelaInclusao() {
        getItem().setTpUnid("99");
        setCodigoUnidPagadora(null);
        setCodigoUnidSaude(null);
        setNomeUnidSaudeSelecionada(null);
        setNomeUnidPagadoraSelecionada(null);
    }

    public void inicializaRelatorio(String complemento) {
        this.setEstadoDefault();
        super.setComplementoRelatorio(complemento);
    }

    public void setEstadoDefault() {
        super.initialize("Empenho");
        super.setComplementoRelatorio("");
    }

    @Override
    public Long getId() {
        return super.getId();
    }

    @Override
    public void setId(Long id) {
        super.setId(id);
        setItem(getItem());
    }

    @Override
    public InstituicaoEntity getItem() {
        return super.getItem();
    }

    @Override
    public void setItem(InstituicaoEntity item) {
        super.setItem(item);
        if ("US".equals(getItem().getTpUnid())) {
            setNomeUnidSaudeSelecionada(getItem().getPphUnidSaude().getCnes() + " - " + getItem().getPphUnidSaude().getNome());
        }
        if ("UP".equals(getItem().getTpUnid())) {
            setNomeUnidPagadoraSelecionada(getItem().getPphUnidadePagadora().getCdUnidadePagadora() + " - " + getItem().getPphUnidadePagadora().getNmUnidadePagadora());
        }
    }

}
