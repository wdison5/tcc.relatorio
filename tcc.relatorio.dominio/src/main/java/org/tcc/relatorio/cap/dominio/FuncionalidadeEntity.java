package org.tcc.relatorio.cap.dominio;

import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.tcc.relatorio.cap.dominio.policy.BaseDefault;
import org.tcc.relatorio.cap.dominio.policy.BaseNotNull;
import org.tcc.relatorio.cap.dominio.util.Policies;
import org.tcc.relatorio.cap.dominio.util.Policy;

/**
 * @author Jose Wdison
 */

@Entity
@Table(name="FUNCIONALIDADE", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"NOME"})}
 )
@Policies(policies={
        @Policy(name="grupoNotNull", policy=BaseNotNull.class),
        @Policy(name="grupoDefault", policy=BaseDefault.class)}
 )
public class FuncionalidadeEntity extends BaseEntity {

    @Column(name="URL")
    private String url;

    @Column(name="TP_OPER")
    private String tipoOperacao;
    
    @Column(name="DESCRICAO", nullable=false, length=255)
    private String descricao;

    @ManyToMany(mappedBy = "funcionalidades")
    private Set<GrupoEntity> grupos;
    
    /**
     * Construtor.
     */
    public FuncionalidadeEntity() {
    }

    /**
     * @return the descricao
     */
    public String getDescricao() {
        return descricao;
    }

    /**
     * @param descricao the descricao to set
     */
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    /**
     * @return the url
     */
    public String getUrl() {
        return url;
    }

    /**
     * @param url the url to set
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * @return the tipoOperacao
     */
    public String getTipoOperacao() {
        return tipoOperacao;
    }

    /**
     * @param tipoOperacao the tipoOperacao to set
     */
    public void setTipoOperacao(String tipoOperacao) {
        this.tipoOperacao = tipoOperacao;
    }

    /**
     * @return the grupos
     */
    public Set<GrupoEntity> getGrupos() {
        return grupos;
    }

    /**
     * @param grupos the grupos to set
     */
    public void setGrupos(Set<GrupoEntity> grupos) {
        this.grupos = grupos;
    }
}
