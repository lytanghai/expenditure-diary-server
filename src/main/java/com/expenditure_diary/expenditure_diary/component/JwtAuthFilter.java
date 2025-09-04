package com.expenditure_diary.expenditure_diary.component;

import com.expenditure_diary.expenditure_diary.module.repository.UserRepository;
import com.expenditure_diary.expenditure_diary.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private UserRepository userRepo;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) {
        try {
            String path = request.getRequestURI();

            // Skip login/register endpoints
            if (path.startsWith("/api/auth/login") || path.startsWith("/api/auth/register")) {
                filterChain.doFilter(request, response);
                return;
            }

            String header = request.getHeader("Authorization");
            if (header != null && header.startsWith("Bearer ")) {
                String token = header.substring(7);
                if (jwtUtil.validateJwt(token)) {
                    String username = jwtUtil.getUsernameFromJwt(token);
                    var user = userRepo.findByUsername(username).orElse(null);
                    if (user != null) {
                        UsernamePasswordAuthenticationToken auth =
                                new UsernamePasswordAuthenticationToken(user, null, null);
                        auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(auth);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("JWT Error: " + e.getMessage());
        }
        try {
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}