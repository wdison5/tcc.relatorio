package org.tcc.relatorio.mbean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.context.FacesContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.Map;
import javax.faces.context.ExternalContext;
import org.tcc.relatorio.dominio.BaseEntity;
import static org.tcc.relatorio.mbean.ManterOp.ASSOCIAR;
import static org.tcc.relatorio.mbean.ManterOp.ATUALIZAR;
import static org.tcc.relatorio.mbean.ManterOp.INCLUIR;
import static org.tcc.relatorio.mbean.ManterOp.LISTAR;
import org.tcc.relatorio.negocio.validator.Validador;
import org.tcc.relatorio.util.ExportarArquivo;
import org.tcc.relatorio.util.FacesUtil;
import org.tcc.relatorio.util.LoggerUtil;
import org.tcc.relatorio.util.jasper.Reports;
import org.tcc.relatorio.hammer.persistencia.exception.BCException;

public abstract class BaseMBean<T extends BaseEntity> implements Serializable {

    private static final long serialVersionUID = 118231614034054149L;
    private static final Logger LOGGER = LoggerFactory.getLogger(BaseMBean.class);

    private Long id;
    private T item;
    private T itemMemoria;
    private List<T> lista;

    private String filtroVazio;
    private String ordem1;
    private String ordem2;
    private Long timeSort = (long) 0;
    private boolean exportar;
    private String complementoRelatorio = "";
    private boolean redirecionar = false;

    private String beanName;
    private Class<T> parameterClass;

    public BaseMBean() {
        LOGGER.debug("... BaseMBean()");
    }

    public void initialize(String bName) {
        try {
            beanName = bName;
            id = Long.valueOf(0);
            lista = new ArrayList<T>();
            item = parameterClass.newInstance();
            itemMemoria = parameterClass.newInstance();
            filtroVazio = "Nenhum registro encontrado";
        } catch (Exception ex) {
            LoggerUtil.error("Erro iniciando " + getDescricaoMBean(), ex, LOGGER, true);
        }
    }

    public abstract List<T> manter(ManterOp op) throws BCException;

    public void manter(String url, boolean forcar, boolean persistir) {
        if (persistir) {
            persistir(url, forcar);
        } else {
            atualizar(url, forcar);
        }
    }

    public void atualizar(String url, boolean forcar) {
        atualizar();
        redirecionar(url, forcar);
    }

    public void persistir(String url, boolean forcar) {
        persistir();
        redirecionar(url, forcar);
    }

    public void redirecionar(String url, boolean forcar) {
        if (forcar) {
            item.getMsg().clear();
        }
        if (item.getMsg().isEmpty()) {
            try {
                this.setRedirecionar(true);
                if (forcar) {
                    clonar(this.item, getItemMemoria());
                }
                FacesContext ctx = FacesContext.getCurrentInstance();
                ExternalContext extContext = ctx.getExternalContext();
                url = extContext.encodeActionURL(ctx.getApplication().getViewHandler().getActionURL(ctx, url));
                extContext.redirect(url);
            } catch (IOException ex) {
                LoggerUtil.error("Erro ao redirecionar para "+url, ex, LOGGER, true);
            }
        }
    }

    public void gravaMemoria() {
        try {
            this.id = Long.valueOf(0);
            this.itemMemoria = parameterClass.newInstance();
            clonar(this.itemMemoria, this.item);
            this.item = parameterClass.newInstance();
        } catch (Exception ex) {
            LoggerUtil.error("Erro interno: " + getDescricaoMBean(), ex, LOGGER, true);
        }
    }

    public void restauraMemoria() {
        try {
            if (this.item.getId() != null) {
                for (int i = 0; i < lista.size(); i++) {
                    if (lista.get(i).getId().equals(this.item.getId())) {
                        T it = parameterClass.newInstance();
                        clonar(it, item);
                        this.lista.set(i, it);
                    }
                }
                clonar(this.item, getItemMemoria());
            } else {
                this.item = parameterClass.newInstance();
                prepararTelaInclusao();
            }
        } catch (Exception ex) {
            LoggerUtil.error("Erro interno: " + getDescricaoMBean(), ex, LOGGER, true);
        }
    }

    /**
     * Adiciona um item.
     *
     */
    public void persistir() {
        LOGGER.debug(getDescricaoMBean());
        try {
            getItem().getMsg().clear();
            manter(INCLUIR);
            if (getItem().isDirty()) {
                FacesUtil.addError(getItem().getMsg());
            } else {
                FacesUtil.addInfo(getDescricaoMBean() + " criado com sucesso: " + getItem().getId());
//                this.prepararTelaInclusao();
                restauraMemoria();
                //FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
            }
        } catch (Exception ex) {
            LoggerUtil.error("Erro na criação do " + getDescricaoMBean() + ": " + getItem().getId(), ex, LOGGER, true);
            getItem().getMsg().add("Erro na criação do " + getDescricaoMBean() + ": " + getItem().getId());
        }
    }

    public void excluir() {
        LOGGER.debug(getDescricaoMBean());
        try {
            getItem().getMsg().clear();
            manter(ManterOp.EXCLUIR);
        } catch (Exception ex) {
            LoggerUtil.error("Erro na exclusão do " + getDescricaoMBean() + ": " + getItem().getId(), ex, LOGGER, true);
            getItem().getMsg().add("Erro na exclusão do " + getDescricaoMBean() + ": " + getItem().getId());
        }
        
        if(getItem().isOk()){
            FacesUtil.addInfo(FacesUtil.ID_COMPONENTE_CUSTOMIZADO, getDescricaoMBean() + " excluido com sucesso: " + getItem().getId());
            for (T lista1 : lista) {
                if(Validador.isEquals(lista1.getId(),getItem().getId())){
                    lista.remove(lista1);
                    break;
                }
            }
        }else{
            List<String> msg = new ArrayList<String>();
            msg.add("Erro ao excluir.");
            msg.add(getItem().getClass().getName()+".id : "+getItem().getId());
            msg.addAll(getItem().getMsg());
            FacesUtil.put(msg);
            FacesUtil.addError(FacesUtil.ID_COMPONENTE_CUSTOMIZADO, getItem().getMsg());
        }
    }

    /**
     * atualiza um item.
     *
     */
    public void atualizar() {
        LOGGER.debug(getDescricaoMBean());
        try {
            getItem().getMsg().clear();
            manter(ATUALIZAR);
            if (getItem().isDirty()) {
                FacesUtil.addError(getItem().getMsg());
            } else {
                FacesUtil.addInfo(getDescricaoMBean() + " atualizado com sucesso: " + getItem().getId());
            }
        } catch (Exception ex) {
            LoggerUtil.error("Erro na atualização do " + getDescricaoMBean() + ": " + getItem().getId(), ex, LOGGER, true);
        }
    }

    /**
     * atualiza um item.
     *
     */
    public void associar() {
        LOGGER.debug(getDescricaoMBean());
        try {
            getItem().getMsg().clear();
            manter(ASSOCIAR);
            if (getItem().isDirty()) {
                FacesUtil.addError(getItem().getMsg());
            } else {
                FacesUtil.addInfo("Associação " + getDescricaoMBean() + " realizada com sucesso: " + getItem().getId());
            }
        } catch (Exception ex) {
            LoggerUtil.error("Erro na associação do " + getDescricaoMBean() + ": " + getItem().getId(), ex, LOGGER, true);
        }
    }

    /**
     * Lista os itens por filtro.
     */
    public void listar() {
        LOGGER.debug(getDescricaoMBean());
        try {
            getLista().clear();
            setLista(manter(LISTAR));
        } catch (Exception ex) {
            LoggerUtil.error("Erro ao listar " + getDescricaoMBean(), ex, LOGGER, true);
        }
    }

    /**
     * Limpa os campos da tela.
     */
    public void reset() {
        LOGGER.info("reset {}", getItem().getId());
        try {
            id = 0l;
            lista = new ArrayList<T>();
            item = parameterClass.newInstance();
            itemMemoria = parameterClass.newInstance();
        } catch (Exception ex) {
            LoggerUtil.error("Erro limpando "+getDescricaoMBean(), ex, LOGGER, true);
        }
    }

//    /**
//     * Auto Completar o input do nome.
//     *
//     * @param query Parte do nome para buscar.
//     * @return List<String> Lista com os possivies nomes do auto complete.
//     */
//    public List<String> completar(String query) {
//        List<String> results = new ArrayList<String>();
//
//        try {
//            getItem().setNome(query);
//            List<T> list = manter(LISTAR);
//            logger.debug("completar().size: {}", list.size());
//            for (T  s : list) {
//                results.add(s.getNome());
//            }
//        } catch (Exception ex) {
//            logger.error("Erro interno: {}-{}", query, ex);
//            FacesContext.getCurrentInstance().addMessage(getDescricaoMBean() + "Form", new FacesMessage(FacesMessage.SEVERITY_ERROR,
//                    "Erro interno: " + getItem().getId()+ " : " + ex.getMessage(), ""));
//        }
//        return results;
//    }
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

        ExportarArquivo e = new ExportarArquivo();
        e.processaPDF(r, this.getComplementoRelatorio());
    }

    protected void clonar(Object clone, Object original) {
        if (original != null) {
            try {
                Method m[];
                for (int i = 1; i <= 2; i++) {
                    if (i == 1) {
                        m = original.getClass().getDeclaredMethods();
                    } else {
                        m = original.getClass().getSuperclass().getDeclaredMethods();
                    }
                    for (Method m1 : m) {
                        if (m1.getName().startsWith("set")) {
                            Class partypes[] = new Class[0];
                            Method meth = original.getClass().getMethod(m1.getName().replace("set", "get"), partypes);
                            Object arglist[] = new Object[1];
                            arglist[0] = meth.invoke(original);
                            m1.invoke(clone, arglist);
                        }
                    }
                }
            } catch (Exception ex) {
                LoggerUtil.info("Erro ao clonar", ex, LOGGER);
            }
        }
    }

    public abstract String compoeFiltro();

    public abstract String compoePeriodo();

    public abstract void prepararTelaInclusao();

    public String getComplementoRelatorio() {
        return complementoRelatorio;
    }

    public void setComplementoRelatorio(String complementoRelatorio) {
        this.complementoRelatorio = complementoRelatorio;
    }

    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Long id) {
        for (T t : lista) {
            if (t.getId().equals(id)) {
                item = t;
            }
        }
        this.id = id;
    }

    /**
     * @return the item
     */
    public T getItem() {
        return item;
    }

    /**
     * @param item the item to set
     */
    public void setItem(T item) {
        this.item = item;
    }

    /**
     * @return the lista
     */
    public List<T> getLista() {
        if (lista == null) {
            lista = new ArrayList<T>();
        }
        return lista;
    }

    /**
     * @param lista the lista to set
     */
    public void setLista(List<T> lista) {
        this.lista = lista;
    }

    /**
     * @return the descricaoMBean
     */
    public String getDescricaoMBean() {
        return beanName;
    }

    /**
     * @param descricaoMBean the descricaoMBean to set
     */
    public void setDescricaoMBean(String descricaoMBean) {
        this.beanName = descricaoMBean;
    }

    /**
     * @return the parameterClass
     */
    public Class<T> getParameterClass() {
        return parameterClass;
    }

    /**
     * @param parameterClass the parameterClass to set
     */
    public void setParameterClass(Class<T> parameterClass) {
        this.parameterClass = parameterClass;
    }

    public String getFiltroVazio() {
        return filtroVazio;
    }

    public void setFiltroVazio(String filtroVazio) {
        this.filtroVazio = filtroVazio;
    }

    public String getOrdem1() {
        return ordem1;
    }

    public void setOrdem1(String ordem1) {
        this.ordem1 = ordem1;
    }

    public String getOrdem2() {
        return ordem2;
    }

    public void setOrdem2(String ordem2) {
        this.ordem2 = ordem2;
    }

    public void setOrdem(String ordem) {
        if ((new Date()).getTime() - timeSort > 1000) {
            this.ordem1 = ordem;
        } else {
            this.ordem2 = ordem;
        }
        timeSort = (new Date()).getTime();
        //logger.info("ordem ---> {} - o1: {} - o2: {}", ordem, this.ordem1, this.ordem2);
    }

    public boolean getExportar() {
        return exportar;
    }

    public void setExportar(boolean exportar) {
        this.exportar = exportar;
    }

    public boolean getRedirecionar() {
        return redirecionar;
    }

    public void setRedirecionar(boolean redirecionar) {
        this.redirecionar = redirecionar;
    }

    public T getItemMemoria() {
        return itemMemoria;
    }

    public void setItemMemoria(T itemMemoria) {
        clonar(this.itemMemoria, itemMemoria);
        //this.itemMemoria = itemMemoria;
    }
}
