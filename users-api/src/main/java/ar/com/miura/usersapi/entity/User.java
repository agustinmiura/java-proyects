package ar.com.miura.usersapi.entity;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import java.util.HashSet;
import java.util.Set;

@javax.persistence.Entity
@javax.persistence.Table(name="user")
@lombok.Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @Column(length = 36, nullable = false, updatable = false)
    private String id = java.util.UUID.randomUUID().toString();

    @javax.persistence.Column(name="username", nullable = false)
    private String username;

    @javax.persistence.Column(name="fullName", nullable = false)
    private String fullName;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "user_roles",
        joinColumns = @JoinColumn(name="user_id"),
        inverseJoinColumns = @JoinColumn(name="role_id")
    )
    private Set<Role> roles = new HashSet<Role>();

    @javax.persistence.Column(name="emailAddress", nullable = false)
    private String emailAddress;

    @javax.persistence.Column(name="status", nullable = false)
    private String status;

    @javax.persistence.Column(name="creationDate", nullable = false)
    private java.time.LocalDateTime creationDate = java.time.LocalDateTime.now();

    @javax.persistence.Column(name="deleteTime", nullable = true)
    private java.time.LocalDateTime deleteTime = null;



}
