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
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;

/**
 *
 * @author jwsouza
 */
@Entity
@Table(name = "PPH_UNIDADE_PAGADORA")
public class PphUnidadePagadoraEntity extends BaseEntity implements Comparable<PphUnidadePagadoraEntity> {
    
    @Size(max = 60)
    @Column(name = "NM_UNIDADE_PAGADORA")
    private String nmUnidadePagadora;
    @Size(max = 20)
    @Column(name = "CD_UNIDADE_PAGADORA")
    private String cdUnidadePagadora;
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
    @Column(name = "FL_ATIVO")
    private Integer flAtivo;
    @OneToMany(mappedBy = "pphUnidadePagadora", cascade = CascadeType.ALL)
    private Set<PphBeneficiarioEntity> pphBeneficiarioSet;
    @OneToOne(mappedBy = "pphUnidadePagadora")
    private InstituicaoEntity instituicao;

    public PphUnidadePagadoraEntity() {
    }

    public PphUnidadePagadoraEntity(Long id) {
        setId(id);
    }

    public PphUnidadePagadoraEntity(Long id, String nmUnidadePagadora) {
        this.nmUnidadePagadora = nmUnidadePagadora;
        setId(id);
    }

    public String getNmUnidadePagadora() {
        return nmUnidadePagadora;
    }

    public void setNmUnidadePagadora(String nmUnidadePagadora) {
        this.nmUnidadePagadora = nmUnidadePagadora;
    }

    public String getCdUnidadePagadora() {
        return cdUnidadePagadora;
    }

    public void setCdUnidadePagadora(String cdUnidadePagadora) {
        this.cdUnidadePagadora = cdUnidadePagadora;
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

    public Set<PphBeneficiarioEntity> getPphBeneficiarioSet() {
        return pphBeneficiarioSet;
    }

    public Integer getFlAtivo() {
        return flAtivo;
    }

    public void setFlAtivo(Integer flAtivo) {
        this.flAtivo = flAtivo;
    }

    public void setPphBeneficiarioSet(Set<PphBeneficiarioEntity> pphBeneficiarioSet) {
        this.pphBeneficiarioSet = pphBeneficiarioSet;
    }

    public InstituicaoEntity getInstituicao() {
        return instituicao;
    }

    public void setInstituicao(InstituicaoEntity instituicao) {
        this.instituicao = instituicao;
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
        if (!(object instanceof PphUnidadePagadoraEntity)) {
            return false;
        }
        PphUnidadePagadoraEntity other = (PphUnidadePagadoraEntity) object;
        if ((this.getId() == null && other.getId() != null) || (this.getId() != null && !this.getId().equals(other.getId()))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.tcc.relatorio.dominio.PphUnidadePagadora[ id=" + getId() + " ]";
    }

    @Override
    public int compareTo(PphUnidadePagadoraEntity o) {
        if (o != null) {
            if (o.getNmUnidadePagadora() != null) {
                return this.getNmUnidadePagadora().compareTo(o.getNmUnidadePagadora());
            } else {
                return -1;
            }
        } else {
            return 1;
        }
    }
    
}
