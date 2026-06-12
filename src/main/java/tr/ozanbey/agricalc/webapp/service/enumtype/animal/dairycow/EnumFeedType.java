package tr.ozanbey.agricalc.webapp.service.enumtype.animal.dairycow;


import lombok.Getter;

import java.util.Objects;

@Getter
public enum EnumFeedType {

    DRY_ROUGHAGE(0),    // Kuru Kaba Yemler
    SILAGE(1),          // Silajlar
    FORAGE(2),          // Yeşil ve Taze Yemler
    ENERGY(3),          // Enerji Yemleri
    PROTEIN(4),         // Protein Yemleri
    DAIRY(5),           // Süt Yemleri
    CATTLE(6),          // Besi Yemleri
    GENERAL(7),         // Genel
    MINERAL(8),         // Mineraller
    VITAMIN(9),         // Vitaminler
    OTHER_FEED(10);     // Diğer Türler

    private final int value;

    EnumFeedType(int value) {
        this.value = value;
    }

    public static EnumFeedType fromValue(Integer value) {
        for (EnumFeedType t : EnumFeedType.values()) {
            if (Objects.equals(t.value, value)) {
                return t;
            }
        }
        throw new IllegalArgumentException("Unknown EnumFeedType value: " + value);
    }

}
