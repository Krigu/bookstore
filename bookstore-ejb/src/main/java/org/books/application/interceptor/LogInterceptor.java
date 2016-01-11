package org.books.application.interceptor;

import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;
import org.apache.log4j.Logger;

public class LogInterceptor {
    
    private static final Logger LOGGER = Logger.getLogger(LogInterceptor.class);
    
    @AroundInvoke
    public Object intercept(InvocationContext context) throws Exception {
        LOGGER.info("Interceptor invoked for  " + context.getMethod().getName());
        LOGGER.info(context.getParameters().length+" parameter(s) is detected");
        try {
            return context.proceed();
        } finally {
            LOGGER.info("End of interception for  " + context.getMethod().getName());
        }
    }
}
