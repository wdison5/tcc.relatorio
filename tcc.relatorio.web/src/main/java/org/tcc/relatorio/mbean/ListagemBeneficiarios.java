package org.tcc.relatorio.mbean;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;

/**
 *
 * @author Eloy
 */
public class ListagemBeneficiarios {
    private String identNome;
    private Date   identNascimento;
    private String identMae;
    private String identRg;
    private String identCpf;

    private String categoria;
    private String unidadeSaude;
    private String unidadePagadora;

    private String enderCep;
    private String enderTipo;
    private String enderLogradouro;
    private String enderNumero;
    private String enderComplemento;
    private String enderBairro;
    private String enderUf;
    private String enderMunicipio;
    private String enderFone1;
    private String enderFone2;
    private String enderCelular;

    private String contatoNome;
    private String contatoRelacao;
    private String contatoFP;
    private String contatoFS;
    private String contatoCE;

    private String bancoInstituicao;
    private String bancoAgencia;
    private String bancoCC;
    private String bancoUf;
    private String bancoMunicipio;

    private String procuradorNome;
    private String procuradorEnderCep;
    private String procuradorEnderTipo;
    private String procuradorEnderLogradouro;
    private String procuradorEnderNumero;
    private String procuradorEnderComplemento;
    private String procuradorEnderBairro;
    private String procuradorEnderUf;
    private String procuradorEnderMunicipio;
    private String procuradorEnderFone1;
    private String procuradorEnderFone2;
    private String procuradorEnderCelular;
    private String procuradorbancoInstituicao;
    private String procuradorbancoAgencia;
    private String procuradorbancoCC;
    private String procuradorbancoUf;
    private String procuradorbancoMunicipio;


    public ListagemBeneficiarios() throws NoSuchMethodException,SecurityException,NoSuchMethodException,IllegalAccessException,IllegalArgumentException,InvocationTargetException {
        Class cls = ListagemBeneficiarios.class;
        Field fieldlist[] = cls.getDeclaredFields();
        for (int i = 0; i < fieldlist.length; i++) {
            if (cls.getDeclaredFields()[i].getType().getSimpleName().equals("String")) {
                String atributo = cls.getDeclaredFields()[i].getName();
                String metodoSet = "set" + atributo.substring(0,1).toUpperCase() + atributo.substring(1);
                Class[] cArg = new Class[1];
                cArg[0] = String.class;
                Method meth = cls.getMethod(metodoSet, cArg);
                Object[] parametros = new Object[1];
                parametros[0] = null;
                meth.invoke(ListagemBeneficiarios.this, parametros);
            }
        }
    }

    private String nvl(String info) {
        return info == null ? "" : info;
    }

    public String getIdentNome() {
        return identNome;
    }

    public void setIdentNome(String identNome) {
        this.identNome = nvl(identNome);
    }

    public Date getIdentNascimento() {
        return identNascimento;
    }

    public void setIdentNascimento(Date identNascimento) {
        this.identNascimento = identNascimento;
    }

    public String getIdentMae() {
        return identMae;
    }

    public void setIdentMae(String identMae) {
        this.identMae = nvl(identMae);
    }

    public String getIdentRg() {
        return identRg;
    }

    public void setIdentRg(String identRg) {
        this.identRg = nvl(identRg);
    }

    public String getIdentCpf() {
        return identCpf;
    }

    public void setIdentCpf(String identCpf) {
        this.identCpf = nvl(identCpf);
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = nvl(categoria);
    }

    public String getUnidadeSaude() {
        return unidadeSaude;
    }

    public void setUnidadeSaude(String unidadeSaude) {
        this.unidadeSaude = nvl(unidadeSaude);
    }

    public String getUnidadePagadora() {
        return unidadePagadora;
    }

    public void setUnidadePagadora(String unidadePagadora) {
        this.unidadePagadora = nvl(unidadePagadora);
    }

    public String getEnderCep() {
        return enderCep;
    }

    public void setEnderCep(String enderCep) {
        this.enderCep = nvl(enderCep);
    }

    public String getEnderTipo() {
        return enderTipo;
    }

    public void setEnderTipo(String enderTipo) {
        this.enderTipo = nvl(enderTipo);
    }

    public String getEnderLogradouro() {
        return enderLogradouro;
    }

    public void setEnderLogradouro(String enderLogradouro) {
        this.enderLogradouro = nvl(enderLogradouro);
    }

    public String getEnderNumero() {
        return enderNumero;
    }

    public void setEnderNumero(String enderNumero) {
        this.enderNumero = nvl(enderNumero);
    }

    public String getEnderComplemento() {
        return enderComplemento;
    }

    public void setEnderComplemento(String enderComplemento) {
        this.enderComplemento = nvl(enderComplemento);
    }

    public String getEnderBairro() {
        return enderBairro;
    }

    public void setEnderBairro(String enderBairro) {
        this.enderBairro = nvl(enderBairro);
    }

    public String getEnderUf() {
        return enderUf;
    }

    public void setEnderUf(String enderUf) {
        this.enderUf = nvl(enderUf);
    }

    public String getEnderMunicipio() {
        return enderMunicipio;
    }

    public void setEnderMunicipio(String enderMunicipio) {
        this.enderMunicipio = nvl(enderMunicipio);
    }

    public String getEnderFone1() {
        return enderFone1;
    }

    public void setEnderFone1(String enderFone1) {
        this.enderFone1 = nvl(enderFone1);
    }

    public String getEnderFone2() {
        return enderFone2;
    }

    public void setEnderFone2(String enderFone2) {
        this.enderFone2 = nvl(enderFone2);
    }

    public String getEnderCelular() {
        return enderCelular;
    }

    public void setEnderCelular(String enderCelular) {
        this.enderCelular = nvl(enderCelular);
    }

    public String getContatoNome() {
        return contatoNome;
    }

    public void setContatoNome(String contatoNome) {
        this.contatoNome = nvl(contatoNome);
    }

    public String getContatoRelacao() {
        return contatoRelacao;
    }

    public void setContatoRelacao(String contatoRelacao) {
        this.contatoRelacao = nvl(contatoRelacao);
    }

    public String getContatoFP() {
        return contatoFP;
    }

    public void setContatoFP(String contatoFP) {
        this.contatoFP = nvl(contatoFP);
    }

    public String getContatoFS() {
        return contatoFS;
    }

    public void setContatoFS(String contatoFS) {
        this.contatoFS = nvl(contatoFS);
    }

    public String getContatoCE() {
        return contatoCE;
    }

    public void setContatoCE(String contatoCE) {
        this.contatoCE = nvl(contatoCE);
    }

    public String getBancoInstituicao() {
        return bancoInstituicao;
    }

    public void setBancoInstituicao(String bancoInstituicao) {
        this.bancoInstituicao = nvl(bancoInstituicao);
    }

    public String getBancoAgencia() {
        return bancoAgencia;
    }

    public void setBancoAgencia(String bancoAgencia) {
        this.bancoAgencia = nvl(bancoAgencia);
    }

    public String getBancoCC() {
        return bancoCC;
    }

    public void setBancoCC(String bancoCC) {
        this.bancoCC = nvl(bancoCC);
    }

    public String getBancoUf() {
        return bancoUf;
    }

    public void setBancoUf(String bancoUf) {
        this.bancoUf = nvl(bancoUf);
    }

    public String getBancoMunicipio() {
        return bancoMunicipio;
    }

    public void setBancoMunicipio(String bancoMunicipio) {
        this.bancoMunicipio = nvl(bancoMunicipio);
    }

    public String getProcuradorNome() {
        return procuradorNome;
    }

    public void setProcuradorNome(String procuradorNome) {
        this.procuradorNome = nvl(procuradorNome);
    }

    public String getProcuradorEnderCep() {
        return procuradorEnderCep;
    }

    public void setProcuradorEnderCep(String procuradorEnderCep) {
        this.procuradorEnderCep = nvl(procuradorEnderCep);
    }

    public String getProcuradorEnderTipo() {
        return procuradorEnderTipo;
    }

    public void setProcuradorEnderTipo(String procuradorEnderTipo) {
        this.procuradorEnderTipo = nvl(procuradorEnderTipo);
    }

    public String getProcuradorEnderLogradouro() {
        return procuradorEnderLogradouro;
    }

    public void setProcuradorEnderLogradouro(String procuradorEnderLogradouro) {
        this.procuradorEnderLogradouro = nvl(procuradorEnderLogradouro);
    }

    public String getProcuradorEnderNumero() {
        return procuradorEnderNumero;
    }

    public void setProcuradorEnderNumero(String procuradorEnderNumero) {
        this.procuradorEnderNumero = nvl(procuradorEnderNumero);
    }

    public String getProcuradorEnderComplemento() {
        return procuradorEnderComplemento;
    }

    public void setProcuradorEnderComplemento(String procuradorEnderComplemento) {
        this.procuradorEnderComplemento = nvl(procuradorEnderComplemento);
    }

    public String getProcuradorEnderBairro() {
        return procuradorEnderBairro;
    }

    public void setProcuradorEnderBairro(String procuradorEnderBairro) {
        this.procuradorEnderBairro = nvl(procuradorEnderBairro);
    }

    public String getProcuradorEnderUf() {
        return procuradorEnderUf;
    }

    public void setProcuradorEnderUf(String procuradorEnderUf) {
        this.procuradorEnderUf = nvl(procuradorEnderUf);
    }

    public String getProcuradorEnderMunicipio() {
        return procuradorEnderMunicipio;
    }

    public void setProcuradorEnderMunicipio(String procuradorEnderMunicipio) {
        this.procuradorEnderMunicipio = nvl(procuradorEnderMunicipio);
    }

    public String getProcuradorEnderFone1() {
        return procuradorEnderFone1;
    }

    public void setProcuradorEnderFone1(String procuradorEnderFone1) {
        this.procuradorEnderFone1 = nvl(procuradorEnderFone1);
    }

    public String getProcuradorEnderFone2() {
        return procuradorEnderFone2;
    }

    public void setProcuradorEnderFone2(String procuradorEnderFone2) {
        this.procuradorEnderFone2 = nvl(procuradorEnderFone2);
    }

    public String getProcuradorEnderCelular() {
        return procuradorEnderCelular;
    }

    public void setProcuradorEnderCelular(String procuradorEnderCelular) {
        this.procuradorEnderCelular = nvl(procuradorEnderCelular);
    }

    public String getProcuradorbancoInstituicao() {
        return procuradorbancoInstituicao;
    }

    public void setProcuradorbancoInstituicao(String procuradorbancoInstituicao) {
        this.procuradorbancoInstituicao = nvl(procuradorbancoInstituicao);
    }

    public String getProcuradorbancoAgencia() {
        return procuradorbancoAgencia;
    }

    public void setProcuradorbancoAgencia(String procuradorbancoAgencia) {
        this.procuradorbancoAgencia = nvl(procuradorbancoAgencia);
    }

    public String getProcuradorbancoCC() {
        return procuradorbancoCC;
    }

    public void setProcuradorbancoCC(String procuradorbancoCC) {
        this.procuradorbancoCC = nvl(procuradorbancoCC);
    }

    public String getProcuradorbancoUf() {
        return procuradorbancoUf;
    }

    public void setProcuradorbancoUf(String procuradorbancoUf) {
        this.procuradorbancoUf = nvl(procuradorbancoUf);
    }

    public String getProcuradorbancoMunicipio() {
        return procuradorbancoMunicipio;
    }

    public void setProcuradorbancoMunicipio(String procuradorbancoMunicipio) {
        this.procuradorbancoMunicipio = nvl(procuradorbancoMunicipio);
    }

}