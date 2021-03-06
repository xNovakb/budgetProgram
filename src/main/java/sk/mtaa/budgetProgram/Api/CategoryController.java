package sk.mtaa.budgetProgram.Api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import java.io.IOException;
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

    @GetMapping("/category/{userId}")
    @SendTo("/topic/category")
    public ResponseEntity<List<Category>> findByUserId(@PathVariable("userId") Long userId){

        if (!userRepository.existsById(userId)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        List<Category> categories = categoryRepository.findByUserId(userId);
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    @MessageMapping("/putCategory")
    @SendTo("/topic/category")
    public ResponseEntity<Category> updateComment(@RequestBody Category categoryRequest) {

        Category category = categoryRepository.findById(categoryRequest.getId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Category with id = " + categoryRequest.getId() + "Not Found"));
        category.setName(categoryRequest.getName());

        return new ResponseEntity<>(categoryRepository.save(category), HttpStatus.OK);
    }

    @MessageMapping("/postCategory")
    @SendTo("/topic/category")
    public Category createCategory(@RequestBody Category categoryRequest){
        System.out.println(categoryRequest.getId());
        return categoryService.createCategory(categoryRequest.getId(), categoryRequest);
    }

    @MessageMapping("/deleteCategory")
    @SendTo("/topic/category")
    public ResponseEntity<List<Account>> deleteAccountOfUser(@RequestParam("id") Long categoryId) {

        if (!categoryRepository.existsById(categoryId)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        categoryRepository.deleteById(categoryId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
