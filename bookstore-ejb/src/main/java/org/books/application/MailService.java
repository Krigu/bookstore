
package org.books.application;

import javax.ejb.Local;

@Local
public interface MailService {
    
    public void sendMail(String emailAdr, String subject, String body);
    
}
