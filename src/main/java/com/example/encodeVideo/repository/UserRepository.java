package com.example.encodeVideo.repository;

import com.example.encodeVideo.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<Users,String> {
    Users findByUsername(String email);
}
