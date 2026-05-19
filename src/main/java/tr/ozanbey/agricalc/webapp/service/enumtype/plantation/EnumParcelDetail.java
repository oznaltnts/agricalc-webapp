package tr.ozanbey.agricalc.webapp.service.enumtype.plantation;


import lombok.Getter;

@Getter
public enum EnumParcelDetail {

    LOCATION_INFO(0),           // Parsel Bilgisi
    LAND_STRUCTURE(1),          // Arazi Yapısı
    SOIL_CHARACTERISTICS(2),    // Toprak Özellikleri
    IRRIGATION_INFORMATION(3);  // Sulama Bilgileri

    private final int value;

    EnumParcelDetail(int value) {
        this.value = value;
    }

}
