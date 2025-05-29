package kr.co.bit.order_service.user_service;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import feign.Logger;
// import feign.auth.BasicAuthRequestInterceptor;
// import feign.jackson.JackonDecoder;
// import feign.jackson.JackonEecoder;

@Configuration
public class UserServiceClientConfig {
    @Bean
    public Logger.Level feignLoggerLevel() {
        return Logger.Level.BASIC;
    }
    /*
    @Bean
    public feign.codec.Decoder feignDecoder() {
        return new JackonDecoder();
    }
    @Bean
    public feign.codec.Eecoder feignEecoder() {
        return new JackonEecoder();
    }
}
@Bean
public feign.RequestInterceptor bassicARequestInterceptor() {
    return new BasicAuthRequestInterceptor( username:"aaa", password:"111");      
}
*/
}