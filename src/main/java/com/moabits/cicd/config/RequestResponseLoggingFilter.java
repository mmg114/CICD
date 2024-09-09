package com.moabits.cicd.config;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
public class RequestResponseLoggingFilter implements Filter {
    private static final Logger logger = LoggerFactory.getLogger("org.springframework.web");


    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        // Envolver el request y response en wrappers
        ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper((HttpServletRequest) request);
        ContentCachingResponseWrapper wrappedResponse = new ContentCachingResponseWrapper((HttpServletResponse) response);

        // Continuar con el filtro
        chain.doFilter(wrappedRequest, wrappedResponse);

        // Log the incoming request (después de la ejecución para asegurar el contenido está completo)
        var req = getRequestBody(wrappedRequest);
        logger.info("Request: method={}, uri={}, params={}, body={}",
                wrappedRequest.getMethod(),
                wrappedRequest.getRequestURI(),
                wrappedRequest.getQueryString(),
                req);
        var resp = getResponseBody(wrappedResponse);
        // Log the outgoing response
        logger.info("Response: status={}, body={}",
                wrappedResponse.getStatus(),
                resp);

        // Asegurarse de que el contenido de la respuesta se escriba correctamente
        wrappedResponse.copyBodyToResponse();
    }

    private String getRequestBody(ContentCachingRequestWrapper request) {
        byte[] content = request.getContentAsByteArray();
        if (content.length > 0) {
            return new String(content, StandardCharsets.UTF_8);
        }
        return "[No Body]";
    }

    private String getResponseBody(ContentCachingResponseWrapper response) {
        byte[] content = response.getContentAsByteArray();
        if (content.length > 0) {
            return new String(content, StandardCharsets.UTF_8);
        }
        return "[No Body]";
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // No initialization necessary
    }

    @Override
    public void destroy() {
        // No cleanup necessary
    }
}
