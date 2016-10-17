/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tcc.relatorio.dominio;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;

/**
 *
 * @author jwsouza
 */
@Entity
@Table(name = "EMPRESA")
public class EmpresaEntity extends BaseEntity implements Comparable<EmpresaEntity> {
    @Size(max = 60)
    @Column(name = "NOME_EMPRESA")
    private String nmEmpresa;
    @Size(max = 20)
    @Column(name = "CD_EMPRESA")
    private String cdEmpresa;
    @Column(name = "DH_INCLUSAO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dhInclusao;
    @Column(name = "ID_USER_INCL")
    private Long idUserIncl;
    @Column(name = "FL_EXCLUSAO")
    private Integer flExclusao;

    public EmpresaEntity() {
    }

    public EmpresaEntity(Long id) {
        setId(id);
    }

    public String getNmEmpresa() {
        return nmEmpresa;
    }

    public void setNmEmpresa(String nmEmpresa) {
        this.nmEmpresa = nmEmpresa;
    }

    public String getCdEmpresa() {
        return cdEmpresa;
    }

    public void setCdEmpresa(String cdEmpresa) {
        this.cdEmpresa = cdEmpresa;
    }

    public Date getDhInclusao() {
        return dhInclusao;
    }

    public void setDhInclusao(Date dhInclusao) {
        this.dhInclusao = dhInclusao;
    }

    public Long getIdUserIncl() {
        return idUserIncl;
    }

    public void setIdUserIncl(Long idUserIncl) {
        this.idUserIncl = idUserIncl;
    }

    public Integer getFlExclusao() {
        return flExclusao;
    }

    public void setFlExclusao(Integer flExclusao) {
        this.flExclusao = flExclusao;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (getId() != null ? getId() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EmpresaEntity)) {
            return false;
        }
        EmpresaEntity other = (EmpresaEntity) object;
        if ((this.getId() == null && other.getId() != null) || (this.getId() != null && !(this.getId().longValue() == other.getId()))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return getClass().getName()+"[ id=" + getId() + " ]";
    }

    @Override
    public int compareTo(EmpresaEntity o) {
        if (o != null) {
            if (this.getNmEmpresa() != null) {
                return this.getNmEmpresa().compareTo(o.getNmEmpresa());
            } else if (this.getCdEmpresa()!= null) {
                return this.getCdEmpresa().compareTo(o.getCdEmpresa());
            } else {
                return -1;
            }
        }else{
            return 1;
        }
    }
}
