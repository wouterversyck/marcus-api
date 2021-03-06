package be.wouterversyck.marcusapi.notes.models;

public class Link extends NoteEntry {
    private String url;
    private String title;
    private String description;
    private String image;

    public Link() {
        super(EntryType.LINK);
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(final String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(final String imageUrl) {
        this.image = imageUrl;
    }
}
