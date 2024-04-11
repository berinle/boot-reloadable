package org.example.bootreloadable;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Slf4j
@Configuration
public class AppConfig {

    @Value("${credentials.key}")
    String key;
    @Value("${credentials.secret}")
    String secret;
    @Value("${credentials.session-token}")
    String token;

    @Getter
    private Map<String, String> config;
    private String configFilePath;

    public AppConfig(@Value("${config.file.path}") String configFilePath) {
        this.configFilePath = configFilePath;
        reloadConfig();
    }

    public void reloadConfig() {
        Properties properties = new Properties();
        try {
            properties.load(Files.newInputStream(Paths.get(configFilePath)));
        } catch (IOException e) {
            log.error("Error loading configuration:", e);
        }
        config = new HashMap<>();
        for (Map.Entry<Object, Object> entry : properties.entrySet()) {
            config.put(String.valueOf(entry.getKey()), (String) entry.getValue());
        }

        System.out.println("config = " + config);
    }

    @Bean
    public AppBean appBean() {
        return new AppBean(key, secret, token);
    }
}
