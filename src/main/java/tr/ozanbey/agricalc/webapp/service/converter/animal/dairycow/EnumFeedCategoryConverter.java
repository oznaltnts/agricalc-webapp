package tr.ozanbey.agricalc.webapp.service.converter.animal.dairycow;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import tr.ozanbey.agricalc.webapp.service.enumtype.animal.dairycow.EnumFeedCategory;

@Converter(autoApply = true)
public class EnumFeedCategoryConverter implements AttributeConverter<EnumFeedCategory, Integer> {

    @Override
    public Integer convertToDatabaseColumn(EnumFeedCategory feedType) {
        return feedType != null ? feedType.getValue() : null;
    }

    @Override
    public EnumFeedCategory convertToEntityAttribute(Integer feedType) {
        return feedType != null ? EnumFeedCategory.fromValue(feedType) : null;
    }
}
