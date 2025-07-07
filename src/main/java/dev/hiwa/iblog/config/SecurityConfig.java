package dev.hiwa.iblog.config;

import dev.hiwa.iblog.security.JwtFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
@Configuration
public class SecurityConfig {

    private final UserDetailsService userDetailsService;
    private final JwtFilter jwtFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(org.springframework.security.config.http.SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/swagger-ui/**")
                        .permitAll()
                        .requestMatchers("/swagger-ui.html")
                        .permitAll()
                        .requestMatchers("/v3/api-docs/**")
                        .permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/posts/drafts")
                        .authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/v1/posts")
                        .authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/v1/posts/**")
                        .permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/categories/**")
                        .permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/tags/**")
                        .permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/v1/tags")
                        .permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/tags/**")
                        .permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/v1/auth/login")
                        .permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/v1/auth/register")
                        .permitAll()
                        .anyRequest()
                        .authenticated())
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

}
