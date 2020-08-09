package be.wouterversyck.marcusapi.notes.models;

import lombok.Data;

@Data
public class ChecklistItem {
    private String name;
    private boolean checked;
}
