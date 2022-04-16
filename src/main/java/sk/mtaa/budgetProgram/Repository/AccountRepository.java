package sk.mtaa.budgetProgram.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sk.mtaa.budgetProgram.Models.Account;

import java.util.List;


public interface AccountRepository extends JpaRepository<Account, Long> {

    List<Account> findByUserIdAndId(Long user_id, Long account_id);

    List<Account> findByUserId(Long user_id);

}
