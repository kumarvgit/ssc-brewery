package guru.sfg.brewery.config;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationEventPublisher;
import org.springframework.security.authentication.DefaultAuthenticationEventPublisher;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;

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
