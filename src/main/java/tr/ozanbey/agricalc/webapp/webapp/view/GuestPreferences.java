package tr.ozanbey.agricalc.webapp.webapp.view;

import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.primefaces.PrimeFaces;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;
import tr.ozanbey.agricalc.webapp.service.service.UserService;
import tr.ozanbey.agricalc.webapp.webapp.controller.BaseController;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


@Component("guestPreferences")
@Getter
@SessionScope
public class GuestPreferences implements Serializable {

    @Autowired
    private UserService userService;

    private String menuMode = "layout-horizontal";

    private String darkMode = "dark";

    private String componentTheme = "green";

    private String topbarTheme = "dark";

    private String menuTheme = "dark";

    private String inputStyle = "outlined";

    private boolean lightLogo = true;

    private final List<ComponentTheme> componentThemes = new ArrayList<>();

    @PostConstruct
    public void init() {
        componentThemes.add(new ComponentTheme("Blue", "blue", "#2c84d8"));
        componentThemes.add(new ComponentTheme("Green", "green", "#34B56F"));
        componentThemes.add(new ComponentTheme("Orange", "orange", "#FF810E"));
        componentThemes.add(new ComponentTheme("Turquoise", "turquoise", "#58AED3"));
        componentThemes.add(new ComponentTheme("Avocado", "avocado", "#AEC523"));
        componentThemes.add(new ComponentTheme("Purple", "purple", "#464DF2"));
        componentThemes.add(new ComponentTheme("Red", "red", "#FF9B7B"));
        componentThemes.add(new ComponentTheme("Yellow", "yellow", "#FFB340"));
    }

    public void setDarkMode(String darkMode) {
        this.darkMode = darkMode;
        this.menuTheme = darkMode;
        this.topbarTheme = darkMode;
        this.lightLogo = !this.topbarTheme.equals("light");
        savePreferenceIfLoggedIn();
    }

    public String getLayout() {
        return "layout-" + this.darkMode;
    }

    public String getTheme() {
        return this.componentTheme + '-' + this.darkMode;
    }

    public void setTopbarTheme(String topbarTheme) {
        this.topbarTheme = topbarTheme;
        if ("layout-horizontal".equals(menuMode)) {
            this.menuTheme = topbarTheme;
        }
        this.lightLogo = !this.topbarTheme.equals("light");
        savePreferenceIfLoggedIn();
    }

    public String getInputStyleClass() {
        return this.inputStyle.equals("filled") ? "ui-input-filled" : "";
    }

    public void onMenuTypeChange() {
        if ("layout-horizontal".equals(menuMode)) {
            menuTheme = topbarTheme;
            PrimeFaces.current().executeScript("PrimeFaces.FreyaConfigurator.changeSectionTheme('" + menuTheme + "' , 'layout-menu')");
        }
        savePreferenceIfLoggedIn();
    }

    @Getter
    @AllArgsConstructor
    public class ComponentTheme {
        String name;
        String file;
        String color;
    }

    public void setMenuMode(String menuMode) {
        this.menuMode = menuMode;
        savePreferenceIfLoggedIn();
    }

    private void savePreferenceIfLoggedIn() {
        if (BaseController.isLoggedIn())
            userService.saveUserPreferences(menuMode, darkMode, componentTheme, topbarTheme, menuTheme, inputStyle, lightLogo);
    }

    public void setComponentTheme(String componentTheme) {
        this.componentTheme = componentTheme;
        savePreferenceIfLoggedIn();
    }

    public void setMenuTheme(String menuTheme) {
        this.menuTheme = menuTheme;
        savePreferenceIfLoggedIn();
    }

    public void setInputStyle(String inputStyle) {
        this.inputStyle = inputStyle;
        savePreferenceIfLoggedIn();
    }

    public void setLightLogo(boolean lightLogo) {
        this.lightLogo = lightLogo;
        savePreferenceIfLoggedIn();
    }

}
