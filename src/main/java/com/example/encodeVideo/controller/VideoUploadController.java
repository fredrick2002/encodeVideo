package com.example.encodeVideo.controller;

import com.example.encodeVideo.service.JWTService;
import com.example.encodeVideo.service.ConvertToHlsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Paths;
import java.util.UUID;

@RestController
@RequestMapping("/api/videos")
public class VideoUploadController {

    private static final String VIDEO_DIR = "videos/";

    @Autowired
    JWTService jwtService;

    @Autowired
    ConvertToHlsService convertToHLS;

    @PostMapping("/upload")
    public String uploadVideo(@RequestParam("file") MultipartFile file) throws IOException, InterruptedException {
        // Generate a unique file name
        String originalFileName = file.getOriginalFilename();
        String videoFileName = UUID.randomUUID().toString() + "_" + originalFileName;
        String videoFilePath = Paths.get(VIDEO_DIR, videoFileName).toAbsolutePath().toString();


        // Ensure the directory exists
        File directory = new File(VIDEO_DIR);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        // Save the file to the specified directory
        File videoFile = new File(videoFilePath);
        file.transferTo(videoFile);
        String orgFileName = videoFileName.substring(0,videoFileName.length()-4);


        convertToHLS.toHLS(videoFilePath,orgFileName,VIDEO_DIR);

        return "Video uploaded and converted to HLS successfully!";
    }
}
