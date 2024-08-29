package com.example.jobportal.config;


import com.example.jobportal.service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class WebSecurityConfig {


    private final String[] publicUrl = {"/",
            "/global-search/**",
            "/register",
            "/register/**",
            "/webjars/**",
            "/resources/**",
            "/assets/**",
            "/css/**",
            "/summernote/**",
            "/js/**",
            "/*.css",
            "/*.js",
            "/*.js.map",
            "/fonts**", "/favicon.ico", "/resources/**", "/error"};

    private final CustomUserDetailsService customUserDetailsService;
    private final CustomAuthSuccessHandler customAuthSuccessHandler;

    public WebSecurityConfig(CustomUserDetailsService customUserDetailsService, CustomAuthSuccessHandler customAuthSuccessHandler) {
        this.customUserDetailsService = customUserDetailsService;
        this.customAuthSuccessHandler = customAuthSuccessHandler;
    }

    @Bean
    protected SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.authenticationProvider(authenticationProvider());

        http.authorizeHttpRequests(auth -> {
            auth.requestMatchers(publicUrl).permitAll();
            auth.anyRequest().authenticated();
        });

        http.formLogin(form -> form.loginPage("/login")
                        .permitAll()
                        .successHandler(customAuthSuccessHandler))
                .logout(logout -> {
                    logout.logoutUrl("/logout");
                    logout.logoutSuccessUrl("/");
                })
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable);

        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {

        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setPasswordEncoder(passwordEncoder());
        authProvider.setUserDetailsService(customUserDetailsService);

        return authProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
