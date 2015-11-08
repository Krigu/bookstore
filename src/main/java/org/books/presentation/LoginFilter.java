/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.books.presentation;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author tjd
 */
@WebFilter(urlPatterns = "/user/*")
public class LoginFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        //Nothing
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        //Cast the ServletRequest
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        //Cast the ServletResponse
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        //Find the customer bean in the session
        CustomerBean customerBean = (CustomerBean) httpServletRequest.getSession().getAttribute("customerBean");
        //check if the customerBean doesn't exist and is not Authenticated
        if (customerBean == null || !customerBean.isAuthenticated()) {
            //Redirect to the login page
            httpServletResponse.sendRedirect(httpServletRequest.getContextPath()+"/login.xhtml");
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
