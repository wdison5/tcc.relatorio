/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tcc.relatorio.dominio;

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
@Table(name = "PPH_ATESTADO_VIDA")
public class PphAtestadoVidaEntity extends BaseEntity implements IDadosUsuario, IDadosData, IExclusao {

    private static final long serialVersionUID = 1L;

    @Column(name = "DT_ATESTADO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtAtestado;
    @Size(max = 60)
    @Column(name = "NM_MEDICO_RESP")
    private String nmMedicoResp;
    @Size(max = 20)
    @Column(name = "CD_CRM")
    private String cdCrm;
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
    private Date dtObito;

    public PphAtestadoVidaEntity() {
    }

    public PphAtestadoVidaEntity(Long id) {
        setId(id);
    }

    public Date getDtAtestado() {
        return dtAtestado;
    }

    public void setDtAtestado(Date dtAtestado) {
        this.dtAtestado = dtAtestado;
    }

    public String getNmMedicoResp() {
        return nmMedicoResp;
    }

    public void setNmMedicoResp(String nmMedicoResp) {
        this.nmMedicoResp = nmMedicoResp;
    }

    public String getCdCrm() {
        return cdCrm;
    }

    public void setCdCrm(String cdCrm) {
        this.cdCrm = cdCrm;
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

    public Date getDtObito() {
        return dtObito;
    }

    public void setDtObito(Date dtObito) {
        this.dtObito = dtObito;
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
        if (!(object instanceof PphAtestadoVidaEntity)) {
            return false;
        }
        PphAtestadoVidaEntity other = (PphAtestadoVidaEntity) object;
        if ((this.getId() == null && other.getId() != null) || (this.getId() != null && !this.getId().equals(other.getId()))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "prodesp.sipph.dominio.PphAtestadoVida[ id=" + getId() + " ]";
    }

}
