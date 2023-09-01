package com.increff.pos.spring;

import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import io.swagger.models.HttpMethod;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	private static Logger logger = Logger.getLogger(SecurityConfig.class);

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http
			// Match only these URLs
				.requestMatchers()//
				.antMatchers("/api/**")//
				.antMatchers("/ui/**")//
				.and().authorizeRequests()//
				// Allow GET requests for various sites
				.antMatchers(HttpMethod.GET, "/api/about").permitAll()
				.antMatchers(HttpMethod.GET, "/api/**").hasAnyAuthority("OPERATOR","SUPERVISOR")
				.antMatchers(HttpMethod.POST, "/api/report/salesReport").hasAnyAuthority("OPERATOR","SUPERVISOR")
				.antMatchers("/api/validate-order-item/**").hasAnyAuthority("OPERATOR","SUPERVISOR")
				.antMatchers("/api/order/**").hasAnyAuthority("OPERATOR","SUPERVISOR")
				.antMatchers("/api/order-item/**").hasAnyAuthority("OPERATOR","SUPERVISOR")
				.antMatchers("/api/invoice/**").hasAnyAuthority("OPERATOR","SUPERVISOR")
				.antMatchers("/api/admin/**").hasAnyAuthority("SUPERVISOR")//
				.antMatchers("/api/**").hasAnyAuthority("SUPERVISOR")//
				.antMatchers("/ui/admin/**").hasAnyAuthority("SUPERVISOR")//
				.antMatchers("/ui/**").hasAnyAuthority("SUPERVISOR","OPERATOR")//
				// Ignore CSRF and CORS
				.and().csrf().disable().cors().disable();
		logger.info("Configuration complete");
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/v2/api-docs", "/configuration/ui", "/swagger-resources", "/configuration/security",
				"/swagger-ui.html", "/webjars/**");
	}

}
