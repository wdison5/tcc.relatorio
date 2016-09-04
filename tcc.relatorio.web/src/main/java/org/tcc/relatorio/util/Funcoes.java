package org.tcc.relatorio.util;

import java.io.ByteArrayInputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tcc.relatorio.cap.dominio.UsuarioEntity;
import org.tcc.relatorio.cap.negocio.UsuarioBC;
import org.tcc.relatorio.hammer.persistencia.exception.BCException;

/**
 *
 * @author eloy
*/
@RequestScoped
@ManagedBean(name = "funcoes")
public class Funcoes {
    
    private static final Logger logger = LoggerFactory.getLogger(Funcoes.class);

    @EJB(name = "UsuariooBC")
    private UsuarioBC usuarioBC;

    public static String usuarioLogado() {
        FacesContext facesContext = FacesContext.getCurrentInstance();    
        HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(false);    
        return (String) session.getAttribute("userId");
    }
    
    public String nomeUsuario(String userId) {
        UsuarioEntity usuarioEntity = new UsuarioEntity();
        usuarioEntity.setUserId(userId);
        try {
            return usuarioBC.consultar(usuarioEntity).getNome();
        } catch (BCException ex) {
            logger.info("Erro ao obter nome do usuário cuja ID é {}: {}", userId, ex.getMessage());
            return "";
        }
    }
    
    public static Date formataData(String data) {   
        if (data == null || data.equals(""))  
            return null;  
          
        Date date;
        try {
            DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            date = (java.util.Date)formatter.parse(data);
        } catch (ParseException e) {
            logger.info("Erro ao converter Data: {}", e.getMessage());
            return null;
        }
        return date;
    }
    
    public static String dataPorExtenso(Date data) {
        String[] diaDaSemana = {"-","Domingo","Segunda-feira","Terça-feira","Quarta-feira","Quinta-feira","Sexta-feira","Sábado"};
        String[] diaDoMes = {"Janeiro","Fevereiro","Março","Abril","Maio","Junho","Julho","Agosto","Setembro","Outubro","Novembro","Dezembro"};
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(data);
        int semana = calendar.get(Calendar.DAY_OF_WEEK);
        int mes = calendar.get(Calendar.MONTH);
        int dia = calendar.get(Calendar.DAY_OF_MONTH);
        int ano = calendar.get(Calendar.YEAR);
        String diaf = diaDaSemana[semana];
        String mesf = diaDoMes[mes];
        return diaf+", "+dia+" de "+mesf+" de "+ano;
    }

   
    public static String XlsToCsv(byte[] buffer) throws Exception {
        HSSFWorkbook workBook = new HSSFWorkbook(new ByteArrayInputStream(buffer));
        HSSFSheet aba1 = workBook.getSheetAt(0);
        Iterator<?> linhaIterator = aba1.rowIterator();

        List<List<HSSFCell>> celulaGrid = new ArrayList<List<HSSFCell>>();
        while (linhaIterator.hasNext()) {
            HSSFRow linha = (HSSFRow) linhaIterator.next();
            Iterator<?> celulaIterator = linha.cellIterator();
            List<HSSFCell> celulaLinhaList = new ArrayList<HSSFCell>();
            while (celulaIterator.hasNext()) {
                HSSFCell celula = (HSSFCell) celulaIterator.next();
                celulaLinhaList.add(celula);
            }
            celulaGrid.add(celulaLinhaList);
        }

        String retorno = "";
        for (List<HSSFCell> celulaLinhaLista : celulaGrid) {
            for (HSSFCell celulaLinhaLista1 : celulaLinhaLista) {
                HSSFCell celula = (HSSFCell) celulaLinhaLista1;
                
                String stringValorCelula;
                if (celula.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
                    if (HSSFDateUtil.isCellDateFormatted(celula)){
                        SimpleDateFormat formatoData;
                        if (HSSFDateUtil.isCellInternalDateFormatted(celula)){
                            formatoData = new SimpleDateFormat("dd/MM/yyyy");
                        } else {
                            formatoData = new SimpleDateFormat("HH:mm");
                        }
                        stringValorCelula = formatoData.format(celula.getDateCellValue()).replaceAll(";", ",").replaceAll("\r\n", " ");
                    } else {
                        stringValorCelula = celula.toString().replaceAll(";", ",").replaceAll("\r\n", " ");
                    }
                } else {
                    stringValorCelula = celula.toString().replaceAll(";", ",").replaceAll("\r\n", " ");
                }
                
                retorno+=stringValorCelula + ";";
            }
            retorno+="\r\n";
        }         
        return retorno;
    }

    public static long getHoras(String hora, SimpleDateFormat formatter) { 
        formatter.setTimeZone(TimeZone.getTimeZone("GMT")); 
        Date data; 
        try { 
            data = formatter.parse(hora); 
        } catch (ParseException e) { 
            return 0; 
        } 
        long rhora = data.getTime(); 
        return rhora; 
    } 
    
    public static Object getSessionObject(String objName) {
        FacesContext ctx = FacesContext.getCurrentInstance();
        ExternalContext extCtx = ctx.getExternalContext();
        Map<String, Object> sessionMap = extCtx.getSessionMap();
        return sessionMap.get(objName);
    }
}
