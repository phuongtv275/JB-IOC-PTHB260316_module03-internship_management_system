package com.example.internshipmanagementsystem.repository;

import com.example.internshipmanagementsystem.entity.Mentor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MentorRepository extends JpaRepository<Mentor, Integer> {

  @Override
  @EntityGraph(attributePaths = "user")
  Page<Mentor> findAll(Pageable pageable);
}
