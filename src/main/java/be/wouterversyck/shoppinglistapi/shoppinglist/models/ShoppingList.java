package be.wouterversyck.shoppinglistapi.shoppinglist.models;

import be.wouterversyck.shoppinglistapi.users.models.User;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.SequenceGenerator;
import javax.persistence.Column;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import java.util.List;

@Data
@Entity
@Table(name = "wv_shopping_list")
@EqualsAndHashCode
public class ShoppingList {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="shopping_list_seq")
    @SequenceGenerator(name = "shopping_list_seq", sequenceName = "shopping_list_seq", initialValue = 1000, allocationSize = 50)
    private long id;

    @Column
    private String name;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private User owner;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "wv_shopping_list_user",
            joinColumns = @JoinColumn(name = "shopping_list_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<User> contributors;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "shoppingList", orphanRemoval = true)
    private List<ShoppingListItem> items;
}
