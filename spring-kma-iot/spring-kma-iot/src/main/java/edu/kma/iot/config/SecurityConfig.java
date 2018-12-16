package edu.kma.iot.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import edu.kma.iot.service.IOTUserDetailsService;
import edu.kma.iot.service.UserAuthProvider;

@EnableWebSecurity
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	@Autowired
	private UserAuthProvider userAuthPro;
	@Autowired
	private IOTUserDetailsService userDetailsService;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().antMatchers("/").permitAll();
		http.authorizeRequests().antMatchers("/login").permitAll();
		http.authorizeRequests().antMatchers("/acount/**").access("hasRole('ROLE_USER')");
		http.authorizeRequests().antMatchers("/device/**").access("hasRole('ROLE_USER')");
		http.formLogin().loginPage("/login")
		.usernameParameter("username").passwordParameter("password")
		.loginProcessingUrl("/check_sec")
		.defaultSuccessUrl("/")
		.failureUrl("/login?error=1");
		http.logout().logoutUrl("/logout").logoutSuccessUrl("/").deleteCookies("JSESSIONID");
		http.sessionManagement().maximumSessions(5).expiredUrl("/login");
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(userAuthPro);
		auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
	}
	@Bean
    public PasswordEncoder passwordEncoder() {
		 return new BCryptPasswordEncoder();
    }

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/css/**");
	}
	
}
