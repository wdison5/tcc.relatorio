/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
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
 *
 * @author 140200
 */
@Entity
@Table(name="GRUPO", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"NOME"})}
 )
@Policies(policies={
        @Policy(name="grupoNotNull", policy=BaseNotNull.class),
        @Policy(name="grupoDefault", policy=BaseDefault.class)}
)
public class GrupoEntity extends BaseEntity {

    @Column(name="DESCRICAO", length=255)
    private String descricao;
    
    @Column(name = "FL_EXCLUSAO")
    private Integer flExclusao;
    
    @ManyToMany
    private Set<FuncionalidadeEntity> funcionalidades;
    
    @ManyToMany(mappedBy = "grupos")
    private Set<UsuarioEntity> usuarios;
    
    public GrupoEntity() {
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Integer getFlExclusao() {
        return flExclusao;
    }

    public void setFlExclusao(Integer flExclusao) {
        this.flExclusao = flExclusao;
    }

    public Set<FuncionalidadeEntity> getFuncionalidades() {
        return funcionalidades;
    }

    public void setFuncionalidades(Set<FuncionalidadeEntity> funcionalidades) {
        this.funcionalidades = funcionalidades;
    }

    /**
     * @return the usuarios
     */
    public Set<UsuarioEntity> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(Set<UsuarioEntity> usuarios) {
        this.usuarios = usuarios;
    }
    
}
