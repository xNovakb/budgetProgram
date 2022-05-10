package sk.mtaa.budgetProgram.Api.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import sk.mtaa.budgetProgram.Models.Category;
import sk.mtaa.budgetProgram.Repository.CategoryRepository;
import sk.mtaa.budgetProgram.Repository.UserRepository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

@Service
@Transactional
public class CategoryService {

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    UserRepository userRepository;

    public Category createCategory(Long userId, Category categoryRequest){
        Category category = userRepository.findById(userId).map(user -> {
            categoryRequest.setUser(user);
            categoryRequest.setAddedAt(LocalDateTime.now());
            return categoryRepository.save(categoryRequest);
        }).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "User with id = " + userId + "Not Found"));
        return categoryRepository.save(category);
    }
}
