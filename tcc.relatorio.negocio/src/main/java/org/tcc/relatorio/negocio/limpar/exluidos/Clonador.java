package org.tcc.relatorio.negocio.limpar.exluidos;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tcc.relatorio.negocio.reflection.ReflectionException;
import org.tcc.relatorio.negocio.reflection.ReflectionUtil;
import org.tcc.relatorio.negocio.validator.Validador;

public class Clonador {

    private static final ReflectionUtil INVOCA = new ReflectionUtil();
    private static final Logger LOGGER = LoggerFactory.getLogger(Clonador.class);

    public Object clonar(Collection colecaoOrigem) {
        Collection colecaoResult = null;
        if (colecaoOrigem != null) {
            if (colecaoOrigem instanceof List) {
                colecaoResult = new ArrayList();
            } else {
                colecaoResult = new HashSet();
            }
            for (Object obj : colecaoOrigem) {
                Object value = clonar(obj);
                if (!Validador.isNull(value)) {
                    colecaoResult.add(obj);
                }
            }
        }
        return colecaoResult;
    }

    public Object clonar(Object origem) {
        Object clone = null;
        if (!Validador.isNull(origem)) {
            if (origem instanceof List || origem instanceof Set) {
                clone = clonar((Collection) origem);
            } else {
                try {
                    clone = origem.getClass().newInstance();
                    INVOCA.clonar(origem, clone);
                } catch (Exception ex) {
                    LOGGER.info("Não conseguiu clonar o obj: {}, devolvendo o mesmo obj.", origem.getClass().getName());
                    LOGGER.debug("ERROR: Não conseguiu clonar o obj: {}. {}", origem.getClass().getName(), ex);
                    clone = origem;
                }
            }
        }
        return clone;
    }

    public Object clonar(Object entity, List<String> lstParamEntityToClear) {
        if (!Validador.isColecao(lstParamEntityToClear)) {
            lstParamEntityToClear = new ArrayList<String>();
            lstParamEntityToClear.add("this");
        }
        String metodoCorrente = null;
        try {
            for (String completeParam : lstParamEntityToClear) {
                String[] paramSplit = completeParam.split("\\.");
                if (paramSplit.length == 1) {
                    metodoCorrente = paramSplit[0];
                    if (Validador.isEquals(metodoCorrente, "this")) {
                        entity = clonar(entity);
                    } else {
                        Object objOld = INVOCA.invocaGet(entity, metodoCorrente);
                        if (!Validador.isNull(objOld)) {
                            Object objNew = clonar(objOld);
                            INVOCA.invocaSet(entity, metodoCorrente, objNew);
                        }
                    }
                } else {
                    Object objOldTmp = null, currentObj = entity;
                    for (int i = 0; i < paramSplit.length; i++) {
                        metodoCorrente = paramSplit[i];
                        if (Validador.isEquals(metodoCorrente, "this")) {
                            continue;
                        }
                        objOldTmp = INVOCA.invocaGet(currentObj, metodoCorrente);
                        if (Validador.isNull(objOldTmp)) {
                            break;
                        } else if (Validador.isColecao(objOldTmp) && (i != (paramSplit.length - 1))) {
                            String pathFieldToClone = "";
                            for (int j = i; j < paramSplit.length; j++) {
                                pathFieldToClone += paramSplit[j];
                            }
                            ArrayList<String> tmpLstPathFieldToClone = new ArrayList<String>();
                            tmpLstPathFieldToClone.add(pathFieldToClone);
                            Object lstResult = clonar((Collection) objOldTmp, tmpLstPathFieldToClone);
                            INVOCA.invocaSet(currentObj, metodoCorrente, lstResult);
                            break;
                        } else if (i == (paramSplit.length - 1)) {
                            Object objNew = clonar(objOldTmp);
                            INVOCA.invocaSet(currentObj, metodoCorrente, objNew);
                        } else {
                            currentObj = objOldTmp;
                        }
                    }
                }
            }
        } catch (ReflectionException ex) {
            LOGGER.debug("Não achou o metodo {}: {}", metodoCorrente, ex);
        }
        return entity;
    }

    public Object clonar(Collection colecaoEntity, List<String> lstPathFieldToClone) {
        Collection colecaoResult = null;
        if (colecaoEntity != null) {
            if (colecaoEntity instanceof List) {
                colecaoResult = new ArrayList();
            } else {
                colecaoResult = new HashSet();
            }
            for (Object obj : colecaoEntity) {
                Object value = clonar(obj, lstPathFieldToClone);
                if (!Validador.isNull(value)) {
                    colecaoResult.add(obj);
                }
            }
        }
        return colecaoResult;
    }
}
