package com.govtech.assignment.repositories;

import com.govtech.assignment.entities.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Integer>, CustomUserRepository { }


