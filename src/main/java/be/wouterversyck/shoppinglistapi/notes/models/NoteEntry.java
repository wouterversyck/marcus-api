package be.wouterversyck.shoppinglistapi.notes.models;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "entryType")
@JsonSubTypes({
        @JsonSubTypes.Type(value = RichText.class, name = "RICH_TEXT"),
        @JsonSubTypes.Type(value = Checklist.class, name = "CHECK_LIST"),
        @JsonSubTypes.Type(value = Link.class, name = "LINK")
})
public abstract class NoteEntry {
    private EntryType entryType;

    public NoteEntry(final EntryType entryType) {
        this.entryType = entryType;
    }

    public EntryType getEntryType() {
        return entryType;
    }
}
