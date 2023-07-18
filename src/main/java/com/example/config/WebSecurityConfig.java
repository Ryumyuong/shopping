package com.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Bean
	public BCryptPasswordEncoder encodePwd() {
		return new BCryptPasswordEncoder();
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {

	    http.authorizeRequests()
	        .mvcMatchers("/runa/main", "/cart/**").hasAnyRole("USER", "ADMIN") 
	        .mvcMatchers("/runa/**", "/newLogin", "/addRuna", "/updateUser", "/userList").hasRole("ADMIN")
	        .anyRequest().permitAll()
	        .and()
	        .formLogin()
	        .loginPage("/")
	        .usernameParameter("userId")
	        .passwordParameter("password")
	        .loginProcessingUrl("/")
	        .defaultSuccessUrl("/runa/main")
	        .failureUrl("/?error=true")
	        .and()
	        .logout()
	        .logoutSuccessUrl("/logout")
	        .invalidateHttpSession(true)
	        .deleteCookies("JSESSIONID");
	}

}
