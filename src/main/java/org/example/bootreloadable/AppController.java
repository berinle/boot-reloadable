package org.example.bootreloadable;

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

@RestController
public class AppController {

    AppBean appBean;
    ApplicationContext ctx;

//    @Value("${credentials.key}")
    @Value("${CREDENTIALS_KEY}")
    String key;
//    @Value("${credentials.secret}")
    @Value("${CREDENTIALS_SECRET}")
    String secret;
    private Environment environment;

    private AppConfig appConfig;

    @Autowired
    public AppController(AppBean appBean, ApplicationContext ctx, Environment environment, AppConfig appConfig) {
        this.appBean = appBean;
        this.ctx = ctx;
        this.environment = environment;
        this.appConfig = appConfig;
    }


    @GetMapping("/magic")
    public void magic() {
        appConfig.reloadConfig();

        Map<String, String> config = appConfig.getConfig();


        DefaultSingletonBeanRegistry registry = (DefaultSingletonBeanRegistry) ctx.getAutowireCapableBeanFactory();
        registry.destroySingleton("appBean");
        registry.registerSingleton("appBean", new AppBean(config.get("k1"), config.get("k2")));

        //re-fetch bean from context
        this.appBean = (AppBean) registry.getSingleton("appBean");

        System.out.println("appBean = " + appBean);
    }
    @GetMapping("/reinitialize")
    public void reinitialize(@RequestParam("key") String _key, @RequestParam("secret") String _secret) {

        DefaultSingletonBeanRegistry registry = (DefaultSingletonBeanRegistry) ctx.getAutowireCapableBeanFactory();
        registry.destroySingleton("appBean");
        registry.registerSingleton("appBean", new AppBean(_key, _secret));

        //re-fetch bean from context
        this.appBean = (AppBean) registry.getSingleton("appBean");
    }

    @GetMapping("/ping")
    public String hello() {

        System.out.println("env key= " + environment.getProperty("CREDENTIALS_KEY"));
        System.out.println("env secret= " + environment.getProperty("CREDENTIALS_SECRET"));

        System.out.println("** env key=" + System.getenv("CREDENTIALS_KEY"));
        System.out.println("** env secret=" + System.getenv("CREDENTIALS_SECRET"));

        System.out.println("appBean = " + appBean);
        return appBean.getKey() + "->" + appBean.getSecret();
    }
}
