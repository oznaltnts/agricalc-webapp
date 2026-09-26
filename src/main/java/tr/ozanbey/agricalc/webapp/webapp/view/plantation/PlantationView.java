package tr.ozanbey.agricalc.webapp.webapp.view.plantation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tr.ozanbey.agricalc.webapp.service.enumtype.EnumStatus;
import tr.ozanbey.agricalc.webapp.service.enumtype.plantation.EnumPlantationType;

import java.io.Serializable;
import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PlantationView implements Serializable {

    private Long userPlantationId;
    private EnumStatus selectedStatus = EnumStatus.ACTIVE;
    private boolean anyPlanStarted = false;
    // Parsel Bilgisi
    private String name;
    private EnumPlantationType selectedType = EnumPlantationType.OPEN_FIELD;
    private Double area;
    private Boolean ownedOrRental;
    private BigDecimal sellOrRentPrice;
    // Adres Bilgisi
    private Long selectedCityId;
    private String selectedCityName;
    private Boolean selectedCityTriple;
    private Long selectedCityDistrictId;
    private String selectedDistrictName;
    private String village;
    private String neighborhood;
    private Integer adaNumber;
    private Integer paftaNumber;
    private Long selectedPrimaryProductId;
    private String selectedPrimaryProductName;
    //    private Long selectedProductOptionId;
    // Arazi Yapısı
    private String selectedStatusType;
    private Boolean selectedNadas;
    private String selectedSlope;
    private String selectedOrientation;
    // Toprak Özellikleri
    private String selectedSoilTexture;
    private String selectedSoilDepth;
    private String selectedOrganicMatter;
    private String selectedSoilSalinity;
    private String selectedLime;
    private String selectedPhosphorus;
    private String selectedPotassium;

    public PlantationView(PlantationView selected) {
        this.userPlantationId = selected.getUserPlantationId();
        this.selectedStatus = selected.getSelectedStatus();
        this.anyPlanStarted = selected.isAnyPlanStarted();
        this.name = selected.getName();
        this.selectedType = selected.getSelectedType();
        this.area = selected.getArea();
        this.ownedOrRental = selected.getOwnedOrRental();
        this.sellOrRentPrice = selected.getSellOrRentPrice();
        this.selectedCityId = selected.getSelectedCityId();
        this.selectedCityName = selected.getSelectedCityName();
        this.selectedCityTriple = selected.getSelectedCityTriple();
        this.selectedCityDistrictId = selected.getSelectedCityDistrictId();
        this.selectedDistrictName = selected.getSelectedDistrictName();
        this.village = selected.getVillage();
        this.neighborhood = selected.getNeighborhood();
        this.adaNumber = selected.getAdaNumber();
        this.paftaNumber = selected.getPaftaNumber();
        this.selectedPrimaryProductId = selected.getSelectedPrimaryProductId();
        this.selectedPrimaryProductName = selected.getSelectedPrimaryProductName();
        this.selectedStatusType = selected.getSelectedStatusType();
        this.selectedNadas = selected.getSelectedNadas();
        this.selectedSlope = selected.getSelectedSlope();
        this.selectedOrientation = selected.getSelectedOrientation();
        this.selectedSoilTexture = selected.getSelectedSoilTexture();
        this.selectedSoilDepth = selected.getSelectedSoilDepth();
        this.selectedOrganicMatter = selected.getSelectedOrganicMatter();
        this.selectedSoilSalinity = selected.getSelectedSoilSalinity();
        this.selectedLime = selected.getSelectedLime();
        this.selectedPhosphorus = selected.getSelectedPhosphorus();
        this.selectedPotassium = selected.getSelectedPotassium();
    }
}
