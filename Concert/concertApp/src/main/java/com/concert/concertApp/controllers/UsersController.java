package com.concert.concertApp.controllers;

import com.concert.concertApp.entities.User;
import com.concert.concertApp.repositories.UserRepository;
import org.hibernate.engine.query.ParameterRecognitionException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/user")
@RestController
public class UsersController {

    private final UserRepository userRepo;

    UsersController(UserRepository userRepository){
        userRepo = userRepository;
    }

    @GetMapping("/fetch")
    public List<User> getAllUsers() {
        return userRepo.findAll();
    }

    @GetMapping("/find/email")
    public ResponseEntity<?> findUserByEmail(String email){
        Optional<User> result = userRepo.findUserByEmail(email);

        return result.isPresent()?ResponseEntity.ok(result.get()) : ResponseEntity.ok("Not found user with email: " + email);
    }

    @GetMapping("/find/id")
    public ResponseEntity<?> findUserById(Long id) {
        User user = null;
        try {
            user = userRepo.findUserById(id).get();
        }catch (Exception i) {
            return new ResponseEntity<>(i.getClass().getName(), HttpStatus.OK);
        }
        return new ResponseEntity<>(user, HttpStatus.OK);

        //Optional<User> result = userRepo.findUserById(id);
        // return  result.isPresent()?ResponseEntity.ok(result.get()) : ResponseEntity.ok("Not found user with id: " + id + "!");
    }

    @PostMapping("/save")
    public ResponseEntity<?> persistUser(String fname, String lname, Integer age, String email, String usrname, String pass, @RequestParam(required = false) Long id) {
       User user = null;

        try {
          user  = userRepo.findUserById(id)
                    .orElse(new User(fname, lname, age, email, usrname, pass));

            if (user.getId() != null) {
                user.setFirstName(fname);
                user.setLastName(lname);
                user.setAge(age);
                user.setEmail(email);
                user.setUsername(usrname);
                user.setPassword(pass);
            }

        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.OK);
        }

        return new ResponseEntity<User>(userRepo.save(user), HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteUser(Long id){
        Optional<User> user = userRepo.findUserById(id);
        if (user.isEmpty()){
            return ResponseEntity.ok("User not found!");
        }
        userRepo.delete(user.get());
        return  ResponseEntity.ok("User with id " + id + " was successfully deleted!");
    }

    @GetMapping("/filter")
    public ResponseEntity<?> filterUsers(String fname, String lname, int currentPage, int perPage){
        Pageable pageable = PageRequest.of(currentPage - 1, perPage);
        Page<User> users = userRepo.filterUsersByName(pageable, fname.toLowerCase(), lname.toLowerCase());
        Map<String, Object> response = new HashMap();
        response.put("totalElements", users.getTotalElements());
        response.put("totalPages", users.getTotalPages());
        response.put("users", users.getContent());

        return ResponseEntity.ok(response);
    }


}
