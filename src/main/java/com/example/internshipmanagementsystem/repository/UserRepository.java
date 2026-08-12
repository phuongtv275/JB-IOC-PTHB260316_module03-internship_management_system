package com.example.internshipmanagementsystem.repository;

import com.example.internshipmanagementsystem.entity.Role;
import com.example.internshipmanagementsystem.entity.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

  Optional<User> findByUsername(String username);

  List<User> findByRole(Role role);

  boolean existsByUsername(String username);

  boolean existsByEmail(String email);

  boolean existsByUsernameAndUserIdNot(String username, Long userId);

  boolean existsByEmailAndUserIdNot(String email, Long userId);
}
