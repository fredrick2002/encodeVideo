package com.example.encodeVideo.service;

import com.example.encodeVideo.utils.AuthenticationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;

@Service
public class MasterPlaylistService {

    @Autowired
    VideoDBService videoDB;

    public void createMasterPlaylist(String outputDirPath, String outputMaster, String orgFileName) throws IOException {
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

        videoDB.saveUser(AuthenticationUtil.getCurrentUsername(), masterPlaylist.getAbsolutePath());
        System.out.println("Master playlist created at: " + masterPlaylist.getAbsolutePath());
    }
}
