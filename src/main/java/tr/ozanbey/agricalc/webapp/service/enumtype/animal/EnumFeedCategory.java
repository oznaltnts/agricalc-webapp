package tr.ozanbey.agricalc.webapp.service.enumtype.animal;


import lombok.Getter;

@Getter
public enum EnumFeedCategory {

    // Kaba Yemler
    ROUGHAGE(new EnumFeedType[]{EnumFeedType.DRY_ROUGHAGE, EnumFeedType.SILAGE, EnumFeedType.FORAGE, EnumFeedType.OTHER_FEED}),
    // Kesif Yemler
    CONCENTRATE(new EnumFeedType[]{EnumFeedType.ENERGY, EnumFeedType.PROTEIN, EnumFeedType.OTHER_FEED}),
    // Karma Fabrika Yemleri
    COMPOUND(new EnumFeedType[]{EnumFeedType.DAIRY, EnumFeedType.CATTLE, EnumFeedType.OTHER_FEED}),
    // Sulu Yemler
    SUCCULENT(new EnumFeedType[]{EnumFeedType.GENERAL, EnumFeedType.OTHER_FEED}),
    // Mineral ve Vitamin Katkıları
    MINERAL_VITAMIN(new EnumFeedType[]{EnumFeedType.MINERAL, EnumFeedType.VITAMIN, EnumFeedType.OTHER_FEED}),
    // Yem Katkıları
    ADDITIVE(new EnumFeedType[]{EnumFeedType.GENERAL, EnumFeedType.OTHER_FEED}),
    // Diğer Yemler
    OTHER_CATEGORY(new EnumFeedType[]{EnumFeedType.OTHER_FEED});

    private final EnumFeedType[] subTypes;

    EnumFeedCategory(EnumFeedType[] subTypes) {
        this.subTypes = subTypes;
    }

}
