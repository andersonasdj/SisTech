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
	
	@Autowired
	private UserAuthenticationFilter filter;
	
	@SuppressWarnings("removal")
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
		
		httpSecurity.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
		
		 httpSecurity
		 		.sessionManagement(session -> session.maximumSessions(2).expiredUrl("/login?invalid-session=true"))
                .csrf(AbstractHttpConfigurer::disable)
                .headers(headers -> headers.disable())
                //.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                		.requestMatchers("/templates/**").permitAll()
                		.requestMatchers("/assets/**").permitAll()
                		.requestMatchers(HttpMethod.POST, "/login").permitAll()
                		.requestMatchers(HttpMethod.POST, "/create").permitAll()
                		.requestMatchers(HttpMethod.GET, "/create").permitAll()
                        .anyRequest().authenticated()
                ).httpBasic().and().addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class)
                .formLogin(formLogin ->
                formLogin
                    .loginPage("/login")
                    .defaultSuccessUrl("/home")
                    .permitAll()
                		)
        		.logout(logout -> logout.logoutSuccessUrl("/login")
                        .logoutUrl("/logout")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID"))
        		;
		 
//		 httpSecurity.sessionManagement()
//			.maximumSessions(1)
//			.maxSessionsPreventsLogin(true)
//			.expiredUrl("/login")
//			.sessionRegistry(sessionRegistry());
		 
//		 	httpSecurity.sessionManagement(t -> t.maximumSessions(1).maxSessionsPreventsLogin(true));
		 
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
