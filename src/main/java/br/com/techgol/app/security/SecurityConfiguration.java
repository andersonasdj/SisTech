package br.com.techgol.app.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
	
	@Autowired private UserAuthenticationFilter filter;
	@Autowired private TwoFactorRedirectFilter twoFactorRedirectFilter;
	@Autowired private CustomAuthenticationSuccessHandler customSuccessHandler;

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
	    httpSecurity
	        .csrf(AbstractHttpConfigurer::disable)
	        .addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class)
	        .addFilterAfter(twoFactorRedirectFilter, UserAuthenticationFilter.class)
	        .authorizeHttpRequests(auth -> auth
	            .requestMatchers("/templates/**", "/assets/**").permitAll()
	            .requestMatchers("/2fa", "/2fa/**", "/verify-2fa").hasAuthority("PRE_2FA")
	            .requestMatchers(HttpMethod.POST, "/login", "/create").permitAll()
	            .requestMatchers(HttpMethod.GET, "/create").permitAll()
	            .anyRequest().authenticated()
	        )
	        .formLogin(form -> form
	            .loginPage("/login")
	            .successHandler(customSuccessHandler)
	            .permitAll()
	        )
	        .sessionManagement(session -> session
	            .invalidSessionUrl("/login")
	            .maximumSessions(1)
	            .sessionRegistry(sessionRegistry())
	        )
	        .logout(logout -> logout
	            .logoutUrl("/logout")
	            .logoutSuccessUrl("/login")
	            .invalidateHttpSession(true)
	            .deleteCookies("JSESSIONID")
	        );

	    return httpSecurity.build();
	}

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}

    @Bean
    PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	SessionRegistry sessionRegistry() {
		return new SessionRegistryImpl();
	}
	
}
