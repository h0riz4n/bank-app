package ru.yandex.account_service.configuration;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
            .csrf(AbstractHttpConfigurer::disable)
            .cors(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(authz -> {
                authz
                    .requestMatchers("/api/accounts").hasAuthority("user")
                    .requestMatchers("/api/balances/**").authenticated()
                    .anyRequest().permitAll();
            })
            .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> {
                JwtAuthenticationConverter jwtAuthConverter = new JwtAuthenticationConverter();
                jwtAuthConverter.setJwtGrantedAuthoritiesConverter(this::toGrantedAuthorities);
                jwt.jwtAuthenticationConverter(jwtAuthConverter);
            }))
            .sessionManagement(sessionManagment -> sessionManagment.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .build();
    }

    private Collection<GrantedAuthority> toGrantedAuthorities(Jwt jwt) {
        Map<String, List<String>> realmAccess = jwt.getClaim("realm_access");
        return realmAccess
            .get("roles")
            .stream()
            .map(SimpleGrantedAuthority::new)
            .collect(Collectors.toList());
    }
}
