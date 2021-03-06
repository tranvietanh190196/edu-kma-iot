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
		http.authorizeRequests().antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')");
		http.authorizeRequests().antMatchers("/acount/**","/device/**").access("hasAnyRole('ROLE_USER,ROLE_ADMIN')");
		http.formLogin().loginPage("/login")
		.usernameParameter("username").passwordParameter("password")
		.loginProcessingUrl("/check_sec")
		.defaultSuccessUrl("/")
		.failureUrl("/login?error=1");
		http.logout().logoutUrl("/logout").logoutSuccessUrl("/").deleteCookies("JSESSIONID");
		http.sessionManagement().maximumSessions(5).expiredUrl("/login");
		http.rememberMe().tokenValiditySeconds(3600*24).rememberMeParameter("remember-me");
		
		http.cors().and().csrf().disable();
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
		web.ignoring().antMatchers("/js/**");
	}
	
	
}
