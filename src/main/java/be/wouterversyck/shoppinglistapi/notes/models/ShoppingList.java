package be.wouterversyck.shoppinglistapi.notes.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;
import java.util.List;

@Data
@Document(collection = "notes")
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShoppingList {
    @Id
    private String id;
    private String name;
    private long owner;
    private List<Long> contributors;
    private List<ShoppingListItem> items;
}
