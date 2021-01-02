package guru.sfg.brewery.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
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
                            ;
                })
                // Ant matcher for beer service
                .authorizeRequests()
                .anyRequest().authenticated()
                .and()
                .formLogin().and()
                .httpBasic();
    }
}
