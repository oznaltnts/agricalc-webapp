package tr.ozanbey.agricalc.webapp.webapp.view;

import lombok.Getter;
import org.primefaces.PrimeFaces;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import java.io.Serializable;


@Component
@Getter
@SessionScope
public class GuestPreferences implements Serializable {

    private String darkMode = "light";

    private String topbarTheme = "dark";

    public void setMenuTheme(String menuTheme) {
        this.menuTheme = menuTheme;
        PrimeFaces.current().executeScript("PrimeFaces.FreyaConfigurator.changeSectionTheme('" + menuTheme + "' , 'layout-menu')");
    }

    private String menuTheme = "dark";

    public void setLightLogo(boolean lightLogo) {
        this.lightLogo = lightLogo;
        PrimeFaces.current().executeScript("PrimeFaces.FreyaConfigurator.changeSectionTheme('" + menuTheme + "' , 'layout-menu')");
    }

    private boolean lightLogo = false;

    public void setDarkMode(String darkMode) {
        this.darkMode = darkMode;
        this.menuTheme = darkMode;
        this.topbarTheme = darkMode;
        this.lightLogo = !this.topbarTheme.equals("light");
        PrimeFaces.current().executeScript("PrimeFaces.FreyaConfigurator.changeSectionTheme('" + menuTheme + "' , 'layout-menu')");
    }

    public String getLayout() {
        return "layout-" + this.darkMode;
    }

    public String getTheme() {
        return "agricalc-" + this.darkMode;
    }

    public void setTopbarTheme(String topbarTheme) {
        this.topbarTheme = topbarTheme;
        this.menuTheme = topbarTheme;
        this.lightLogo = !this.topbarTheme.equals("light");
        PrimeFaces.current().executeScript("PrimeFaces.FreyaConfigurator.changeSectionTheme('" + menuTheme + "' , 'layout-menu')");
    }

}
