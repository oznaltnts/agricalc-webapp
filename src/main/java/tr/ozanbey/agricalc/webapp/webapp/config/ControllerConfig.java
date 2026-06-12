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
        registry.addViewController("/public/how-it-works").setViewName("forward:/public/how-it-works.xhtml");
        registry.addViewController("/login").setViewName("forward:/public/login.xhtml");
        registry.addViewController("/register").setViewName("forward:/public/register.xhtml");

        registry.addViewController("/secured/profile").setViewName("forward:/secured/profile.xhtml");

        registry.addViewController("/secured/plantation/asset").setViewName("forward:/secured/plantation/plant-asset.xhtml");
        registry.addViewController("/secured/plantation/parcel").setViewName("forward:/secured/plantation/plant-parcel.xhtml");
        registry.addViewController("/secured/plantation/product").setViewName("forward:/secured/plantation/plant-product.xhtml");

        registry.addViewController("/secured/animal/dairy-cow/barn").setViewName("forward:/secured/animal/dairy-cow/dairy-cow-barn.xhtml");
        registry.addViewController("/secured/animal/dairy-cow/count").setViewName("forward:/secured/animal/dairy-cow/dairy-cow-count.xhtml");
        registry.addViewController("/secured/animal/dairy-cow/feed").setViewName("forward:/secured/animal/dairy-cow/dairy-cow-feed.xhtml");
        registry.addViewController("/secured/animal/dairy-cow/feed-rasyon").setViewName("forward:/secured/animal/dairy-cow/dairy-cow-feed-rasyon.xhtml");
        registry.addViewController("/secured/animal/dairy-cow/cost").setViewName("forward:/secured/animal/dairy-cow/dairy-cow-cost.xhtml");
        registry.addViewController("/secured/animal/dairy-cow/income").setViewName("forward:/secured/animal/dairy-cow/dairy-cow-income.xhtml");
        registry.addViewController("/secured/animal/dairy-cow/result").setViewName("forward:/secured/animal/dairy-cow/dairy-cow-result.xhtml");

        registry.addViewController("/secured/admin/user-management").setViewName("forward:/secured/admin/user-management.xhtml");
        registry.addViewController("/secured/admin/animal/dairy-cow/cow-management").setViewName("forward:/secured/admin/animal/dairy-cow/cow-management.xhtml");
        registry.addViewController("/secured/admin/animal/dairy-cow/cow-coefficient").setViewName("forward:/secured/admin/animal/dairy-cow/cow-coefficient.xhtml");
        registry.addViewController("/secured/admin/animal/dairy-cow/feed-management").setViewName("forward:/secured/admin/animal/dairy-cow/feed-management.xhtml");
        registry.addViewController("/secured/admin/animal/dairy-cow/cost-management").setViewName("forward:/secured/admin/animal/dairy-cow/cost-management.xhtml");

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
