package sk.mtaa.budgetProgram.Api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import sk.mtaa.budgetProgram.Models.Transaction;
import sk.mtaa.budgetProgram.Repository.AccountRepository;
import sk.mtaa.budgetProgram.Repository.CategoryRepository;
import sk.mtaa.budgetProgram.Repository.TransactionRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:12345")
@RestController
@RequestMapping("/api")
public class TransactionController {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @MessageMapping("/getTransaction")
    @SendTo("/topic/transaction")
    public ResponseEntity<Transaction> getTransaction(@RequestParam("transactionId") Long transactionId){

        Optional<Transaction> transaction = transactionRepository.findById(transactionId);
        if (transaction.isPresent()) {
            return new ResponseEntity<>(transaction.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @MessageMapping("/getTransactions")
    @SendTo("/topic/transaction")
    public ResponseEntity<List<Transaction>> getTransactionsById(@RequestParam("id") Long userId){

        List<Transaction> transactions = transactionRepository.findByAccountUserId(userId);
        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }

    @MessageMapping("/transaction/account")
    @SendTo("/topic/transaction")
    public ResponseEntity<List<Transaction>> getTransactionsByAccount(@RequestParam("id") Long accountId){
        if (!accountRepository.existsById(accountId)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        List<Transaction> transactions = transactionRepository.findByAccountId(accountId);
        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }

    @MessageMapping("/transaction/category")
    @SendTo("/topic/transaction")
    public ResponseEntity<List<Transaction>> getTransactionsByCategory(@RequestParam("id") Long categoryId){
        if (!categoryRepository.existsById(categoryId)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        List<Transaction> transactions = transactionRepository.findByCategoryId(categoryId);
        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }

    @MessageMapping("/transaction/addedAt")
    @SendTo("/topic/transaction")
    public ResponseEntity<List<Transaction>> getTransactionsByMonth(@RequestParam("monthStart") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime monthStart,
                                                                    @RequestParam("monthEnd") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime monthEnd){

        return new ResponseEntity<>(transactionRepository.findByAddedAtBetween(monthStart, monthEnd), HttpStatus.OK);
    }

    @MessageMapping("/putTransaction")
    @SendTo("/topic/transaction")
    public ResponseEntity<Transaction> updateComment(@RequestParam("id") long transactionId,
                                                  @RequestBody Transaction transactionRequest) {

        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Transaction with id = " + transactionId + "Not Found"));

        transaction.getAccount().setValue(transaction.getAccount().getValue() - transaction.getAmount() + transactionRequest.getAmount());
        transaction.setAmount(transactionRequest.getAmount());
        transaction.setDescription(transactionRequest.getDescription());
        transaction.setRecurring(transactionRequest.isRecurring());
        transaction.setRecurringDays(transactionRequest.getRecurringDays());

        return new ResponseEntity<>(transactionRepository.save(transaction), HttpStatus.OK);
    }

    @MessageMapping("/transaction/{accountId}/{categoryId}")
    @SendTo("/topic/transaction")
    public ResponseEntity<Transaction> createTransaction(@PathVariable("accountId") Long accountId,
                                                         @PathVariable("categoryId") Long categoryId,
                                                         @RequestBody Transaction transactionRequest){

        accountRepository.findById(accountId).map(account -> {
            account.setValue(account.getValue() + transactionRequest.getAmount());
            transactionRequest.setAccount(account);
            transactionRequest.setAddedAt(LocalDateTime.now());

            categoryRepository.findById(categoryId).map(category -> {
                transactionRequest.setCategory(category);
                return transactionRepository.save(transactionRequest);
            }).orElseThrow(() -> new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Category with id = " + categoryId + "Not Found"));

            return transactionRepository.save(transactionRequest);
        }).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Account with id = " + accountId + "Not Found"));

        return new ResponseEntity<>(transactionRequest, HttpStatus.CREATED);
    }

    @MessageMapping("/deleteTransaction")
    @SendTo("/topic/transaction")
    public ResponseEntity<Transaction> deleteTransaction(@RequestParam("id") Long transactionId) {

        if (!transactionRepository.existsById(transactionId)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new RuntimeException("TransactionId " + transactionId + "not found"));

        transaction.getAccount().setValue(transaction.getAccount().getValue() + transaction.getAmount());

        transactionRepository.save(transaction);
        transactionRepository.deleteById(transactionId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
