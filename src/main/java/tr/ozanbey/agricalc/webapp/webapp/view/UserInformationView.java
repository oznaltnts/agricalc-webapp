package tr.ozanbey.agricalc.webapp.webapp.view;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserInformationView implements Serializable {

    @Pattern(regexp = "^[1-9][0-9]{10}$", message = "TCKN 11 rakam olarak giriniz")
    private String tckn;
    @Size(min = 3, message = "Adınızı en az 3 karakter olarak giriniz")
    private String name;
    @Email(message = "Email formatına uygun olarak giriniz")
    private String email;
    private Long userCityId;
    private String district;
    private String village;
    private String neighborhood;

}
