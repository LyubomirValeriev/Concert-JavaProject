package com.concert.concertApp.repositories;

import com.concert.concertApp.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findUserByEmail(String email);

    @Query("SELECT u FROM User u WHERE u.Id = :id" )
    Optional<User> findUserById(Long id);

    @Query("SELECT u " +
            "FROM User u " +
            "WHERE " +
            "lower(u.firstName) " +
            "LIKE :#{#firstName == null || #firstName.isEmpty()? '%' : #firstName+'%'} " +
            "AND lower(u.lastName) " +
            "LIKE :#{#lastName == null || #lastName.isEmpty()? '%' : #lastName+'%'}")
    Page<User> filterUsersByName(Pageable pageable, String firstName, String lastName);

    @Query("SELECT usr FROM User usr WHERE usr.username = :usrname" )
    User checkUniqueUsrname(String usrname);

    @Query("SELECT user FROM User user WHERE user.email = :email" )
    User checkUniqueEmail(String email);
}
