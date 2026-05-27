package tr.ozanbey.agricalc.webapp.webapp.controller;


import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component("plantProductController")
@ViewScoped
@Getter
@Setter
public class PlantProductController extends BaseController {

    private String selectedProduct;
    private String selectedProductType;
    private String selectedTreeAge;
    private String selectedTreeType;

    private LocalDateTime selectedFlowerDate;
    private LocalDateTime minDate;
    private LocalDateTime maxDate;
    private LocalDateTime selectedHarvestDate;

    @PostConstruct
    public void init() {
        minDate = LocalDateTime.now().minusYears(1);
        maxDate = LocalDateTime.now().plusYears(1);
    }

    public void handleProductSelect() {
        if (selectedProduct != null) {
        }
    }

}
