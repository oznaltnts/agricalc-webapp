package tr.ozanbey.agricalc.webapp.service.enumtype.plantation;


import lombok.Getter;

@Getter
public enum EnumPlantAsset {

    TRACTOR(0),      // traktör
    TILLAGE(1),      // toprak işleme
    SPRAYING(2),     // ilaçlama ekipmanı
    FERTILIZER(3),   // gübreleme makinesi
    SEEDING(4),      // ekim makineleri
    HARVESTING(5),   // hasat makineleri
    BALER(6),        // balyalama makineleri
    OTHER(7);        // diğer makineler

    private final int value;

    EnumPlantAsset(int value) {
        this.value = value;
    }

}
