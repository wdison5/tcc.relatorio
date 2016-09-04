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
@Table(name = "PPH_COMUNICACAO")
public class PphComunicacaoEntity extends BaseEntity implements IDadosUsuario, IDadosData{
    //private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Size(max = 2)
    @Column(name = "TP_COMUNIC")
    private String tpComunic;
    @Size(max = 60)
    @Column(name = "DS_COMUNIC")
    private String dsComunic;
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
    @JoinColumn(name = "ID_PROCURADOR", referencedColumnName = "ID")
    @ManyToOne
    private PphProcuradorEntity pphProcurador;
    @JoinColumn(name = "ID_CONTATO", referencedColumnName = "ID")
    @ManyToOne
    private PphContatoEntity pphContato;
    @JoinColumn(name = "ID_BENEFICIARIO", referencedColumnName = "ID")
    @ManyToOne
    private PphBeneficiarioEntity pphBeneficiario;

    public PphComunicacaoEntity() {
    }

    public PphComunicacaoEntity(Long id) {
        setId(id);
    }

    public String getTpComunic() {
        return tpComunic;
    }

    public void setTpComunic(String tpComunic) {
        this.tpComunic = tpComunic;
    }

    public String getDsComunic() {
        return dsComunic;
    }

    public void setDsComunic(String dsComunic) {
        this.dsComunic = dsComunic;
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

    public PphProcuradorEntity getPphProcurador() {
        return pphProcurador;
    }

    public void setPphProcurador(PphProcuradorEntity pphProcurador) {
        this.pphProcurador = pphProcurador;
    }

    public PphContatoEntity getPphContato() {
        return pphContato;
    }

    public void setPphContato(PphContatoEntity pphContato) {
        this.pphContato = pphContato;
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
        if(getId()!=null){
            hash = getId().hashCode();
        }else if(getDsComunic()!=null && getTpComunic()!=null){
            hash = getDsComunic().hashCode()+getTpComunic().hashCode();
        }else if(getDsComunic()!=null){
            hash = getDsComunic().hashCode();
        }
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PphComunicacaoEntity)) {
            return false;
        }
        PphComunicacaoEntity other = (PphComunicacaoEntity) object;
        if ((getId() == null && other.getId() != null) || (getId() != null && getId().longValue() != other.getId())) {
            return false;
        }else if((getDsComunic() == null && other.getDsComunic() != null) || (getDsComunic() != null && !getDsComunic().equals(other.getDsComunic()))){
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "prodesp.sipph.dominio.PphComunicacao[ id=" + getId() + " ]";
    }
    
}
