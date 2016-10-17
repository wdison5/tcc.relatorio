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
import org.tcc.relatorio.dominio.EmpresaEntity;
import org.tcc.relatorio.negocio.EmpresaBC;
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

    @EJB(name = "EmpresaBC")
    private EmpresaBC empresaBC;

    private Long empresa;
    private String nomeEmpresaSelecionada;
    
    private List<EmpresaEntity> empresas;
    private List<Long> empresasSelec;
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
            empresas = empresaBC.list(userId);

            if (Validador.isColecao(empresas)) {
                Collections.sort(empresas, new Comparator<EmpresaEntity>() {
                    @Override
                    public int compare(EmpresaEntity o1, EmpresaEntity o2) {
                        return o1 != null ? o1.compareTo(o2) : -1;
                    }
                });
            }
            empresasSelec = new ArrayList<Long>();
        } catch (Exception e) {
            LoggerUtil.error("Erro iniciando " + getDescricaoMBean(), e, LOGGER, true);
        }
    }

    @Override
    public List<UsuarioEntity> manter(ManterOp op) throws BCException {
        switch (op) {
            case INCLUIR:
                preparaEmpresas();
                usr.incluir(getItem());
                break;
            case ATUALIZAR:
                preparaEmpresas();
                usr.atualizar(getItem());
                break;
            case EXCLUIR:
                usr.excluir(getItem());
                break;
            case LISTAR:
                Set<UsuarioEntity> set = usr.listar(getItem(), (List<Long>) Funcoes.getSessionObject("lstEmpresaId"), usuarioLogado);
                if (set != null) {
                    return new ArrayList<UsuarioEntity>(set);
                }
        }
        return null;
    }

    private void preparaEmpresas() {
        if (Validador.isColecao(empresasSelec)) {
            List<EmpresaEntity> listaEmpresaSelecionada = new ArrayList<EmpresaEntity>();
            for (EmpresaEntity instit : empresas) {
                for (Long instSele : empresasSelec) {
                    if (instSele.longValue() == instit.getId()) {
                        listaEmpresaSelecionada.add(instit);
                    }
                }
            }
            getItem().setEmpresas(listaEmpresaSelecionada);
        } else if (empresas.size() == 1) {
            getItem().setEmpresas(empresas);
        }
    }

    @Override
    public List<String> completar(String query) {
        List<String> results = new ArrayList<String>();

        UsuarioEntity user = new UsuarioEntity();
        try {
            user.setUserId(query);
            Set<UsuarioEntity> list = usr.listar(user, (List<Long>) Funcoes.getSessionObject("lstEmpresaId"), usuarioLogado);
            LOGGER.debug("completar().size: {}", list.size());
            for (UsuarioEntity s : list) {
                results.add(s.getUserId());
            }
        } catch (Exception ex) {
            LoggerUtil.error("Erro ao listar " + getDescricaoMBean() + ": " + getItem().getNome() + ": " + query, ex, LOGGER, true);
        }
        return results;
    }

    public Long getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Long empresa) {
        this.empresa = empresa;
    }

    public List<EmpresaEntity> getEmpresas() {
        return empresas;
    }

    public String getNomeEmpresaSelecionada() {
        return nomeEmpresaSelecionada;
    }

    public void setNomeEmpresaSelecionada(String nomeEmpresaSelecionada) {
        this.nomeEmpresaSelecionada = nomeEmpresaSelecionada;
    }

    public boolean isTodas() {
        return todas;
    }

    public void setTodas(boolean todas) {
        this.todas = todas;
    }

    public List<Long> getEmpresasSelec() {
        return empresasSelec;
    }

    public void setEmpresasSelec(List empresasSelec) {
        this.empresasSelec.clear();
        if (Validador.isColecao(empresasSelec)) {
            Object objValue = empresasSelec.get(0);
            if (objValue instanceof Long) {
                this.empresasSelec = empresasSelec;
            } else if (objValue instanceof String) {
                for (int i = empresasSelec.size(); i > 0; i--) {
                    this.empresasSelec.add(new Long((String) empresasSelec.get(i - 1)));
                }
            }
        }
    }

    public String labelEmpresa(EmpresaEntity institu) {
        String result = "";
        if (institu != null) {
            result = institu.getCdEmpresa() + " - " + institu.getNmEmpresa();
        }
        return result;
    }

    public void toggleEmpresas(AjaxBehaviorEvent event) {
        empresasSelec.clear();
        if (todas) {
            for (EmpresaEntity empresa : empresas) {
                empresasSelec.add(empresa.getId());
            }
        }
        LOGGER.debug("Sentando o usuario para " + (todas ? "todas as empresas." : "nenhuma empresa."));
    }

    public void selectedItemsChanged(ValueChangeEvent event) {
        List oldValue = (List) event.getOldValue();
        List newValue = (List) event.getNewValue();
        boolean oldTodas = todas;
        if (Validador.isColecao(newValue)) {
            setEmpresasSelec(newValue);
        }
        todas = empresasSelec.size() == empresas.size();
        if (oldTodas != todas) {
            FacesUtil.update("todas");
        }
    }

    @Override
    public void setId(Long id) {
        super.setId(id);
        try {
            List<EmpresaEntity> lstEmpresa = empresaBC.list(getItem().getUserId());
            empresasSelec.clear();
            if (Validador.isColecao(lstEmpresa)) {
                for (EmpresaEntity instit : lstEmpresa) {
                    empresasSelec.add(instit.getId());
                }
                if (lstEmpresa.size() == 1) {
                    nomeEmpresaSelecionada = labelEmpresa(lstEmpresa.get(0));
                }
            }

            getItem().setEmpresas(lstEmpresa);

            if (empresasSelec.size() == empresas.size()) {
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
