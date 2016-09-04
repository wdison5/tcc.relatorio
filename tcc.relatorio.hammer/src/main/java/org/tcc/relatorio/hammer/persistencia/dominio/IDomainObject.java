package org.tcc.relatorio.hammer.persistencia.dominio;

import java.io.Serializable;
import java.util.List;

/**
 * @author wdi_s
 */
public interface IDomainObject<T extends Serializable> extends Serializable {
    T getId();

    void setId(T var1);

    List<String> getMsg();

    boolean isDirty();
}
