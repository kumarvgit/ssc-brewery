package guru.sfg.brewery.services.security;

import guru.sfg.brewery.domain.security.User;
import guru.sfg.brewery.repositories.security.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserUnlockService {

    private final UserRepository userRepository;

    @Scheduled(fixedRate = 300000)
    public void unlockAccounts() {

        log.debug("Running unlock account");

        List<User> allByAccountNonLockedAndLastModifiedDateIsBefore = userRepository.findAllByAccountNonLockedAndLastModifiedDateIsBefore(false,
                Timestamp.valueOf(LocalDateTime.now().minusSeconds(30)));

        if(!allByAccountNonLockedAndLastModifiedDateIsBefore.isEmpty()) {
            log.debug("Locked account found");
            allByAccountNonLockedAndLastModifiedDateIsBefore.forEach(p -> {
                p.setAccountNonLocked(true);
            });

            userRepository.saveAll(allByAccountNonLockedAndLastModifiedDateIsBefore);
        }
    }
}
