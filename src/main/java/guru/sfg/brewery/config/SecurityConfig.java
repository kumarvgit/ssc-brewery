package guru.sfg.brewery.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.LdapShaPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;

@Configuration
@Slf4j
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    PasswordEncoder passwordEncoder() {
//        Noop password encoder
//        return NoOpPasswordEncoder.getInstance();

//        LDAP password encoder
//        return new LdapShaPasswordEncoder();

//        SHA 256 encoder
//        return new StandardPasswordEncoder();

//          BCrypt encoder
//        return new BCryptPasswordEncoder();

//        using a factory to get all different types of algos
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests(authorize -> {
                    authorize.antMatchers("/","/webjars/**", "/login", "/resources/**").permitAll();
                })
                // Above authorization needs to be done before generic otherwise we will not get the bypass
                .authorizeRequests(authorize -> {
                    authorize
                            .antMatchers("/beers/find", "/beers*").permitAll() // adding find beer to permit all
                            .antMatchers(HttpMethod.GET, "/api/v1/beer/**").permitAll() // Permitting on path with only get requests
                            .mvcMatchers(HttpMethod.GET, "/api/v1/beerUpc/{upc}").permitAll() // using mvc matchers
                            ;
                })
                // Ant matcher for beer service
                .authorizeRequests()
                .anyRequest().authenticated()
                .and()
                .formLogin().and()
                .httpBasic();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
        .withUser("spring")
                // specify no password encoder in SpEL (spring Expression language)

        // use LDAP encoder
//        .password("{SSHA}pzmyIka3tGtN0ptiuY/RtPaHKSfr+YHH+2PYyA==")

        // SHA 256 encoder
//        .password("5a5db2b9f1bd84a7ce46bd096ca9271f3819ecbaf61ce47cf8d20eaae7004ea80e196a18c8532912")

        // BCrypt encoder
        .password("{bcrypt}$2a$10$8FaWyk5vlogMrDZt1t8b3etFBcwJgWzDQQ1d9wAaZUunR75.gizIm")
        .roles("ADMIN")
//                // using and is called Fluent API

        .and()
        .withUser("user")

// specify no password encoder in SpEL (spring Expression language)
//      .password("{noop}password")

//      use bean to supply encoder
//      LDAP encoder
//      .password("{SSHA}GxAtMrccecHcPTAtFv20Eikx0vOUT0ItTU0hWg==")

        // SHA 256 encoder
        .password("{sha256}5a5db2b9f1bd84a7ce46bd096ca9271f3819ecbaf61ce47cf8d20eaae7004ea80e196a18c8532912")

        // BCrypt encoder
//        .password("$2a$10$4b9rjFaT7Kv6do0ANbDhwO4xCgsm307emQOahYqDU2X8Q5vK7NSgS")
        .roles("USER")

        .and()
        .withUser("scott")
        // using LDAP encoder
        .password("{ldap}{SSHA}GW0AHJ3Wav8dDXOt9bx+6cUzyOFh3jjDwMX42g==")

         // SHA 256 encoder
//         .password("83f2e696f86dc7df0918b8b81d7ff581f0eda18c75a693b83d717d57b444aaa7e0b08946a7029535")

          // BCrypt encoder
//         .password("$2a$10$/8DKY6LrYQbZ.gLwpXl50OsKYuy9bMfWY1ybtlcghuEfQ0nsSZzbO")
         .roles("CUSTOMER")
        ;
    }

// commenting this after adding
// guru.sfg.brewery.config.SecurityConfig.configure(org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder)
//    /**
//     * provide own user details service overriding below
//     * spring.security.user.name=spring
//     * spring.security.user.password=guru
//     * @return {@link UserDetailsService}
//     */
//    @Override
//    @Bean
//    protected UserDetailsService userDetailsService() {
//
//        UserDetails admin = User.withDefaultPasswordEncoder()
//                .username("spring")
//                .password("guru")
//                .roles("ADMIN")
//                .build();
//
//        UserDetails user = User.withDefaultPasswordEncoder()
//                .username("user")
//                .password("password")
//                .roles("USER")
//                .build();
//
//        return new InMemoryUserDetailsManager(admin, user);
//    }
}
