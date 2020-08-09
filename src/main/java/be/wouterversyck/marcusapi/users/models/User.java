package be.wouterversyck.marcusapi.users.models;

import be.wouterversyck.marcusapi.users.persistence.RoleConverter;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.SequenceGenerator;
import javax.persistence.Column;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

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
    @NotBlank
    @Pattern(regexp = "^[a-zA-Z0-9]{1,25}$")
    private String username;

    @Column(nullable = false, unique = true)
    @Email
    private String email;

    @Column
    private String password;

    @Convert(converter = RoleConverter.class)
    @Column(name = "role_id")
    @NotNull
    private Role role;
}
