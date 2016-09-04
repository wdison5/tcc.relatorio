/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tcc.relatorio.cap.web.mbean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import javax.annotation.PostConstruct;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.ValueChangeEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tcc.relatorio.cap.dominio.UsuarioEntity;
import org.tcc.relatorio.cap.negocio.UsuarioBC;
import org.tcc.relatorio.dominio.InstituicaoEntity;
import org.tcc.relatorio.dominio.PphUnidSaudeEntity;
import org.tcc.relatorio.dominio.PphUnidadePagadoraEntity;
import org.tcc.relatorio.negocio.InstituicaoBC;
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
@ManagedBean(name = "usuarioMBean")
public class UsuarioMBean extends BaseMBean<UsuarioEntity> implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 118231614034054149L;

    private static final Logger LOGGER = LoggerFactory.getLogger(UsuarioMBean.class);

    @EJB(name = "UsuarioBC")
    private UsuarioBC usr;

    @EJB(name = "InstituicaoBC")
    private InstituicaoBC instituicaoBC;

    private Long unidSaude;
    private Long instituicao;
    private Long unidPagadora;
    private String nomeUnidSaudeSelecionada;
    private String nomeInstituicaoSelecionada;

    private List<PphUnidSaudeEntity> unidSaudes;
    private List<PphUnidadePagadoraEntity> unidPagadoras;
    private List<InstituicaoEntity> instituicoes;
    private List<Long> instituicoesSelec;
    private boolean exporta = false;
    private boolean todas = false;
    private UsuarioEntity usuarioLogado;

    /**
     * Construtor default.
     */
    public UsuarioMBean() {

        setParameterClass(UsuarioEntity.class);
        super.initialize("usuario");
        LOGGER.debug(getDescricaoMBean());

    }

    @PostConstruct
    public void initialize() {

        try {

            this.usuarioLogado = (UsuarioEntity) Funcoes.getSessionObject("usuarioLogado");
            String userId = null;
            if (usuarioLogado != null) {
                userId = usuarioLogado.getUserId();
            }
            instituicoes = instituicaoBC.listComUSaudUPaga(userId);
            unidSaudes = new ArrayList<PphUnidSaudeEntity>();
            unidPagadoras = new ArrayList<PphUnidadePagadoraEntity>();

            if (Validador.isColecao(instituicoes)) {
                Collections.sort(instituicoes, new Comparator<InstituicaoEntity>() {
                    @Override
                    public int compare(InstituicaoEntity o1, InstituicaoEntity o2) {
                        return o1 != null ? o1.compareTo(o2) : -1;
                    }
                });

                for (InstituicaoEntity instituicao : instituicoes) {
                    if (instituicao.getPphUnidSaude() != null && instituicao.getPphUnidSaude().getId() != null) {
                        unidSaudes.add(instituicao.getPphUnidSaude());
                    }
                    if (instituicao.getPphUnidadePagadora() != null && instituicao.getPphUnidadePagadora().getId() != null) {
                        unidPagadoras.add(instituicao.getPphUnidadePagadora());
                    }
                }
            }
            instituicoesSelec = new ArrayList<Long>();
        } catch (Exception e) {
            LoggerUtil.error("Erro iniciando " + getDescricaoMBean(), e, LOGGER, true);
        }
    }

    @Override
    public List<UsuarioEntity> manter(ManterOp op) throws BCException {
        switch (op) {
            case INCLUIR:
                preparaInstituicoes();
                usr.incluir(getItem());
                break;
            case ATUALIZAR:
                preparaInstituicoes();
                usr.atualizar(getItem());
                break;
            case EXCLUIR:
                usr.excluir(getItem());
                break;
            case LISTAR:
                Set<UsuarioEntity> set = usr.listar(getItem(), (List<Long>) Funcoes.getSessionObject("lstInstituicaoId"), usuarioLogado);
                if (set != null) {
                    return new ArrayList<UsuarioEntity>(set);
                }
        }
        return null;
    }

    private void preparaInstituicoes() {
        if (Validador.isColecao(instituicoesSelec)) {
            List<InstituicaoEntity> listaInstituicaoSelecionada = new ArrayList<InstituicaoEntity>();
            for (InstituicaoEntity instit : instituicoes) {
                for (Long instSele : instituicoesSelec) {
                    if (instSele.longValue() == instit.getId()) {
                        listaInstituicaoSelecionada.add(instit);
                    }
                }
            }
            getItem().setInstituicoes(listaInstituicaoSelecionada);
        }else if(instituicoes.size() == 1){
            getItem().setInstituicoes(instituicoes);
        }
    }

    @Override
    public List<String> completar(String query) {
        List<String> results = new ArrayList<String>();

        UsuarioEntity user = new UsuarioEntity();
        try {
            user.setUserId(query);
            Set<UsuarioEntity> list = usr.listar(user, (List<Long>) Funcoes.getSessionObject("lstInstituicaoId"), usuarioLogado);
            LOGGER.debug("completar().size: {}", list.size());
            for (UsuarioEntity s : list) {
                results.add(s.getUserId());
            }
        } catch (Exception ex) {
            LoggerUtil.error("Erro ao listar " + getDescricaoMBean() + ": " + getItem().getNome()+": "+query , ex, LOGGER, true);
        }
        return results;
    }

    public Long getUnidSaude() {
        return unidSaude;
    }

    public void setUnidSaude(Long unidSaude) {
        this.unidSaude = unidSaude;
    }

    public Long getInstituicao() {
        return instituicao;
    }

    public void setInstituicao(Long instituicao) {
        this.instituicao = instituicao;
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

    public List<InstituicaoEntity> getInstituicoes() {
        return instituicoes;
    }

    public List<PphUnidadePagadoraEntity> getUnidPagadoras() {
        return unidPagadoras;
    }

    public PphUnidSaudeEntity[] getUnidSaudesArray() {
        return unidSaudes != null ? unidSaudes.toArray(new PphUnidSaudeEntity[unidSaudes.size()]) : null;
    }

    public String getNomeUnidSaudeSelecionada() {
        return nomeUnidSaudeSelecionada;
    }

    public void setNomeUnidSaudeSelecionada(String nomeUnidSaudeSelecionada) {
        this.nomeUnidSaudeSelecionada = nomeUnidSaudeSelecionada;
    }

    public String getNomeInstituicaoSelecionada() {
        return nomeInstituicaoSelecionada;
    }

    public void setNomeInstituicaoSelecionada(String nomeInstituicaoSelecionada) {
        this.nomeInstituicaoSelecionada = nomeInstituicaoSelecionada;
    }

    public boolean isTodas() {
        return todas;
    }

    public void setTodas(boolean todas) {
        this.todas = todas;
    }

    public List<Long> getInstituicoesSelec() {
        return instituicoesSelec;
    }

    public void setInstituicoesSelec(List instituicoesSelec) {
        this.instituicoesSelec.clear();
        if (Validador.isColecao(instituicoesSelec)) {
            Object objValue = instituicoesSelec.get(0);
            if (objValue instanceof Long) {
                this.instituicoesSelec = instituicoesSelec;
            } else if (objValue instanceof String) {
                for (int i = instituicoesSelec.size(); i > 0; i--) {
                    this.instituicoesSelec.add(new Long((String) instituicoesSelec.get(i - 1)));
                }
            }
        }
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

    public void toggleInstituicoes(AjaxBehaviorEvent event) {
        instituicoesSelec.clear();
        if (todas) {
            for (InstituicaoEntity instit : instituicoes) {
                instituicoesSelec.add(instit.getId());
            }
        }
        LOGGER.debug("Sentando o usuario para " + (todas ? "todas as instituições." : "nenhuma instituição."));
    }

    public void selectedItemsChanged(ValueChangeEvent event) {
        List oldValue = (List) event.getOldValue();
        List newValue = (List) event.getNewValue();
        boolean oldTodas = todas;
        if (Validador.isColecao(newValue)) {
            setInstituicoesSelec(newValue);
        }
        todas = instituicoesSelec.size() == instituicoes.size();
        if (oldTodas != todas) {
            FacesUtil.update("todas");
        }
    }

    @Override
    public void setId(Long id) {
        super.setId(id);
        try {
            List<InstituicaoEntity> lstInstituicao = instituicaoBC.listComUSaudUPaga(getItem().getUserId());
            instituicoesSelec.clear();
            if (Validador.isColecao(lstInstituicao)) {
                for (InstituicaoEntity instit : lstInstituicao) {
                    instituicoesSelec.add(instit.getId());
                    if (instit.getPphUnidSaude() != null && instit.getPphUnidSaude().getId() != null) {
                        LOGGER.debug(instit.getPphUnidSaude().getNome());
                    }
                    if (instit.getPphUnidadePagadora() != null && instit.getPphUnidadePagadora().getId() != null) {
                        LOGGER.debug(instit.getPphUnidadePagadora().getNmUnidadePagadora());
                    }
                }
                if (lstInstituicao.size() == 1) {
                    nomeInstituicaoSelecionada = labelInsituicao(lstInstituicao.get(0));
                }
            }

            getItem().setInstituicoes(lstInstituicao);

            if (instituicoesSelec.size() == instituicoes.size()) {
                todas = true;
            } else {
                todas = false;
            }
        } catch (Exception ex) {
            LoggerUtil.error("Erro interno: " + getDescricaoMBean() + ": " + getItem().getNome(), ex, LOGGER, true);
        }
    }

    @Override
    public Long getId() {
        return super.getId();
    }

    public void preparaExclusao(Long id) {
        super.setId(id);
    }
}
