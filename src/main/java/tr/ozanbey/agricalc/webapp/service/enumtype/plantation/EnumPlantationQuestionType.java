package tr.ozanbey.agricalc.webapp.service.enumtype.plantation;


import lombok.Getter;

import java.util.Objects;

@Getter
public enum EnumPlantationQuestionType {

    INCOME(0),
    EXPENSE_SOIL(1),
    EXPENSE_PLANTING(2),
    EXPENSE_FERTILIZER(3),
    EXPENSE_WEED(4),
    EXPENSE_IRRIGATION(5),
    EXPENSE_CULTURAL(6),
    EXPENSE_PROTECTION(7),
    EXPENSE_HARVEST(8),
    EXPENSE_BLEND(9),
    EXPENSE_DRYING(10),
    EXPENSE_BALING(11),
    EXPENSE_PACKAGING(12),
    GENERAL(13);

    private final int value;

    EnumPlantationQuestionType(int value) {
        this.value = value;
    }

    public static EnumPlantationQuestionType fromValue(Integer value) {
        for (EnumPlantationQuestionType s : EnumPlantationQuestionType.values()) {
            if (Objects.equals(s.value, value)) {
                return s;
            }
        }
        throw new IllegalArgumentException("Unknown PlantationQuestionType value: " + value);
    }

    public static EnumPlantationQuestionType[] expenseValues() {
        return new EnumPlantationQuestionType[]{EnumPlantationQuestionType.EXPENSE_SOIL,
                EnumPlantationQuestionType.EXPENSE_PLANTING,
                EnumPlantationQuestionType.EXPENSE_FERTILIZER,
                EnumPlantationQuestionType.EXPENSE_WEED,
                EnumPlantationQuestionType.EXPENSE_IRRIGATION,
                EnumPlantationQuestionType.EXPENSE_CULTURAL,
                EnumPlantationQuestionType.EXPENSE_PROTECTION,
                EnumPlantationQuestionType.EXPENSE_HARVEST,
                EnumPlantationQuestionType.EXPENSE_BLEND,
                EnumPlantationQuestionType.EXPENSE_DRYING,
                EnumPlantationQuestionType.EXPENSE_BALING,
                EnumPlantationQuestionType.EXPENSE_PACKAGING};
    }

}
