package org.secondhand.secondhandhousebackend.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.secondhand.secondhandhousebackend.DTO.Result;
import org.secondhand.secondhandhousebackend.entity.Users;
import org.secondhand.secondhandhousebackend.service.UsersService;
import org.secondhand.secondhandhousebackend.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UsersService usersService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 获取请求路径和方法
        String requestPath = request.getRequestURI();
        String method = request.getMethod();
        
        // 定义不需要JWT验证的路径（登录和注册接口、文件下载、Swagger文档）
        boolean isPublicPath = requestPath.equals("/users/login") 
                || requestPath.equals("/users/register")
                || requestPath.startsWith("/files/download/")  // 文件下载不需要验证
                || requestPath.startsWith("/swagger-ui")  // Swagger UI
                || requestPath.startsWith("/v3/api-docs")  // Swagger API文档
                || requestPath.startsWith("/swagger-resources")  // Swagger资源
                || requestPath.startsWith("/error")
                || requestPath.startsWith("/css/")
                || requestPath.startsWith("/js/")
                || requestPath.startsWith("/images/")
                || requestPath.equals("/favicon.ico");
        
        // 如果是公开路径，直接放行
        if (isPublicPath) {
            return true;
        }
        
        // 处理OPTIONS预检请求（CORS）
        if ("OPTIONS".equalsIgnoreCase(method)) {
            return true;
        }
        
        // 其他所有路径都需要JWT验证
        // 1. 从请求头中获取token
        String authHeader = request.getHeader("Authorization");
        String token = jwtUtil.getTokenFromHeader(authHeader);

        // 2. 检查token是否存在
        if (token == null || token.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(objectMapper.writeValueAsString(Result.fail("请先登录")));
            return false;
        }

        // 3. 验证token
        if (!jwtUtil.validateToken(token)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(objectMapper.writeValueAsString(Result.fail("Token无效或已过期")));
            return false;
        }

        // 4. 从token中获取用户ID，查询用户信息
        try {
            Integer userId = jwtUtil.getUserIdFromToken(token);
            Users user = usersService.getById(userId);
            
            if (user == null) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write(objectMapper.writeValueAsString(Result.fail("用户不存在")));
                return false;
            }

            // 5. 将用户信息放到request attribute中，供后续使用
            request.setAttribute("user", user);
            request.setAttribute("userId", userId);
            
            return true;
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(objectMapper.writeValueAsString(Result.fail("Token解析失败")));
            return false;
        }
    }
}