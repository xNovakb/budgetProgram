package sk.mtaa.budgetProgram.Models;

import javax.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_generator")
    private long id;
    @Column(name = "email")
    private String email;
    @Column(name = "password")
    private String password;
    @Column(name = "registered_at")
    private LocalDateTime registeredAt;
    @Column(name = "photo_url")
    private String photo;
    @Column(name = "role")
    private int role;

    public User(String email, String password, LocalDateTime registeredAt, int role) {
        this.email = email;
        this.password = password;
        this.registeredAt = registeredAt;
        this.role = role;
    }

    public User(String email, String password, int role) {
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public User() {

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LocalDateTime getRegisteredAt() {
        return registeredAt;
    }

    public void setRegisteredAt(LocalDateTime registeredAt) {
        this.registeredAt = registeredAt;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", registeredAt=" + registeredAt +
                ", photo='" + photo + '\'' +
                ", role=" + role +
                '}';
    }
}
