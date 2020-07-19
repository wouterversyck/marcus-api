package be.wouterversyck.shoppinglistapi.notes.models;

import lombok.Data;

@Data
public class ChecklistItem {
    private String name;
    private boolean checked;
}
