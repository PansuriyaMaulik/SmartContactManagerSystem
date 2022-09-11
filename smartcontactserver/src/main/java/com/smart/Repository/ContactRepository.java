package com.smart.Repository;

import com.smart.model.Contact;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.List;

public interface ContactRepository extends JpaRepository<Contact, Integer> {

    //Custom method to get logging user contact list
    @Query("from Contact as c where c.user.id =:userId")
    // Pageable have this info. 1. current page - page 2. contact per page
    public Page<Contact> findContactByUser(@Param("userId") int userId, Pageable pageable);

    //Pagination...
}
