package guru.sfg.brewery.config;

import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.http.HttpServletRequest;

public class RestUrlAuthFilter extends AbstractRestAuthFilter{


    protected RestUrlAuthFilter(RequestMatcher requiresAuthenticationRequestMatcher) {
        super(requiresAuthenticationRequestMatcher);
    }

    @Override
    protected String getUsername(HttpServletRequest httpServletRequest) {
        return httpServletRequest.getParameter("apiKey");
    }

    @Override
    protected String getPassword(HttpServletRequest httpServletRequest) {
        return httpServletRequest.getParameter("apiSecret");
    }
}
