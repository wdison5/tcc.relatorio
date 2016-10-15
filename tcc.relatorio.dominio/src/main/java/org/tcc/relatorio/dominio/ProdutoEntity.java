package org.tcc.relatorio.dominio;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

/**
 * @author Jose Wdison
 */
@Entity
@Table(name = "PRODUTO")
public class ProdutoEntity extends BaseEntity {
    private static final long serialVersionUID = 1L;
    @Size(max = 500)
    @Column(name = "DESCRICAO")
    private String descricao;
    @Column(name = "DATA_REFERENCIA")
    @Temporal(TemporalType.DATE)
    private Date dataReferencia;
    @Column(name = "QUANTIDADE")
    private Integer quantidade;
    @Min(value=0L)
    @Column(name = "VALOR_UNITARIO")
    private BigDecimal valorUnitario;
    @Min(value=0L)
    @Column(name = "VALOR_CUSTO")
    private BigDecimal valorCusto;
    @Column(name = "ID_USUARIO")
    private Long idUsuario;
    @Column(name = "ID_INSTITUICAO")
    private Long idInstituicao;
    @JoinColumn(name = "ID_TIPO_PRODUTO", referencedColumnName = "ID")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private TipoProdutoEntity tipoProduto;

    public ProdutoEntity() {
    }

    public ProdutoEntity(Long idProduto) {
        this.setId(idProduto);
    }

    public ProdutoEntity(String descricao, Date dataReferencia, BigDecimal valorUnitario, BigDecimal valorCusto, Long idTipoProduto, String descricaoTipoProduto) {
        this.descricao = descricao;
        this.dataReferencia = dataReferencia;
        this.valorUnitario = valorUnitario;
        this.valorCusto = valorCusto;
        this.tipoProduto = new TipoProdutoEntity(idTipoProduto, descricaoTipoProduto);
    }
    
    public ProdutoEntity(String descricao, Date dataReferencia, BigDecimal valorUnitario, BigDecimal valorCusto, Long idTipoProduto, String descricaoTipoProduto, Integer quantidade) {
        this.setId(999999L);//Id falso para evitar erros no relatorio
        this.descricao = descricao;
        this.dataReferencia = dataReferencia;
        this.valorUnitario = valorUnitario;
        this.valorCusto = valorCusto;
        this.quantidade = quantidade;
        this.tipoProduto = new TipoProdutoEntity(idTipoProduto, descricaoTipoProduto);
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Date getDataReferencia() {
        return dataReferencia;
    }

    public void setDataReferencia(Date dataReferencia) {
        this.dataReferencia = dataReferencia;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public BigDecimal getValorUnitario() {
        return valorUnitario;
    }

    public void setValorUnitario(BigDecimal valorUnitario) {
        this.valorUnitario = valorUnitario;
    }

    public BigDecimal getValorCusto() {
        return valorCusto;
    }

    public void setValorCusto(BigDecimal valorCusto) {
        this.valorCusto = valorCusto;
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Long getIdInstituicao() {
        return idInstituicao;
    }

    public void setIdInstituicao(Long idInstituicao) {
        this.idInstituicao = idInstituicao;
    }

    public TipoProdutoEntity getTipoProduto() {
        return tipoProduto;
    }

    public void setTipoProduto(TipoProdutoEntity tipoProduto) {
        this.tipoProduto = tipoProduto;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (getId() != null ? getId().hashCode() : 0) + (getDescricao()!= null ? getDescricao().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof ProdutoEntity)) {
            return false;
        }
        ProdutoEntity other = (ProdutoEntity) object;
        return this.getId() != null && other.getId() != null && this.getId().longValue()== other.getId()
                || this.getDescricao()!= null && other.getDescricao()!= null && this.getDescricao().equals(other.getDescricao());
    }

    @Override
    public String toString() {
        return new StringBuilder(this.getClass().getName())
            .append("[ idProduto=").append(getId())
            .append(", descricao").append(getDescricao())
            .append(" ]").toString();
    }
    
}
