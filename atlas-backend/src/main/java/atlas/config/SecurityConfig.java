package atlas.config;

import atlas.security.JwtAccessDeniedHandler;
import atlas.security.JwtAuthenticationEntryPoint;
import atlas.security.JwtAuthenticationFilter;
import atlas.security.OAuth2AuthenticationSuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
@Configuration
public class SecurityConfig {

        private final JwtAuthenticationFilter jwtAuthenticationFilter;
        private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
        private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
        private final OAuth2AuthenticationSuccessHandler oauth2AuthenticationSuccessHandler;

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

                return http
                                .csrf(csrf -> csrf.disable())

                                .cors(cors -> {
                                })

                                .sessionManagement(session -> session.sessionCreationPolicy(
                                                SessionCreationPolicy.IF_REQUIRED))

                                .authorizeHttpRequests(auth -> auth
                                                .requestMatchers(
                                                                "/auth/register",
                                                                "/auth/login",
                                                                "/auth/recuperar-senha",
                                                                "/auth/resetar-senha",
                                                                "/oauth2/**",
                                                                "/login/oauth2/**")
                                                .permitAll()

                                                .anyRequest().authenticated())

                                .oauth2Login(oauth2 -> oauth2
                                                .successHandler(oauth2AuthenticationSuccessHandler)
                                                .failureHandler((request, response, exception) -> {

                                                        response.sendRedirect(
                                                                        "http://localhost:4200/login");
                                                }))

                                .exceptionHandling(exception -> exception
                                                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                                                .accessDeniedHandler(jwtAccessDeniedHandler))

                                .addFilterBefore(
                                                jwtAuthenticationFilter,
                                                UsernamePasswordAuthenticationFilter.class)

                                .build();
        }

        @Bean
        public PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }

}