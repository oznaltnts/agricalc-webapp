package tr.ozanbey.agricalc.webapp.webapp.controller;


import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tr.ozanbey.agricalc.webapp.service.domain.City;
import tr.ozanbey.agricalc.webapp.service.domain.UserInformation;
import tr.ozanbey.agricalc.webapp.service.service.HomeService;
import tr.ozanbey.agricalc.webapp.service.service.UserService;

import java.util.List;

@Component("profileController")
@ViewScoped
@Getter
@Setter
public class ProfileController extends BaseController {

    @Autowired
    private UserService userService;

    @Autowired
    private HomeService homeService;

    @Pattern(regexp = "^[1-9][0-9]{10}$", message = "TCKN 11 rakam olarak giriniz")
    private String tckn;

    @Size(min = 3, message = "Adınızı en az 3 karakter olarak giriniz")
    private String name;

    @Email(message = "Email formatına uygun olarak giriniz")
    private String email;

    private List<City> cityList;
    private Long userCityId;
    private String district;
    private String village;
    private String neighborhood;

    @PostConstruct
    public void init() {
        cityList = homeService.getAllCities();
        UserInformation userInformation = userService.getInformationByUserId(getCurrentUser().getUser().getId());
        if (userInformation != null) {
            name = userInformation.getNameSurname();
            tckn = userInformation.getTckn();
            email = userInformation.getEmail();
            userCityId = userInformation.getCity().getId();
            district = userInformation.getDistrict();
            village = userInformation.getVillage();
            neighborhood = userInformation.getNeighborhood();
        }
    }

    public void updateUserProfile() {
    }

}
