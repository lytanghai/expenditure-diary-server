package com.expenditure_diary.expenditure_diary.component;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class RateLimit implements Filter {

    // Map to store request counts per IP address
    private final Map<String, RequestInfo> requestCountsPerIpAddressInc = new ConcurrentHashMap<>();

    private static class RequestInfo {
        AtomicInteger count = new AtomicInteger(0);
        long firstRequestTime = System.currentTimeMillis();
    }

    // Maximum requests allowed per minute
    @Value("${server.rate_limit:50}")
    private int MAX_REQUESTS_PER_MINUTE;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        String clientIpAddress = httpServletRequest.getRemoteAddr();

        // Get or create RequestInfo
        RequestInfo info = requestCountsPerIpAddressInc.computeIfAbsent(clientIpAddress, k -> new RequestInfo());

        // Reset count if more than 3 minutes have passed
        long now = System.currentTimeMillis();
        if (now - info.firstRequestTime > 3 * 60 * 1000) { // 3 minutes
            info.count.set(0);
            info.firstRequestTime = now;
        }

        int requests = info.count.incrementAndGet();

        if (httpServletRequest.getRequestURI().contains("/login") && requests > 5) {
            httpServletResponse.setStatus(423);
            httpServletResponse.getWriter().write("Too many incorrect username or password! Please try again later.");
            return;
        }

        if (!httpServletRequest.getRequestURI().contains("/login") && requests > MAX_REQUESTS_PER_MINUTE) {
            httpServletResponse.setStatus(429);
            httpServletResponse.getWriter().write("Too many requests. Please try again later.");
            return;
        }

        chain.doFilter(request, response);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Optional: Initialization logic, if needed
    }

    @Override
    public void destroy() {
        // Optional: Cleanup resources, if needed
    }

}
