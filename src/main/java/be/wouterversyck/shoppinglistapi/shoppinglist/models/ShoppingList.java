package be.wouterversyck.shoppinglistapi.shoppinglist.models;

import be.wouterversyck.shoppinglistapi.users.models.User;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "wv_shopping_list")
@EqualsAndHashCode
public class ShoppingList {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private long id;

    @Column
    private String name;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private User owner;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "shopping_list_id")
    private List<ShoppingListItem> items;
}
