package com.spark.InventorySalesManager.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class IpAllowlistFilter extends OncePerRequestFilter  {

    private final AppProperties appProperties;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String clientIp = getClientIp(request);

        List<String> allowedIps = appProperties.getAllowedIps();

        if (allowedIps == null || allowedIps.isEmpty()) {
            // No IP restrictions applied if list empty (optional: you can deny all here)
            filterChain.doFilter(request, response);
            return;
        }

        if (!allowedIps.contains(clientIp)) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"Access denied from IP: " + clientIp + "\"}");
            return;
        }

        filterChain.doFilter(request, response);
    }

    private String getClientIp(HttpServletRequest request) {
        // Check X-Forwarded-For header if behind proxy/load balancer
        String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader != null && !xfHeader.isBlank()) {
            return xfHeader.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }

}
