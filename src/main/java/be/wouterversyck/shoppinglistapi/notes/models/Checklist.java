package be.wouterversyck.shoppinglistapi.notes.models;

import java.util.List;

public class Checklist extends NoteEntry {

    public Checklist() {
        super(EntryType.CHECK_LIST);
    }

    private String name;
    private List<ChecklistItem> items;

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public List<ChecklistItem> getItems() {
        return items;
    }

    public void setItems(final List<ChecklistItem> items) {
        this.items = items;
    }
}
