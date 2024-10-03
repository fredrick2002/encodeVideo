package com.example.encodeVideo.service;

import com.example.encodeVideo.model.UserVideo;
import com.example.encodeVideo.repository.VideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class VideoDBService {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private VideoRepository videoRepository;

    // Method to save a user or update their video array in MongoDB
    public void saveUser(String username, String videoName) {
        Query query = new Query(Criteria.where("username").is(username));
        UserVideo user = mongoTemplate.findOne(query, UserVideo.class);

        if (user == null) {
            // User does not exist, create a new user
            user = new UserVideo();
            user.setUserName(username);
            user.setVideos(new ArrayList<>()); // Initialize the videos list
        }

        // Add the video name to the user's videos array
        user.getVideos().add(videoName);
        mongoTemplate.save(user); // Save the user (insert or update)
    }
    public UserVideo getVideo(String username){
        return videoRepository.findByusername(username);
    }
}
