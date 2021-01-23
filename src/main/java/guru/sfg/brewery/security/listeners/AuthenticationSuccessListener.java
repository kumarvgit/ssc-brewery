package guru.sfg.brewery.security.listeners;

import guru.sfg.brewery.domain.security.LoginSuccess;
import guru.sfg.brewery.domain.security.User;
import guru.sfg.brewery.repositories.security.LoginSuccessRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class AuthenticationSuccessListener {

    private final LoginSuccessRepository loginSuccessRepository;
    // register as listener
    @EventListener
    public void listen(AuthenticationSuccessEvent event) {
        log.debug("User logged in");
        // another way to have the object prepared and get the data out at the end
        LoginSuccess.LoginSuccessBuilder loginSuccessBuilder = LoginSuccess.builder();
        Object source = event.getSource();

        UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) source;
        Object principal = token.getPrincipal();
        User user  = null;
        if(principal instanceof User) {

            user = (User) principal;
            log.debug("User logged in: " + user.getUsername());
            loginSuccessBuilder.user(user);
        }

        Object details = ((UsernamePasswordAuthenticationToken) source).getDetails();
        if(details instanceof WebAuthenticationDetails) {
            WebAuthenticationDetails loginDetails = (WebAuthenticationDetails) details;

            log.debug(user.getUsername() + " logged in from IP " + loginDetails.getRemoteAddress());
            loginSuccessBuilder.sourceIp(loginDetails.getRemoteAddress());
        }
        LoginSuccess save = loginSuccessRepository.save(loginSuccessBuilder.build());
        log.debug("Saved login success: " + save.getId());
    }
}
