/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tcc.relatorio.dominio;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
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
@Table(name = "INSTITUICAO")
public class InstituicaoEntity extends BaseEntity implements Comparable<InstituicaoEntity> {

    @Size(max = 2)
    @Column(name = "TP_UNID")
    private String tpUnid;
    @Column(name = "DH_INCLUSAO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dhInclusao;
    @Column(name = "ID_USER_INCL")
    private Long idUserIncl;
    @Column(name = "FL_EXCLUSAO")
    private Integer flExclusao;
    @JoinColumn(name = "ID_UNID_SAUDE", referencedColumnName = "ID")
    @OneToOne(fetch = FetchType.LAZY)
    private PphUnidSaudeEntity pphUnidSaude;
    @JoinColumn(name = "ID_UNID_PAGADORA", referencedColumnName = "ID")
    @OneToOne(fetch = FetchType.LAZY)
    private PphUnidadePagadoraEntity pphUnidadePagadora;

    public InstituicaoEntity() {
    }

    public InstituicaoEntity(Long id) {
        setId(id);
    }

    public String getTpUnid() {
        return tpUnid;
    }

    public void setTpUnid(String tpUnid) {
        this.tpUnid = tpUnid;
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

    public PphUnidSaudeEntity getPphUnidSaude() {
        return pphUnidSaude;
    }

    public void setPphUnidSaude(PphUnidSaudeEntity pphUnidSaude) {
        this.pphUnidSaude = pphUnidSaude;
    }

    public PphUnidadePagadoraEntity getPphUnidadePagadora() {
        return pphUnidadePagadora;
    }

    public void setPphUnidadePagadora(PphUnidadePagadoraEntity pphUnidadePagadora) {
        this.pphUnidadePagadora = pphUnidadePagadora;
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
        if (!(object instanceof InstituicaoEntity)) {
            return false;
        }
        InstituicaoEntity other = (InstituicaoEntity) object;
        if ((this.getId() == null && other.getId() != null) || (this.getId() != null && !(this.getId().longValue() == other.getId()))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "prodesp.sipph.dominio.Instituicao[ id=" + getId() + " ]";
    }

    @Override
    public int compareTo(InstituicaoEntity o) {
        int result = -1;
        if (o != null) {
            if (getTpUnid() == null && o.getTpUnid() == null) {
                result =  0;
            } else if (getTpUnid() != null && getTpUnid().equals(o.getTpUnid())) {
                if ("US".equals(getTpUnid())) {
                    result = getPphUnidSaude().compareTo(o.getPphUnidSaude());
                }
                if ("UP".equals(getTpUnid())) {
                    result = getPphUnidadePagadora().compareTo(o.getPphUnidadePagadora());
                }
            } else if (getTpUnid() != null && o.getTpUnid() != null) {
                result = getTpUnid().compareTo(o.getTpUnid());
            } else {
                return -1;
            }
        }else{
            result = 1;
        }
        
        return result;
    }
}
