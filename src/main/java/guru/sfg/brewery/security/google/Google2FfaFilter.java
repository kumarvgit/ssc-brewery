package guru.sfg.brewery.security.google;

import guru.sfg.brewery.domain.security.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.boot.autoconfigure.security.servlet.StaticResourceRequest;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
public class Google2FfaFilter extends GenericFilterBean {

    private final AuthenticationTrustResolver authenticationTrustResolver = new AuthenticationTrustResolverImpl();

    private final Google2faFailureHandler google2faFailureHandler = new Google2faFailureHandler();

    private final RequestMatcher urls2fs = new AntPathRequestMatcher("/user/verify2fa");

    private final RequestMatcher urlResource = new AntPathRequestMatcher("/resources/**");

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // get hold of static resources
        StaticResourceRequest.StaticResourceRequestMatcher staticResourceRequestMatcher =
                PathRequest.toStaticResources().atCommonLocations();

        // avoid the paths which do not need this verification
        if (urls2fs.matches(httpRequest)
                || urlResource.matches(httpRequest)
                ||staticResourceRequestMatcher.matcher(httpRequest).isMatch()) {
            filterChain.doFilter(httpRequest, httpResponse);
            return;
        }

        // logic
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(null != authentication && !authenticationTrustResolver.isAnonymous(authentication)) {
            log.debug("Process 2FA resolver");
            if(null != authentication.getPrincipal() && authentication.getPrincipal() instanceof User) {
                User user = (User) authentication.getPrincipal();

                if(user.getUseGoogle2fa() && user.getGoogle2faRequired()) {
                    log.debug("2FA acquired");
                    google2faFailureHandler.onAuthenticationFailure(httpRequest, httpResponse, null);

                    // stop the filter chain
                    return;
                }
            }
        }

        filterChain.doFilter(httpRequest, httpResponse);
    }
}
