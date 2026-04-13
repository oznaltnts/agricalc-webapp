package tr.ozanbey.agricalc.webapp.webapp.util.context;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class SpringContextAccessor {

    private static SpringContextAccessor instance;

    @Autowired
    private ApplicationContext applicationContext;

    public static <T> T getBean(Class<T> clazz) {
        return instance.applicationContext.getBean(clazz);
    }

    public static <T> T getBean(String name, Class<T> clazz) {
        return instance.applicationContext.getBean(name, clazz);
    }

    @PostConstruct
    public void registerInstance() {
        instance = this;
    }
}
