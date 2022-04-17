package ar.com.miura.usersapi.entity;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;

@javax.persistence.Entity
@javax.persistence.Table(name="role")
@lombok.Data
@AllArgsConstructor
@NoArgsConstructor
public class Role {

    @Id
    @Column(length = 36, nullable = false, updatable = false)
    private String id = java.util.UUID.randomUUID().toString();

    @javax.persistence.Column(name="name", nullable = false)
    private String name;

    @javax.persistence.Column(name="creationDate", nullable = false)
    private java.time.LocalDateTime creationDate = java.time.LocalDateTime.now();

    @javax.persistence.Column(name="deleteTime", nullable = true)
    private java.time.LocalDateTime deleteTime = null;

}
