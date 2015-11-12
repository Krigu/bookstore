/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.books.presentation.filter;

import java.io.IOException;
import javax.inject.Inject;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.books.presentation.bean.CustomerBean;

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
            cb.setLoginTarget(httpServletRequest.getRequestURI().substring(contextPath.length())+"?faces-redirect=true&menuId="+menuId);
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
