/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tcc.relatorio.util;

import javax.mail.util.ByteArrayDataSource;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.MultiPartEmail;
import org.apache.commons.mail.SimpleEmail;

/**
 *
 * @author jwsouza
 */
public class EmailUtil {

    private static final String HOSTNAME = "smtp.gmail.com";
    private static final String USERNAME = "wdison5";
    private static final String PASSWORD = System.getProperty("email.senha");
    private static final String EMAILORIGEM = "wdison5@gmail.com";

    public static void sendEmailAttachment(org.tcc.relatorio.dominio.Email dadosEmail) throws EmailException {
        String relatorioGerencial = "Relatorio Gerencial";
        MultiPartEmail email = new MultiPartEmail();
        email.setTLS(true);
        email.setSmtpPort(587);
        email.setHostName(HOSTNAME);
        email.setAuthenticator(new DefaultAuthenticator(USERNAME, PASSWORD));
        email.addTo(dadosEmail.getDestino(), dadosEmail.getNome());
        email.setFrom(EMAILORIGEM, "Sistema de " + relatorioGerencial);
        email.setSubject(relatorioGerencial);
        email.setMsg(relatorioGerencial);

        for (byte[] pdfBytes : dadosEmail.getAnexos()) {
            email.attach(new ByteArrayDataSource(pdfBytes, "application/pdf"),
                    "document.pdf", relatorioGerencial,
                    EmailAttachment.ATTACHMENT);
        }
        
        // send the email
        email.send();
    }
    
    public static Email conectaEmailSimples() throws EmailException {
        Email email = new SimpleEmail();
        email.setHostName(HOSTNAME);
        email.setSmtpPort(587);
        email.setAuthenticator(new DefaultAuthenticator(USERNAME, PASSWORD));
        email.setTLS(false);
        email.setFrom(EMAILORIGEM);
        return email;
    }

    public static void enviaEmail() throws EmailException {
        Email email = new SimpleEmail();
        email = conectaEmailSimples();
        email.setSubject("Relatorio Gerencial!");
        email.setMsg("Relatorio Atualizados.");
        email.addTo("email de destino");
        String resposta = email.send();
    }
    
    public static void sendEmailAttachment_old(String emailDestino, String nome, byte[] pdfBytes) throws EmailException {
        String relatorioGerencial = "Relatorio Gerencial";
        MultiPartEmail email = new MultiPartEmail();
        email.setTLS(true);
        email.setSmtpPort(587);
        email.setHostName(HOSTNAME);
        email.setAuthenticator(new DefaultAuthenticator(USERNAME, PASSWORD));
        email.addTo(emailDestino, nome);
        email.setFrom(EMAILORIGEM, "Sistema de " + relatorioGerencial);
        email.setSubject(relatorioGerencial);
        email.setMsg(relatorioGerencial);

        email.attach(new ByteArrayDataSource(pdfBytes, "application/pdf"),
                "document.pdf", relatorioGerencial,
                EmailAttachment.ATTACHMENT);
        
        // send the email
        email.send();
    }
}
