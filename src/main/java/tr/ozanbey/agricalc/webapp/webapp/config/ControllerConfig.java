package tr.ozanbey.agricalc.webapp.webapp.config;


import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.ErrorPageRegistrar;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ControllerConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("forward:/public/home.xhtml");
        registry.addViewController("/public/public").setViewName("forward:/public/public.xhtml");
        registry.addViewController("/secured/secured").setViewName("forward:/secured/secured.xhtml");
        registry.addViewController("/common/access-denied").setViewName("forward:/common/access.xhtml");
        registry.addViewController("/common/error").setViewName("forward:/common/error.xhtml");
        registry.addViewController("/common/not-found").setViewName("forward:/common/notfound.xhtml");
        registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
    }

    @Bean
    public ErrorPageRegistrar errorPageRegistrar() {
        return registry -> registry
                .addErrorPages(new ErrorPage(HttpStatus.NOT_FOUND, "/common/not-found"),
                        new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/common/error"),
                        new ErrorPage(HttpStatus.FORBIDDEN, "/common/access-denied"));
    }
}
