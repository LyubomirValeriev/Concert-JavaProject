package com.concert.concertApp.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.engine.query.ParameterRecognitionException;
import javax.persistence.*;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Set;
import java.util.regex.Pattern;


@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private  Long Id;

    @Column(nullable = false, length = 30)
    private String firstName;

    @Column(nullable = false, length = 30)
    private String lastName;

    @Column(nullable = false, length = 3)
    private String age;

    @Column(nullable = false)
    private String email;

<<<<<<< HEAD
    @Column(nullable = false)
    private String username;
=======
    @JsonIgnore
    private String username;
    @JsonIgnore
    private String password;
>>>>>>> Luybo-Concert-to-Hall

    @Column(nullable = false)
    private String password;

<<<<<<< HEAD
    public User(String firstName, String lastName, String age, String email, String username, String password)
=======
@OneToMany(mappedBy = "user")
private Set<Reservation> reservations;




    public User(String firstName,
                String lastName, Integer age,
                String email, String username,
                String password)
>>>>>>> Luybo-Concert-to-Hall
            throws NoSuchAlgorithmException {
        this.firstName = firstName;
        this.lastName = lastName;
        this.setAge(age);
        this.setEmail(email);
        this.username = username;
        this.setPassword(password);
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
        if (firstName == null || firstName.isEmpty())
            throw new NullPointerException("Please enter your first name");
        this.firstName = firstName.trim();
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        if (lastName == null || lastName.isEmpty())
            throw new NullPointerException("Please enter your last name");
        this.lastName = lastName.trim();
    }

    public String getAge() { return age; }

    public void setAge(String age) {
        if (age == null || age.isEmpty())
         throw new NullPointerException("Please enter your age");

        if(!isNumeric(age) || Integer.parseInt(age) > 130) {
         throw new ParameterRecognitionException("Please check your age input");
        }
            this.age = age;
    }

    public String getEmail(String email) {
        return email;
    }

    public void setEmail(String email) {
        if (email == null || email.isEmpty())
            throw new NullPointerException("Please enter your email");

        if (!isValid(email)){
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
        if (password == null || password.isEmpty())
            throw new NullPointerException("Please enter your password");

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

    public static String encrypt(String pass) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");

        byte[] messageDigest = md.digest(pass.getBytes());

        BigInteger bigInt = new BigInteger(1, messageDigest);

        return bigInt.toString(16);
    }

<<<<<<< HEAD
    public static boolean isNumeric(String age) {
        try {
            Integer.parseInt(age);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
=======


    public Set<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(Set<Reservation> reservations) {
        this.reservations = reservations;
>>>>>>> Luybo-Concert-to-Hall
    }
}
