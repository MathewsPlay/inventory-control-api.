package com.matheuss.controle_estoque_api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // Array com os caminhos públicos do Swagger que o Spring Security deve ignorar
    private static final String[] SWAGGER_WHITELIST = {
        "/swagger-ui/**",
        "/v3/api-docs/**",
        "/swagger-resources/**",
        "/swagger-resources"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http ) throws Exception {
        http
            .csrf(csrf -> csrf.disable( )) // Desabilita a proteção CSRF (comum em APIs stateless)
            .authorizeHttpRequests(auth -> auth
                // Libera o acesso aos caminhos do Swagger definidos acima
                .requestMatchers(SWAGGER_WHITELIST).permitAll()
                
                // Por enquanto, permite o acesso a todas as outras requisições
                .anyRequest().permitAll() 
            );
        
        return http.build( );
    }
}
