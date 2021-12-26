package com.concert.concertApp.controllers;

import com.concert.concertApp.entities.User;
import com.concert.concertApp.repositories.UserRepository;
import org.hibernate.PropertyValueException;
import org.hibernate.engine.query.ParameterRecognitionException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
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

    @GetMapping("/find/email")
    public ResponseEntity<?> findUserByEmail(String email){
        Optional<User> result = userRepo.findUserByEmail(email.trim());

        return result.isPresent()?ResponseEntity.ok(result.get()) : ResponseEntity.ok("Not found user with email: " + email);
    }

    @GetMapping("/find/id")
    public ResponseEntity<?> findUserById(Long id) { //проверката за ид остава част от фронтенда
        try {
           User user = userRepo.findUserById(id).get();
            return new ResponseEntity<>(user, HttpStatus.OK);
        }catch (Exception i) {
            return new ResponseEntity<>(i.getClass().getName(), HttpStatus.OK);
        }
    }

    @PostMapping("/save")
    public ResponseEntity<?> persistUser(String fname, String lname, String age, String email, String usrname, String pass, @RequestParam(required = false) Long id)
            throws PropertyValueException {
        User resultUsrname = userRepo.checkUniqueUsrname(usrname.trim());
        if (resultUsrname != null)
            return ResponseEntity.ok("Username " + usrname + " is already taken.");

        User resultEmail = userRepo.checkUniqueEmail(email.trim());
        if (resultEmail != null)
            return ResponseEntity.ok("Email " + email + " is already exist.");

        try {
            User user = userRepo.findUserById(id)
                    .orElse(new User(fname, lname, age, email, usrname, pass));

            if (user.getId() != null) {
                user.setFirstName(fname);
                user.setLastName(lname);
                user.setAge(age);
                user.setEmail(email);
                user.setUsername(usrname);
                user.setPassword(pass);
            }
            userRepo.save(user);
            return ResponseEntity.ok("User " + user.getFirstName() + " saved successfully");

        }catch (DataIntegrityViolationException dive){
            return ResponseEntity.ok("It is mandatory to fill all user fields correctly. Check your personal data.");
        } catch (ParameterRecognitionException e) {
            return ResponseEntity.ok(e.getMessage());  // проверка за невалидни данни - еmail, age
        }catch (NoSuchAlgorithmException nsae) {
            return ResponseEntity.ok(nsae.getMessage());  // за хеширането
        }catch (NullPointerException npe){
            return  ResponseEntity.ok(npe.getMessage());  // prowerka za towa dali e `eknato i dali e prazen string
        }

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
    public ResponseEntity<?> filterUsers(@RequestParam(defaultValue = "") String fname,
                                         @RequestParam(defaultValue = "") String lname,
                                         @RequestParam(defaultValue = "1") int currentPage,
                                         @RequestParam(defaultValue = "3") int perPage){
        Pageable pageable = PageRequest.of(currentPage - 1, perPage);
        Page<User> userPages = userRepo.filterUsersByName(pageable, (fname.toLowerCase()).trim(), (lname.toLowerCase()).trim());
        Map<String, Object> response = new HashMap();
        response.put("totalElements", userPages.getTotalElements());
        response.put("totalPages", userPages.getTotalPages());
        response.put("users", userPages.getContent());

        return ResponseEntity.ok(response);
    }


}
