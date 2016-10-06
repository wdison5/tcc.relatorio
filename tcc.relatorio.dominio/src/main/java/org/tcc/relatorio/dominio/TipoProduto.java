package org.tcc.relatorio.dominio;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author Jose Wdison
 */
@Entity
@Table(name = "TIPO_PRODUTO")
public class TipoProduto implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID_TIPO_PRODUTO")
    private Long idTipoProduto;
    @Size(max = 500)
    @Column(name = "DESCRICAO")
    private String descricao;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoProduto", fetch = FetchType.LAZY)
    private List<Produto> produtoList;

    public TipoProduto() {
    }

    public TipoProduto(Long idTipoProduto) {
        this.idTipoProduto = idTipoProduto;
    }

    public Long getIdTipoProduto() {
        return idTipoProduto;
    }

    public void setIdTipoProduto(Long idTipoProduto) {
        this.idTipoProduto = idTipoProduto;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public List<Produto> getProdutoList() {
        return produtoList;
    }

    public void setProdutoList(List<Produto> produtoList) {
        this.produtoList = produtoList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (getIdTipoProduto() != null ? getIdTipoProduto().hashCode() : 0) + (getDescricao() != null ? getDescricao().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof TipoProduto)) {
            return false;
        }
        TipoProduto other = (TipoProduto) object;
        return this.getIdTipoProduto() != null && other.getIdTipoProduto() != null && this.getIdTipoProduto().longValue() ==  other.getIdTipoProduto()
                || this.getDescricao() != null && other.getDescricao()!= null && this.getDescricao().equals(other.getDescricao());
    }

    @Override
    public String toString() {
        return new StringBuilder(this.getClass().getName())
            .append("[ idTipoProduto=").append(getIdTipoProduto())
            .append(", descricao").append(getDescricao())
            .append(" ]").toString();
    }
}
