package com.vulner.unify_auth.configuration.oauth;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

import javax.servlet.http.HttpServletResponse;

/**
 * @author Jason
 * @create 2019/12/19
 * @since 1.0.0
 * @description 资源认证服务配置
 */
@Configuration
@EnableResourceServer
@Order(3)
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {
    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .exceptionHandling()
                .authenticationEntryPoint((request, response, authException) -> response.sendError(HttpServletResponse.SC_UNAUTHORIZED))
                // 需要权限控制的接口
                .and()
                .requestMatchers().antMatchers("/account_auth/**")
                .requestMatchers().antMatchers("/account_manage/**")
                .and()
                .authorizeRequests()
                .antMatchers("/account_auth/**").authenticated()
                .antMatchers("/account_manage/**").authenticated()
                // 权限放开的API接口
                .and()
                .authorizeRequests()
                .antMatchers("/system/**").permitAll()
                .antMatchers("/actuator/**").permitAll()
                // 需要 basic 认证
                .and()
                .httpBasic();
    }
}
