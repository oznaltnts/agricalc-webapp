package tr.ozanbey.agricalc.webapp.webapp.view;

import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.primefaces.PrimeFaces;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


@Component("guestPreferences")
@Scope("session")
@Getter
public class GuestPreferences implements Serializable {

    @Setter
    private String menuMode = "layout-horizontal";

    private String darkMode = "dark";

    @Setter
    private String componentTheme = "green";

    private String topbarTheme = "dark";

    @Setter
    private String menuTheme = "dark";

    @Setter
    private String inputStyle = "outlined";

    @Setter
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
    }

    public String getLayout() {
        return "layout-" + this.darkMode;
    }

    public String getTheme() {
        return this.componentTheme + '-' + this.darkMode;
    }

    public void setTopbarTheme(String topbarTheme) {
        this.topbarTheme = topbarTheme;
        this.lightLogo = !this.topbarTheme.equals("light");
    }

    public String getInputStyleClass() {
        return this.inputStyle.equals("filled") ? "ui-input-filled" : "";
    }

    public void onMenuTypeChange() {
        if ("layout-horizontal".equals(menuMode)) {
            menuTheme = topbarTheme;
            PrimeFaces.current().executeScript("PrimeFaces.FreyaConfigurator.changeSectionTheme('" + menuTheme + "' , 'layout-menu')");
        }
    }

    @Getter
    @AllArgsConstructor
    public class ComponentTheme {
        String name;
        String file;
        String color;
    }

}
