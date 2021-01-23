package guru.sfg.brewery.config;

import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorConfig;
import com.warrenstrange.googleauth.ICredentialRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationEventPublisher;
import org.springframework.security.authentication.DefaultAuthenticationEventPublisher;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;
import java.util.concurrent.TimeUnit;

/**
 * This is a bean required for persistence token and validate the last login
 * from this table provided right from spring security
 *
 * create table persistent_logins (
 * username varchar(64) not null,
 * series varchar(64) primary key,
 * token varchar(64) not null,
 * last_used timestamp not null
 * );
 *
 */
@Configuration
public class SecurityBean {

    @Bean
    GoogleAuthenticator googleAuthenticator(ICredentialRepository credentialRepository) {

        GoogleAuthenticatorConfig.GoogleAuthenticatorConfigBuilder configBuilder =
                new GoogleAuthenticatorConfig.GoogleAuthenticatorConfigBuilder();

        configBuilder.setTimeStepSizeInMillis(TimeUnit.SECONDS.toMillis(60))
                .setWindowSize(10)
                .setNumberOfScratchCodes(0);

        GoogleAuthenticator googleAuthenticator = new GoogleAuthenticator(configBuilder.build());
        googleAuthenticator.setCredentialRepository(credentialRepository);
        return googleAuthenticator;
    }

    // Add auth event bean
    @Bean
    public AuthenticationEventPublisher authenticationEventPublisher (ApplicationEventPublisher applicationEventPublisher) {
        return new DefaultAuthenticationEventPublisher();
    }
    // Add repo bean
    @Bean
    public PersistentTokenRepository persistentTokenRepository (DataSource dataSource) {

        JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
        jdbcTokenRepository.setDataSource(dataSource);
        return jdbcTokenRepository;
    }
}
