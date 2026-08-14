package tr.ozanbey.agricalc.webapp.service.enumtype.plantation;


import lombok.Getter;
import software.xdev.chartjs.model.color.RGBAColor;

import java.util.Objects;

@Getter
public enum EnumPlantationQuestionType {

    INCOME(0, RGBAColor.LIGHT_PINK),
    EXPENSE_SOIL(1, RGBAColor.RED),
    EXPENSE_PLANTING(2, RGBAColor.YELLOW),
    EXPENSE_FERTILIZER(3, RGBAColor.LIME),
    EXPENSE_WEED(4, RGBAColor.MAGENTA),
    EXPENSE_IRRIGATION(5, RGBAColor.ORANGE),
    EXPENSE_CULTURAL(6, RGBAColor.CYAN),
    EXPENSE_PROTECTION(7, RGBAColor.CRIMSON),
    EXPENSE_HARVEST(8, RGBAColor.GREEN_YELLOW),
    EXPENSE_BLEND(9, RGBAColor.VIOLET),
    EXPENSE_DRYING(10, RGBAColor.GOLD),
    EXPENSE_BALING(11, RGBAColor.TURQUOISE),
    EXPENSE_PACKAGING(12, RGBAColor.PALE_TURQUOISE),
    GENERAL(13, RGBAColor.DEEP_PINK);

    private final int value;
    private final RGBAColor color;

    EnumPlantationQuestionType(int value, RGBAColor color) {
        this.value = value;
        this.color = color;
    }

    public static EnumPlantationQuestionType fromValue(Integer value) {
        for (EnumPlantationQuestionType s : EnumPlantationQuestionType.values()) {
            if (Objects.equals(s.value, value)) {
                return s;
            }
        }
        throw new IllegalArgumentException("Unknown PlantationQuestionType value: " + value);
    }
}
