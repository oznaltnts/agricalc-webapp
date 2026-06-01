package tr.ozanbey.agricalc.webapp.service.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import tr.ozanbey.agricalc.webapp.service.enumtype.animal.EnumFeedCategory;

@Converter(autoApply = true)
public class EnumFeedCategoryConverter implements AttributeConverter<EnumFeedCategory, Integer> {

    @Override
    public Integer convertToDatabaseColumn(EnumFeedCategory feedCategory) {
        return feedCategory != null ? feedCategory.getValue() : null;
    }

    @Override
    public EnumFeedCategory convertToEntityAttribute(Integer feedCategory) {
        return feedCategory != null ? EnumFeedCategory.fromValue(feedCategory) : null;
    }

}
