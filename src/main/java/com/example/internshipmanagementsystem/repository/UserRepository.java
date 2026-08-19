package com.example.internshipmanagementsystem.repository;

import com.example.internshipmanagementsystem.entity.Role;
import com.example.internshipmanagementsystem.entity.User;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {

  Optional<User> findByUsername(String username);

  Page<User> findByRole(Role role, Pageable pageable);

  boolean existsByUsername(String username);

  boolean existsByEmail(String email);

  boolean existsByUsernameAndUserIdNot(String username, Integer userId);

  boolean existsByEmailAndUserIdNot(String email, Integer userId);
}
