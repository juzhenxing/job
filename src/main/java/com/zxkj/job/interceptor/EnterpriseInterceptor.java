package com.zxkj.job.interceptor;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class EnterpriseInterceptor extends HandlerInterceptorAdapter {

    //在请求处理之前进行调用（Controller方法调用之前
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
//        System.out.printf("preHandle被调用");
        HttpSession httpSession = httpServletRequest.getSession();
        Object undergraduatePo = httpSession.getAttribute("enterpriseVo");
        if(undergraduatePo == null){
            String errorMessage = java.net.URLEncoder.encode("您还未登录","utf-8");
            httpServletResponse.sendRedirect(httpServletRequest.getContextPath() + "/enterprise/login?errorMessage=" + errorMessage);
            return false;
        }
        return true;    //如果false，停止流程，api被拦截
    }

}
