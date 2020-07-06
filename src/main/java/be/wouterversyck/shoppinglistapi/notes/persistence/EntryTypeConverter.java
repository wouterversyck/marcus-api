package be.wouterversyck.shoppinglistapi.notes.persistence;

import be.wouterversyck.shoppinglistapi.notes.models.EntryType;

import javax.persistence.AttributeConverter;

public class EntryTypeConverter implements AttributeConverter<EntryType, Integer> {

    @Override
    public Integer convertToDatabaseColumn(final EntryType role) {
        return role.getId();
    }

    @Override
    public EntryType convertToEntityAttribute(final Integer id) {
        return EntryType.fromId(id);
    }
}

