package sk.mtaa.budgetProgram.Api;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import sk.mtaa.budgetProgram.Constants.Constants;
import sk.mtaa.budgetProgram.Models.User;
import sk.mtaa.budgetProgram.Repository.UserRepository;

import java.io.IOException;
import java.sql.Blob;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:12345")
@RestController
@RequestMapping("/")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> findByEmailAndPassword(@RequestBody User userRequest) {
        Optional<User> userData = userRepository.findByEmailAndPassword(userRequest.getEmail(), userRequest.getPassword());
        return userData.map(user -> new ResponseEntity<>(generateJWTToken(user), HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(null, HttpStatus.NOT_FOUND));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<User> getUser(@PathVariable("userId") Long userId) {
        Optional<User> userData = userRepository.findById(userId);
        return userData.map(user -> new ResponseEntity<>(user, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }


    @PostMapping("/register")
    public ResponseEntity<User> createUser(@RequestBody User userRequest) {
        try {
            User user = userRepository
                    .save(new User(userRequest.getEmail(), userRequest.getPassword(), LocalDateTime.now(), userRequest.getRole()));
            return new ResponseEntity<>(null, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/uploadPhoto/{userId}")
    public ResponseEntity<User> uploadPhoto(@RequestParam("imageFile") MultipartFile imageFile, @PathVariable("userId") Long userId) throws IOException {
        User userData = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "User with id = " + userId + "Not Found"));

        userData.setPhoto(imageFile.getBytes());

        return new ResponseEntity<>(userRepository.save(userData), HttpStatus.OK);
    }

    @GetMapping("/userPhoto/{userId}")
    public ResponseEntity<byte[]> getFile(@PathVariable("userId") Long userId) {
        User userData = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "User with id = " + userId + "Not Found"));
        byte[] image = userData.getPhoto();

        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(image);
    }

    private Map<String, String> generateJWTToken(User user){
        long timestamp = System.currentTimeMillis();
        String token = Jwts.builder().signWith(SignatureAlgorithm.HS256, Constants.API_SECRET_KEY)
                .setIssuedAt(new Date(timestamp))
                .setExpiration(new Date(timestamp + Constants.TOKEN_VALIDITY))
                .claim("id", user.getId())
                .claim("email", user.getEmail())
                .claim("role", user.getRole())
                .compact();
        Map<String, String> map = new HashMap<>();
        map.put("token", token);
        map.put("id", ((Long) user.getId()).toString());
        map.put("email", user.getEmail());
        map.put("role", ((Integer) user.getRole()).toString());
        return map;
    }
}
