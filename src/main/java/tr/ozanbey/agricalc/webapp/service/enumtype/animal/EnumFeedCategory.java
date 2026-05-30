package tr.ozanbey.agricalc.webapp.service.enumtype.animal;


import lombok.Getter;

@Getter
public enum EnumFeedCategory {

    ROUGHAGE(0),        // Kaba Yemler
    CONCENTRATE(1),     // Kesif Yemler
    COMPOUND(2),        // Karma Fabrika Yemleri
    SUCCULENT(3),       // Sulu Yemler
    MINERAL_VITAMIN(4), // Mineral ve Vitamin Katkıları
    ADDITIVE(5),        // Yem Katkıları
    OTHER_CATEGORY(6);  // Diğer Yemler

    private final int value;

    EnumFeedCategory(int value) {
        this.value = value;
    }

}
