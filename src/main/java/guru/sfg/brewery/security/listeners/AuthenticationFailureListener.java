package guru.sfg.brewery.security.listeners;

import guru.sfg.brewery.domain.security.LoginFailure;
import guru.sfg.brewery.domain.security.User;
import guru.sfg.brewery.repositories.security.LoginFailureRepository;
import guru.sfg.brewery.repositories.security.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
@Slf4j
@RequiredArgsConstructor
public class AuthenticationFailureListener {

    private final LoginFailureRepository loginFailureRepository;
    private final UserRepository userRepository;
    @EventListener
    public void listen(AuthenticationFailureBadCredentialsEvent event) {

        LoginFailure.LoginFailureBuilder builder = LoginFailure.builder();
        log.debug("Bad creds supplied");

        Object source = event.getSource();
        Object principal;
        if(source instanceof UsernamePasswordAuthenticationToken) {
            Object details = ((UsernamePasswordAuthenticationToken) source).getDetails();

            principal = ((UsernamePasswordAuthenticationToken) source).getPrincipal();
            if(null != principal) {
                log.debug("Invalid login event from user: " + principal.toString());

                userRepository.findByUsername(principal.toString()).ifPresent(builder::user);

                builder.username(principal.toString());
            } else {
                log.debug("Invalid login event from user had null value");
            }
            if(details instanceof WebAuthenticationDetails) {
                String remoteAddress = ((WebAuthenticationDetails) details).getRemoteAddress();
                log.debug("Invalid login attempt for " + principal.toString() + " from IP" + remoteAddress);
                builder.sourceIp(remoteAddress);
            }

            LoginFailure save = loginFailureRepository.save(builder.build());
            log.debug("Failed login attempt saved: " + save.getId());
            if(null != save.getUser()) {
                lockUserAccount(save.getUser());
            }
        }


    }

    private void lockUserAccount(User user) {
        List<LoginFailure> allByUserAndCreateDateIsAfter = loginFailureRepository.findAllByUserAndCreateDateIsAfter(user, Timestamp.valueOf(LocalDateTime.now().minusDays(1)));

        if(allByUserAndCreateDateIsAfter.size() > 3) {
            user.setAccountNonLocked(false);
            User save = userRepository.save(user);
            log.debug(user.getUsername() + " is locked " + save.getAccountNonLocked());
        }
    }
}
