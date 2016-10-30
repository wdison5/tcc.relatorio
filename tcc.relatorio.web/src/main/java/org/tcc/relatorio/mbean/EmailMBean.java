/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tcc.relatorio.mbean;

import com.itextpdf.text.DocumentException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import org.apache.commons.mail.EmailException;
import org.tcc.relatorio.cap.dominio.UsuarioEntity;
import org.tcc.relatorio.dominio.Email;
import org.tcc.relatorio.dominio.ProdutoEntity;
import org.tcc.relatorio.hammer.persistencia.exception.BCException;
import org.tcc.relatorio.negocio.TccRelatorioBC;
import org.tcc.relatorio.util.EmailUtil;
import org.tcc.relatorio.util.ExportarArquivo;
import org.tcc.relatorio.util.jasper.Reports;

/**
 *
 * @author Jose Wdison de Souza <a>wdison@hotmail.com</a>
 */
@RequestScoped
@ManagedBean
public class EmailMBean {

    @EJB(name = "RelatorioBC")
    private TccRelatorioBC relatorioBC;

    private Date dataPeriodoDe;
    private Date dataPeriodoAte;
    private Long idTipoProduto;
    private String tituloTela;

    public String enviar() throws ParseException, BCException, DocumentException, EmailException {
        Reports r = new Reports(this);
        r.getParametros().put("IMAGEM_CABECALHO", ExportarArquivo.getPathFile("imgTcc/apresentacao1.png"));
        r.getParametros().put("IMAGEM_RODAPE", ExportarArquivo.getPathFile("imgTcc/download.png"));
        r.getParametros().put("periodo", compoePeriodo());

        ExportarArquivo e = new ExportarArquivo();
        Email dadosEmail = new Email("wdison5@gmail.com", "Jose Wdison de Souza");
        
        for (String item : new String[]{"P+V", "B+V", "S+V"}) {
            inicializaRelatorio(item);
            r.setRegistros((ArrayList) pesquisar());
            byte[] gerarPDF = e.gerarPDF(r);
            dadosEmail.add(gerarPDF);
        }
        
        EmailUtil.sendEmailAttachment(dadosEmail);

        System.out.println("Email enviado com sucesso!");
        return "Email enviado com sucesso!";
    }

    public void inicializaRelatorio(String complemento) {
        switch (complemento) {
            case "P+V":
                tituloTela = "Pratos + Vendidos";
                setIdTipoProduto(1L);
                break;
            case "B+V":
                tituloTela = "Bebidas + Vendidas";
                setIdTipoProduto(2L);
                break;
            case "S+V":
                tituloTela = "Sobremesas + Vendidas";
                setIdTipoProduto(3L);
                break;
            case "P-V":
                tituloTela = "Pratos - Vendidos";
                setIdTipoProduto(1L);
                break;
            case "B-V":
                tituloTela = "Bebidas - Vendidas";
                setIdTipoProduto(2L);
                break;
            case "S-V":
                tituloTela = "Sobremesas - Vendidas";
                setIdTipoProduto(3L);
                break;
        }
    }

    public String compoePeriodo() {
        Date date = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.DAY_OF_MONTH, 1);
        c.set(Calendar.MONTH, Calendar.OCTOBER);
        this.setDataPeriodoDe(c.getTime());
        this.setDataPeriodoAte(new Date());

        SimpleDateFormat formatoData = new SimpleDateFormat("dd/MM/yyyy");
        if (getDataPeriodoDe() != null && getDataPeriodoAte() != null) {
            String dataDe = formatoData.format(getDataPeriodoDe());
            String dataAte = formatoData.format(getDataPeriodoAte());
            return "Período de " + dataDe + " a " + dataAte;
        } else {
            return "";
        }
    }

    public Date getDataPeriodoDe() {
        return dataPeriodoDe;
    }

    public void setDataPeriodoDe(Date dataPeriodoDe) {
        this.dataPeriodoDe = dataPeriodoDe;
    }

    public Date getDataPeriodoAte() {
        return dataPeriodoAte;
    }

    public void setDataPeriodoAte(Date dataPeriodoAte) {
        this.dataPeriodoAte = dataPeriodoAte;
    }

    public Long getIdTipoProduto() {
        return idTipoProduto;
    }

    public void setIdTipoProduto(Long idTipoProduto) {
        this.idTipoProduto = idTipoProduto;
    }

    public String getTituloTela() {
        return tituloTela;
    }

    public void setTituloTela(String tituloTela) {
        this.tituloTela = tituloTela;
    }

    public List<ProdutoEntity> pesquisar() throws BCException {
        UsuarioEntity usuarioLogado = new UsuarioEntity(1L);

        ProdutoEntity produtoInicial = new ProdutoEntity();
        ProdutoEntity produtoFinal = new ProdutoEntity();

        if (dataPeriodoDe != null) {
            produtoInicial.setDataReferencia(dataPeriodoDe);
        }
        if (dataPeriodoAte != null) {
            produtoFinal.setDataReferencia(dataPeriodoAte);
        }

        List<ProdutoEntity> set = relatorioBC.pesquisar(produtoInicial, produtoFinal, usuarioLogado, idTipoProduto);
        if (set != null) {
            return new ArrayList<ProdutoEntity>(set);
        }
        return null;
    }
}
