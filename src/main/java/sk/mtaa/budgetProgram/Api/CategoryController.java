package sk.mtaa.budgetProgram.Api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import sk.mtaa.budgetProgram.Api.Service.CategoryService;
import sk.mtaa.budgetProgram.Models.Account;
import sk.mtaa.budgetProgram.Models.Category;
import sk.mtaa.budgetProgram.Repository.CategoryRepository;
import sk.mtaa.budgetProgram.Repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@CrossOrigin(origins = "http://localhost:12345")
@RestController
@RequestMapping("/api")
public class CategoryController {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryService categoryService;

    @MessageMapping("/category/{userId}")
    @SendTo("/topic/category")
    public ResponseEntity<List<Category>> findByUserId(@PathVariable("userId") Long userId){

        if (!userRepository.existsById(userId)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        List<Category> categories = categoryRepository.findByUserId(userId);
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    @MessageMapping("/putCategory/{categoryId}")
    @SendTo("/topic/category")
    public ResponseEntity<Category> updateComment(@PathVariable("categoryId") long categoryId,
                                                 @RequestBody Category categoryRequest) {

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Category with id = " + categoryId + "Not Found"));
        category.setName(categoryRequest.getName());

        return new ResponseEntity<>(categoryRepository.save(category), HttpStatus.OK);
    }

    @MessageMapping("/postCategory/{id}")
    @SendTo("/topic/category")
    public Category createCategory(@PathVariable("id") Long userId, @RequestBody Category categoryRequest){
        Category category = categoryService.createCategory(userId, categoryRequest);

        return categoryRepository.save(category);
    }

    @MessageMapping("/deleteCategory/{id}")
    @SendTo("/topic/category")
    public ResponseEntity<List<Account>> deleteAccountOfUser(@PathVariable(value = "id") long categoryId) {

        if (!categoryRepository.existsById(categoryId)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        categoryRepository.deleteById(categoryId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
