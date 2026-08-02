package tr.ozanbey.agricalc.webapp.webapp.controller.plantation;


import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tr.ozanbey.agricalc.webapp.service.domain.UserPlantParcel;
import tr.ozanbey.agricalc.webapp.service.enumtype.plantation.EnumParcelDetail;
import tr.ozanbey.agricalc.webapp.service.service.UserParcelService;

import java.util.List;

@Component
@ViewScoped
@Getter
@Setter
public class ParcelProfileController extends PlantationController {

    @Autowired
    private UserParcelService userParcelService;

    private List<UserPlantParcel> plantParcelList;
    private EnumParcelDetail[] parcelDetails = EnumParcelDetail.values();

    @PostConstruct
    public void init() {
        fillParcelList();
    }

    private void fillParcelList() {
        plantParcelList = userParcelService.getPlantParcelList(getCurrentUser().getUser().getId());
    }


}
