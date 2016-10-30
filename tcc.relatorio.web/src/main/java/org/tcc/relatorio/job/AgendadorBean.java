package org.tcc.relatorio.job;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.URL;
import java.net.URLConnection;
import javax.ejb.Schedule;
import javax.ejb.Stateless;

/**
 *
 * @author Jose Wdison de Souza <wdison@hotmail.com>
 */
@Stateless
public class AgendadorBean implements Serializable {

    @Schedule(hour = "21", persistent = false)
    public void jobSendEmail() {
        try {
            URL url = new URL("http://localhost:8080/relatorio/public/email.jsf");
            URLConnection connection = url.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                System.out.println(inputLine);
            }
            in.close();
            System.out.println("Email enviado com sucesso!");
        } catch (Exception e) {
            System.out.println("Falha ao enviar email!");

        }
    }
}
