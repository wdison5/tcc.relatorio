package org.tcc.relatorio.dominio;

import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 *
 * @author Jose Wdison
 */
@Entity
@Table(name = "PPH_UNID_SAUDE")
public class PphUnidSaudeEntity extends BaseEntity implements Comparable<PphUnidSaudeEntity> {

    @Column(name = "DS_UNID", nullable = false, length = 100)
    private String nome;

    @Column(name = "CD_CNES")
    private int cnes;

    @Column(name = "SISTEMAEXTERNO_ID")
    private Long idSistemaExterno;

    @Column(name = "FL_ATIVO")
    private Integer flAtivo;

    @OneToMany(mappedBy = "pphUnidSaude", cascade = CascadeType.ALL)
    private Set<PphBeneficiarioEntity> pphBeneficiarioSet;

    @OneToOne(mappedBy = "pphUnidSaude")
    private InstituicaoEntity instituicao;

    public PphUnidSaudeEntity(Long id) {
        super.setId(id);
    }

    public PphUnidSaudeEntity(Long id, String nome) {
        super.setId(id);
        this.nome = nome;
    }

    public PphUnidSaudeEntity() {
    }

    public String getNome() {
        return this.nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getCnes() {
        return cnes;
    }

    public void setCnes(int cnes) {
        this.cnes = cnes;
    }

    public Long getIdSistemaExterno() {
        return idSistemaExterno;
    }

    public void setIdSistemaExterno(Long idSistemaExterno) {
        this.idSistemaExterno = idSistemaExterno;
    }

    public Integer getFlAtivo() {
        return flAtivo;
    }

    public void setFlAtivo(Integer flAtivo) {
        this.flAtivo = flAtivo;
    }

    public Set<PphBeneficiarioEntity> getPphBeneficiarioSet() {
        return pphBeneficiarioSet;
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
    public boolean equals(Object o) {
        if (!(o instanceof InstituicaoEntity)) {
            return false;
        }
        PphUnidSaudeEntity other = (PphUnidSaudeEntity) o;
        if ((this.getId() == null && other.getId() != null) || (this.getId() != null && !this.getId().equals(other.getId()))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 83 * hash + (this.nome != null ? this.nome.hashCode() : 0);
        hash = 83 * hash + (this.getId() != null ? this.getId().hashCode() : 0);
        hash = 83 * hash + this.cnes;
        hash = 83 * hash + (this.instituicao != null ? this.instituicao.hashCode() : 0);
        return hash;
    }

    @Override
    public int compareTo(PphUnidSaudeEntity o) {
        if (o != null) {
            if (o.getNome() != null) {
                return this.getNome().compareTo(o.getNome());
            } else {
                return -1;
            }
        } else {
            return 1;
        }
    }

}
