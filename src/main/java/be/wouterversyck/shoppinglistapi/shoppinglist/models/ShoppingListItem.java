package be.wouterversyck.shoppinglistapi.shoppinglist.models;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@Data
@Entity
@Table(name = "wv_shopping_list_items")
@EqualsAndHashCode
public class ShoppingListItem {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private long id;

    @Column
    private String name;
}
