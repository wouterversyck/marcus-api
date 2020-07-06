package be.wouterversyck.shoppinglistapi.notes.models;


import java.util.Arrays;

public enum EntryType {
    SHOPPING_LIST(1),
    SHOPPING_LIST_ITEM(2),
    LINK(3),
    RICH_TEXT(4);

    private final Integer id;

    EntryType(final Integer id) {
        this.id = id;
    }

    public static EntryType fromId(final Integer id) {
        return Arrays.stream(values())
                .filter(role -> role.id.equals(id))
                .findFirst()
                .orElseThrow(() ->
                        new IllegalArgumentException(String.format("EntryType not found for id: [%s]", id)));
    }

    public Integer getId() {
        return id;
    }
}
