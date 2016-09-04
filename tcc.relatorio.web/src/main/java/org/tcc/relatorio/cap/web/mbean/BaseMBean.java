/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tcc.relatorio.cap.web.mbean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import org.tcc.relatorio.cap.dominio.BaseEntity;
import static org.tcc.relatorio.cap.web.mbean.ManterOp.INCLUIR;
import static org.tcc.relatorio.cap.web.mbean.ManterOp.ATUALIZAR;
import static org.tcc.relatorio.cap.web.mbean.ManterOp.LISTAR;
import static org.tcc.relatorio.cap.web.mbean.ManterOp.ASSOCIAR;
import static org.tcc.relatorio.cap.web.mbean.ManterOp.EXCLUIR;
import org.tcc.relatorio.negocio.validator.Validador;
import org.tcc.relatorio.util.FacesUtil;
import org.tcc.relatorio.util.LoggerUtil;
import org.tcc.relatorio.hammer.persistencia.exception.BCException;

/**
 *
 * @author Roger
 */
public abstract class BaseMBean<T extends BaseEntity>implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 118231614034054149L;

	private static final Logger LOGGER = LoggerFactory.getLogger(BaseMBean.class);

    private Long id;

    private T item;
    private List<T> lista;
    
    private String beanName;
    private Class<T> parameterClass;
    
    /**
     * Construtor default.
     */
    public BaseMBean() {
        LOGGER.debug("... BaseMBean()");
    }
    
    public void initialize(String bName) {
        try {
            beanName = bName;
            id = Long.valueOf(0);
            lista = new ArrayList<T>();
            item = parameterClass.newInstance();
        } catch (Exception ex) {
            LoggerUtil.error("Erro iniciando " + getDescricaoMBean(), ex, LOGGER, true);
        }
    }

    public abstract List<T> manter(ManterOp op) throws BCException;
    
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
                FacesUtil.addInfo(getDescricaoMBean() + " criado com sucesso: " + getItem().getNome());
            }
        } catch (Exception ex) {
            LoggerUtil.error("Erro na criação do " + getDescricaoMBean() + ": " + getItem().getNome(), ex, LOGGER, true);
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
                FacesUtil.addInfo(getDescricaoMBean() + " atualizado com sucesso: " + getItem().getNome());
            }
        } catch (Exception ex) {
            LoggerUtil.error("Erro na atualização do " + getDescricaoMBean() + ": " + getItem().getNome(), ex, LOGGER, true);
        }
    }
    
    public void excluir() {
        LOGGER.debug(getDescricaoMBean());
        try {
            getItem().getMsg().clear();
            manter(EXCLUIR);
        } catch (Exception ex) {
            getItem().getMsg().add("Erro na exclusão do " + getDescricaoMBean() + ": " + getItem().getId() + " : " + ex.getMessage());
            LoggerUtil.error("Erro na exclusão do " + getDescricaoMBean() + ": " + getItem().getNome(), ex, LOGGER, true);
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
    public void associar() {
        LOGGER.debug(getDescricaoMBean());
        try {
            getItem().getMsg().clear();
            manter(ASSOCIAR);
            if (getItem().isDirty()) {
                FacesUtil.addError(getItem().getMsg());
            } else {
                FacesUtil.addInfo("Associação " + getDescricaoMBean() + " realizada com sucesso: " + getItem().getNome());
            }
        } catch (Exception ex) {
            LoggerUtil.error("Erro na Associação do(a) " + getDescricaoMBean() + ": " + getItem().getNome(), ex, LOGGER, true);
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
        LOGGER.info("reset {}", getItem().getNome());
        try {
            id = Long.valueOf(0l);
            lista = new ArrayList<T>();
            item = parameterClass.newInstance();
        } catch (Exception ex) {
            LoggerUtil.error("Erro limpando " + getDescricaoMBean(), ex, LOGGER, true);
        }
    }

    /**
     * Auto Completar o input do nome.
     *
     * @param query Parte do nome para buscar.
     * @return List<String> Lista com os possivies nomes do auto complete.
     */
    public List<String> completar(String query) {
        List<String> results = new ArrayList<String>();

        try {
            getItem().setNome(query);
            List<T> list = manter(LISTAR);
            LOGGER.debug("completar().size: {}", list.size());
            for (T  s : list) {
                results.add(s.getNome());
            }
        } catch (Exception ex) {
            LoggerUtil.error("Error ao completar parametros para listar " + getDescricaoMBean(), ex, LOGGER, true);
        }
        return results;
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
        for(T t : lista) {
            if(t.getId().equals(id)) {
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
        if(lista==null){
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
}
