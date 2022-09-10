package com.smart.Repository;

import com.smart.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Integer> {

    @Query("Select u from User u where u.email = :email")
    public User getUserByUserName(@Param("email") String email);
}
