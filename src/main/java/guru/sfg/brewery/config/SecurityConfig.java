package guru.sfg.brewery.config;

import guru.sfg.brewery.security.RestHeaderAuthFilter;
import guru.sfg.brewery.security.RestUrlAuthFilter;
import guru.sfg.brewery.security.SfgPasswordEncoderFactories;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@Slf4j
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    /**
     * create filter
     * @param authenticationManager
     * @return
     */
    public RestHeaderAuthFilter restHeaderAuthFilter  (AuthenticationManager authenticationManager) {

        RestHeaderAuthFilter filter = new RestHeaderAuthFilter(new AntPathRequestMatcher("/api/**"));
        filter.setAuthenticationManager(authenticationManager);
        return filter;
    }

    public RestUrlAuthFilter restUrlAuthFilter (AuthenticationManager authenticationManager) {
        RestUrlAuthFilter filter = new RestUrlAuthFilter(new AntPathRequestMatcher("/api/**"));
        filter.setAuthenticationManager(authenticationManager);
        return filter;
    }


    @Override
    public void configure(WebSecurity web) throws Exception {
        super.configure(web);
    }

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
//        return PasswordEncoderFactories.createDelegatingPasswordEncoder();

        // using custom encoder factory
        return SfgPasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

//         adding a header filter
        http.addFilterBefore(
                        restHeaderAuthFilter(authenticationManager()), UsernamePasswordAuthenticationFilter.class).csrf().disable();
        http.addFilterBefore(
                        restUrlAuthFilter(authenticationManager()), UsernamePasswordAuthenticationFilter.class);

        http.authorizeRequests(authorize -> {
                    authorize.antMatchers("/","/webjars/**", "/login", "/resources/**").permitAll();
                })
                // Above authorization needs to be done before generic otherwise we will not get the bypass
                .authorizeRequests(authorize -> {
                    authorize
                            .antMatchers("/h2-console/**").permitAll()
//                            .antMatchers("/beers/find", "/beers*").permitAll() // adding find beer to permit all
                            .antMatchers(HttpMethod.GET, "/api/v1/beer/**") // Permitting on path with only get requests
                                .hasAnyRole("ADMIN", "CUSTOMER", "USER")
                            .mvcMatchers(HttpMethod.GET, "/api/v1/beerUpc/{upc}").not().anonymous() // using mvc matchers
                            .mvcMatchers(HttpMethod.DELETE, "/api/v1/beer/**")
                                .hasRole("ADMIN") // do not append ROLE_
                            .mvcMatchers(HttpMethod.GET, "/brewery/api/v1/breweries")
                                .hasAnyRole("ADMIN", "CUSTOMER")
                            .mvcMatchers(HttpMethod.GET, "/brewery/breweries**")
                                .hasAnyRole("ADMIN", "CUSTOMER")
                            .mvcMatchers("/beers/find", "/beers/{beerId}")
                                .hasAnyRole("ADMIN", "CUSTOMER", "USER")
                            ;
                })
                // Ant matcher for beer service
                .authorizeRequests()
                .anyRequest().authenticated()
                .and()
                .formLogin().and()
                .httpBasic();

                // configuration of h2 to allow iFrames
                http.headers().frameOptions().sameOrigin();
    }


    // Bring in JpaUserDetailsRepositories
//    @Autowired
//    JpaUserDetailsService jpaUserDetailsService;
    /**
     * Provide authentication manager
     * @param auth
     * @throws Exception
     */
//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {


        // This is going to work perfectly, but there are no additional implementation this is goign to work just fine by annotating
//        auth.userDetailsService(this.jpaUserDetailsService);

//        IN-MEMORY AUTH BEGIN
        /*auth.inMemoryAuthentication()
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
//        .password("{ldap}{SSHA}GW0AHJ3Wav8dDXOt9bx+6cUzyOFh3jjDwMX42g==")


         // SHA 256 encoder
//         .password("83f2e696f86dc7df0918b8b81d7ff581f0eda18c75a693b83d717d57b444aaa7e0b08946a7029535")

          // BCrypt encoder
//         .password("$2a$10$/8DKY6LrYQbZ.gLwpXl50OsKYuy9bMfWY1ybtlcghuEfQ0nsSZzbO")

         // overriding default encoder with a strength of 15
         .password("{bcrypt10}$2a$10$wqdESomygPUJFE/aGY3DausS2X/Ag3MjjPpxjYvlId4MuXaxC.j8u")
         .roles("CUSTOMER")
        ;*/

//        IN-MEMORY AUTH END
//    }

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
