package org.tcc.relatorio.cap.dominio;

import java.util.Date;
import java.util.List;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import org.tcc.relatorio.cap.dominio.policy.UsuarioDefault;
import org.tcc.relatorio.cap.dominio.policy.UsuarioNotNull;
import org.tcc.relatorio.cap.dominio.util.Policies;
import org.tcc.relatorio.cap.dominio.util.Policy;
import org.tcc.relatorio.dominio.EmpresaEntity;

/**
 * @author Jose Wdison
 */
@Entity
@Table(name = "USUARIO", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"USER_ID"}),
    @UniqueConstraint(columnNames = {"EMAIL"})
}
)
@Policies(policies = {
    //@Policy(name="senhaExpirada", policy=SenhaExpirada.class),
    @Policy(name = "usuarioNotNull", policy = UsuarioNotNull.class),
    @Policy(name = "usuarioDefault", policy = UsuarioDefault.class)}
)
public class UsuarioEntity extends BaseEntity {

    @Column(name = "USER_ID", nullable = false, length = 50)
    private String userId;

    @Column(name = "SENHA", nullable = false, length = 50)
    private String senha;

    @Column(name = "EXP_SENHA")
    @Temporal(TemporalType.DATE)
    private Date dataExpSenha;

    @Column(name = "EMAIL", nullable = false, length = 50)
    private String email;

    @Column(name = "CPF", length = 14)
    private String cpf;

    @Column(name = "RG", length = 10)
    private String rg;

    @Column(name = "DT_CRIACAO", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date dataCriacao;

    @Column(name = "TENTATIVAS", nullable = false)
    private int tentativas;

    @Column(name = "BLOQ", nullable = false)
    private boolean bloqueado;

    @Column(name = "DT_NASCIMENTO")
    @Temporal(TemporalType.DATE)
    private Date dataNasc;

    @Column(name = "SEXO")
    private String sexo;

    @Column(name = "ACESSO_GERAL")
    private boolean acessoGeral;
    
    @Column(name = "FL_EXCLUSAO")
    private Integer flExclusao;

    @ManyToMany
    private Set<GrupoEntity> grupos;

    @ManyToMany
    @JoinTable(name = "USUARIO_EMPRESA",
            joinColumns = {
                @JoinColumn(name = "USUARIO_ID")},
            inverseJoinColumns = {
                @JoinColumn(name = "EMPRESA_ID")}
    )
    private List<EmpresaEntity> empresas;

    @Transient
    private boolean senhaExpirada;

    /**
     * Construtor.
     */
    public UsuarioEntity() {
    }
    
    public UsuarioEntity(Long id) {
        setId(id);
    }

    /**
     * @return the userId
     */
    public String getUserId() {
        return userId;
    }

    /**
     * @param userId the userId to set
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * @return the senha
     */
    public String getSenha() {
        return senha;
    }

    /**
     * @param senha the senha to set
     */
    public void setSenha(String senha) {
        this.senha = senha;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return the cpf
     */
    public String getCpf() {
        return cpf;
    }

    /**
     * @param cpf the cpf to set
     */
    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    /**
     * @return the rg
     */
    public String getRg() {
        return rg;
    }

    /**
     * @param rg the rg to set
     */
    public void setRg(String rg) {
        this.rg = rg;
    }

    /**
     * @return the dataCriacao
     */
    public Date getDataCriacao() {
        return dataCriacao;
    }

    /**
     * @param dataCriacao the dataCriacao to set
     */
    public void setDataCriacao(Date dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    /**
     * @return the tentativas
     */
    public int getTentativas() {
        return tentativas;
    }

    /**
     * @param tentativas the tentativas to set
     */
    public void setTentativas(int tentativas) {
        this.tentativas = tentativas;
    }

    /**
     * @return the bloqueado
     */
    public boolean isBloqueado() {
        return bloqueado;
    }

    /**
     * @param bloqueado the bloqueado to set
     */
    public void setBloqueado(boolean bloqueado) {
        this.bloqueado = bloqueado;
    }

    /**
     * @return the dataNasc
     */
    public Date getDataNasc() {
        return dataNasc;
    }

    /**
     * @param dataNasc the dataNasc to set
     */
    public void setDataNasc(Date dataNasc) {
        this.dataNasc = dataNasc;
    }

    /**
     * @return the sexo
     */
    public String getSexo() {
        return sexo;
    }

    /**
     * @param sexo the sexo to set
     */
    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    /**
     * @return the dataExpSenha
     */
    public Date getDataExpSenha() {
        return dataExpSenha;
    }

    /**
     * @param dataExpSenha the dataExpSenha to set
     */
    public void setDataExpSenha(Date dataExpSenha) {
        this.dataExpSenha = dataExpSenha;
    }

    public boolean isAcessoGeral() {
        return acessoGeral;
    }

    public void setAcessoGeral(boolean acessoGeral) {
        this.acessoGeral = acessoGeral;
    }

    public Integer getFlExclusao() {
        return flExclusao;
    }

    public void setFlExclusao(Integer flExclusao) {
        this.flExclusao = flExclusao;
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

    /**
     * @return the senhaExpirada
     */
    public boolean isSenhaExpirada() {
        return senhaExpirada;
    }

    /**
     * @param senhaExpirada the senhaExpirada to set
     */
    public void setSenhaExpirada(boolean senhaExpirada) {
        this.senhaExpirada = senhaExpirada;
    }

    public List<EmpresaEntity> getEmpresas() {
        return empresas;
    }

    public void setEmpresas(List<EmpresaEntity> empresas) {
        this.empresas = empresas;
    }

}
