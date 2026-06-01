package tr.ozanbey.agricalc.webapp.service.enumtype.animal;


import lombok.Getter;

import java.util.Objects;

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

    public static EnumFeedCategory fromValue(Integer value) {
        for (EnumFeedCategory c : EnumFeedCategory.values()) {
            if (Objects.equals(c.value, value)) {
                return c;
            }
        }
        throw new IllegalArgumentException("Unknown FeedCategory value: " + value);
    }

}
