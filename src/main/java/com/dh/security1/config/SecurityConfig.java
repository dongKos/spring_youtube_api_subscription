package com.dh.security1.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;

import com.dh.security1.config.oauth.PrincipalOauth2UserService;

@Configuration
@EnableWebSecurity	
@EnableGlobalMethodSecurity(securedEnabled=true, prePostEnabled=true)	
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private PrincipalOauth2UserService principalOauth2UserService;
	
	@Bean
	public BCryptPasswordEncoder encodePwd() {
		return new BCryptPasswordEncoder();
	}
	
	@Autowired
    private CustomLogoutHandler logoutHandler;
	

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable();
		http.authorizeRequests()
			.antMatchers("/user/**").authenticated()
			.anyRequest().permitAll()
			.and()
			.formLogin()
			.loginPage("/")
			.loginProcessingUrl("/login")	
			.and()
			.logout()
            .logoutUrl("/logout")
            .addLogoutHandler(logoutHandler)
            .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler(HttpStatus.OK))
            .permitAll()
			.and()
			.oauth2Login()
			.defaultSuccessUrl("/loginSuccess", true)
			.userInfoEndpoint()
			.userService(principalOauth2UserService)
			;	
	}
}
