package com.hubertart.crud;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface UsersRepository extends CrudRepository<Users, Long> {
    @Query("SELECT id, password FROM Users WHERE email = ?1")
    String findByEmail(String email);
}
