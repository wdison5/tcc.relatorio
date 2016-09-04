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
import org.tcc.relatorio.dominio.interfaces.IDadosData;
import org.tcc.relatorio.dominio.interfaces.IDadosUsuario;

/**
 *
 * @author jwsouza
 */
@Entity
@Table(name = "PPH_AGENCIA_BANCARIA")
public class PphAgenciaBancariaEntity extends BaseEntity implements IDadosUsuario, IDadosData {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Size(max = 20)
    @Column(name = "NR_AGENCIA")
    private String nrAgencia;
    @Size(max = 60)
    @Column(name = "NM_AG_BANCARIA")
    private String nmAgBancaria;
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
    @JoinColumn(name = "ID_MUNICIPIO", referencedColumnName = "ID")
    @ManyToOne
    private PphMunicipioEntity pphMunicipio;
    @JoinColumn(name = "ID_BANCO", referencedColumnName = "ID")
    @ManyToOne
    private PphBancoEntity pphBanco;
    @OneToMany(mappedBy = "pphAgenciaBancaria", cascade = CascadeType.REFRESH)
    private Set<PphBeneficiarioEntity> pphBeneficiarioSet;
    @OneToMany(mappedBy = "pphAgenciaBancaria", cascade = CascadeType.REFRESH)
    private Set<PphProcuradorEntity> pphProcuradorSet;

    public PphAgenciaBancariaEntity() {
    }

    public PphAgenciaBancariaEntity(Long id) {
        setId(id);
    }

    public String getNrAgencia() {
        return nrAgencia;
    }

    public void setNrAgencia(String nrAgencia) {
        this.nrAgencia = nrAgencia;
    }

    public String getNmAgBancaria() {
        return nmAgBancaria;
    }

    public void setNmAgBancaria(String nmAgBancaria) {
        this.nmAgBancaria = nmAgBancaria;
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

    public PphMunicipioEntity getPphMunicipio() {
        return pphMunicipio;
    }

    public void setPphMunicipio(PphMunicipioEntity pphMunicipio) {
        this.pphMunicipio = pphMunicipio;
    }

    public PphBancoEntity getPphBanco() {
        return pphBanco;
    }

    public void setPphBanco(PphBancoEntity pphBanco) {
        this.pphBanco = pphBanco;
    }

    public Set<PphBeneficiarioEntity> getPphBeneficiarioSet() {
        return pphBeneficiarioSet;
    }

    public void setPphBeneficiarioSet(Set<PphBeneficiarioEntity> pphBeneficiarioSet) {
        this.pphBeneficiarioSet = pphBeneficiarioSet;
    }

    public Set<PphProcuradorEntity> getPphProcuradorSet() {
        return pphProcuradorSet;
    }

    public void setPphProcuradorSet(Set<PphProcuradorEntity> pphProcuradorSet) {
        this.pphProcuradorSet = pphProcuradorSet;
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
        if (!(object instanceof PphAgenciaBancariaEntity)) {
            return false;
        }
        PphAgenciaBancariaEntity other = (PphAgenciaBancariaEntity) object;
        if ((getId() == null && other.getId() != null) || (this.getId() != null && !this.getId().equals(other.getId()))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.tcc.relatorio.dominio.PphAgenciaBancaria[ id=" + getId() + " ]";
    }
    
}
