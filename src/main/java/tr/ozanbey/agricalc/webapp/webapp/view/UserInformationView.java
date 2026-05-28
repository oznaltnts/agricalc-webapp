package tr.ozanbey.agricalc.webapp.webapp.view;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tr.ozanbey.agricalc.webapp.service.enumtype.EnumRole;
import tr.ozanbey.agricalc.webapp.service.enumtype.EnumStatus;

import java.io.Serializable;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserInformationView implements Serializable {

    private Long userId;
    private EnumStatus status;
    private String phone;
    private EnumRole role;
    private LocalDateTime lastLogin;

    @Pattern(regexp = "^[1-9][0-9]{10}$", message = "TCKN 11 rakam olarak giriniz")
    private String tckn;
    @Size(min = 3, message = "Adınızı en az 3 karakter olarak giriniz")
    private String name;
    @Email(message = "Email formatına uygun olarak giriniz")
    private String email;
    private Long userCityId;
    private String cityName;
    private String district;
    private String village;
    private String neighborhood;

    private boolean editInfo = false;

    public UserInformationView(Long userId, EnumStatus status, String phone, EnumRole role, LocalDateTime lastLogin, String tckn, String name, String email, Long userCityId, String cityName, String district, String village, String neighborhood) {
        this.userId = userId;
        this.status = status;
        this.phone = phone;
        this.role = role;
        this.lastLogin = lastLogin;
        this.tckn = tckn;
        this.name = name;
        this.email = email;
        this.userCityId = userCityId;
        this.cityName = cityName;
        this.district = district;
        this.village = village;
        this.neighborhood = neighborhood;
    }
}
