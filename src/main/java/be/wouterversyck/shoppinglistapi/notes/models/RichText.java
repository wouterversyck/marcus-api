package be.wouterversyck.shoppinglistapi.notes.models;

public class RichText extends NoteEntry {
    private String name;
    private String contents;

    public RichText() {
        super(EntryType.RICH_TEXT);
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(final String contents) {
        this.contents = contents;
    }
}
