package org.tcc.relatorio.hammer.persistencia.dominio;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Transient;

/**
 * @author wdi_s
 */
public abstract class DomainObject<T extends Serializable> implements IDomainObject<T> {
    private static final long serialVersionUID = 1L;
    @Transient
    private List<String> msg = new ArrayList<String>();

    public DomainObject() {
    }

    public int hashCode() {
        byte hash = 0;
        int hash1 = hash + (this.getId() != null?this.getId().hashCode():0);
        return hash1;
    }

    public boolean equals(Object object) {
        if(object == null) {
            return false;
        } else if(object == this) {
            return true;
        } else if(!(object instanceof DomainObject)) {
            return false;
        } else {
            DomainObject other = (DomainObject)object;
            return this.getId() == null && other.getId() != null?false:(this.getId() != null && this.getId() != other.getId()?false:this.getId() == other.getId());
        }
    }

    public List<String> getMsg() {
        return this.msg;
    }

    public boolean isDirty() {
        return !this.msg.isEmpty();
    }
}
