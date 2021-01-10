package guru.sfg.brewery.bootstrap;

import guru.sfg.brewery.domain.security.Authority;
import guru.sfg.brewery.domain.security.User;
import guru.sfg.brewery.repositories.security.AuthorityRepository;
import guru.sfg.brewery.repositories.security.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class UserDataLoader implements CommandLineRunner {

    private final UserRepository userRepository;
    private final AuthorityRepository authorityRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        loadSecurityData();
    }

    private void loadSecurityData() {

        Authority authorityAdmin = null;
        if (!authorityRepository.findByRole("ADMIN").isPresent()) {
            authorityAdmin = authorityRepository.save(Authority.builder().role("ADMIN").build());
            log.debug("Authority loaded - 1");
        }

        Authority authorityUser = null;
        if (!authorityRepository.findByRole("USER").isPresent()) {
            authorityUser = authorityRepository.save(Authority.builder().role("USER").build());
            log.debug("Authority loaded - 2");
        }

        Authority authorityCustomer = null;
        if (!authorityRepository.findByRole("CUSTOMER").isPresent()) {
            authorityCustomer = authorityRepository.save(Authority.builder().role("CUSTOMER").build());
            log.debug("Authority loaded - 3");
        }

        if(null != authorityAdmin) {
            userRepository.save(User.builder()
                    .username("spring")
                    .password(passwordEncoder.encode("guru"))
                    .authority(authorityAdmin) // here is the charm of @singular :) we are adding a singular instance of data
                    .build());
            log.debug("User loaded - 1");
        }

        if (null != authorityUser) {
            userRepository.save(User.builder()
                    .username("user")
                    .password(passwordEncoder.encode("password"))
                    .authority(authorityUser)
                    .build());
            log.debug("User loaded - 2");
        }

        if (null != authorityCustomer) {
            userRepository.save(User.builder()
                    .username("scott")
                    .password(passwordEncoder.encode("tiger"))
                    .authority(authorityCustomer)
                    .build());
            log.debug("User loaded - 3");
        }

        log.debug("Authority count: " + authorityRepository.count());
        log.debug("User count: " + userRepository.count());
    }
}
