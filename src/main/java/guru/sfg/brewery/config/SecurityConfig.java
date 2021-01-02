package guru.sfg.brewery.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@Slf4j
public class SecurityConfig extends WebSecurityConfigurerAdapter {

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
        .password("{noop}guru")
        .roles("ADMIN")
        .and()
        .withUser("user")
                // specify no password encoder in SpEL (spring Expression language)
        .password("{noop}password")
        .roles("USER")
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
