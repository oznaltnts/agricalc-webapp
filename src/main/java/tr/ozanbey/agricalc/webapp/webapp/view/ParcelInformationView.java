package tr.ozanbey.agricalc.webapp.webapp.view;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tr.ozanbey.agricalc.webapp.service.enumtype.plantation.EnumParcelType;

import java.io.Serializable;
import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ParcelInformationView implements Serializable {

    // Parsel Bilgisi
    private EnumParcelType selectedParcelType;
    @NotBlank(message = "Bu parsele bir isim verin")
    @Size(min = 3, message = "Parsel ismini en az 3 karakter olarak giriniz")
    private String parcelName;
    private BigDecimal parcelPrice;
    private Integer adaNumber;
    private Integer paftaNumber;
    private BigDecimal areaDecare;
    private Boolean isRent;
    private BigDecimal rentPrice;
    // Arazi Yapısı
    private String selectedStatusType;
    private Boolean selectedNadasStatus;
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
    // Sulama Bilgileri
    private String selectedWateringSource;
    private String selectedWateringType;
    private BigDecimal waterPrice;
    private String electricSource;

}
