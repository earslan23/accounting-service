package com.accounting.ilab.filter;


import com.accounting.ilab.model.response.base.ErrorResponse;
import com.accounting.ilab.service.JwtService;
import com.accounting.ilab.service.UserService;
import com.accounting.ilab.util.ApiResponseUtil;
import com.accounting.ilab.util.LogBean;
import com.accounting.ilab.util.MessageBean;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.LocaleResolver;

import java.io.IOException;
import java.util.Locale;
import java.util.UUID;

@Slf4j
@Order(1)
@Component
@RequiredArgsConstructor
public class RequestFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserService userService;
    private final LogBean logBean;
    private final LocaleResolver localeResolver;
    private final MessageBean messageBean;
    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response, @NonNull FilterChain filterChain)
            throws ServletException, IOException {


        CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
        characterEncodingFilter.setEncoding("UTF-8");
        characterEncodingFilter.setForceEncoding(true);
        request.setCharacterEncoding("UTF-8");

        try {
            Locale locale = localeResolver.resolveLocale(request);
            messageBean.setLocale(locale);
        } catch (Exception ex) {
            messageBean.setLocale(new Locale("tr"));
            log.error("RequestFilter exception : {}", ApiResponseUtil.getFormattedMessage(ex.getMessage()));
        }

        log.info("Starting a transaction for req : {}", request.getRequestURI());
        logBean.setTraceId(UUID.randomUUID().toString());


        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        String jwt;
        String userEmail;
        if (StringUtils.isEmpty(authHeader) || !StringUtils.startsWith(authHeader, "Bearer ")) {
            filterChain.doFilter(request, response);
            log.info("Committing a transaction for req : {}", request.getRequestURI());
            return;
        }

        try {
            jwt = authHeader.substring(7);
            userEmail = jwtService.extractUserName(jwt);
        } catch (Exception e) {
            handleException(response, e);
            filterChain.doFilter(request, response);
            return;
        }

        if (StringUtils.isNotEmpty(userEmail)
                && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userService.userDetailsService()
                    .loadUserByUsername(userEmail);
            if (jwtService.isTokenValid(jwt, userDetails)) {
                SecurityContext context = SecurityContextHolder.createEmptyContext();
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                context.setAuthentication(authToken);
                SecurityContextHolder.setContext(context);
            }
        }
        filterChain.doFilter(request, response);
        log.info("Committing a transaction for req : {}", request.getRequestURI());
    }

    private void handleException(HttpServletResponse response, Exception ex) throws IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json");
        log.error("Exception for token : {}", ex);
        ErrorResponse errorResponse = ErrorResponse
                .builder()
                .message(ApiResponseUtil.getFormattedMessage(HttpStatus.UNAUTHORIZED.getReasonPhrase()))
                .httpStatus(HttpStatus.UNAUTHORIZED.value())
                .build();
        this.objectMapper.writeValue(response.getOutputStream(), ApiResponseUtil.generateGenericErrorResponse(errorResponse));
    }

}
