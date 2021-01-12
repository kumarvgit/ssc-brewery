package guru.sfg.brewery.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.StringUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public abstract class AbstractRestAuthFilter extends AbstractAuthenticationProcessingFilter {

    protected AbstractRestAuthFilter(RequestMatcher requiresAuthenticationRequestMatcher) {
        super(requiresAuthenticationRequestMatcher);
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;


        if (logger.isDebugEnabled()) {
            logger.debug("Request is to process authentication");
        }

        try {
            Authentication authResult = attemptAuthentication(request, response);

            if (null != authResult) {
                successfulAuthentication(request, response, chain, authResult);
            } else {
                // continue the filter chain to allow basic authentication
                chain.doFilter(request, response);
            }

        } catch (AuthenticationException aEx) {

            log.error("Authentication failed",  aEx);
            // handle bad credentials
            unsuccessfulAuthentication(request, response, aEx);
        }

    }


    @Override
    public Authentication attemptAuthentication(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws AuthenticationException, IOException, ServletException {
//        String userName = getUsername(httpServletRequest);
//        userName = (null != userName) ? userName : "";
//
//        String password = getPassword(httpServletRequest);
//        password = (null != password ? password : "");
//
//        log.debug("Authentication user: " + userName);
//        // get token
//        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userName, password);
//
//        if (!StringUtils.isEmpty(userName)) {
//            // set the token
//            return this.getAuthenticationManager().authenticate(token);
//        } else {
//            return null;
//        }

        {
            String userName = getUsername(httpServletRequest);
            String password = getPassword(httpServletRequest);

            if (userName == null) {
                userName = "";
            }

            if (password == null) {
                password = "";
            }

            log.debug("Authenticating User: " + userName);

            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userName, password);

            if (!StringUtils.isEmpty(userName)) {
                return this.getAuthenticationManager().authenticate(token);
            } else {
                return null;
            }
        }

    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response, FilterChain chain, Authentication authResult)
            throws IOException, ServletException {

        if (logger.isDebugEnabled()) {
            logger.debug("Authentication success. Updating SecurityContextHolder to contain: "
                    + authResult);
        }

        SecurityContextHolder.getContext().setAuthentication(authResult);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request,
                                              HttpServletResponse response, AuthenticationException failed)
            throws IOException, ServletException {
        SecurityContextHolder.clearContext();

        if (log.isDebugEnabled()) {
            log.debug("Authentication request failed: " + failed.toString(), failed);
            log.debug("Updated SecurityContextHolder to contain null Authentication");
        }


        // custom handler for authentication
        response.sendError(HttpStatus.UNAUTHORIZED.value(),
                HttpStatus.UNAUTHORIZED.getReasonPhrase());
    }


    protected abstract String getPassword(HttpServletRequest httpServletRequest);

    protected abstract String getUsername(HttpServletRequest httpServletRequest);

}

