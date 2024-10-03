package com.example.encodeVideo.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;

@Document(collection = "user_video")
public class UserVideo {

    @Id
    private String id;
    private String username;
    private List<String> videos;

    // Constructors, getters, and setters
    public UserVideo() {}

    public UserVideo(String email, List<String> videos) {
        this.username = username;
        this.videos = videos;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUserName(String username) {
        this.username = username;
    }

    public List<String> getVideos() {
        return videos;
    }

    public void setVideos(List<String> videos) {
        this.videos = videos;
    }
}
