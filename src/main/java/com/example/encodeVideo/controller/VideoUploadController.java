package com.example.encodeVideo.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Paths;
import java.util.UUID;

@RestController
@RequestMapping("/api/videos")
public class VideoUploadController {

    private static final String VIDEO_DIR = "videos/";
//    private static final String HLS_DIR = "hls/";

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

//        // Convert video to HLS
//        String hlsOutputPath = Paths.get(HLS_DIR, videoFileName).toAbsolutePath().toString();
//        Files.createDirectories(Paths.get(hlsOutputPath));1   1
        convertToHLS(videoFilePath,originalFileName);

        return "Video uploaded and converted to HLS successfully!";
    }

    private void convertToHLS(String inputFilePath, String orgFileName) throws IOException, InterruptedException {
        String output360p = VIDEO_DIR + orgFileName + "/" + "360p" +"/"+ orgFileName + "_360p.m3u8";
        String output720p = VIDEO_DIR + orgFileName + "/" + "720p" +"/"+ orgFileName + "_720p.m3u8";
        String output1080p = VIDEO_DIR + orgFileName + "/" + "1080p" +"/"+ orgFileName + "_1080p.m3u8";
        String outputMaster = "master.m3u8";

        File directory = new File(VIDEO_DIR + orgFileName);
        File _1080p = new File(VIDEO_DIR + orgFileName +"/"+ "1080p");
        File _720p = new File(VIDEO_DIR + orgFileName +"/"+ "720p");
        File _360p = new File(VIDEO_DIR + orgFileName +"/"+ "360p");
        if (!directory.exists()) {
            directory.mkdirs();
        }
        if (!_1080p.exists()) {
            _1080p.mkdirs();
        }
        if (!_720p.exists()) {
            _720p.mkdirs();
        }
        if (!_360p.exists()) {
            _360p.mkdirs();
        }


        // FFmpeg command to convert video to HLS
        ProcessBuilder encode = new ProcessBuilder(
                "ffmpeg","-i", inputFilePath,
                "-vf", "scale=w=640:h=360:force_original_aspect_ratio=decrease", "-c:a", "aac", "-ar", "48000", "-c:v", "h264_nvenc", "-profile:v", "main", "-crf", "20", "-sc_threshold", "0", "-g", "48", "-keyint_min", "48", "-hls_time", "4", "-hls_playlist_type", "vod", "-b:v", "800k", "-maxrate", "1000k", "-bufsize", "1200k", "-b:a", "128k", "-hls_segment_filename", VIDEO_DIR +orgFileName+ "/" +"360p"+"/"+ orgFileName + "_360p_%03d.ts", output360p,
                "-vf", "scale=w=1280:h=720:force_original_aspect_ratio=decrease", "-c:a", "aac", "-ar", "48000", "-c:v", "h264_nvenc", "-profile:v", "main", "-crf", "20", "-sc_threshold", "0", "-g", "48", "-keyint_min", "48", "-hls_time", "4", "-hls_playlist_type", "vod", "-b:v", "7500k", "-maxrate", "8000k", "-bufsize", "4200k", "-b:a", "256k", "-hls_segment_filename",VIDEO_DIR +orgFileName+ "/" +"720p"+ "/" + orgFileName + "_720p_%03d.ts", output720p,
                "-vf", "scale=w=1920:h=1080:force_original_aspect_ratio=decrease", "-c:a", "aac", "-ar", "48000", "-c:v", "h264_nvenc", "-profile:v", "main", "-crf", "20", "-sc_threshold", "0", "-g", "48", "-keyint_min", "48", "-hls_time", "4", "-hls_playlist_type", "vod", "-b:v", "10000k", "-maxrate", "12000k", "-bufsize", "7500k", "-b:a", "320k", "-hls_segment_filename", VIDEO_DIR +orgFileName+ "/" +"1080p"+"/"+ orgFileName + "_1080p_%03d.ts", output1080p
//                "-c:v", "copy", "-c:a", "copy", "-hls_time", "10", "-hls_list_size", "0", "-f", "hls", outputFilePath
        );


        encode.redirectErrorStream(true);  // Combine standard output and error output
        Process process = encode.start();

        // Read and print the process output in real-time
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);  // Print FFmpeg output to the console
            }
        }

        int exitCode = process.waitFor();
        if (exitCode != 0) {
            throw new RuntimeException("FFmpeg process failed with exit code " + exitCode);
        }
        String outputDirPath = VIDEO_DIR + "/" + orgFileName + "/";
        // Create the master playlist after HLS streams are generated
        createMasterPlaylist(outputDirPath, outputMaster, orgFileName);
    }

    private void createMasterPlaylist(String outputDirPath, String outputMaster, String orgFileName) throws IOException {
        File masterPlaylist = new File(Paths.get(outputDirPath, outputMaster).toString());
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(masterPlaylist))) {
            writer.write("#EXTM3U\n");

            writer.write("#EXT-X-STREAM-INF:BANDWIDTH=800000,RESOLUTION=640x360\n");
            writer.write("360p/" +orgFileName+"_360p.m3u8"  + "\n");

            writer.write("#EXT-X-STREAM-INF:BANDWIDTH=2800000,RESOLUTION=1280x720\n");
            writer.write("720p/" +orgFileName+"_720p.m3u8"  + "\n");

            writer.write("#EXT-X-STREAM-INF:BANDWIDTH=5000000,RESOLUTION=1920x1080\n");
            writer.write("1080p/" +orgFileName+"_1080p.m3u8"   + "\n");
        }

        System.out.println("Master playlist created at: " + masterPlaylist.getAbsolutePath());
    }
}
