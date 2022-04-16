package sk.mtaa.budgetProgram.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "transaction_generator")
    private long id;
    @Column(name = "amount")
    private float amount;
    @Column(name = "description")
    private String description;
    @Column(name = "is_recurring")
    private boolean isRecurring;
    @Column(name = "recurring_days")
    private int recurringDays;
    @Column(name = "added_at")
    private LocalDateTime addedAt;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Account account;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Category category;

    public Transaction() {

    }

    public Transaction(float amount, String description, boolean isRecurring, int recurringDays, LocalDateTime addedAt, Account account, Category category) {
        this.amount = amount;
        this.description = description;
        this.isRecurring = isRecurring;
        this.recurringDays = recurringDays;
        this.addedAt = addedAt;
        this.account = account;
        this.category = category;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isRecurring() {
        return isRecurring;
    }

    public void setRecurring(boolean recurring) {
        isRecurring = recurring;
    }

    public int getRecurringDays() {
        return recurringDays;
    }

    public void setRecurringDays(int recurringDays) {
        this.recurringDays = recurringDays;
    }

    public LocalDateTime getAddedAt() {
        return addedAt;
    }

    public void setAddedAt(LocalDateTime addedAt) {
        this.addedAt = addedAt;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", amount=" + amount +
                ", description='" + description + '\'' +
                ", isRecurring=" + isRecurring +
                ", recurringDays=" + recurringDays +
                ", addedAt=" + addedAt +
                ", account=" + account +
                ", category=" + category +
                '}';
    }
}
