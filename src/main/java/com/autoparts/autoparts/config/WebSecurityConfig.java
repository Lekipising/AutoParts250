// Authors: Liplan Lekipising and catherine Muthoni

package com.autoparts.autoparts.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
    private DataSource dataSource;

	@Autowired
	public void configAuthentication(AuthenticationManagerBuilder auth) throws Exception{
		auth.jdbcAuthentication().dataSource(dataSource).passwordEncoder(new BCryptPasswordEncoder()).usersByUsernameQuery("select username, password, enabled from users where username=?").authoritiesByUsernameQuery("select username, role from users where username=?");
	}


	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http.authorizeRequests()
				.antMatchers("/addorder").hasAuthority("USER")
				.antMatchers("/shippings", "/orders", "/orderlist", "editlogo", "/users").hasAuthority("ADMIN")
				.antMatchers("/myaccount").hasAnyAuthority("ADMIN", "USER")
				.anyRequest().permitAll()
				.and()
				.formLogin()
				.loginPage("/login")
				.permitAll()
				.and()
				.logout()
				.permitAll();		
	}


	@Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder(11);
    }

}
