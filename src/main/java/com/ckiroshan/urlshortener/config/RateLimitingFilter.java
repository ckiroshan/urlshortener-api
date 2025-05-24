package com.ckiroshan.urlshortener.config;

import com.ckiroshan.urlshortener.service.RateLimitingService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class RateLimitingFilter extends OncePerRequestFilter {
    private final RateLimitingService rateLimitingService;
    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws IOException, ServletException {
        // Extract username or fallback to IP address for rate limit tracking
        String username = request.getUserPrincipal() != null ?
                request.getUserPrincipal().getName() : null;
        String ip = getClientIp(request);
        String identifier = username != null ? username : ip;

        // Set rate limiting headers in the response
        setRateLimitHeaders(response, identifier);

        // If request is not allowed, send 429 response
        if (!rateLimitingService.allowRequest(request, username)) {
            sendRateLimitExceededResponse(response, identifier);
            return;
        }

        // Continue with the request processing
        filterChain.doFilter(request, response);
    }

    // Adds headers: limit, remaining, and reset time to response
    private void setRateLimitHeaders(HttpServletResponse response, String identifier) {
        if (rateLimitingService.getConfig().isEnabled()) {
            response.setHeader("X-RateLimit-Limit",
                    String.valueOf(rateLimitingService.getConfig().getCapacity()));
            response.setHeader("X-RateLimit-Remaining",
                    String.valueOf(rateLimitingService.getRemainingTokens(identifier)));
            response.setHeader("X-RateLimit-Reset",
                    rateLimitingService.getResetTime(identifier));
        }
    }

    // Returns a 429 Too Many Requests JSON response
    private void sendRateLimitExceededResponse(HttpServletResponse response, String identifier) throws IOException {
        response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        objectMapper.writeValue(response.getWriter(), Map.of(
                "error", "Too many requests",
                "message", "Rate limit exceeded",
                "retryAfter", rateLimitingService.getResetTime(identifier) + " seconds"
        ));
    }

    // Extract client IP from header or request
    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        return (ip == null || ip.isEmpty()) ? request.getRemoteAddr() : ip;
    }
}