package sk.mtaa.budgetProgram.Api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import sk.mtaa.budgetProgram.Models.Account;
import sk.mtaa.budgetProgram.Repository.AccountRepository;
import sk.mtaa.budgetProgram.Repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@CrossOrigin(origins = "http://localhost:12345")
@RestController
@RequestMapping("/api")
public class AccountController {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/accounts/{userId}")
    public ResponseEntity<List<Account>> getAllAccountsByUserId(@PathVariable(value = "userId") Long userId) {
        if (!userRepository.existsById(userId)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        List<Account> accounts = accountRepository.findByUserId(userId);
        return new ResponseEntity<>(accounts, HttpStatus.OK);
    }

    @GetMapping("/accounts/{userId}/{accountId}")
    public ResponseEntity<List<Account>> getAllAccountsByUserIdAndAccount(@PathVariable(value = "userId") Long userId,
                                                                          @PathVariable(value = "accountId") Long accountId) {
        if (!userRepository.existsById(userId)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        List<Account> accounts = accountRepository.findByUserIdAndId(userId, accountId);
        return new ResponseEntity<>(accounts, HttpStatus.OK);
    }

    @PostMapping("/account/{userId}")
    public ResponseEntity<Account> createAccount(@PathVariable(value = "userId") long userId,
                                                 @RequestBody Account accountRequest) {
        Account account = userRepository.findById(userId).map(user -> {
            accountRequest.setUser(user);
            accountRequest.setAdded_at(LocalDateTime.now());
            return accountRepository.save(accountRequest);
        }).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "User with id = " + userId + "Not Found"));
        return new ResponseEntity<>(account, HttpStatus.CREATED);
    }

    @PutMapping("/account/{accountId}")
    public ResponseEntity<Account> updateComment(@PathVariable("accountId") long accountId,
                                                 @RequestBody Account accountRequest) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Account with id = " + accountId + "Not Found"));
        account.setValue(accountRequest.getValue());
        return new ResponseEntity<>(accountRepository.save(account), HttpStatus.OK);
    }

    @DeleteMapping("/user/{userId}/account/{accountId}")
    public ResponseEntity<List<Account>> deleteAccountOfUser(@PathVariable(value = "userId") long userId,
                                                             @PathVariable(value = "accountId") long accountId) {
        if (!userRepository.existsById(userId)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        accountRepository.deleteById(accountId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}


