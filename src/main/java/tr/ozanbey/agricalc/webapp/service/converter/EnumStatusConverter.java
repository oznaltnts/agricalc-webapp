package tr.ozanbey.agricalc.webapp.service.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import tr.ozanbey.agricalc.webapp.service.enumtype.EnumStatus;

@Converter(autoApply = true)
public class EnumStatusConverter implements AttributeConverter<EnumStatus, Integer> {

    @Override
    public Integer convertToDatabaseColumn(EnumStatus status) {
        return status != null ? status.getValue() : null;
    }

    @Override
    public EnumStatus convertToEntityAttribute(Integer status) {
        return status != null ? EnumStatus.fromValue(status) : null;
    }
}
