package tr.ozanbey.agricalc.webapp.service.enumtype;

import lombok.Getter;

import java.util.Objects;

@Getter
public enum EnumRole {
    ADMIN(1),
    USER(2);

    private final Integer value;

    EnumRole(Integer value) {
        this.value = value;
    }

    public static EnumRole fromValue(Integer value) {
        for (EnumRole a : EnumRole.values()) {
            if (Objects.equals(a.value, value)) {
                return a;
            }
        }
        throw new IllegalArgumentException("Unknown Role value: " + value);
    }

}
