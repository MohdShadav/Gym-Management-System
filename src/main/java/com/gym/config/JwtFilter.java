package com.gym.config;

import com.gym.model.User;
import com.gym.repository.UserRepository;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class JwtFilter implements Filter {

    @Autowired
    private JwtUtil jwtUtil;   // ✅ FIX: inject

    @Autowired
    private UserRepository userRepository;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;

        String authHeader = req.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {

            String token = authHeader.substring(7);

            if (jwtUtil.validateToken(token)) {

                String email = jwtUtil.extractEmail(token);

                // 🔥 DB se user fetch karo
                User user = userRepository.findByEmail(email).orElse(null);

                if (user != null) {

                    // 🔥 Role ko authority me convert karo
                    List<SimpleGrantedAuthority> authorities =
                            List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole()));

                    UsernamePasswordAuthenticationToken auth =
                            new UsernamePasswordAuthenticationToken(email, null, authorities);

                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            }
        }

        chain.doFilter(request, response);
    }
}