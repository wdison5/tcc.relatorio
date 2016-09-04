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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "PPH_MUNICIPIO")
public class PphMunicipioEntity extends BaseEntity {
    private static final long serialVersionUID = 1L;
    
    @Column(name = "CD_IBGE")
    private Long cdIbge;
    @Size(max = 60)
    @Column(name = "NM_MUNICIPIO")
    private String nmMunicipio;
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
    @OneToMany(mappedBy = "pphMunicipio", cascade = CascadeType.ALL)
    private Set<PphAgenciaBancariaEntity> pphAgenciaBancariaSet;
    @OneToMany(mappedBy = "pphMunicipio", cascade = CascadeType.ALL)
    private Set<PphEnderecoEntity> pphEnderecoSet;
    @JoinColumn(name = "ID_ESTADO", referencedColumnName = "ID")
    @ManyToOne
    private PphEstadoEntity pphEstado;

    public PphMunicipioEntity() {
    }

    public PphMunicipioEntity(Long cdIbge) {
        this.cdIbge = cdIbge;
    }

    public Long getCdIbge() {
        return cdIbge;
    }

    public void setCdIbge(Long cdIbge) {
        this.cdIbge = cdIbge;
    }

    public String getNmMunicipio() {
        return nmMunicipio;
    }

    public void setNmMunicipio(String nmMunicipio) {
        this.nmMunicipio = nmMunicipio;
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

    public Set<PphAgenciaBancariaEntity> getPphAgenciaBancariaSet() {
        return pphAgenciaBancariaSet;
    }

    public void setPphAgenciaBancariaSet(Set<PphAgenciaBancariaEntity> pphAgenciaBancariaSet) {
        this.pphAgenciaBancariaSet = pphAgenciaBancariaSet;
    }

    public Set<PphEnderecoEntity> getPphEnderecoSet() {
        return pphEnderecoSet;
    }

    public void setPphEnderecoSet(Set<PphEnderecoEntity> pphEnderecoSet) {
        this.pphEnderecoSet = pphEnderecoSet;
    }

    public PphEstadoEntity getPphEstado() {
        return pphEstado;
    }

    public void setPphEstado(PphEstadoEntity pphEstado) {
        this.pphEstado = pphEstado;
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
        if (!(object instanceof PphMunicipioEntity)) {
            return false;
        }
        PphMunicipioEntity other = (PphMunicipioEntity) object;
        if ((this.getId() == null && other.getId() != null) || (this.getId() != null && !this.getId().equals(other.getId()))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.tcc.relatorio.dominio.PphMunicipio[ cdIbge=" + cdIbge + "; id=" + getId() + " ]";
    }
    
}
