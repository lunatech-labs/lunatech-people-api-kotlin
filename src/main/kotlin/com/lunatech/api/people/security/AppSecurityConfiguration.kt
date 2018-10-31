package com.lunatech.api.people.security

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.authentication.AuthenticationFailureHandler

@Configuration
class AppSecurityConfiguration : WebSecurityConfigurerAdapter() {

    @Autowired
    lateinit var authManager: AuthenticationManager

    override fun configure(http: HttpSecurity?) {
        val authFilter = AppAuthFilter()
        authFilter.setAuthenticationManager(authManager)

        http!!.
                antMatcher("/api/**").
                csrf().disable().
                sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).
                and().addFilter(authFilter).authorizeRequests().anyRequest().authenticated()
                .and().exceptionHandling()
    }
}