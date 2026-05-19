package tr.ozanbey.agricalc.webapp.webapp.controller;


import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tr.ozanbey.agricalc.webapp.service.domain.City;
import tr.ozanbey.agricalc.webapp.service.service.CityService;
import tr.ozanbey.agricalc.webapp.service.service.UserService;
import tr.ozanbey.agricalc.webapp.webapp.view.UserInformationView;

import java.util.List;

@Component("profileController")
@ViewScoped
@Getter
@Setter
public class ProfileController extends BaseController {

    @Autowired
    private UserService userService;

    @Autowired
    private CityService cityService;

    private List<City> cityList;
    private UserInformationView informationView;

    @PostConstruct
    public void init() {
        cityList = cityService.getAllCities();
        informationView = userService.getInformationByUserId();
    }

    public void updateUserProfile() {
        if (userService.save(informationView)) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "Kayıt başarılı",
                            "Kullanıcı bilgileriniz güncellenmiştir."));
        }
    }

}
