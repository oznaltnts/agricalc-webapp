package tr.ozanbey.agricalc.webapp.webapp.view;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tr.ozanbey.agricalc.webapp.service.enumtype.plantation.EnumPlantAsset;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserPlantAssetView implements Serializable {

    private EnumPlantAsset plantAsset;
    private List<AssetDetail> detailList = new ArrayList<>();

    public enum SprayingType {
        SPRAYER,
        ATOMIZER
    }

    public enum SeedingType {
        DRILL_1,
        DRILL_2
    }

    public enum OtherType {
        CAR,
        VAN,
        TRUCK,
        DRONE
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class AssetDetail implements Serializable {
        private Long recordId;

        private String assetModel;
        private BigDecimal assetPrice;
        //traktör
        private String tractorBrand;
        //ilaçlama ekipmanı
        private SprayingType sprayingType;
        private BigDecimal sprayingTank;
        //ekim makineleri
        private SeedingType seedingType;
        //hasat makineleri
        private String harvestingType = "Biçerdöver";
        //balyalama makineleri
        //hasat makineleri
        //diğer makineler
        private boolean rentIncome = false;
        private BigDecimal rentIncomePrice;
        //diğer makineler
        private OtherType otherType;
    }

}
