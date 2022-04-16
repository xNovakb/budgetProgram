package sk.mtaa.budgetProgram.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sk.mtaa.budgetProgram.Models.Category;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    List<Category> findByUserId(Long userId);

}
