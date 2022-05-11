package sk.mtaa.budgetProgram.DTO;

import lombok.*;

import java.time.LocalDateTime;

public class TransactionDto {

    private Long id;
    private Long categoryId;
    private Long accountId;
    private float amount;
    private String description;
    private boolean isRecurring;
    private int recurringDays;
    private LocalDateTime addedAt;

    public TransactionDto(Long id, Long categoryId, Long accountId, float amount, String description, boolean isRecurring, int recurringDays, LocalDateTime addedAt) {
        this.id = id;
        this.categoryId = categoryId;
        this.accountId = accountId;
        this.amount = amount;
        this.description = description;
        this.isRecurring = isRecurring;
        this.recurringDays = recurringDays;
        this.addedAt = addedAt;
    }

    public TransactionDto() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
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
}
