package tr.ozanbey.agricalc.webapp.webapp.controller;


import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tr.ozanbey.agricalc.webapp.service.service.UserService;

@Component("registerController")
@ViewScoped
@Getter
@Setter
public class RegisterController extends BaseController {

    @Autowired
    private UserService userService;

    private final String regexPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,}$";

    @Email(message = "Email formatına uygun olarak giriniz")
    private String email;

    //^\(5:(5 ile başlamalı,[0-9]{2}: 2 rakam,\) :parantez ve boşluk,[0-9]{3} :3 rakam ve boşluk,[0-9]{2} : 2 rakam ve boşluk,[0-9]{2}$:2 rakam
    @Pattern(regexp = "^\\(5[0-9]{2}\\) [0-9]{3} [0-9]{2} [0-9]{2}$", message = "Telefon numaranısı formatına uygun olarak olarak giriniz")
    private String phone;

    @NotBlank(message = "Şifre en az 8 karakter olmalı, büyük/küçük harf ve rakam içermelidir")
    @Pattern(regexp = regexPattern, message = "Şifre en az 8 karakter olmalı, büyük/küçük harf ve rakam içermelidir")
    private String password;

    @PostConstruct
    public void init() {
    }

    public void register() {
        try {
            userService.registerUser(email, phone, password);
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Kayıt başarılı", "5 saniye içinde giriş ekranına yönlendirileceksiniz."));
            NavigationController.redirectToLoginWithDuration(5000);
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Kullanıcı mevcut", "Bu bilgiler ile kullanıcı zaten kayıtlı."));
        }
    }


}
