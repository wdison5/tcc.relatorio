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
@Table(name = "PPH_CONTATO")
public class PphContatoEntity extends BaseEntity implements IDadosUsuario, IDadosData{

    //private static final long serialVersionUID = 1L;
    
    @Size(max = 100)
    @Column(name = "NM_CONTATO")
    private String nmContato;
    @Size(max = 100)
    @Column(name = "DS_GRAU_OBS")
    private String dsGrauObs;
    @Column(name = "FL_ATIVO")
    private Short flAtivo;
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
    @OneToMany(mappedBy = "pphContato", cascade = CascadeType.ALL)
    private Set<PphComunicacaoEntity> pphComunicacaoSet;

    public PphContatoEntity() {
    }

    public PphContatoEntity(Long id) {
        setId(id);
    }

    public String getNmContato() {
        return nmContato;
    }

    public void setNmContato(String nmContato) {
        this.nmContato = nmContato;
    }

    public String getDsGrauObs() {
        return dsGrauObs;
    }

    public void setDsGrauObs(String dsGrauObs) {
        this.dsGrauObs = dsGrauObs;
    }

    public Short getFlAtivo() {
        return flAtivo;
    }

    public void setFlAtivo(Short flAtivo) {
        this.flAtivo = flAtivo;
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

    public PphBeneficiarioEntity getPphBeneficiario() {
        return pphBeneficiario;
    }

    public void setPphBeneficiario(PphBeneficiarioEntity pphBeneficiario) {
        this.pphBeneficiario = pphBeneficiario;
    }

    public Set<PphComunicacaoEntity> getPphComunicacaoSet() {
        return pphComunicacaoSet;
    }

    public void setPphComunicacaoSet(Set<PphComunicacaoEntity> pphComunicacaoSet) {
        this.pphComunicacaoSet = pphComunicacaoSet;
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
        if (!(object instanceof PphContatoEntity)) {
            return false;
        }
        PphContatoEntity other = (PphContatoEntity) object;
        if ((this.getId() == null && other.getId() != null) || (this.getId() != null && !this.getId().equals(other.getId()))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.tcc.relatorio.dominio.PphContato[ id=" + getId() + " ]";
    }

}
