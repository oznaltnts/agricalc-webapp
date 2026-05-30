package tr.ozanbey.agricalc.webapp.service.enumtype.animal;


import lombok.Getter;

@Getter
public enum EnumFeedType {

    DRY_ROUGHAGE(0),    // Kuru Kaba Yemler
    SILAGE(1),          // Silajlar
    FORAGE(2),          // Yeşil ve Taze Yemler
    FIBROUS(3),         // Kırpıntı ve Lifli Ürünler
    ENERGY(4),          // Enerji Yemleri
    PROTEIN(5),         // Protein Yemleri
    DAIRY(6),           // Süt Yemleri
    CATTLE(7),          // Besi Yemleri
    WATERY(8),          // SULU YEMLER
    GENERAL(9),         // Genel
    MINERAL(10),        // Mineraller
    VITAMIN(11),        // Vitaminler
    OTHER_FEED(12);     // Diğer Türler

    private final int value;

    EnumFeedType(int value) {
        this.value = value;
    }

}
