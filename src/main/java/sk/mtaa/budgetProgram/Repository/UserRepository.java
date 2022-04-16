package sk.mtaa.budgetProgram.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sk.mtaa.budgetProgram.Models.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmailAndPassword(String email, String password);
}
