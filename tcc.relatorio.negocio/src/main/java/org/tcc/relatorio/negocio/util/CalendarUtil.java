/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tcc.relatorio.negocio.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 *
 * @author jwsouza
 */
public class CalendarUtil {
    public static String formata(Calendar c, String formato){
        if(c==null)c=Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(formato);
        return sdf.format(c.getTime());
    }
}
