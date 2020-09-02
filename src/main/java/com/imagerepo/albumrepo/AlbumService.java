package com.imagerepo.albumrepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;

@Service
public class AlbumService {

    private final AlbumRepo albumRepo;

    @Autowired
    public AlbumService(AlbumRepo albumRepo) {
        this.albumRepo = albumRepo;
    }

    public void uploadAlbum(MultipartFile imgfile, String title, String description,
                            Genre[] genres, String artist) throws IOException {
        AlbumModel albumModel = new AlbumModel(imgfile.getOriginalFilename(), title, description, imgfile.getBytes(),
                Arrays.asList(genres), imgfile.getContentType(),artist);
        albumRepo.save(albumModel);
    }
//    public ArrayList<AlbumModel> searchByKeyword(String input) {
//
//    }

    public static byte[] decompress(byte[] data) {
        Inflater inflater = new Inflater();
        inflater.setInput(data);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] buffer = new byte[1024];
        try {
            while (!inflater.finished()) {
                int cnt = inflater.inflate(buffer);
                outputStream.write(buffer, 0 , cnt);
            }
            outputStream.close();
        }
        catch (DataFormatException | IOException e) {
            //todo handle
        }
        return outputStream.toByteArray();
    }
}
