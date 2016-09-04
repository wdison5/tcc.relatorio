package org.tcc.relatorio.util;

import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ResourceBundle;
import javax.faces.context.FacesContext;
import javax.management.MBeanException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tcc.relatorio.mbean.exception.util.MBeanExceptionUtil;

/**
 *
 * @author Jose Wdison
 */
public class BuscaCep {

    Logger LOGGER = LoggerFactory.getLogger(BuscaCep.class);

    public Cep cep(String cepSolicitado) throws MBeanException {
        Cep c = null;
        try {
            String resourceBundleName = "configuracao";
            String resourceBundleKey = "url.cep.prodesp";
//            String resourceBundleKey = "url.cep";
            String urlBase;
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ResourceBundle bundle = facesContext.getApplication().getResourceBundle(facesContext, resourceBundleName);
            urlBase = bundle.getString(resourceBundleKey);
            String montaURL = (urlBase + cepSolicitado);

            URL url = new URL(montaURL);
            InputStream openStream = url.openStream();
            Reader r = new InputStreamReader(openStream, "UTF8");
            BufferedReader br = new BufferedReader(r);
            String output = br.readLine();
            Gson gson = new Gson();
            output = output.replaceAll("\\[", "").replaceAll("\\]", "");
            c = gson.fromJson(output, Cep.class);
            br.close();
            r.close();
            openStream.close();
            return c;
        } catch (MalformedURLException e) {
            throw MBeanExceptionUtil.prepara(LOGGER, e);
        } catch (IOException e) {
            throw MBeanExceptionUtil.prepara(LOGGER, e);
        }
    }
}
