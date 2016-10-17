package org.tcc.relatorio.util;

import java.io.File;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tcc.relatorio.negocio.validator.Validador;
import org.tcc.relatorio.util.jasper.ReportPadrao;
import org.tcc.relatorio.util.jasper.Reports;

/**
 * Classe para geracao de relatorios no formato PDF e XLS.
 * @author eloy
 * @since 07/11/2014
 */

@RequestScoped
@ManagedBean(name = "exportarArquivo")
public class ExportarArquivo  {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExportarArquivo.class);
    private String nomeArquivo;

    public String getNomeArquivo(String complemento) {
        SimpleDateFormat sd = new SimpleDateFormat("yyyyMMdd_HHmm");
        Date dataAtual = new Date(System.currentTimeMillis());
        this.nomeArquivo = sd.format(dataAtual) + "_SIPPH_" + complemento;

        return nomeArquivo;
    }

    /**
     * Faz a validacao da lista de dados, retornando uma mensagem de que nao
     * existe registros caso esta lista esteja vazia. Se a lista de dados
     * tiver pelo menos um registro, o agrupador das opcoes de exportacao 
     * sera exibido.
     * @param dados Lista de dados para exportacao.
     */
    public void validarLista(List dados) {
        if (dados!=null && dados.size()>0) {
            
        }
        if (!Validador.isColecao(dados)) {
            FacesUtil.addError("Sem registros para exportar!");
        } else {
            LOGGER.info("classe: {}", dados.getClass());
            FacesUtil.exec("ovl.show()");
        }
    }

    public void processaPDF(Reports r, String complementoDoNome) {
        LOGGER.info("Classe: {}", r.getClasse().getClass().getSimpleName());
        try {
            String caminho = FacesContext.getCurrentInstance().getExternalContext().getRealPath("WEB-INF") + "/classes/reports/";
            String arquivo = caminho + r.getNomeArquivo() + complementoDoNome + ".jasper";
            LOGGER.info("Arquivo Report: {}", arquivo);

            File file = new File(arquivo);

            if(file.exists()){
                FacesContext fc = FacesContext.getCurrentInstance();
                HttpServletResponse response = (HttpServletResponse) fc.getExternalContext().getResponse();
                HttpServletRequest  request  = (HttpServletRequest ) fc.getExternalContext().getRequest();

                // ParÃ¢metros
                HttpSession sessao = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
                File logoFile = new File(sessao.getServletContext().getRealPath(
                        System.getProperty("file.separator") + "img" + 
                        System.getProperty("file.separator") + "logo_report.png"));
                String caminhoLogo = logoFile.getAbsolutePath();
                LOGGER.info("Caminho Imagem: {}", caminhoLogo);
                r.getParametros().put("PATH_IMAGE_LOGO", caminhoLogo);
                r.getParametros().put("funcionario", "Funcionário: " + 
                        (String) request.getSession().getAttribute("userId") + " - " +
                        (String) request.getSession().getAttribute("username"));

                // Monta o relatÃ³rio
                JasperPrint print;
                if (complementoDoNome.contains("SQL")) {
                    Context initContext = new InitialContext();
                    DataSource ds = (DataSource) initContext.lookup("jdbc/sipphDS");
                    Connection conn = ds.getConnection();                    
                    print = JasperFillManager.fillReport(arquivo, r.getParametros(), conn);
                } else {
                    print = JasperFillManager.fillReport(arquivo, r.getParametros(), new JRBeanCollectionDataSource(r.getRegistros()));
                }
                byte[] reportpdf = JasperExportManager.exportReportToPdf(print);

                response.reset();
                response.setContentType("application/PDF");
                response.setHeader("Content-Disposition", "attachment; filename=\"" + getNomeArquivo(complementoDoNome.replace("SQL", "")) + ".pdf" + "\"");
                response.setDateHeader("Expires", 0);
                response.getOutputStream().write(reportpdf, 0, reportpdf.length);
                response.getOutputStream().flush();
                fc.responseComplete();
                fc.renderResponse();
            } else {
                LOGGER.info("Arquivo inexistente: {}", arquivo);
                ReportPadrao reportPadrao = new ReportPadrao();
                reportPadrao.setArquivo(getNomeArquivo(r.getNomeArquivo()));
                reportPadrao.geraArquivos(r);
            }
        } catch (Exception ex) {
            LoggerUtil.info(ex, LOGGER);
            LoggerUtil.info("Esta mensagem", ex, LOGGER, true);
        }
    }
    
    
    public void processaXLS(Object document) {
        HSSFWorkbook wb = (HSSFWorkbook) document;
        HSSFSheet sheet = wb.getSheetAt(0);
        HSSFRow header = sheet.getRow(0);

        HSSFCellStyle cellStyle = wb.createCellStyle();  
        cellStyle.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
        cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

        for(int i=0; i < header.getPhysicalNumberOfCells();i++) {
            HSSFCell cell = header.getCell(i);
            cell.setCellStyle(cellStyle);

            sheet.setColumnWidth(i, 10000);
        }
    }
}