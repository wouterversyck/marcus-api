package be.wouterversyck.shoppinglistapi.linkpreview.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LinkPreview {
    private String title;
    private String description;
    private String image;
    private String url;
}
