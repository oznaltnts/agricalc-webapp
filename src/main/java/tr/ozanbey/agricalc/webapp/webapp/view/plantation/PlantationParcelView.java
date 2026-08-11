package tr.ozanbey.agricalc.webapp.webapp.view.plantation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tr.ozanbey.agricalc.webapp.service.domain.plantation.PlantationProduct;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PlantationParcelView implements Serializable {

    private Long parcelCityId;
    private Long selectedProductId;
    private PlantationProduct selectedProduct;

    private Long selectedProductOptionId;
    private Long selectedSellOptionId;


}
