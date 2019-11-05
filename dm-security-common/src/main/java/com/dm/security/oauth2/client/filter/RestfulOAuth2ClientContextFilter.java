package com.dm.security.oauth2.client.filter;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.client.filter.OAuth2ClientContextFilter;
import org.springframework.security.oauth2.client.resource.UserRedirectRequiredException;
import org.springframework.security.oauth2.common.DefaultThrowableAnalyzer;
import org.springframework.security.web.util.ThrowableAnalyzer;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.NestedServletException;
import org.springframework.web.util.UriComponents;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 一个基于Restful风格的OAuth2ClientContextFilter，用于处理Oauth2Client的Redirect请求
 * 
 * {@link OAuth2ClientContextFilter}
 * 
 * @author ldwqh0@outlook.com
 * 
 * @since 0.2.1
 *
 */
public class RestfulOAuth2ClientContextFilter implements Filter, InitializingBean {

    /**
     * Key in request attributes for the current URI in case it is needed by rest
     * client code that needs to send a redirect URI to an authorization server.
     */
    public static final String CURRENT_URI = "currentUri";

    private ThrowableAnalyzer throwableAnalyzer = new DefaultThrowableAnalyzer();

    private ObjectMapper objectMapper;

    @Autowired(required = false)
    public void setObjectMapgger(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public void doFilter(ServletRequest servletRequest,
            ServletResponse servletResponse, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        request.setAttribute(CURRENT_URI, calculateCurrentUri(request));

        try {
            chain.doFilter(servletRequest, servletResponse);
        } catch (IOException ex) {
            throw ex;
        } catch (Exception ex) {
            // Try to extract a SpringSecurityException from the stacktrace
            Throwable[] causeChain = throwableAnalyzer.determineCauseChain(ex);
            UserRedirectRequiredException redirect = (UserRedirectRequiredException) throwableAnalyzer
                    .getFirstThrowableOfType(UserRedirectRequiredException.class, causeChain);
            if (redirect != null) {
                writeResponse(redirect, request, response);
            } else {
                if (ex instanceof ServletException) {
                    throw (ServletException) ex;
                }
                if (ex instanceof RuntimeException) {
                    throw (RuntimeException) ex;
                }
                throw new NestedServletException("Unhandled exception", ex);
            }
        }
    }

    /**
     * Redirect the user according to the specified exception.
     * 
     * @param e        The user redirect exception.
     * @param request  The request.
     * @param response The response.
     */
    protected void writeResponse(UserRedirectRequiredException e,
            HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String redirectUri = e.getRedirectUri();
        Map<String, String> body = new HashMap<String, String>();
        Map<String, String> requestParams = e.getRequestParams();
        for (Map.Entry<String, String> param : requestParams.entrySet()) {
            body.put(param.getKey(), param.getValue());
        }
        body.put("redirectUri", redirectUri);
        if (e.getStateKey() != null) {
            body.put("state", e.getStateKey());
        }
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.getWriter().write(objectMapper.writeValueAsString(body));
    }

    /**
     * Calculate the current URI given the request.
     * 
     * @param request The request.
     * @return The current uri.
     */
    protected String calculateCurrentUri(HttpServletRequest request)
            throws UnsupportedEncodingException {
        ServletUriComponentsBuilder builder = ServletUriComponentsBuilder
                .fromRequest(request);
        // Now work around SPR-10172...
        String queryString = request.getQueryString();
        boolean legalSpaces = queryString != null && queryString.contains("+");
        if (legalSpaces) {
            builder.replaceQuery(queryString.replace("+", "%20"));
        }
        UriComponents uri = null;
        try {
            uri = builder.replaceQueryParam("code").build(true);
        } catch (IllegalArgumentException ex) {
            // ignore failures to parse the url (including query string). does't
            // make sense for redirection purposes anyway.
            return null;
        }
        String query = uri.getQuery();
        if (legalSpaces) {
            query = query.replace("%20", "+");
        }
        return ServletUriComponentsBuilder.fromUri(uri.toUri())
                .replaceQuery(query).build().toString();
    }

    public void init(FilterConfig filterConfig) throws ServletException {
    }

    public void destroy() {
    }

    public void setThrowableAnalyzer(ThrowableAnalyzer throwableAnalyzer) {
        this.throwableAnalyzer = throwableAnalyzer;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (Objects.isNull(objectMapper)) {
            this.objectMapper = new ObjectMapper();
        }
    }

}
