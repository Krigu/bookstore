
package org.books.application;

import org.books.application.interceptor.ValidationInterceptor;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.ejb.Asynchronous;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


@Stateless(name = "MailService")
@Interceptors(ValidationInterceptor.class)
public class MailServiceBean implements MailService {

    private static final Logger LOGGER = Logger.getLogger(MailServiceBean.class.getName());
    @Resource(name="mail/bookstore") 
    private Session mailSession;

    @Override
    @Asynchronous
    public void sendMail(String emailAdr, String subject, String body) {
        MimeMessage message = new MimeMessage(mailSession);
        try {
            message.setFrom(new InternetAddress(mailSession.getProperty("mail.from")));
            InternetAddress[] address = {new InternetAddress(emailAdr)};
            message.setRecipients(RecipientType.TO, address);
            message.setSubject(subject);
            message.setText(body);
            Transport.send(message);
        } catch (MessagingException ex) {
            LOGGER.log(Level.WARNING,"Sending mail to " + emailAdr + " failed: " + ex.getMessage(), ex);
        }    
    }

}
