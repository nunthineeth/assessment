package com.kbtg.bootcamp.posttest.securityconfig;

import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.stereotype.Component;

import static org.springframework.security.config.Customizer.withDefaults;

@Component
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private final AuthenticationUserDetailService authenticationUserDetailService;
    private final JwtAuthFilter jwtAuthFilter;
    private final Http401UnauthorizedEntryPoint http401UnauthorizedEntryPoint;

    public SecurityConfig(AuthenticationUserDetailService authenticationUserDetailService, JwtAuthFilter authFilter, Http401UnauthorizedEntryPoint http401UnauthorizedEntryPoint) {
        this.authenticationUserDetailService = authenticationUserDetailService;
        this.jwtAuthFilter = authFilter;
        this.http401UnauthorizedEntryPoint = http401UnauthorizedEntryPoint;
    }

    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        /*http.authorizeHttpRequests((requests) -> requests.anyRequest().authenticated());
//        http.formLogin(withDefaults()); //close from login
        http.httpBasic(withDefaults());
        return http.build();
         */

        http.exceptionHandling((exception)-> exception.authenticationEntryPoint(http401UnauthorizedEntryPoint).accessDeniedPage("/error/accedd-denied"));

        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers("/v3/api-docs/**","/swagger-ui/**").permitAll()
                        .requestMatchers("/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/auth/authenticate").permitAll()
//                        .requestMatchers("/user").hasAnyRole("MEMBER", "ADMIN")
//                        .requestMatchers("/admin").hasAnyRole("ADMIN")
//                        .requestMatchers(HttpMethod.PUT, "/member").hasAuthority("MEMBER_UPDATE")
                        .anyRequest()
                        .authenticated())

                .addFilterBefore(jwtAuthFilter, BasicAuthenticationFilter.class)
//                .addFilterBefore(new ApiKeyAuthFilter(), BasicAuthenticationFilter.class)
                .httpBasic(withDefaults())
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    /***
     * user ที่เข้ามาต้องทำการ has password ด้วย BCrypt ก่อน แล้วทำการเทียบว่าตรงกันมั้ย ถ้าตรงก็ให้เข้ามาใช้ระบบเราได้
     * @return
     */
//    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public AuthenticationProvider authenticationProvider() {
        final DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(authenticationUserDetailService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }
}
