package org.example.bootreloadable;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.support.DefaultSingletonBeanRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

// https://www.baeldung.com/spring-reinitialize-singleton-bean

@Slf4j
@RestController
public class AppController {

    private static final String ACCESS_KEY = "credentials.key";
    private static final String SECRET_KEY = "credentials.secret";
    private static final String TOKEN_KEY = "credentials.session-token";

    AppBean appBean;
    ApplicationContext ctx;

    private AppConfig appConfig;

    @Autowired
    public AppController(AppBean appBean, ApplicationContext ctx, AppConfig appConfig) {
        this.appBean = appBean;
        this.ctx = ctx;
        this.appConfig = appConfig;
    }

    @GetMapping("/refresh")
    public void refresh() {
        appConfig.reloadConfig();

        Map<String, String> config = appConfig.getConfig();

        DefaultSingletonBeanRegistry registry = (DefaultSingletonBeanRegistry) ctx.getAutowireCapableBeanFactory();
        registry.destroySingleton("appBean");
        registry.registerSingleton("appBean", new AppBean(config.get(ACCESS_KEY), config.get(SECRET_KEY), config.get(TOKEN_KEY)));

        //re-fetch bean from context
        this.appBean = (AppBean) registry.getSingleton("appBean");
        log.debug("appBean: {}", appBean);
    }

    @GetMapping("/ping")
    public String hello() {
        return appBean.getKey() + "->" + appBean.getSecret();
    }
}
