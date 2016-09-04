package org.tcc.relatorio.negocio.limpar.exluidos;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tcc.relatorio.enumeracao.Confirmacao;
import org.tcc.relatorio.negocio.reflection.ReflectionException;
import org.tcc.relatorio.negocio.reflection.ReflectionUtil;
import org.tcc.relatorio.negocio.validator.Validador;

public class LimpadorExcluidos {

    private static final ReflectionUtil INVOCA = new ReflectionUtil();
    private static final Logger LOGGER = LoggerFactory.getLogger(LimpadorExcluidos.class);

    public Object limpar(List listEntity) {
        List tmp = new ArrayList();
        for (Object col : listEntity) {
            Object value = limpar(col);
            if (!Validador.isNull(value)) {
                tmp.add(col);
            }
        }
        return tmp;
    }

    public Object limpar(Set setEntity) {
        Set tmp = new HashSet();
        for (Object col : setEntity) {
            Object value = limpar(col);
            if (!Validador.isNull(value)) {
                tmp.add(col);
            }
        }
        return tmp;
    }

    public Object limpar(Object entity) {
        if (!Validador.isNull(entity)) {
            if (entity instanceof List) {
                limpar((List) entity);
            } else if (entity instanceof Set) {
                limpar((Set) entity);
            } else {
                try {
                    Number flExclusao = (Number) INVOCA.invocaGet(entity, "flExclusao");
                    if (Validador.isEquals(flExclusao, Confirmacao.SIM.getId())) {
                        entity = null;
                    }
                } catch (Exception ex) {
                    LOGGER.debug("Não achou o metodo getFlExclusao: {}", ex);
                }
            }
        }
        return entity;
    }

    public Object limpar(Object entity, List<String> lstParamEntityToClear) {
        if (Validador.isColecao(lstParamEntityToClear)) {
            Collections.sort(lstParamEntityToClear);
            String metodoCorrente = null;
            try {
                for (String completeParam : lstParamEntityToClear) {
                    String[] paramSplit = completeParam.split("\\.");
                    if (paramSplit.length == 1) {
                        metodoCorrente = paramSplit[0];
                        if (Validador.isEquals(metodoCorrente, "this")) {
                            Object newObject = limpar(entity);
                            if (Validador.isNull(newObject)) {
                                return null;
                            }
                        } else {
                            Object objValue = INVOCA.invocaGet(entity, metodoCorrente);
                            if (!Validador.isNull(objValue)) {
                                Object newObject = limpar(objValue);
                                INVOCA.invocaSet(entity, metodoCorrente, newObject);
                            }
                        }
                    } else {
                        Object objTmp = null, currentObj = entity;
                        for (int i = 0; i < paramSplit.length; i++) {
                            metodoCorrente = paramSplit[i];
                            objTmp = INVOCA.invocaGet(currentObj, metodoCorrente);
                            if (Validador.isNull(objTmp)) {
                                break;
                            } else if (Validador.isColecao(objTmp)) {
                                String pathToValid = "";
                                for (int j = i; j < paramSplit.length; j++) {
                                    pathToValid += paramSplit[j];
                                }
                                ArrayList<String> tmpListPathToValid = new ArrayList<String>();
                                tmpListPathToValid.add(pathToValid);
                                limpar(objTmp, tmpListPathToValid);
                                break;
                            } else if (i == (paramSplit.length - 1)) {
                                Object newObject = limpar(objTmp);
                                INVOCA.invocaSet(currentObj, metodoCorrente, newObject);
                            } else {
                                currentObj = objTmp;
                            }
                        }
                    }
                }
            } catch (ReflectionException ex) {
                LOGGER.debug("Não achou o metodo {}: {}", metodoCorrente, ex);
                java.util.logging.Logger.getLogger(LimpadorExcluidos.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return entity;
    }

    public List limpar(List lstEntity, List<String> lstparamEntityToClear) {
        List tmp = new ArrayList();
        for (Object entity : lstEntity) {
            Object value = limpar(entity, lstparamEntityToClear);
            if (!Validador.isNull(value)) {
                tmp.add(entity);
            }
        }
        return tmp;
    }

    public Set limpar(Set setEntity, List<String> lstparamEntityToClear) {
        Set tmp = new HashSet();
        for (Object entity : setEntity) {
            Object value = limpar(entity, lstparamEntityToClear);
            if (!Validador.isNull(value)) {
                tmp.add(entity);
            }
        }
        return tmp;
    }

}
