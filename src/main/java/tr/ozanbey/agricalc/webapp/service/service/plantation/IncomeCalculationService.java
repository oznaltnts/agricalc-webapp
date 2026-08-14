package tr.ozanbey.agricalc.webapp.service.service.plantation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tr.ozanbey.agricalc.webapp.service.domain.City;
import tr.ozanbey.agricalc.webapp.service.domain.plantation.PlantationProductQuestion;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class IncomeCalculationService {

    //400
    private double mainProductYield17PerDa(double mainYield17PerDa, int harvestCount) {
        return mainYield17PerDa * harvestCount;
    }

    //0,2
    private double mainProductYield13PerDa(double mainYield13PerDa) {
        return mainYield13PerDa / 1000;
    }

    //17250
    private BigDecimal mainProductGrossIncome3012PerDa(BigDecimal salePrice30PerKg, double mainYield12PerDa) {
        return salePrice30PerKg.multiply(BigDecimal.valueOf(mainYield12PerDa));
    }

    //60000
    private BigDecimal mainProductGrossIncome1430PerDa(double mainYield14PerDa, BigDecimal salePrice30PerKg) {
        return BigDecimal.valueOf(mainYield14PerDa).multiply(salePrice30PerKg);
    }

    //6000
    private BigDecimal mainProductGrossIncome1730PerDa(double mainYield17PerDa, BigDecimal salePrice30PerKg) {
        return BigDecimal.valueOf(mainYield17PerDa).multiply(salePrice30PerKg);
    }

    //3
    private BigDecimal mainProductGrossIncome3013PerDa(BigDecimal salePrice30PerKg, double mainProductYield13PerDa) {
        return salePrice30PerKg.multiply(BigDecimal.valueOf(mainProductYield13PerDa));
    }

    //35000
    private BigDecimal mainProductGrossIncome1531PerDa(double mainYield15PerDa, BigDecimal salePrice31PerKg) {
        return BigDecimal.valueOf(mainYield15PerDa).multiply(salePrice31PerKg);
    }

    //3080
    private BigDecimal mainProductGrossIncome1632PerDa(double mainYield16PerDa, BigDecimal salePrice32PerKg) {
        return BigDecimal.valueOf(mainYield16PerDa).multiply(salePrice32PerKg);
    }

    //140083
    private BigDecimal mainProductGrossIncome(double mainYield17PerDa, int harvestCount,
                                              BigDecimal salePrice30PerKg, double mainYield12PerDa,
                                              double mainYield14PerDa,
                                              double mainProductYield13PerDa,
                                              double mainYield15PerDa, BigDecimal salePrice31PerKg,
                                              double mainYield16PerDa, BigDecimal salePrice32PerKg) {
        double mainProductYield17PerDa = mainProductYield17PerDa(mainYield17PerDa, harvestCount);

        return mainProductGrossIncome3012PerDa(salePrice30PerKg, mainYield12PerDa)
                .add(mainProductGrossIncome1430PerDa(mainYield14PerDa, salePrice30PerKg))
                .add(mainProductGrossIncome1730PerDa(mainProductYield17PerDa, salePrice30PerKg))
                .add(mainProductGrossIncome3013PerDa(salePrice30PerKg, mainProductYield13PerDa))
                .add(mainProductGrossIncome1531PerDa(mainYield15PerDa, salePrice31PerKg))
                .add(mainProductGrossIncome1632PerDa(mainYield16PerDa, salePrice32PerKg));
    }

    //1035
    private double highQualityYield1218PerDa(double mainYield12PerDa, double lowQuality18Rate) {
        return mainYield12PerDa * (100 - lowQuality18Rate) / 100;
    }

    //115
    private double lowQualityYield1218PerDa(double mainYield12PerDa, double lowQuality18Rate) {
        return mainYield12PerDa * lowQuality18Rate / 100;
    }

    //15755
    private BigDecimal highQualityGrossIncome3012PerDa(double highQualityYield1218PerDa, BigDecimal salePrice30PerKg, double lowQualityYield1218PerDa, BigDecimal salePrice33PerKg) {
        return BigDecimal.valueOf(highQualityYield1218PerDa).multiply(salePrice30PerKg)
                .add(BigDecimal.valueOf(lowQualityYield1218PerDa).multiply(salePrice33PerKg));
    }

    //32880
    private BigDecimal highQualityGrossIncome(double mainYield12PerDa, double lowQuality18Rate, BigDecimal salePrice30PerKg, BigDecimal salePrice33PerKg) {
        double highQualityYield1218PerDa = highQualityYield1218PerDa(mainYield12PerDa, lowQuality18Rate);
        double lowQualityYield1218PerDa = lowQualityYield1218PerDa(mainYield12PerDa, lowQuality18Rate);

        return highQualityGrossIncome3012PerDa(highQualityYield1218PerDa, salePrice30PerKg, lowQualityYield1218PerDa, salePrice33PerKg);
    }

    //17850
    private BigDecimal juiceGrossIncome3012PerDa(BigDecimal mainProductGrossIncome3012PerDa, double juiceYield19PerDa, BigDecimal juicePrice34PerKg) {
        return mainProductGrossIncome3012PerDa.add(BigDecimal.valueOf(juiceYield19PerDa).multiply(juicePrice34PerKg));
    }

    //37200
    private BigDecimal juiceGrossIncome(BigDecimal juiceGrossIncome3012PerDa) {
        return juiceGrossIncome3012PerDa;
    }

    //17700
    private BigDecimal sideStrawGrossIncome3012PerDa(BigDecimal mainProductGrossIncome3012PerDa, double sideStrawYield20PerDa, BigDecimal sidePrice35PerKg) {
        return mainProductGrossIncome3012PerDa.add(BigDecimal.valueOf(sideStrawYield20PerDa).multiply(sidePrice35PerKg));
    }

    //60450
    private BigDecimal sideStrawGrossIncome3014PerDa(BigDecimal mainProductGrossIncome1430PerDa, double sideStrawYield20PerDa, BigDecimal sidePrice35PerKg) {
        return mainProductGrossIncome1430PerDa.add(BigDecimal.valueOf(sideStrawYield20PerDa).multiply(sidePrice35PerKg));
    }

    //78150
    private BigDecimal sideStrawGrossIncome(BigDecimal sideStrawGrossIncome3012PerDa, BigDecimal sideStrawGrossIncome3014PerDa) {
        return sideStrawGrossIncome3012PerDa.add(sideStrawGrossIncome3014PerDa);
    }

    //17325
    private BigDecimal sideGrainGrossIncome3012PerDa(BigDecimal mainProductGrossIncome3012PerDa, double sideGrainYield21PerDa, BigDecimal sidePrice35PerKg) {
        return mainProductGrossIncome3012PerDa.add(BigDecimal.valueOf(sideGrainYield21PerDa).multiply(sidePrice35PerKg));
    }

    //60075
    private BigDecimal sideGrainGrossIncome3014PerDa(BigDecimal mainProductGrossIncome1430PerDa, double sideGrainYield21PerDa, BigDecimal sidePrice35PerKg) {
        return mainProductGrossIncome1430PerDa.add(BigDecimal.valueOf(sideGrainYield21PerDa).multiply(sidePrice35PerKg));
    }

    //77400
    private BigDecimal sideGrainGrossIncome(BigDecimal sideGrainGrossIncome3012PerDa, BigDecimal sideGrainGrossIncome3014PerDa) {
        return sideGrainGrossIncome3012PerDa.add(sideGrainGrossIncome3014PerDa);
    }

    //17505
    private BigDecimal sideFodderGrossIncome3012PerDa(BigDecimal mainProductGrossIncome3012PerDa, double sideFodderYield22PerDa, BigDecimal sidePrice35PerKg) {
        return mainProductGrossIncome3012PerDa.add(BigDecimal.valueOf(sideFodderYield22PerDa).multiply(sidePrice35PerKg));
    }

    //60255
    private BigDecimal sideFodderGrossIncome3014PerDa(BigDecimal mainProductGrossIncome1430PerDa, double sideFodderYield22PerDa, BigDecimal sidePrice35PerKg) {
        return mainProductGrossIncome1430PerDa.add(BigDecimal.valueOf(sideFodderYield22PerDa).multiply(sidePrice35PerKg));
    }

    //77760
    private BigDecimal sideFodderGrossIncome(BigDecimal sideFodderGrossIncome3012PerDa, BigDecimal sideFodderGrossIncome3014PerDa) {
        return sideFodderGrossIncome3012PerDa.add(sideFodderGrossIncome3014PerDa);
    }

    //17302,5
    private BigDecimal sideSeedGrossIncome3012PerDa(BigDecimal mainProductGrossIncome3012PerDa, double sideSeedYield23PerDa, BigDecimal sidePrice35PerKg) {
        return mainProductGrossIncome3012PerDa.add(BigDecimal.valueOf(sideSeedYield23PerDa).multiply(sidePrice35PerKg));
    }

    //17302,5
    private BigDecimal sideSeedGrossIncome(BigDecimal sideSeedGrossIncome3012PerDa) {
        return sideSeedGrossIncome3012PerDa;
    }

    //17340
    private BigDecimal sidePickleGrossIncome3012PerDa(BigDecimal mainProductGrossIncome3012PerDa, double sidePickleYield24PerDa, BigDecimal sidePrice35PerKg) {
        return mainProductGrossIncome3012PerDa.add(BigDecimal.valueOf(sidePickleYield24PerDa).multiply(sidePrice35PerKg));
    }

    //17340
    private BigDecimal sidePickleGrossIncome(BigDecimal sidePickleGrossIncome3012PerDa) {
        return sidePickleGrossIncome3012PerDa;
    }

    //0,55
    private double sideProductYield13PerDa(double sideYield13PerDa) {
        return sideYield13PerDa / 100;
    }

    //93,55
    private BigDecimal sideProductGrossIncome3013PerDa(double mainProductYield13PerDa, BigDecimal salePrice30PerKg, BigDecimal salePrice36PerGr, double sideProductYield13PerDa) {
        return BigDecimal.valueOf(mainProductYield13PerDa).multiply(salePrice30PerKg)
                .add(salePrice36PerGr.add(BigDecimal.valueOf(sideProductYield13PerDa)));
    }

    //93,55
    private BigDecimal sideProductGrossIncome(BigDecimal sideProductGrossIncome3013PerDa) {
        return sideProductGrossIncome3013PerDa;
    }

    //478209,05
    private BigDecimal totalGrossIncome(double mainYield17PerDa, int harvestCount, double mainYield13PerDa, BigDecimal salePrice30PerKg, double mainYield12PerDa, double mainYield14PerDa, double mainYield15PerDa, BigDecimal salePrice31PerKg, double mainYield16PerDa, BigDecimal salePrice32PerKg,
                                        double lowQuality18Rate, BigDecimal salePrice33PerKg,
                                        double juiceYield19PerDa, BigDecimal juicePrice34PerKg,
                                        double sideStrawYield20PerDa, BigDecimal sidePrice35PerKg,
                                        double sideGrainYield21PerDa,
                                        double sideFodderYield22PerDa,
                                        double sideSeedYield23PerDa,
                                        double sidePickleYield24PerDa,
                                        double sideYield13PerDa, BigDecimal salePrice36PerGr) {
        BigDecimal mainProductGrossIncome3012PerDa = mainProductGrossIncome3012PerDa(salePrice30PerKg, mainYield12PerDa);
        BigDecimal juiceGrossIncome3012PerDa = juiceGrossIncome3012PerDa(mainProductGrossIncome3012PerDa, juiceYield19PerDa, juicePrice34PerKg);

        BigDecimal sideStrawGrossIncome3012PerDa = sideStrawGrossIncome3012PerDa(mainProductGrossIncome3012PerDa, sideStrawYield20PerDa, sidePrice35PerKg);
        BigDecimal mainProductGrossIncome1430PerDa = mainProductGrossIncome1430PerDa(mainYield14PerDa, salePrice30PerKg);
        BigDecimal sideStrawGrossIncome3014PerDa = sideStrawGrossIncome3014PerDa(mainProductGrossIncome1430PerDa, sideStrawYield20PerDa, sidePrice35PerKg);

        BigDecimal sideGrainGrossIncome3012PerDa = sideGrainGrossIncome3012PerDa(mainProductGrossIncome3012PerDa, sideGrainYield21PerDa, sidePrice35PerKg);
        BigDecimal sideGrainGrossIncome3014PerDa = sideGrainGrossIncome3014PerDa(mainProductGrossIncome1430PerDa, sideGrainYield21PerDa, sidePrice35PerKg);

        BigDecimal sideFodderGrossIncome3012PerDa = sideFodderGrossIncome3012PerDa(mainProductGrossIncome3012PerDa, sideFodderYield22PerDa, sidePrice35PerKg);
        BigDecimal sideFodderGrossIncome3014PerDa = sideFodderGrossIncome3014PerDa(mainProductGrossIncome1430PerDa, sideFodderYield22PerDa, sidePrice35PerKg);

        BigDecimal sideSeedGrossIncome3012PerDa = sideSeedGrossIncome3012PerDa(mainProductGrossIncome3012PerDa, sideSeedYield23PerDa, sidePrice35PerKg);

        BigDecimal sidePickleGrossIncome3012PerDa = sidePickleGrossIncome3012PerDa(mainProductGrossIncome3012PerDa, sidePickleYield24PerDa, sidePrice35PerKg);

        double mainProductYield13PerDa = mainProductYield13PerDa(mainYield13PerDa);
        double sideProductYield13PerDa = sideProductYield13PerDa(sideYield13PerDa);
        BigDecimal sideProductGrossIncome3013PerDa = sideProductGrossIncome3013PerDa(mainProductYield13PerDa, salePrice30PerKg, salePrice36PerGr, sideProductYield13PerDa);

        return mainProductGrossIncome(mainYield17PerDa, harvestCount, salePrice30PerKg, mainYield12PerDa, mainYield14PerDa, mainProductYield13PerDa, mainYield15PerDa, salePrice31PerKg, mainYield16PerDa, salePrice32PerKg)
                .add(highQualityGrossIncome(mainYield12PerDa, lowQuality18Rate, salePrice30PerKg, salePrice33PerKg))
                .add(juiceGrossIncome(juiceGrossIncome3012PerDa))
                .add(sideStrawGrossIncome(sideStrawGrossIncome3012PerDa, sideStrawGrossIncome3014PerDa))
                .add(sideGrainGrossIncome(sideGrainGrossIncome3012PerDa, sideGrainGrossIncome3014PerDa))
                .add(sideFodderGrossIncome(sideFodderGrossIncome3012PerDa, sideFodderGrossIncome3014PerDa))
                .add(sideSeedGrossIncome(sideSeedGrossIncome3012PerDa))
                .add(sidePickleGrossIncome(sidePickleGrossIncome3012PerDa))
                .add(sideProductGrossIncome(sideProductGrossIncome3013PerDa));
    }

    public BigDecimal calculateIncome(List<PlantationProductQuestion> productQuestionList, Long parcelPlanId, City city) {
        double mainYield17PerDa = doubleValueSetter(productQuestionList, 17L);
        int harvestCount = integerValueSetter(productQuestionList, 299L);
        double mainYield13PerDa = doubleValueSetter(productQuestionList, 13L);
        BigDecimal salePrice30PerKg = decimalValueSetter(productQuestionList, 30L);
        double mainYield12PerDa = doubleValueSetter(productQuestionList, 12L);
        double mainYield14PerDa = doubleValueSetter(productQuestionList, 14L);
        double mainYield15PerDa = doubleValueSetter(productQuestionList, 15L);
        BigDecimal salePrice31PerKg = decimalValueSetter(productQuestionList, 31L);
        double mainYield16PerDa = doubleValueSetter(productQuestionList, 16L);
        BigDecimal salePrice32PerKg = decimalValueSetter(productQuestionList, 32L);
        double lowQuality18Rate = doubleValueSetter(productQuestionList, 18L);
        BigDecimal salePrice33PerKg = decimalValueSetter(productQuestionList, 33L);
        double juiceYield19PerDa = doubleValueSetter(productQuestionList, 19L);
        BigDecimal juicePrice34PerKg = decimalValueSetter(productQuestionList, 34L);
        double sideStrawYield20PerDa = doubleValueSetter(productQuestionList, 20L);
        BigDecimal sidePrice35PerKg = decimalValueSetter(productQuestionList, 35L);
        double sideGrainYield21PerDa = doubleValueSetter(productQuestionList, 21L);
        double sideFodderYield22PerDa = doubleValueSetter(productQuestionList, 22L);
        double sideSeedYield23PerDa = doubleValueSetter(productQuestionList, 23L);
        double sidePickleYield24PerDa = doubleValueSetter(productQuestionList, 24L);
        double sideYield13PerDa = doubleValueSetter(productQuestionList, 13L);
        BigDecimal salePrice36PerGr = decimalValueSetter(productQuestionList, 36L);

        return totalGrossIncome(mainYield17PerDa, harvestCount, mainYield13PerDa, salePrice30PerKg, mainYield12PerDa,
                mainYield14PerDa, mainYield15PerDa, salePrice31PerKg, mainYield16PerDa, salePrice32PerKg,
                lowQuality18Rate, salePrice33PerKg, juiceYield19PerDa, juicePrice34PerKg, sideStrawYield20PerDa,
                sidePrice35PerKg, sideGrainYield21PerDa, sideFodderYield22PerDa, sideSeedYield23PerDa,
                sidePickleYield24PerDa, sideYield13PerDa, salePrice36PerGr);
    }

    private BigDecimal decimalValueSetter(List<PlantationProductQuestion> questionList, Long questionId) {
        Optional<PlantationProductQuestion> optional = questionList.stream()
                .filter(q -> q.getPlantationQuestion().getId().equals(questionId))
                .findAny();
        if (optional.isPresent() && optional.get().getBigDecimalValue() != null && optional.get().getBigDecimalValue().compareTo(BigDecimal.ZERO) > 0) {
            if (optional.get().getMaximumValue() != null && optional.get().getBigDecimalValue().compareTo(optional.get().getMaximumValue()) > 0) {
                return optional.get().getMaximumValue();
            } else if (optional.get().getMinimumValue() != null && optional.get().getMinimumValue().compareTo(optional.get().getBigDecimalValue()) > 0) {
                return optional.get().getMinimumValue();
            } else {
                return optional.get().getBigDecimalValue();
            }
        }
        return BigDecimal.ZERO;
    }

    private Double doubleValueSetter(List<PlantationProductQuestion> questionList, Long questionId) {
        Optional<PlantationProductQuestion> optional = questionList.stream()
                .filter(q -> q.getPlantationQuestion().getId().equals(questionId))
                .findAny();
        if (optional.isPresent() && optional.get().getDoubleValue() != null && optional.get().getDoubleValue() > 0) {
            if (optional.get().getMaximumValue() != null && optional.get().getDoubleValue() > optional.get().getMaximumValue().doubleValue()) {
                return optional.get().getMaximumValue().doubleValue();
            } else if (optional.get().getMinimumValue() != null && optional.get().getMinimumValue().doubleValue() > optional.get().getDoubleValue()) {
                return optional.get().getMinimumValue().doubleValue();
            } else {
                return optional.get().getDoubleValue();
            }
        }
        return 0d;
    }

    private Integer integerValueSetter(List<PlantationProductQuestion> questionList, Long questionId) {
        Optional<PlantationProductQuestion> optional = questionList.stream()
                .filter(q -> q.getPlantationQuestion().getId().equals(questionId))
                .findAny();
        if (optional.isPresent() && optional.get().getIntegerValue() != null && optional.get().getIntegerValue() > 0) {
            if (optional.get().getMaximumValue() != null && optional.get().getIntegerValue().doubleValue() > optional.get().getMaximumValue().doubleValue()) {
                return optional.get().getMaximumValue().intValue();
            } else if (optional.get().getMinimumValue() != null && optional.get().getMinimumValue().doubleValue() > optional.get().getIntegerValue().doubleValue()) {
                return optional.get().getMinimumValue().intValue();
            } else {
                return optional.get().getIntegerValue();
            }
        }
        return 0;
    }


}
