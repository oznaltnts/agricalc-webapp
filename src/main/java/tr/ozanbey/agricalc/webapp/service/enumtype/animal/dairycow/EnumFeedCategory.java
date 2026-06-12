package tr.ozanbey.agricalc.webapp.service.enumtype.animal.dairycow;


import lombok.Getter;

import java.util.Objects;

@Getter
public enum EnumFeedCategory {

    ROUGHAGE(0, new EnumFeedType[]{EnumFeedType.DRY_ROUGHAGE, EnumFeedType.SILAGE, EnumFeedType.FORAGE, EnumFeedType.OTHER_FEED}), // Kaba Yemler
    CONCENTRATE(1, new EnumFeedType[]{EnumFeedType.ENERGY, EnumFeedType.PROTEIN, EnumFeedType.OTHER_FEED}),                        // Kesif Yemler
    COMPOUND(2, new EnumFeedType[]{EnumFeedType.DAIRY, EnumFeedType.CATTLE, EnumFeedType.OTHER_FEED}),                             // Karma Fabrika Yemleri
    SUCCULENT(3, new EnumFeedType[]{EnumFeedType.GENERAL, EnumFeedType.OTHER_FEED}),                                               // Sulu Yemler
    MINERAL_VITAMIN(4, new EnumFeedType[]{EnumFeedType.MINERAL, EnumFeedType.VITAMIN, EnumFeedType.OTHER_FEED}),                   // Mineral ve Vitamin Katkıları
    ADDITIVE(5, new EnumFeedType[]{EnumFeedType.GENERAL, EnumFeedType.OTHER_FEED}),                                                // Yem Katkıları
    OTHER_CATEGORY(6, new EnumFeedType[]{EnumFeedType.OTHER_FEED});                                                                // Diğer Yemler

    private final int value;
    private final EnumFeedType[] subTypes;

    EnumFeedCategory(int value, EnumFeedType[] subTypes) {
        this.subTypes = subTypes;
        this.value = value;
    }

    public static EnumFeedCategory fromValue(Integer value) {
        for (EnumFeedCategory t : EnumFeedCategory.values()) {
            if (Objects.equals(t.value, value)) {
                return t;
            }
        }
        throw new IllegalArgumentException("Unknown EnumFeedCategory value: " + value);
    }
}
