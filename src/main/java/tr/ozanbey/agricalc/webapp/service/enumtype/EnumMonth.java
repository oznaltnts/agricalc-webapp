package tr.ozanbey.agricalc.webapp.service.enumtype;

import lombok.Getter;

public enum EnumMonth {
    JANUARY(1),
    FEBRUARY(2),
    MARCH(3),
    APRIL(4),
    MAY(5),
    JUNE(6),
    JULY(7),
    AUGUST(8),
    SEPTEMBER(9),
    OCTOBER(10),
    NOVEMBER(11),
    DECEMBER(12);

    @Getter
    private final Integer value;

    EnumMonth(Integer value) {
        this.value = value;
    }
}
