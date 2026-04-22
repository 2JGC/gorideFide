package com.goridemoto.config;

import com.goridemoto.service.impl.CustomUserDetailsService;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final CustomUserDetailsService customUserDetailsService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;

    public SecurityConfig(CustomUserDetailsService customUserDetailsService,
                          BCryptPasswordEncoder passwordEncoder,
                          OAuth2SuccessHandler oAuth2SuccessHandler) {
        this.customUserDetailsService = customUserDetailsService;
        this.passwordEncoder = passwordEncoder;
        this.oAuth2SuccessHandler = oAuth2SuccessHandler;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customUserDetailsService).passwordEncoder(passwordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
            .antMatchers(
                "/", "/book-now", "/book-now/**", "/transaction-completed",
                "/gallery", "/contact", "/login", "/register", "/about",
                "/forgot-password", "/reset-password",
                "/faq", "/faqs", "/terms", "/cambiar-idioma",
                "/css/**", "/img/**", "/uploads/**"
            ).permitAll()
            .antMatchers("/admin/**").hasRole("ADMIN")
            .antMatchers("/perfil/**").authenticated()
            .anyRequest().authenticated()
            .and()
            // Login con email/password
            .formLogin()
                .loginPage("/login")
                .usernameParameter("email")
                .defaultSuccessUrl("/", false)
                .permitAll()
            .and()
            // Login con Meta (Facebook)
            .oauth2Login()
                .loginPage("/login")
                .successHandler(oAuth2SuccessHandler)
                .permitAll()
            .and()
            .logout()
                .logoutSuccessUrl("/")
                .permitAll();
    }
}
