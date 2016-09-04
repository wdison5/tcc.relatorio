package org.tcc.relatorio.dominio;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
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
import org.tcc.relatorio.dominio.interfaces.IExclusao;

/**
 *
 * @author jwsouza
 */
@Entity
@Table(name = "PPH_BENEFICIARIO")
public class PphBeneficiarioEntity extends BaseEntity implements IDadosUsuario, IDadosData, IExclusao{
    
    @Size(max = 100)
    @Column(name = "NM_BENEFICIARIO")
    private String nmBeneficiario;
    
    @Size(max = 2)
    @Column(name = "TP_BENEFICIARIO")
    private String tpBeneficiario;
    
    @Column(name = "DT_NASCIMENTO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtNascimento;
    
    @Size(max = 100)
    @Column(name = "NM_MAE")
    private String nmMae;
    
    @Size(max = 20)
    @Column(name = "CD_RG_BENEFICIARIO")
    private String cdRgBeneficiario;
    
    @Size(max = 20)
    @Column(name = "NR_CPF_BENEFICIARIO")
    private String nrCpfBeneficiario;
    
    @Size(max = 20)
    @Column(name = "NR_CONTA_CORRENTE")
    private String nrContaCorrente;
   
    @Column(name = "DT_OBITO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtObito;
    
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
   
    @Column(name = "FL_EXCLUSAO")
    private Integer flExclusao;
    
    @OneToMany(mappedBy = "pphBeneficiario", cascade = CascadeType.ALL)
    private Set<PphContatoEntity> pphContatoSet;
    
    @OneToMany(mappedBy = "pphBeneficiario", cascade = CascadeType.ALL)
    private Set<PphAtestadoVidaEntity> pphAtestadoVidaSet;
    
    @OneToMany(mappedBy = "pphBeneficiario", cascade = CascadeType.ALL)
    private Set<PphEnderecoEntity> pphEnderecoSet;
    
    @OneToMany(mappedBy = "pphBeneficiario", cascade = CascadeType.ALL)
    private Set<PphEmpenhoEntity> pphEmpenhoSet;
    
    @OneToMany(mappedBy = "pphBeneficiario", cascade = CascadeType.ALL)
    private Set<PphComunicacaoEntity> pphComunicacaoSet;
    
    @JoinColumn(name = "ID_UNID_SAUDE", referencedColumnName = "ID", nullable = false)
    @ManyToOne
    private PphUnidSaudeEntity pphUnidSaude;
    
    @JoinColumn(name = "ID_UNIDADE_PAGADORA", referencedColumnName = "ID")
    @ManyToOne
    private PphUnidadePagadoraEntity pphUnidadePagadora;
    
    @OneToMany(mappedBy = "pphBeneficiario", cascade = CascadeType.ALL)
    private Set<PphBeneficiarioEntity> pphBeneficiarioSet;
    
    @JoinColumn(name = "ID_BENEFICIARIO_INICIAL", referencedColumnName = "ID")
    @ManyToOne
    private PphBeneficiarioEntity pphBeneficiario;
    
    @JoinColumn(name = "ID_AG_BANCARIA", referencedColumnName = "ID")
    @ManyToOne
    private PphAgenciaBancariaEntity pphAgenciaBancaria;
    
    @OneToMany(mappedBy = "pphBeneficiario", cascade = CascadeType.ALL)
    private Set<PphProcuradorEntity> pphProcuradorSet;

    public PphBeneficiarioEntity() {
    }

    public PphBeneficiarioEntity(Long id) {
        setId(id);
    }

    public String getNmBeneficiario() {
        return nmBeneficiario;
    }

    public void setNmBeneficiario(String nmBeneficiario) {
        this.nmBeneficiario = nmBeneficiario;
    }

    public String getTpBeneficiario() {
        return tpBeneficiario;
    }

    public void setTpBeneficiario(String tpBeneficiario) {
        this.tpBeneficiario = tpBeneficiario;
    }

    public Date getDtNascimento() {
        return dtNascimento;
    }

    public void setDtNascimento(Date dtNascimento) {
        this.dtNascimento = dtNascimento;
    }

    public String getNmMae() {
        return nmMae;
    }

    public void setNmMae(String nmMae) {
        this.nmMae = nmMae;
    }

    public String getCdRgBeneficiario() {
        return cdRgBeneficiario;
    }

    public void setCdRgBeneficiario(String cdRgBeneficiario) {
        this.cdRgBeneficiario = cdRgBeneficiario;
    }

    public String getNrCpfBeneficiario() {
        return nrCpfBeneficiario;
    }

    public void setNrCpfBeneficiario(String nrCpfBeneficiario) {
        this.nrCpfBeneficiario = nrCpfBeneficiario;
    }

    public String getNrContaCorrente() {
        return nrContaCorrente;
    }

    public void setNrContaCorrente(String nrContaCorrente) {
        this.nrContaCorrente = nrContaCorrente;
    }

    public Date getDtObito() {
        return dtObito;
    }

    public void setDtObito(Date dtObito) {
        this.dtObito = dtObito;
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

    public Set<PphContatoEntity> getPphContatoSet() {
        return pphContatoSet;
    }

    public void setPphContatoSet(Set<PphContatoEntity> pphContatoSet) {
        this.pphContatoSet = pphContatoSet;
    }

    public Set<PphAtestadoVidaEntity> getPphAtestadoVidaSet() {
        return pphAtestadoVidaSet;
    }

    public void setPphAtestadoVidaSet(Set<PphAtestadoVidaEntity> pphAtestadoVidaSet) {
        this.pphAtestadoVidaSet = pphAtestadoVidaSet;
    }

    public Set<PphEnderecoEntity> getPphEnderecoSet() {
        return pphEnderecoSet;
    }

    public void setPphEnderecoSet(Set<PphEnderecoEntity> pphEnderecoSet) {
        this.pphEnderecoSet = pphEnderecoSet;
    }

    public List<PphEnderecoEntity> getPphEnderecoList() {
        return pphEnderecoSet!=null ? new ArrayList(pphEnderecoSet):null;
    }

    public void setPphEnderecoList(List<PphEnderecoEntity> pphEnderecoList) {
        this.pphEnderecoSet = pphEnderecoList != null ? new HashSet<PphEnderecoEntity> (pphEnderecoList) : null;
    }

    public Set<PphEmpenhoEntity> getPphEmpenhoSet() {
        return pphEmpenhoSet;
    }

    public void setPphEmpenhoSet(Set<PphEmpenhoEntity> pphEmpenhoSet) {
        this.pphEmpenhoSet = pphEmpenhoSet;
    }

    public Set<PphComunicacaoEntity> getPphComunicacaoSet() {
        return pphComunicacaoSet;
    }

    public void setPphComunicacaoSet(Set<PphComunicacaoEntity> pphComunicacaoSet) {
        this.pphComunicacaoSet = pphComunicacaoSet;
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

    public Set<PphBeneficiarioEntity> getPphBeneficiarioSet() {
        return pphBeneficiarioSet;
    }

    public void setPphBeneficiarioSet(Set<PphBeneficiarioEntity> pphBeneficiarioSet) {
        this.pphBeneficiarioSet = pphBeneficiarioSet;
    }

    public PphBeneficiarioEntity getPphBeneficiario() {
        return pphBeneficiario;
    }

    public void setPphBeneficiario(PphBeneficiarioEntity pphBeneficiario) {
        this.pphBeneficiario = pphBeneficiario;
    }

    public PphAgenciaBancariaEntity getPphAgenciaBancaria() {
        return pphAgenciaBancaria;
    }

    public void setPphAgenciaBancaria(PphAgenciaBancariaEntity pphAgenciaBancaria) {
        this.pphAgenciaBancaria = pphAgenciaBancaria;
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
        if (object == null || !(object instanceof PphBeneficiarioEntity)) {
            return false;
        }
        PphBeneficiarioEntity other = (PphBeneficiarioEntity) object;
        if ((this.getId() == null && other.getId() != null) || (this.getId() != null && !this.getId().equals(other.getId()))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.tcc.relatorio.dominio.PphBeneficiario[ id=" + getId() + " ]";
    }

    @Override
    public Integer getFlExclusao() {
        return this.flExclusao;
    }

    @Override
    public void setFlExclusao(Integer flExclusao) {
        this.flExclusao = flExclusao;
    }
}
