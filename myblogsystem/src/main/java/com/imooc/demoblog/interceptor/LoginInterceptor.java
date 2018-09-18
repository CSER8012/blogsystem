package com.imooc.demoblog.interceptor;


import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(request.getSession().getAttribute("currentUser") == null) {

            System.out.println(request.getServletPath());
            response.sendRedirect("/adminer");
            return false;
        }

        return true;
    }
}
