package org.tcc.relatorio.util.jasper;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import org.primefaces.context.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tcc.relatorio.util.jasper.LayOutOrientacao.Orientacao;

/**
 * Classe para geração de relatórios no formato PDF e XLS.
 * @author eloy
 * @since 07/11/2014
 */

public class ReportPadrao {

    private static final Logger logger = LoggerFactory.getLogger(ReportPadrao.class);
    private final String separador = java.io.File.separator;   
    private Orientacao orientacaoRelatorio = Orientacao.PAISAGEM;

    private String arquivo = "relatorio";
    
    private Image logo;

    private List dados;
    private String campos;
    private String titulos;
    private String larguras;
    private String cabecalho1;
    private String cabecalho2;
    private String cabecalho3;
    private String filtro;
    private int colunas;
    private int[] largs;

    /**
     * Método Construtor
     */
    public ReportPadrao() {
    }

    /**
     * Faz a validação da lista de dados, retornando uma mensagem de que não
     * existe registros caso esta lista esteja vazia. Se a lista de dados 
     * tiver pelo menos um registro, o agrupador das opções de exportação 
     * sera exibido.
     * @param dados Lista de dados para exportação.
     */
    public void validarLista(List dados) {
        if (dados == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Não há registros para exportar!", "Erro"));
        } else if (dados.isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Não há registros para exportar!", "Erro"));
        } else {
            RequestContext.getCurrentInstance().execute("ovl.show()");
        }
    }

    /**
     * Monta os titulos das colunas do relatório, de acordo com as informações
     * da lista de dados.
     * @return Retorna um <b>PdfPTable</b> contendo os titulos das colunas.
     * @throws DocumentException 
     */
    private PdfPTable titulo() throws DocumentException{
        PdfPTable table = new PdfPTable(colunas);
        table.setWidths(largs);
        for (String titulo : titulos.split(";")) {
            Font font = new Font(FontFamily.TIMES_ROMAN, 10, Font.BOLD);
            PdfPCell cel = new PdfPCell(); 
            cel.setBorderWidthTop(0);
            cel.setBorderWidthLeft(0);
            cel.setBorderWidthRight(0);
            cel.setBorderWidthBottom(1);
            Paragraph parag=new Paragraph(titulo, font);
            cel.addElement(parag);
            table.addCell(cel);
            logger.info("coluna: {}", titulo);
        }
        return table;
    }
    
    /**
     * Monta o cabeçalho do relatório, contendo os titulos e o logotipo.
     * @return Retorna um <b>PdfPTable</b> contendo o cabeçalho do relatório.
     * @throws DocumentException 
     */
    private PdfPTable cabecalho() throws DocumentException{
        PdfPTable cabecalho = new PdfPTable(3);
        cabecalho.setWidths(new int[]{20,60,20});

        logo.setAlignment(Element.ALIGN_LEFT);

        Font font1 = new Font(FontFamily.TIMES_ROMAN, 15, Font.BOLD);
        Font font2 = new Font(FontFamily.TIMES_ROMAN, 13, Font.NORMAL);
        Font font3 = new Font(FontFamily.TIMES_ROMAN, 11, Font.ITALIC);
        Font font4 = new Font(FontFamily.TIMES_ROMAN, 10, Font.NORMAL);
        
        Paragraph paragrafo1=new Paragraph(cabecalho1,font1);
        Paragraph paragrafo2=new Paragraph(cabecalho2,font2);
        Paragraph paragrafo3=new Paragraph(cabecalho3,font3);
        Paragraph paragrafo4=new Paragraph(filtro,font4);

        paragrafo1.setAlignment(Element.ALIGN_CENTER);
        paragrafo2.setAlignment(Element.ALIGN_CENTER);
        paragrafo3.setAlignment(Element.ALIGN_CENTER);
        paragrafo4.setAlignment(Element.ALIGN_CENTER);
        
        paragrafo1.setSpacingAfter(1);
        paragrafo2.setSpacingAfter(3);
        paragrafo3.setSpacingAfter(5);
        paragrafo4.setSpacingAfter(5);

        PdfPCell celulaLogo = new PdfPCell(logo); 
        celulaLogo.setBorderWidth(0);

        PdfPCell celulaCabecalho = new PdfPCell(); 
        celulaCabecalho.addElement(paragrafo1);
        celulaCabecalho.addElement(paragrafo2);
        celulaCabecalho.addElement(paragrafo3);
        celulaCabecalho.addElement(paragrafo4);
        celulaCabecalho.setBorderWidth(0);
        
        PdfPCell celulaData = new PdfPCell(); 
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        celulaData.setBorderWidth(0);
        Font font = new Font(FontFamily.TIMES_ROMAN, 10, Font.NORMAL);
        Paragraph dataAtual = new Paragraph(sdf.format(new Date()), font);
        dataAtual.setSpacingAfter(3);
        dataAtual.setSpacingBefore(3);
        dataAtual.setAlignment(Element.ALIGN_RIGHT);
        celulaData.addElement(dataAtual);

        cabecalho.addCell(celulaLogo);
        cabecalho.addCell(celulaCabecalho);
        cabecalho.addCell(celulaData);
        
        return cabecalho;
    }

    /**
     * Método para criação do rodapé do relatório. Nele estara contido a numeração das páginas.
     * @param writer
     * @param document
     * @param pagina 
     */
    private void rodape(PdfWriter writer, Document document, int pagina){
        Rectangle page = document.getPageSize();
        PdfPTable foot = new PdfPTable(1);
        Font font = new Font(FontFamily.TIMES_ROMAN, 11, Font.ITALIC);
        PdfPCell cell = new PdfPCell(new Phrase("Página: " + String.valueOf(pagina), font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
        cell.setBorderWidthTop(1);
        cell.setBorderWidthLeft(0);
        cell.setBorderWidthRight(0);
        cell.setBorderWidthBottom(0);
        foot.addCell(cell);
        foot.setTotalWidth(page.getWidth() - document.leftMargin() - document.rightMargin());
        foot.writeSelectedRows(0, -1, document.leftMargin(), document.bottomMargin(), writer.getDirectContent());
    }

    /**
     * Gera o relatório em formato PDF.
     * @param outputStream 
     * @throws DocumentException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     * @throws ClassNotFoundException
     * @throws NoSuchMethodException
     * @throws IOException 
     */
    private void geraPDF(OutputStream outputStream) throws DocumentException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, ClassNotFoundException, NoSuchMethodException, IOException{
        Document doc;
        int lpp;
        logger.info("orientação: {}",this.orientacaoRelatorio);
        if (this.orientacaoRelatorio == Orientacao.RETRATO) {
            doc = new Document(PageSize.A4, 10, 10, 40, 40);
            lpp = 34;
        } else {
            doc = new Document(PageSize.A4.rotate(), 10, 10, 40, 40);
            lpp = 21;
        }
        PdfWriter pdfWriter = PdfWriter.getInstance(doc, outputStream);
        doc.open();
        
        int pagina = 0;
        int registros = 0;
        int registrosPorPagina = 999;
        boolean zebra = false;
        PdfPTable table;
        for (int linha = 1; linha <= dados.size(); linha++) {
            registros++;
            if (registrosPorPagina >= lpp){
                pagina++;
                doc.newPage();
                doc.add(this.cabecalho());
                doc.add(this.titulo());
                zebra = false;
                registrosPorPagina=0;
                rodape(pdfWriter, doc, pagina);
            }
            table = new PdfPTable(colunas);
            table.setWidths(largs);
            for (String campo : campos.split(";")) {
                Object dadosObj = dados.get(linha-1);
                String entidade = dados.get(0).getClass().getCanonicalName();
                Object retobj = null;
                for (String cmp : campo.split("->")) {
                    Class c = Class.forName(entidade);
                    
                    Method metodo;
                    try {
                        metodo = c.getMethod("get" + cmp.substring(0,1).toUpperCase() + cmp.substring(1) );
                    } catch (NoSuchMethodException e) {
                        metodo = c.getMethod("is" + cmp.substring(0,1).toUpperCase() + cmp.substring(1) );
                    }
                    retobj = metodo.invoke(dadosObj);
                    
                    dadosObj = retobj;
                    if (retobj != null) {
                        entidade = retobj.getClass().getCanonicalName();
                    }
                }
                String retval = "";
                if (retobj != null) {
                    if (retobj.getClass().getCanonicalName().contains("Date") || 
                        retobj.getClass().getCanonicalName().contains("Timestamp")) {
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                        retval = sdf.format(retobj);
                    } else {
                        retval = retobj.toString();
                    }
                }
                
                Font font = new Font(FontFamily.TIMES_ROMAN, 8, Font.NORMAL);
                Paragraph paragrafo = new Paragraph(retval, font);
                paragrafo.setAlignment(Element.ALIGN_LEFT);
                PdfPCell celula = new PdfPCell();
                celula.setBorderWidth(0);
                celula.setFixedHeight(18);
                if (zebra) { celula.setBackgroundColor(BaseColor.LIGHT_GRAY); }
                celula.addElement(paragrafo);                
                
                table.addCell(celula);
            }
            zebra = !zebra;
            doc.add(table);
            registrosPorPagina++;
        }
        Font font = new Font(FontFamily.TIMES_ROMAN, 11, Font.ITALIC);
        String textoRegistros;
        if (registros == 1) { textoRegistros = "Um Registro Impresso"; }
        else { textoRegistros = "Total de Registros Impressos: " + String.valueOf(registros); }
        Paragraph totalRegistros = new Paragraph(textoRegistros, font);
        totalRegistros.setAlignment(Element.ALIGN_CENTER);
        doc.add(totalRegistros);

        doc.close();
        outputStream.flush();
        outputStream.close();
    }
    
    /**
     * Padroniza a separação dos campos obtidos nos parâmetros da chamada do componente
     * de exportação do relatório. O caracter padrão é ponto e virgula (;), com essa
     * padronização, torna-se possivel o uso dos seguintes caracteres para separação
     * dos campos:
     * (\) barra invertida;
     * (/) barra normal;
     * (,) virgula;
     * (:) dois pontos;
     * (;) ponto e virgula.
     * @param string dados para validação.
     * @return Retorna a mesma String de entrada, com a validação efetuada.
     */
    private String validaStrings(String string) {
        String seps[] = new String[]{"\"","/",",",":"};
        for (String sep : seps) { string = string.replaceAll(sep, ";"); }
        return string;
    }

    public void geraArquivos(Reports r) {
        this.dados = (List) r.getRegistros();
        this.campos = validaStrings((String) r.getParametros().get("campos"));
        this.titulos = validaStrings((String) r.getParametros().get("titulos"));
        this.larguras = validaStrings((String) r.getParametros().get("larguras"));
        this.cabecalho1 = (String) r.getParametros().get("cabecalho1");
        this.cabecalho2 = (String) r.getParametros().get("cabecalho2");
        this.cabecalho3 = (String) r.getParametros().get("cabecalho3");
        this.filtro = (String) r.getParametros().get("filtro");
        this.colunas = this.campos.split(";").length;
        this.largs = new int[colunas];
        for (int i = 0; i < colunas; i++){ this.largs[i] = Integer.parseInt(this.larguras.split(";")[i]); }

        try {
            String extensao = "pdf";            
            FacesContext fc = FacesContext.getCurrentInstance();

            ServletContext scontext = (ServletContext) fc.getExternalContext().getContext();
            String caminho = scontext.getRealPath(separador + "img");
            logo = Image.getInstance(caminho + separador + "logo_prodesp2.png");
            logo.scalePercent(80, 80);

            HttpServletResponse response = (HttpServletResponse) fc.getExternalContext().getResponse();
            response.reset();
            response.setContentType("application/" + extensao);
            response.setHeader("Content-Disposition", "attachment; filename=\"" + this.arquivo + "." + extensao + "\"");
            response.setDateHeader("Expires", 0);
            OutputStream outputStream;
            outputStream = response.getOutputStream();
            
            geraPDF(outputStream);

            fc.responseComplete();
        } catch (IOException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ex.getMessage(), "Erro!"));
        } catch (DocumentException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ex.getMessage(), "Erro!"));
        } catch (ClassNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ex.getMessage(), "Erro!"));
        } catch (NoSuchMethodException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ex.getMessage(), "Erro!"));
        } catch (SecurityException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ex.getMessage(), "Erro!"));
        } catch (IllegalAccessException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ex.getMessage(), "Erro!"));
        } catch (IllegalArgumentException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ex.getMessage(), "Erro!"));
        } catch (InvocationTargetException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ex.getMessage(), "Erro!"));
        }
    }

    public void setArquivo(String arquivo) {
        this.arquivo = arquivo;
    }
    
}