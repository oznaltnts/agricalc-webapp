package tr.ozanbey.agricalc.webapp.service.enumtype.animal;


import lombok.Getter;

import java.util.Objects;

@Getter
public enum EnumCowType {

    COW(0),             // İnek Sayısı
    PREGNANT_HEIFER(1), // Gebe Düve Sayısı
    HEIFER(2),          // Düve sayısı
    STEER(3),           // Dana Sayısı
    CALF(4);            // Buzağı sayısı

    private final int value;

    EnumCowType(int value) {
        this.value = value;
    }

    public static EnumCowType fromValue(Integer value) {
        for (EnumCowType t : EnumCowType.values()) {
            if (Objects.equals(t.value, value)) {
                return t;
            }
        }
        throw new IllegalArgumentException("Unknown EnumFeedType value: " + value);
    }

}
