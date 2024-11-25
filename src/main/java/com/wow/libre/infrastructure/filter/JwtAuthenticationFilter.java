package com.wow.libre.infrastructure.filter;


import com.fasterxml.jackson.databind.*;
import com.wow.libre.domain.constant.*;
import com.wow.libre.domain.exception.*;
import com.wow.libre.domain.port.in.jwt.*;
import com.wow.libre.domain.shared.*;
import com.wow.libre.infrastructure.filter.dto.*;
import io.jsonwebtoken.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.apache.commons.lang3.*;
import org.apache.logging.log4j.*;
import org.slf4j.Logger;
import org.slf4j.*;
import org.springframework.http.*;
import org.springframework.lang.*;
import org.springframework.security.authentication.*;
import org.springframework.security.core.*;
import org.springframework.security.core.context.*;
import org.springframework.security.web.authentication.*;
import org.springframework.stereotype.*;
import org.springframework.web.filter.*;

import java.io.*;
import java.util.*;

import static com.wow.libre.domain.constant.Constants.*;


@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private static final Logger LOGGER = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    private final JwtPort jwtPort;

    public JwtAuthenticationFilter(JwtPort jwtPort) {
        this.jwtPort = jwtPort;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    @NonNull HttpServletResponse response, @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        GenericResponse<Void> responseBody = new GenericResponse<>();

        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String transactionId = request.getHeader(Constants.HEADER_TRANSACTION_ID);
        ThreadContext.put(Constants.CONSTANT_UNIQUE_ID, transactionId);

        LOGGER.info("{} Transaction {}", request.getRequestURI(), transactionId);

        try {

            if (StringUtils.isEmpty(authHeader) || !StringUtils.startsWith(authHeader, "Bearer ")) {
                filterChain.doFilter(request, response);
                return;
            }

            RequestWrapper requestWrapper = new RequestWrapper(request);
            ResponseWrapper responseWrapper = new ResponseWrapper(response);

            final String jwt = authHeader.substring(7);
            final String email = jwtPort.extractUsername(jwt);
            final Long userId = jwtPort.extractUserId(jwt);

            requestWrapper.setHeader(Constants.HEADER_EMAIL, email);
            requestWrapper.setHeader(HEADER_USER_ID, String.valueOf(userId));

            if (SecurityContextHolder.getContext().getAuthentication() == null) {

                if (jwtPort.isTokenValid(jwt)) {
                    Collection<GrantedAuthority> authority = jwtPort.extractRoles(jwt);

                    UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(email, null,
                                    authority);
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            }

            filterChain.doFilter(requestWrapper, responseWrapper);
            response.setStatus(responseWrapper.getStatus());
            response.setContentType(request.getContentType());
            response.getOutputStream().write(responseWrapper.getByteArray());
        } catch (GenericErrorException e) {
            responseBody.setMessage(e.getMessage());
            responseBody.setTransactionId(e.transactionId);
            response.setStatus(e.httpStatus.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.getOutputStream().write(new ObjectMapper().writeValueAsBytes(responseBody));
        } catch (ExpiredJwtException e) {
            responseBody.setMessage("Invalid JWT, has expired");
            responseBody.setCode(401);
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.getOutputStream().write(new ObjectMapper().writeValueAsBytes(responseBody));
        }
    }
}
