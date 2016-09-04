/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tcc.relatorio.dominio;

import java.util.Date;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;

/**
 *
 * @author jwsouza
 */
@Entity
@Table(name = "PPH_ESTADO")
public class PphEstadoEntity extends BaseEntity {
    private static final long serialVersionUID = 1L;
    
    @Size(max = 2)
    @Column(name = "CD_UF")
    private String cdUf;
    @Size(max = 60)
    @Column(name = "NM_ESTADO")
    private String nmEstado;
    @Column(name = "DH_INCLUSAO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dhInclusao;
    @Column(name = "ID_USER_INCL")
    private Long idUserIncl;
    @Column(name = "DH_ALTERACAO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dhAlteracao;
    @Column(name = "ID_USER_ALT")
    private Long idUserAlt;
    @OneToMany(mappedBy = "pphEstado", cascade = CascadeType.ALL)
    private Set<PphMunicipioEntity> pphMunicipioSet;

    public PphEstadoEntity() {
    }

    public PphEstadoEntity(String cdUf) {
        this.cdUf = cdUf;
    }

    public String getCdUf() {
        return cdUf;
    }

    public void setCdUf(String cdUf) {
        this.cdUf = cdUf;
    }

    public String getNmEstado() {
        return nmEstado;
    }

    public void setNmEstado(String nmEstado) {
        this.nmEstado = nmEstado;
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

    public Date getDhAlteracao() {
        return dhAlteracao;
    }

    public void setDhAlteracao(Date dhAlteracao) {
        this.dhAlteracao = dhAlteracao;
    }

    public Long getIdUserAlt() {
        return idUserAlt;
    }

    public void setIdUserAlt(Long idUserAlt) {
        this.idUserAlt = idUserAlt;
    }

    public Set<PphMunicipioEntity> getPphMunicipioSet() {
        return pphMunicipioSet;
    }

    public void setPphMunicipioSet(Set<PphMunicipioEntity> pphMunicipioSet) {
        this.pphMunicipioSet = pphMunicipioSet;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (getId() != null ? getId().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PphEstadoEntity)) {
            return false;
        }
        PphEstadoEntity other = (PphEstadoEntity) object;
        if ((this.getId() == null && other.getId() != null) || (this.getId() != null && !this.getId().equals(other.getId()))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "prodesp.sipph.dominio.PphEstado[ cdUf=" + cdUf + "; id=" + getId() + " ]";
    }
    
}
