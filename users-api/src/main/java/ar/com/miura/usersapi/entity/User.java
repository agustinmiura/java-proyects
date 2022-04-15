package ar.com.miura.usersapi.entity;

@javax.persistence.Entity
@javax.persistence.Table(name="user")
public class User {

    @javax.persistence.Id
    private String id = java.util.UUID.randomUUID().toString();

    @javax.persistence.Column(name="username", nullable = false)
    private String username;

    @javax.persistence.Column(name="fullName", nullable = false)
    private String fullName;

    @javax.persistence.Column(name="role", nullable = false)
    private String role;

    @javax.persistence.Column(name="emailAddress", nullable = false)
    private String emailAddress;

    @javax.persistence.Column(name="status", nullable = false)
    private String status;

    @javax.persistence.Column(name="creationDate", nullable = false)
    private java.time.LocalDateTime creationDate = java.time.LocalDateTime.now();

    @javax.persistence.Column(name="deleteTime", nullable = true)
    private java.time.LocalDateTime deleteTime = null;

}
