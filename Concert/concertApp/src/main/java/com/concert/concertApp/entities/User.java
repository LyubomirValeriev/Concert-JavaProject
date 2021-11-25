package com.concert.concertApp.entities;

import org.hibernate.engine.query.ParameterRecognitionException;
import org.springframework.http.ResponseEntity;

import javax.persistence.*;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Pattern;


@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long Id;

    private String firstName;
    private String lastName;
    private Integer age;
    private String email;
    private String username;
    private String password;


    public User(String firstName, String lastName, Integer age, String email, String username, String password)
            throws NoSuchAlgorithmException {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;

        if(isValid(email))
            this.email = email;
        else
            throw new ParameterRecognitionException("Invalid email format!");

        this.username = username;
        this.password = encrypt(password);
    }

    public User() {
    }

    public Long getId() {
        return Id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getEmail(String email) {return email;}

    public void setEmail(String email) {
        if (isValid(email) == false){
            throw new ParameterRecognitionException("Invalid email format!");
        }

        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password)
            throws NoSuchAlgorithmException {
        this.password = encrypt(password);
    }

    public static boolean isValid(String checkEmail)
    {
        String emailRegex = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
                + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";

        Pattern pat = Pattern.compile(emailRegex);
        if (checkEmail == null)
            return false;
        return pat.matcher(checkEmail).matches();
    }

    public String encrypt(String pass) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");

        byte[] messageDigest = md.digest(pass.getBytes());

        BigInteger bigInt = new BigInteger(1, messageDigest);

        return bigInt.toString(16);
    }

}
