package tr.ozanbey.agricalc.webapp.service.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import tr.ozanbey.agricalc.webapp.service.enumtype.EnumRole;

@Converter(autoApply = true)
public class EnumRoleConverter implements AttributeConverter<EnumRole, Integer> {

    @Override
    public Integer convertToDatabaseColumn(EnumRole role) {
        return role != null ? role.getValue() : null;
    }

    @Override
    public EnumRole convertToEntityAttribute(Integer role) {
        return role != null ? EnumRole.fromValue(role) : null;
    }

}
