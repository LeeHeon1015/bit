package kr.co.bit.user_service.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Autowired
    private JwtAthenticationFilter jwtAthenticationFilter;
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public AuthenticationManager authenticationManager( 
        AuthenticationConfiguration authenticationConfiguration ) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();        
    }
    @Bean
    public SecurityFilterChain filterChain( HttpSecurity http ) throws Exception {
        http.csrf(
            csrf -> csrf.disable() 
        ).sessionManagement( 
            session -> session.sessionCreationPolicy( SessionCreationPolicy.STATELESS )
        ).authorizeHttpRequests(
            authz -> authz.requestMatchers( "/user/input", "/user/login", "/user/logout" ).permitAll()
                .requestMatchers( "/auth/input", "/auth/login", "/auth/logout" ).permitAll()
                // .requestMatchers( "/user/{id}" ).permitAll()
                .requestMatchers( "/user/{id}" ).hasRole("USER" )
                .anyRequest().authenticated()
        ).logout(
            logout -> logout.logoutUrl( "/user/logout" )
                .logoutSuccessHandler( new HttpStatusReturningLogoutSuccessHandler( HttpStatus.OK ) )
                .clearAuthentication( true )
                .invalidateHttpSession( true )
        );
        http.addFilterBefore( jwtAthenticationFilter,  UsernamePasswordAuthenticationFilter.class );
        return http.build();
    }
}
