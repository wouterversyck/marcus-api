package be.wouterversyck.shoppinglistapi.shoppinglist.models;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.SequenceGenerator;
import javax.persistence.Column;

@Data
@Entity
@Table(name = "wv_shopping_list_items")
@EqualsAndHashCode
public class ShoppingListItem {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="shopping_list_items_seq")
    @SequenceGenerator(name = "shopping_list_items_seq", sequenceName = "shopping_list_items_seq", initialValue = 1000, allocationSize=50)
    private long id;

    @Column
    private String name;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "shopping_list_id")
    private ShoppingList shoppingList;
}
