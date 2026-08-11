package tr.ozanbey.agricalc.webapp.service.enumtype.plantation;


import lombok.Getter;
import software.xdev.chartjs.model.color.RGBAColor;

import java.util.List;

@Getter
public enum EnumAllocationType {

    TRANSACTION_COUNT(RGBAColor.LIGHT_PINK),
    DIESEL_AMOUNT(RGBAColor.LIGHT_SKY_BLUE),
    DIESEL_COST(RGBAColor.RED),
    LABOR_AMOUNT(RGBAColor.AQUA_MARINE),
    LABOR_COST(RGBAColor.YELLOW),
    LUMP_SUM_COST(RGBAColor.LIME),
    SEED_COST(RGBAColor.MAGENTA),
    SEEDLING_COST(RGBAColor.ORANGE),
    CUTTING_COST(RGBAColor.CYAN),
    TUBER_COST(RGBAColor.CRIMSON),
    FERTILIZER_COST(RGBAColor.GREEN_YELLOW),
    HERBICIDE_COST(RGBAColor.VIOLET),
    MULCH_COST(RGBAColor.GOLD),
    WATER_COST(RGBAColor.TURQUOISE),
    AMORTIZATION_AMOUNT(RGBAColor.PALE_TURQUOISE),
    POLE_COST(RGBAColor.DEEP_PINK),
    STRING_COST(RGBAColor.DARK_TURQUOISE),
    NET_COVER_COST(RGBAColor.LIGHT_BLUE),
    MEDICINE_COST(RGBAColor.KHAKI),
    MATERIAL_COST(RGBAColor.PINK);

    private final RGBAColor color;

    EnumAllocationType(RGBAColor color) {
        this.color = color;
    }

    public static List<EnumAllocationType> getAllocationCostTypes() {
        return List.of(EnumAllocationType.DIESEL_COST, EnumAllocationType.LABOR_COST, EnumAllocationType.LUMP_SUM_COST,
                EnumAllocationType.SEED_COST, EnumAllocationType.SEEDLING_COST, EnumAllocationType.CUTTING_COST,
                EnumAllocationType.TUBER_COST, EnumAllocationType.FERTILIZER_COST, EnumAllocationType.HERBICIDE_COST,
                EnumAllocationType.MULCH_COST, EnumAllocationType.WATER_COST, EnumAllocationType.POLE_COST,
                EnumAllocationType.STRING_COST, EnumAllocationType.NET_COVER_COST, EnumAllocationType.MEDICINE_COST,
                EnumAllocationType.MATERIAL_COST);
    }

}
