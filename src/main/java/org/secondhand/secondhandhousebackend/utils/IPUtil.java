package org.secondhand.secondhandhousebackend.utils;

import jakarta.servlet.http.HttpServletRequest;

/**
 * IP工具类
 * 用于获取客户端真实IP地址
 */
public class IPUtil {
    
    /**
     * 获取客户端真实IP地址
     * 支持通过代理服务器访问的情况
     * 
     * @param request HttpServletRequest对象
     * @return 客户端真实IP地址
     */
    public static String getClientIP(HttpServletRequest request) {
        String ip = null;
        
        // 1. 尝试从X-Forwarded-For请求头获取IP（经过代理服务器）
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty() && !"unknown".equalsIgnoreCase(xForwardedFor)) {
            // X-Forwarded-For可能包含多个IP，第一个是真实IP
            int index = xForwardedFor.indexOf(',');
            if (index != -1) {
                ip = xForwardedFor.substring(0, index).trim();
            } else {
                ip = xForwardedFor.trim();
            }
        }
        
        // 2. 如果X-Forwarded-For获取不到，尝试从X-Real-IP获取
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        
        // 3. 如果还是获取不到，尝试从Proxy-Client-IP获取
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        
        // 4. 如果还是获取不到，尝试从WL-Proxy-Client-IP获取
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        
        // 5. 如果还是获取不到，使用request.getRemoteAddr()获取
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        
        // 6. 处理IPv6的本地回环地址
        if ("0:0:0:0:0:0:0:1".equals(ip) || "::1".equals(ip)) {
            ip = "127.0.0.1";
        }
        
        // 7. 如果IP为空，返回默认值
        if (ip == null || ip.isEmpty()) {
            ip = "未知";
        }
        
        return ip;
    }
}

