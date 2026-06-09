package tr.ozanbey.agricalc.webapp.service.enumtype.animal;


import lombok.Getter;

import java.util.List;
import java.util.Objects;

@Getter
public enum EnumCostType {

    MAINTENANCE_SALARY(0, List.of("COUNT", "COST")),            // Çitlikte Maaşlı Çalışan Bakım Personeli
    MAINTENANCE_HOURLY(1, List.of("COUNT", "COST", "HOUR")),    // Maaş almadan çalışan Aile İşgücü (küçük aile  işletmeler)
    ENERGY_WATER(2, List.of("COUNT", "COST")),                  // Enerji - Su Gideri Soruları
    VETERINARY(3, List.of("COST")),                         // Veterner Hizmetleri
    INSEMINATION(4, List.of("COUNT", "COST")),                  // Suni Tohumlama Gideri
    INSURANCE(5, List.of("COST")),                          // Hayvan Hayat Sigortası
    INTEREST(6, List.of("COUNT", "COST", "HOUR")),              // işletme Sermayesi Faizi Hesabı
    TARSIM(7, List.of("COST"));                             // Bir Birim Süt Hayvanı Gideri

    private final int value;
    private final List<String> columns;

    EnumCostType(int value, List<String> columns) {
        this.value = value;
        this.columns = columns;
    }

    public static EnumCostType fromValue(Integer value) {
        for (EnumCostType t : EnumCostType.values()) {
            if (Objects.equals(t.value, value)) {
                return t;
            }
        }
        throw new IllegalArgumentException("Unknown EnumCostType value: " + value);
    }

}
