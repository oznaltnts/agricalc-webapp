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
        registry.addViewController("/secured/farmer").setViewName("forward:/secured/farmer-profile.xhtml");

        registry.addViewController("/secured/plantation/business").setViewName("forward:/secured/plantation/business-profile.xhtml");
        registry.addViewController("/secured/plantation/parcel").setViewName("forward:/secured/plantation/parcel-profile.xhtml");
        registry.addViewController("/secured/plantation/parcel-plan").setViewName("forward:/secured/plantation/parcel-plan.xhtml");

        registry.addViewController("/secured/plantation/income-profile").setViewName("forward:/secured/plantation/income-profile.xhtml");

        registry.addViewController("/secured/plantation/expense-soil-profile").setViewName("forward:/secured/plantation/expense-soil-profile.xhtml");
        registry.addViewController("/secured/plantation/expense-planting-profile").setViewName("forward:/secured/plantation/expense-planting-profile.xhtml");
        registry.addViewController("/secured/plantation/expense-fertilizer-profile").setViewName("forward:/secured/plantation/expense-fertilizer-profile.xhtml");
        registry.addViewController("/secured/plantation/expense-weed-profile").setViewName("forward:/secured/plantation/expense-weed-profile.xhtml");
        registry.addViewController("/secured/plantation/expense-irrigation-profile").setViewName("forward:/secured/plantation/expense-irrigation-profile.xhtml");
        registry.addViewController("/secured/plantation/expense-cultural-profile").setViewName("forward:/secured/plantation/expense-cultural-profile.xhtml");
        registry.addViewController("/secured/plantation/expense-protection-profile").setViewName("forward:/secured/plantation/expense-protection-profile.xhtml");
        registry.addViewController("/secured/plantation/expense-harvest-profile").setViewName("forward:/secured/plantation/expense-harvest-profile.xhtml");
        registry.addViewController("/secured/plantation/expense-blend-profile").setViewName("forward:/secured/plantation/expense-blend-profile.xhtml");
        registry.addViewController("/secured/plantation/expense-drying-profile").setViewName("forward:/secured/plantation/expense-drying-profile.xhtml");
        registry.addViewController("/secured/plantation/expense-baling-profile").setViewName("forward:/secured/plantation/expense-baling-profile.xhtml");
        registry.addViewController("/secured/plantation/expense-packaging-profile").setViewName("forward:/secured/plantation/expense-packaging-profile.xhtml");

        registry.addViewController("/secured/plantation/plan-result").setViewName("forward:/secured/plantation/plan-result.xhtml");

        registry.addViewController("/secured/plantation/expense-general-profile").setViewName("forward:/secured/plantation/general-profile.xhtml");

        registry.addViewController("/secured/plantation/asset-old").setViewName("forward:/secured/plantation/old/plant-asset.xhtml");
        registry.addViewController("/secured/plantation/parcel-old").setViewName("forward:/secured/plantation/old/plant-parcel.xhtml");
        registry.addViewController("/secured/plantation/product-old").setViewName("forward:/secured/plantation/old/plant-product.xhtml");

        registry.addViewController("/secured/animal/dairy-cow/barn").setViewName("forward:/secured/animal/dairy-cow/dairy-cow-barn.xhtml");
        registry.addViewController("/secured/animal/dairy-cow/count").setViewName("forward:/secured/animal/dairy-cow/dairy-cow-count.xhtml");
        registry.addViewController("/secured/animal/dairy-cow/feed").setViewName("forward:/secured/animal/dairy-cow/dairy-cow-feed.xhtml");
        registry.addViewController("/secured/animal/dairy-cow/feed-rasyon").setViewName("forward:/secured/animal/dairy-cow/dairy-cow-feed-rasyon.xhtml");
        registry.addViewController("/secured/animal/dairy-cow/cost").setViewName("forward:/secured/animal/dairy-cow/dairy-cow-cost.xhtml");
        registry.addViewController("/secured/animal/dairy-cow/income").setViewName("forward:/secured/animal/dairy-cow/dairy-cow-income.xhtml");
        registry.addViewController("/secured/animal/dairy-cow/result").setViewName("forward:/secured/animal/dairy-cow/dairy-cow-result.xhtml");

        registry.addViewController("/secured/animal/cattle/barn").setViewName("forward:/secured/animal/cattle/cattle-barn.xhtml");

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
