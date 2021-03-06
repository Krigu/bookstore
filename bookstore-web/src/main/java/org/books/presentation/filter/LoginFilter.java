package org.books.presentation.filter;

import org.books.presentation.bean.account.CustomerBean;

import javax.inject.Inject;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 *
 * @author tjd
 */
@WebFilter(urlPatterns = "/user/*")
public class LoginFilter implements Filter {

    @Inject
    private CustomerBean cb;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException { 
        //Nothing
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        //check if the customerBean doesn't exist and is not Authenticated
        if (!cb.isAuthenticated()) {
            //Cast the ServletRequest
            HttpServletRequest httpServletRequest = (HttpServletRequest) request;
            //Cast the ServletResponse
            HttpServletResponse httpServletResponse = (HttpServletResponse) response;
       
            String contextPath = httpServletRequest.getContextPath();
            String menuId = httpServletRequest.getParameter("menuId");

            //Set the target for the login
            cb.setNavigationTarget(httpServletRequest.getRequestURI().substring(contextPath.length())+"?faces-redirect=true&menuId="+menuId);
            //Redirect to the login page
            httpServletResponse.sendRedirect(contextPath + "/login.xhtml?menuId="+menuId);
        } else {
            //User is anthententificated
            chain.doFilter(request, response);
        }

    }

    @Override
    public void destroy() {
        //Nothing
    }

}
