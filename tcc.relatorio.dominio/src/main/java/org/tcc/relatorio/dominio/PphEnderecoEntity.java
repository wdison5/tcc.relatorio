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
import javax.validation.constraints.Size;
import org.tcc.relatorio.dominio.interfaces.IDadosData;
import org.tcc.relatorio.dominio.interfaces.IDadosUsuario;

/**
 *
 * @author jwsouza
 */
@Entity
@Table(name = "PPH_ENDERECO")
public class PphEnderecoEntity extends BaseEntity implements IDadosUsuario, IDadosData{

    @Size(max = 2)
    @Column(name = "TP_ENDERECO")
    private String tpEndereco;
    @Column(name = "FL_ATIVO")
    private Short flAtivo;
    @Size(max = 10)
    @Column(name = "TP_LOGRADOURO")
    private String tpLogradouro;
    @Size(max = 72)
    @Column(name = "DS_ENDERECO")
    private String dsEndereco;
    @Size(max = 20)
    @Column(name = "NR_ENDERECO")
    private String nrEndereco;
    @Size(max = 60)
    @Column(name = "DS_COMPL_ENDERECO")
    private String dsComplEndereco;
    @Size(max = 72)
    @Column(name = "NM_BAIRRO")
    private String nmBairro;
    @Column(name = "CD_CEP")
    private String cdCep;
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
    @JoinColumn(name = "ID_PROCURADOR", referencedColumnName = "ID")
    @ManyToOne
    private PphProcuradorEntity pphProcurador;
    @JoinColumn(name = "ID_MUNICIPIO", referencedColumnName = "ID")
    @ManyToOne
    private PphMunicipioEntity pphMunicipio;
    @JoinColumn(name = "ID_BENEFICIARIO", referencedColumnName = "ID")
    @ManyToOne
    private PphBeneficiarioEntity pphBeneficiario;

    public PphEnderecoEntity() {
    }

    public PphEnderecoEntity(Long id) {
        setId(id);
    }

    public String getTpEndereco() {
        return tpEndereco;
    }

    public void setTpEndereco(String tpEndereco) {
        this.tpEndereco = tpEndereco;
    }

    public Short getFlAtivo() {
        return flAtivo;
    }

    public void setFlAtivo(Short flAtivo) {
        this.flAtivo = flAtivo;
    }

    public String getTpLogradouro() {
        return tpLogradouro;
    }

    public void setTpLogradouro(String tpLogradouro) {
        this.tpLogradouro = tpLogradouro;
    }

    public String getDsEndereco() {
        return dsEndereco;
    }

    public void setDsEndereco(String dsEndereco) {
        this.dsEndereco = dsEndereco;
    }

    public String getNrEndereco() {
        return nrEndereco;
    }

    public void setNrEndereco(String nrEndereco) {
        this.nrEndereco = nrEndereco;
    }

    public String getDsComplEndereco() {
        return dsComplEndereco;
    }

    public void setDsComplEndereco(String dsComplEndereco) {
        this.dsComplEndereco = dsComplEndereco;
    }

    public String getNmBairro() {
        return nmBairro;
    }

    public void setNmBairro(String nmBairro) {
        this.nmBairro = nmBairro;
    }

    public String getCdCep() {
        return cdCep;
    }

    public void setCdCep(String cdCep) {
        this.cdCep = cdCep;
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

    public PphProcuradorEntity getPphProcurador() {
        return pphProcurador;
    }

    public void setPphProcurador(PphProcuradorEntity pphProcurador) {
        this.pphProcurador = pphProcurador;
    }

    public PphMunicipioEntity getPphMunicipio() {
        return pphMunicipio;
    }

    public void setPphMunicipio(PphMunicipioEntity pphMunicipio) {
        this.pphMunicipio = pphMunicipio;
    }

    public PphBeneficiarioEntity getPphBeneficiario() {
        return pphBeneficiario;
    }

    public void setPphBeneficiario(PphBeneficiarioEntity pphBeneficiario) {
        this.pphBeneficiario = pphBeneficiario;
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
        if (!(object instanceof PphEnderecoEntity)) {
            return false;
        }
        PphEnderecoEntity other = (PphEnderecoEntity) object;
        if ((this.getId() == null && other.getId() != null) || (this.getId() != null && !this.getId().equals(other.getId()))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.tcc.relatorio.dominio.PphEndereco[ id=" + getId() + " ]";
    }
    
}
