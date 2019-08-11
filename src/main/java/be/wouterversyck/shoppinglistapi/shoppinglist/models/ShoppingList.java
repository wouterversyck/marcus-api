package be.wouterversyck.shoppinglistapi.shoppinglist.models;

import be.wouterversyck.shoppinglistapi.users.models.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @JsonIgnore
    @ManyToOne(optional = false)
    private User owner;

    @OneToMany
    @JoinColumn(name = "shopping_list_id")
    private List<ShoppingListItem> items;
}
