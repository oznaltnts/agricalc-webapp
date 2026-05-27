package tr.ozanbey.agricalc.webapp.webapp.controller;


import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import lombok.Getter;
import lombok.Setter;
import org.primefaces.PrimeFaces;
import org.primefaces.event.TabChangeEvent;
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
        fillUserInfo();
    }

    private void fillUserInfo() {
        informationView = userService.getInformationByUserId(getCurrentUser().getUser().getId());
        if (informationView != null) {
            if (informationView.getUserCityId() != null) {
                cityList.stream()
                        .filter(c -> c.getId().equals(informationView.getUserCityId()))
                        .findFirst().ifPresent(city -> informationView.setCityName(city.getName()));
            }
        }
    }

    public void onTabChange(TabChangeEvent event) {
        informationView.setEditInfo(false);
    }

    public void editProfileInfo(Integer tabNum) {
        PrimeFaces.current().executeScript("PF('profileTabWidget').select(" + tabNum + ");");
        informationView.setEditInfo(true);
    }

    public void cancelSaveButton() {
        informationView.setEditInfo(false);
    }

    public void saveAndNextButton(Integer tabNum) {
        updateUserProfile();
        if (tabNum == 1) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Kayıt başarılı", "3 saniye içinde demirbaş ekranına yönlendirileceksiniz."));
            NavigationController.redirectToUrlWithDuration("/secured/plant-asset", 3000);
        } else {
            PrimeFaces.current().executeScript("PF('profileTabWidget').select(1);");
        }
    }

    private void updateUserProfile() {
        if (userService.save(informationView, getCurrentUser().getUser())) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "Kayıt başarılı", "Kullanıcı bilgileriniz güncellenmiştir."));
        }
    }

}
