package com.imagerepo.albumrepo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

@Service
public class AlbumService {

    private final AlbumRepo albumRepo;
    private static final Logger LOG = LoggerFactory.getLogger(AlbumService.class);

    @Autowired
    public AlbumService(AlbumRepo albumRepo) {
        this.albumRepo = albumRepo;
    }

    public void uploadAlbum(MultipartFile imgfile, String title, String description,
                            Genre[] genres, String artist) throws IOException {
        AlbumModel albumModel = new AlbumModel(imgfile.getOriginalFilename(), title, description,
                imgfile.getBytes(), Arrays.asList(genres), imgfile.getContentType(),artist);
        albumRepo.save(albumModel);
    }

    public HashSet<AlbumModel> searchByKeyword(String input) {
        return albumRepo.findByMatchingString(input);
    }

    public HashSet<AlbumModel> searchByGenre(Genre[] genres) {
        HashSet<AlbumModel> albums = new HashSet<>(Collections.emptySet());
        for (Genre genre : genres) {
            albums.addAll(albumRepo.findByMatchingAnyGenre(genre));
        }
        return albums;
    }

    public HashSet<AlbumModel> searchByGenreMatchAll(Genre[] genres) {
        HashSet<AlbumModel> albums = new HashSet<>(Collections.emptySet());
        for (Genre genre : genres) {
            albums.addAll(albumRepo.findByMatchingAnyGenre(genre));
        }
        albums.removeIf(a -> !(Arrays.equals(a.getGenres().toArray(), genres)));
        return albums;
    }

    private byte[] compressBytes(byte[] data) {
        Deflater deflater = new Deflater();
        deflater.setInput(data);
        deflater.finish();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] buffer = new byte[1024];
        while (!deflater.finished()) {
            int count = deflater.deflate(buffer);
            outputStream.write(buffer, 0, count);
        }
        try {
            outputStream.close();
        } catch (IOException e) {
        }
        System.out.println("Compressed Image Byte Size - " + outputStream.toByteArray().length);
        return outputStream.toByteArray();
    }

    private byte[] decompress(byte[] data) {
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

    private HashSet<AlbumModel> convertToImages(HashSet<AlbumModel> encryptedAlbums) {
        HashSet<AlbumModel> decryptedAlbums = new HashSet<>(Collections.emptySet());
        for (AlbumModel album : encryptedAlbums) {
            decryptedAlbums.add(
                    new AlbumModel(album.getFilename(),album.getTitle(),
                            album.getDescription(), decompress(album.getPicture()), album.getGenres(),
                            album.getType(), album.getArtist())
            );
        }
        return decryptedAlbums;
    }
}
