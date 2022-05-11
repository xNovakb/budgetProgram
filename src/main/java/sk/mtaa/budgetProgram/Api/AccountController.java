package sk.mtaa.budgetProgram.Api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
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
    @SendTo("/topic/account")
    public ResponseEntity<List<Account>> getAllAccountsByUserId(@PathVariable("userId") Long userId) {
        if (!userRepository.existsById(userId)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        List<Account> accounts = accountRepository.findByUserId(userId);
        return new ResponseEntity<>(accounts, HttpStatus.OK);
    }

    @MessageMapping("/accounts/{userId}/{accountId}")
    @SendTo("/topic/account")
    public ResponseEntity<List<Account>> getAllAccountsByUserIdAndAccount(@PathVariable(value = "userId") Long userId,
                                                                          @PathVariable(value = "accountId") Long accountId) {
        if (!userRepository.existsById(userId)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        List<Account> accounts = accountRepository.findByUserIdAndId(userId, accountId);
        return new ResponseEntity<>(accounts, HttpStatus.OK);
    }

    @MessageMapping("/postAccount")
    @SendTo("/topic/account")
    public ResponseEntity<Account> createAccount(@RequestBody Account accountRequest) {
        Account account = userRepository.findById(accountRequest.getId()).map(user -> {
            accountRequest.setUser(user);
            accountRequest.setAdded_at(LocalDateTime.now());
            return accountRepository.save(accountRequest);
        }).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "User with id = " + accountRequest.getId() + "Not Found"));
        return new ResponseEntity<>(account, HttpStatus.CREATED);
    }

    @MessageMapping("/account")
    @SendTo("/topic/account")
    public ResponseEntity<Account> updateComment( @RequestBody Account accountRequest) {
        Account account = accountRepository.findById(accountRequest.getId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Account with id = " + accountRequest.getId() + "Not Found"));
        account.setValue(accountRequest.getValue());
        return new ResponseEntity<>(accountRepository.save(account), HttpStatus.OK);
    }

    @MessageMapping("/user/{userId}/account/{accountId}")
    @SendTo("/topic/account")
    public ResponseEntity<List<Account>> deleteAccountOfUser(@RequestParam("id") Long accountId) {
        if (!accountRepository.existsById(accountId)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        accountRepository.deleteById(accountId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}


