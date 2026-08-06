package tr.ozanbey.agricalc.webapp.webapp.controller.plantation;


import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tr.ozanbey.agricalc.webapp.service.domain.City;
import tr.ozanbey.agricalc.webapp.service.domain.plantation.UserPlantParcel;
import tr.ozanbey.agricalc.webapp.service.enumtype.plantation.EnumParcelType;
import tr.ozanbey.agricalc.webapp.service.service.CityService;
import tr.ozanbey.agricalc.webapp.service.service.plantation.UserPlantParcelService;
import tr.ozanbey.agricalc.webapp.webapp.controller.BaseController;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
@ViewScoped
@Getter
@Setter
public class ParcelDetailController extends BaseController {

    @Autowired
    private UserPlantParcelService userPlantParcelService;
    @Autowired
    private CityService cityService;

    private Long parcelId;
    private UserPlantParcel userPlantParcel;

    private List<City> cityList;
    private EnumParcelType[] parcelTypes = EnumParcelType.values();
    private boolean isParcelRental;

    @PostConstruct
    public void init() {
        cityList = cityService.getAllCities();
    }

    public void fillParcelList() throws IOException {
        if (parcelId == null || !checkParcelIdForUser(parcelId)) {
            super.navigationController.redirectToUrl("/secured/plantation/parcel");
            return;
        }
    }

    public void setParcelId(Long parcelId) {
        if (Objects.equals(this.parcelId, parcelId)) {
            return;
        }
        this.parcelId = parcelId;
    }

    private boolean checkParcelIdForUser(Long parcelId) {
        Optional<UserPlantParcel> optionalUserPlantParcel = userPlantParcelService.getUserParcelByIdAndUserId(parcelId, getCurrentUser().getUser().getId());
        if (optionalUserPlantParcel.isPresent()) {
            userPlantParcel = optionalUserPlantParcel.get();
            if (userPlantParcel.getRentPrice() == null) {
                isParcelRental = true;
            }
            return true;
        }
        return false;
    }

    public void saveAndPlanList() throws IOException {
        if (isParcelRental) {
            userPlantParcel.setRentPrice(null);
        } else {
            userPlantParcel.setParcelPrice(null);
        }
        userPlantParcelService.saveParcelDetail(userPlantParcel);
        super.navigationController.redirectToUrl("/secured/plantation/parcel");
    }
}
