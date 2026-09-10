package tr.ozanbey.agricalc.webapp.service.converter.literacy;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import tr.ozanbey.agricalc.webapp.service.enumtype.literacy.EnumLiteracyQuestionType;

@Converter(autoApply = true)
public class EnumLiteracyQuestionConverter implements AttributeConverter<EnumLiteracyQuestionType, Integer> {

    @Override
    public Integer convertToDatabaseColumn(EnumLiteracyQuestionType questionType) {
        return questionType != null ? questionType.getValue() : null;
    }

    @Override
    public EnumLiteracyQuestionType convertToEntityAttribute(Integer questionType) {
        return questionType != null ? EnumLiteracyQuestionType.fromValue(questionType) : null;
    }
}
