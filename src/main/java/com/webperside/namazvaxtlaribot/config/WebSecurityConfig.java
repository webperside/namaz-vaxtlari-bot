package com.webperside.namazvaxtlaribot.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Value("${admin-panel.username}")
    private String username;

    @Value("${admin-panel.password}")
    private String password;


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().authorizeRequests()
                .antMatchers("/admin/**").authenticated()
                .anyRequest().permitAll().and()
                .formLogin().defaultSuccessUrl("/admin/users");
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser(username).password(password)
                .credentialsExpired(false)
                .accountExpired(false)
                .accountLocked(false)
                .authorities("WRITE_PRIVILEGES", "READ_PRIVILEGES")
                .roles("ADMIN");
    }

}
