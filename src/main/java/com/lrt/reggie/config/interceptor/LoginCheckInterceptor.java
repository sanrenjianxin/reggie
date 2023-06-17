package com.lrt.reggie.config.interceptor;

import com.alibaba.fastjson.JSON;
import com.lrt.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Slf4j
public class LoginCheckInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 1. 判断登录状态，如果已经登录，则直接放行
        if (request.getSession().getAttribute("employee") != null) {
            return true;
        }
        log.info("被拦截的请求路径{}",request.getRequestURI());
        // 2. 如果未登录则返回登录结果,通过输出流的方式向客户端传送数据
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
        return true;
    }
}
