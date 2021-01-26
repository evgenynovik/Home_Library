package com.netcracker.education.services.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private static final String PREFIX = "/library/v1/";
    private static final String[] AUTH_WHITELIST = {
            "/v2/api-docs",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui/**",
            "/webjars/**"
    };
    private final UserDetailsService userDetailsService;

    @Autowired
    public SecurityConfiguration(@Qualifier("customUserDetailsService") UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers(AUTH_WHITELIST).permitAll()
                .antMatchers(HttpMethod.GET, PREFIX + "books").permitAll()
                .antMatchers(HttpMethod.POST, PREFIX + "authors", PREFIX + "books", PREFIX + "genres",
                        PREFIX + "series").hasAuthority(Role.ROLE_ADMIN.name())
                .antMatchers(HttpMethod.PUT, PREFIX + "books/**", PREFIX + "authors/**",
                        PREFIX + "series/**", PREFIX + "genres/**").hasAuthority(Role.ROLE_ADMIN.name())
                .antMatchers(HttpMethod.DELETE, PREFIX + "books/**", PREFIX + "authors/**",
                        PREFIX + "series/**", PREFIX + "genres/**",
                        PREFIX + "notifications/*").hasAuthority(Role.ROLE_ADMIN.name())
                .antMatchers(HttpMethod.GET, PREFIX + "users/all", PREFIX + "cards/all",
                        PREFIX + "books/users/*", PREFIX + "users/borrowers/*",
                        PREFIX + "cards/*").hasAuthority(Role.ROLE_ADMIN.name())
                .antMatchers(HttpMethod.PATCH, PREFIX + "cards/*",
                        PREFIX + "books/**").hasAuthority(Role.ROLE_ADMIN.name())
                .antMatchers(HttpMethod.POST, PREFIX + "notifications").hasAuthority(Role.ROLE_USER.name())
                .antMatchers(HttpMethod.POST, PREFIX + "users").anonymous()
                .anyRequest().authenticated()
                .and()
                .httpBasic()
                .and()
                .logout()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .csrf()
                .disable();
    }

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }
}
