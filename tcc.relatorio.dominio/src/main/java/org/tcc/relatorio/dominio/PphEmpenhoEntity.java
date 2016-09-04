/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tcc.relatorio.dominio;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.Size;
import org.tcc.relatorio.dominio.interfaces.IDadosData;
import org.tcc.relatorio.dominio.interfaces.IDadosUsuario;
import org.tcc.relatorio.dominio.interfaces.IExclusao;

/**
 *
 * @author jwsouza
 */
@Entity
@Table(name = "PPH_EMPENHO")
public class PphEmpenhoEntity extends BaseEntity implements IDadosUsuario, IDadosData, IExclusao{
    
    @Size(max = 20)
    @Column(name = "NR_EMPENHO")
    private String nrEmpenho;
    @Column(name = "DT_EMPENHO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtEmpenho;
    @Column(name = "VL_EMPENHO")
    private BigDecimal vlEmpenho;
    @Column(name = "FL_EXCLUSAO")
    private Integer flExclusao;
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
    @JoinColumn(name = "ID_BENEFICIARIO", referencedColumnName = "ID")
    @ManyToOne
    private PphBeneficiarioEntity pphBeneficiario;
    @Transient
    private Integer nrEmpenhoInt;

    public PphEmpenhoEntity() {
    }

    public PphEmpenhoEntity(Long id) {
        setId(id);
    }

    public String getNrEmpenho() {
        return nrEmpenho;
    }

    public void setNrEmpenho(String nrEmpenho) {
        this.nrEmpenho = nrEmpenho;
    }

    public Date getDtEmpenho() {
        return dtEmpenho;
    }

    public void setDtEmpenho(Date dtEmpenho) {
        this.dtEmpenho = dtEmpenho;
    }

    public BigDecimal getVlEmpenho() {
        return vlEmpenho;
    }

    public void setVlEmpenho(BigDecimal vlEmpenho) {
        this.vlEmpenho = vlEmpenho;
    }

    @Override
    public Integer getFlExclusao() {
        return flExclusao;
    }

    @Override
    public void setFlExclusao(Integer flExclusao) {
        this.flExclusao = flExclusao;
    }

    @Override
    public Date getDhInclusao() {
        return dhInclusao;
    }

    @Override
    public void setDhInclusao(Date dhInclusao) {
        this.dhInclusao = dhInclusao;
    }

    @Override
    public Long getIdUserIncl() {
        return idUserIncl;
    }

    @Override
    public void setIdUserIncl(Long idUserIncl) {
        this.idUserIncl = idUserIncl;
    }

    @Override
    public Date getDhAlteracao() {
        return dhAlteracao;
    }

    @Override
    public void setDhAlteracao(Date dhAlteracao) {
        this.dhAlteracao = dhAlteracao;
    }

    @Override
    public Long getIdUserAlt() {
        return idUserAlt;
    }

    @Override
    public void setIdUserAlt(Long idUserAlt) {
        this.idUserAlt = idUserAlt;
    }

    public PphBeneficiarioEntity getPphBeneficiario() {
        return pphBeneficiario;
    }

    public void setPphBeneficiario(PphBeneficiarioEntity pphBeneficiario) {
        this.pphBeneficiario = pphBeneficiario;
    }

    public Integer getNrEmpenhoInt() {
        nrEmpenhoInt = null;
        try {
            if(nrEmpenho!=null){
                nrEmpenhoInt = Integer.valueOf(nrEmpenho);
            }
        } catch (Exception e) {}
        
        return nrEmpenhoInt;
    }

    public void setNrEmpenhoInt(Integer nrEmpenhoInt) {
        nrEmpenho = (nrEmpenhoInt!=null)? nrEmpenhoInt.toString() : null;
        this.nrEmpenhoInt = nrEmpenhoInt;
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
        if (!(object instanceof PphEmpenhoEntity)) {
            return false;
        }
        PphEmpenhoEntity other = (PphEmpenhoEntity) object;
        if ((this.getId() == null && other.getId() != null) || (this.getId() != null && !this.getId().equals(other.getId()))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.tcc.relatorio.dominio.PphEmpenho[ id=" + getId() + " ]";
    }
    
}
