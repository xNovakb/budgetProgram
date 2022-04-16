package sk.mtaa.budgetProgram.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "accounts")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "account_generator")
    private long id;
    @Column(name = "name")
    private String name;
    @Column(name = "value")
    private float value;
    @Column(name = "added_at")
    private LocalDateTime added_at;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private User user;

    public Account(String name, float value, LocalDateTime added_at, User user) {
        this.name = name;
        this.value = value;
        this.added_at = added_at;
        this.user = user;
    }

    public Account() {

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public LocalDateTime getAdded_at() {
        return added_at;
    }

    public void setAdded_at(LocalDateTime added_at) {
        this.added_at = added_at;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", value=" + value +
                ", added_at=" + added_at +
                ", user=" + user +
                '}';
    }
}
