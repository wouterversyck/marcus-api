package be.wouterversyck.marcusapi.users.models;

import lombok.Data;

import javax.persistence.Table;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.GenerationType;
import javax.persistence.GeneratedValue;
import javax.persistence.SequenceGenerator;
import javax.persistence.Column;


@Data
@Entity
@Table(name = "wv_role")
public class RoleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="role_seq")
    @SequenceGenerator(name = "role_seq", sequenceName = "role_seq", initialValue = 1000, allocationSize = 50)
    private long id;

    @Column
    private String name;
}
