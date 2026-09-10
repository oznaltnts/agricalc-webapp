package tr.ozanbey.agricalc.webapp.service.enumtype.literacy;


import lombok.Getter;
import software.xdev.chartjs.model.color.RGBAColor;

import java.util.Objects;

@Getter
public enum EnumLiteracyQuestionType {

    BUDGET(0, RGBAColor.LIGHT_PINK),
    DEBT(1, RGBAColor.RED),
    OPERATIONAL(2, RGBAColor.YELLOW),
    SAVING(3, RGBAColor.LIME);

    private final int value;
    private final RGBAColor color;

    EnumLiteracyQuestionType(int value, RGBAColor color) {
        this.value = value;
        this.color = color;
    }

    public static EnumLiteracyQuestionType fromValue(Integer value) {
        for (EnumLiteracyQuestionType s : EnumLiteracyQuestionType.values()) {
            if (Objects.equals(s.value, value)) {
                return s;
            }
        }
        throw new IllegalArgumentException("Unknown LiteracyQuestionType value: " + value);
    }
}
