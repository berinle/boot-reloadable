package org.example.bootreloadable;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    //    @Value("${credentials.key}")
    @Value("${CREDENTIALS_KEY}")
    String key;
    //    @Value("${credentials.secret}")
    @Value("${CREDENTIALS_SECRET}")
    String secret;

    @Bean
//    @RefreshScope
    public AppBean appBean() {
        return new AppBean(key, secret);
    }
}
