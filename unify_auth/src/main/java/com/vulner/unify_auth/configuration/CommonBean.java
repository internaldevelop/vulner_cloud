package com.vulner.unify_auth.configuration;

import com.vulner.unify_auth.service.logger.SysLogService;
import com.vulner.unify_auth.service.logger.SysLogger;
import feign.Contract;
import feign.Feign;
import feign.codec.Decoder;
import feign.codec.Encoder;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
public class CommonBean {
//    注入的jar包如果不能直接使用 @autowired 自动注入 bean，可以采用如下方法创建 bean。
//    @Bean
//    public SysLogger sysLogger() {
//        return new SysLogger();
//    }
}
