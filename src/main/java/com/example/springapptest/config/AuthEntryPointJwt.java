package com.example.springapptest.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

@Component
public class AuthEntryPointJwt implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException authenticationException)
            throws IOException, ServletException {
        httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
        String message;
        final Exception exception = (Exception) httpServletRequest.getAttribute("Exception");

        if (exception != null) {
            byte[] body = new ObjectMapper().writeValueAsBytes(Collections.singletonMap("cause", exception.toString()));
            httpServletResponse.getOutputStream().write(body);
        } else {
            if (authenticationException.getCause() != null) {
                message = authenticationException.getCause().toString() + " " + authenticationException.getMessage();
            }
            else {
                message=authenticationException.getMessage();
            }
            byte[] body = new ObjectMapper().writeValueAsBytes(Collections.singletonMap("error", message));

            httpServletResponse.getOutputStream().write(body);
        }

    }
}
