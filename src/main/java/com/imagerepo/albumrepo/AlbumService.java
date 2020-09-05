package com.imagerepo.albumrepo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.*;

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

    public HashSet<AlbumModel> getAll() {
        HashSet<AlbumModel> allAlbums = new HashSet<>();
        Iterable<AlbumModel> albums = albumRepo.findAll();
        for (AlbumModel album: albums) {
            allAlbums.add(album);
        }
        return allAlbums;
    }
}
