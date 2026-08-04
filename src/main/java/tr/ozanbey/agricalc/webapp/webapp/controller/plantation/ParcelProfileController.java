package tr.ozanbey.agricalc.webapp.webapp.controller.plantation;


import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tr.ozanbey.agricalc.webapp.service.domain.plantation.PlantationProduct;
import tr.ozanbey.agricalc.webapp.service.domain.plantation.UserPlantParcel;
import tr.ozanbey.agricalc.webapp.service.enumtype.EnumStatus;
import tr.ozanbey.agricalc.webapp.service.enumtype.plantation.EnumParcelDetail;
import tr.ozanbey.agricalc.webapp.service.service.plantation.PlantationProductService;
import tr.ozanbey.agricalc.webapp.service.service.plantation.UserParcelService;

import java.util.List;

@Component
@ViewScoped
@Getter
@Setter
public class ParcelProfileController extends PlantationController {

    @Autowired
    private UserParcelService userParcelService;

    @Autowired
    private PlantationProductService productService;

    private List<PlantationProduct> plantationProductList;
    private Long selectedProductId;

    private List<UserPlantParcel> plantParcelList;
    private EnumParcelDetail[] parcelDetails = EnumParcelDetail.values();

    @PostConstruct
    public void init() {
        fillParcelList();
    }

    private void fillParcelList() {
        plantParcelList = userParcelService.getPlantParcelList(getCurrentUser().getUser().getId());
    }

    public void fillProductList() {
        plantationProductList = productService.getActiveProducts(EnumStatus.ACTIVE);
    }


}
