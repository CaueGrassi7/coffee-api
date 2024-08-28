package com.example.products_api.repositories;

import com.example.products_api.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserRepository extends JpaRepository<User, String> {

     UserDetails findByLogin(String login);
}
