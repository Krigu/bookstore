package org.books.application.interceptor;

import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LogInterceptor {
    
    private static final Logger LOGGER = Logger.getLogger(LogInterceptor.class.getName());
    
    @AroundInvoke
    public Object intercept(InvocationContext context) throws Exception {
        LOGGER.log(Level.INFO, "Interceptor invoked for  {0}", context.getMethod().getName());
        LOGGER.log(Level.INFO, "{0} parameter(s) is detected", context.getParameters().length);
        try {
            return context.proceed();
        } finally {
            LOGGER.log(Level.INFO, "End of interception for  {0}", context.getMethod().getName());
        }
    }
}
