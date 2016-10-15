package org.tcc.relatorio.dominio;

import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Size;

/**
 * @author Jose Wdison
 */
@Entity
@Table(name = "TIPO_PRODUTO")
public class TipoProdutoEntity extends BaseEntity{
    private static final long serialVersionUID = 1L;
    
    @Size(max = 500)
    @Column(name = "DESCRICAO")
    private String descricao;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoProduto", fetch = FetchType.LAZY)
    private List<ProdutoEntity> produtoList;

    public TipoProdutoEntity() {
    }

    public TipoProdutoEntity(Long idTipoProduto) {
        this.setId(idTipoProduto);
    }
    public TipoProdutoEntity(Long idTipoProduto, String descricao) {
        this.setId(idTipoProduto);
        this. descricao = descricao;
    }
    
    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public List<ProdutoEntity> getProdutoList() {
        return produtoList;
    }

    public void setProdutoList(List<ProdutoEntity> produtoList) {
        this.produtoList = produtoList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (getId() != null ? getId().hashCode() : 0) + (getDescricao() != null ? getDescricao().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof TipoProdutoEntity)) {
            return false;
        }
        TipoProdutoEntity other = (TipoProdutoEntity) object;
        return this.getId() != null && other.getId() != null && this.getId().longValue() ==  other.getId()
                || this.getDescricao() != null && other.getDescricao()!= null && this.getDescricao().equals(other.getDescricao());
    }

    @Override
    public String toString() {
        return new StringBuilder(this.getClass().getName())
            .append("[ idTipoProduto=").append(getId())
            .append(", descricao").append(getDescricao())
            .append(" ]").toString();
    }
}
