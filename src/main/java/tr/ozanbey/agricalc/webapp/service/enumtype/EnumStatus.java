package tr.ozanbey.agricalc.webapp.service.enumtype;

import lombok.Getter;

@Getter
public enum EnumStatus {
    DELETED(-1),
    PASSIVE(0),
    ACTIVE(1);

    private final Integer value;

    EnumStatus(Integer value) {
        this.value = value;
    }

    public static EnumStatus fromValue(Integer value) {
        for (EnumStatus s : EnumStatus.values()) {
            if (s.value == value) {
                return s;
            }
        }
        throw new IllegalArgumentException("Unknown Status value: " + value);
    }
}
