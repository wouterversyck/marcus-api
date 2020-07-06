package be.wouterversyck.shoppinglistapi.notes.models;

import be.wouterversyck.shoppinglistapi.notes.persistence.EntryTypeConverter;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.CascadeType;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.SequenceGenerator;
import javax.persistence.Column;
import java.util.List;

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
    private String contents;

    @Convert(converter = EntryTypeConverter.class)
    @Column(name = "entry_type_id")
    private EntryType entryType;

    @Column
    private boolean checked;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "parentEntry", orphanRemoval = true)
    private List<ShoppingListItem> children;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_entry_id")
    private ShoppingList parentEntry;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shopping_list_id")
    private ShoppingList shoppingList;
}
