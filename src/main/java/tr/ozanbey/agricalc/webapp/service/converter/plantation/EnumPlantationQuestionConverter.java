package tr.ozanbey.agricalc.webapp.service.converter.plantation;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import tr.ozanbey.agricalc.webapp.service.enumtype.plantation.EnumPlantationQuestionType;

@Converter(autoApply = true)
public class EnumPlantationQuestionConverter implements AttributeConverter<EnumPlantationQuestionType, Integer> {

    @Override
    public Integer convertToDatabaseColumn(EnumPlantationQuestionType questionType) {
        return questionType != null ? questionType.getValue() : null;
    }

    @Override
    public EnumPlantationQuestionType convertToEntityAttribute(Integer questionType) {
        return questionType != null ? EnumPlantationQuestionType.fromValue(questionType) : null;
    }
}
