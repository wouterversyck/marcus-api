package be.wouterversyck.shoppinglistapi.users.models;

import be.wouterversyck.shoppinglistapi.shoppinglist.models.ShoppingList;
import be.wouterversyck.shoppinglistapi.users.persistence.RoleConverter;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.CascadeType;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
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
@Table(name = "wv_user")
@EqualsAndHashCode
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="user_seq")
    @SequenceGenerator(name = "user_seq", sequenceName = "user_seq", initialValue = 1000, allocationSize = 50)
    private long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column
    private String password;

    @Convert(converter = RoleConverter.class)
    @Column(name = "role_id")
    private Role role;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ShoppingList> ownedLists;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "wv_shopping_list_user",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "shopping_list_id"))
    private List<ShoppingList> contributionLists;
}
