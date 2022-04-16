package sk.mtaa.budgetProgram.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sk.mtaa.budgetProgram.Models.Transaction;

import java.time.LocalDateTime;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByAccountId(Long accountId);
    List<Transaction> findByCategoryId(Long categoryId);
    List<Transaction> findByAddedAtBetween(LocalDateTime start, LocalDateTime end);
}
