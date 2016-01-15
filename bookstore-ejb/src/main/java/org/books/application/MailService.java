
package org.books.application;

import javax.ejb.Local;
import javax.validation.constraints.NotNull;

@Local
public interface MailService {

    public void sendMail(@NotNull String emailAdr, @NotNull String subject, @NotNull String body);

}
