package com.example.testSecurity.Security;


import com.example.testSecurity.filter.CustonAuthentificationFilter;
import com.example.testSecurity.filter.customAuthorizationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.bind.annotation.GetMapping;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final UserDetailsService userDetailsService;
   // private final BCryptPasswordEncoder bCryptPasswordEncoder;


    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
       // super.configure(auth);
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }


    //auth et autorisation
    @Override
    protected void configure(HttpSecurity http) throws Exception {

       // super.configure(http);
        //processus de filtrage
        CustonAuthentificationFilter custonAuthentificationFilter=new CustonAuthentificationFilter(authenticationManagerBean());
        custonAuthentificationFilter.setFilterProcessesUrl("/api/login");


        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(STATELESS);
        http.authorizeRequests().antMatchers("/api/login/**","/refreshtoken/**").permitAll();
        http.authorizeRequests().antMatchers(HttpMethod.GET,"/api/user/**").hasAnyAuthority("Role_USER");
        http.authorizeRequests().antMatchers(HttpMethod.POST,"/api/user/adduser/**").hasAnyAuthority("Role_ADMIN");
        http.authorizeRequests().anyRequest().authenticated();

        //http.authorizeRequests().anyRequest().permitAll();
       // http.addFilter(new CustonAuthentificationFilter(authenticationManagerBean()));
        http.addFilter(custonAuthentificationFilter);
        http.addFilterBefore(new customAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);
    }
    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws  Exception{
       return super.authenticationManagerBean();
    }
}
