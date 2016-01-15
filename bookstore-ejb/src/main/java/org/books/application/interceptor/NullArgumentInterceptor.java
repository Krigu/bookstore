package org.books.application.interceptor;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

public class NullArgumentInterceptor {

    private static final Logger LOGGER = Logger.getLogger(NullArgumentInterceptor.class.getName());

    @AroundInvoke
    public Object intercept(InvocationContext context) throws Exception {
        for (Object object : context.getParameters()) {
            if (object == null) {
                LOGGER.log(Level.INFO, "Null argument was in methode  {0}", context.getMethod().getName());
                throw new IllegalArgumentException("Null argument");
            }
        }
        return context.proceed();
    }
}
