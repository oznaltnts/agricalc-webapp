package tr.ozanbey.agricalc.webapp.service.converter.animal.dairycow;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import tr.ozanbey.agricalc.webapp.service.enumtype.animal.dairycow.EnumFeedType;

@Converter(autoApply = true)
public class EnumFeedTypeConverter implements AttributeConverter<EnumFeedType, Integer> {

    @Override
    public Integer convertToDatabaseColumn(EnumFeedType feedType) {
        return feedType != null ? feedType.getValue() : null;
    }

    @Override
    public EnumFeedType convertToEntityAttribute(Integer feedType) {
        return feedType != null ? EnumFeedType.fromValue(feedType) : null;
    }
}
