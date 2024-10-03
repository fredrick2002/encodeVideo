package com.example.encodeVideo.repository;

import com.example.encodeVideo.model.UserVideo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VideoRepository extends MongoRepository<UserVideo, String> {
    UserVideo findByusername(String username);
}
