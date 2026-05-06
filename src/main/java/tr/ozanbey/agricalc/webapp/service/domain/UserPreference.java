package tr.ozanbey.agricalc.webapp.service.domain;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Setter
@Entity
@Table(name = "user_preferences")
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
@AllArgsConstructor
public class UserPreference extends AbstractEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "menu_mode", nullable = false)
    private String menuMode;

    @Column(name = "dark_mode", nullable = false)
    private String darkMode;

    @Column(name = "component_theme", nullable = false)
    private String componentTheme;

    @Column(name = "topbar_theme", nullable = false)
    private String topbarTheme;

    @Column(name = "menu_theme", nullable = false)
    private String menuTheme;

    @Column(name = "input_style", nullable = false)
    private String inputStyle;

    @Column(name = "light_logo", nullable = false, columnDefinition = "TINYINT")
    private boolean lightLogo;

    public static UserPreference createWithTemplateData(User user) {
        return new UserPreference(user, "layout-horizontal", "dark", "green", "dark", "dark", "outlined", true);
    }

}
