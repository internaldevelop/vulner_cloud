package com.vulner.bend_server.configuration.oauth2;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

import javax.servlet.http.HttpServletResponse;

/**
 * @author Jason
 * @create 2019/12/19
 * @since 1.0.0
 * @description OAuth资源服务配置
 */
@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .exceptionHandling()
                .authenticationEntryPoint((request, response, authException) -> response.sendError(HttpServletResponse.SC_UNAUTHORIZED))
                .and()
                .requestMatchers().antMatchers("/test/**", "/uni_auth/**", "/api/**")
                .requestMatchers().antMatchers("/vuldb/**")
                .requestMatchers().antMatchers("/firmware/**")
                .and()
                .authorizeRequests()
                .antMatchers("/test/**", "/uni_auth/**", "/api/**").authenticated()
                .antMatchers("/vuldb/**").authenticated()
                .antMatchers("/firmware/**").authenticated()
                .and()
                .httpBasic();
    }
}
