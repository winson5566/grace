package com.awinson.config;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * Created by winson on 2016/12/8.
 */
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    DataSource dataSource;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http
                .authorizeRequests()
                .antMatchers("/css/**").permitAll()
                .antMatchers("/u/**").hasRole("NORMAL")
                .antMatchers("/admin/**").hasRole("ADMIN")
                .and()
                .formLogin()
                    .loginPage("/login")
                    .permitAll()
                    .and()
                .logout()
                    .logoutSuccessUrl("/")
                    .permitAll();
    }

//    @Autowired
//    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
//        auth
//                .inMemoryAuthentication()
//                .withUser("winson").password("w5566").roles("USER");
//    }

    @Autowired
    public void configAuthentication(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication().dataSource(dataSource)
                .usersByUsernameQuery(
                        "select username as principal,password as credentials,locked " +
                                "from sys_user " +
                                "where username=?")
                .authoritiesByUsernameQuery(
                        "SELECT u.username as principal,r.name as role " +
                                "from sys_user u " +
                                "INNER JOIN sys_user_role ur ON u.id = ur.sys_user_id " +
                                "INNER JOIN sys_role r ON ur.sys_role_id = r.id " +
                                "WHERE u.username=?")
                .rolePrefix("ROLE_");
    }


}
