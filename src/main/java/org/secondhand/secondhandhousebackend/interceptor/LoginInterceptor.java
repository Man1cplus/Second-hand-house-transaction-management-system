package org.secondhand.secondhandhousebackend.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.secondhand.secondhandhousebackend.entity.Users;
import org.springframework.web.servlet.HandlerInterceptor;

public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 1. 获取Session中的用户信息
        HttpSession session = request.getSession();
        Users user = (Users) session.getAttribute("user");

        // 2. 检查是否登录
        if (user == null) {
            // 3. 未登录则返回401错误
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"success\":false,\"errorMsg\":\"请先登录\"}");
            return false;
        }

        // 4. 已登录放行
        return true;
    }
}