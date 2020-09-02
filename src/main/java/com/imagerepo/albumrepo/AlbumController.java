package com.imagerepo.albumrepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;

@RestController
@CrossOrigin(origins = "http://localhost:1234")
@RequestMapping(path = "api/v1")
public class AlbumController {

    private final AlbumService albumService;

    @Autowired
    public AlbumController(AlbumService albumRepo) {
        this.albumService = albumRepo;
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadImage(@RequestBody MultipartFile imgfile,
                                         @RequestBody String title,
                                         @RequestBody String description,
                                         @RequestBody Genre[] genres,
                                         @RequestBody String artist) {

        if (StringUtils.isEmpty(title) || StringUtils.isEmpty(artist) || StringUtils.isEmpty(artist) || imgfile == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        try {
            this.albumService.uploadAlbum(imgfile, title, description, genres, artist);
        }
        catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchForAlbums(@RequestParam String text, @RequestParam Genre[] genres, @RequestParam boolean exactMatch) {
        HashSet<AlbumModel> textMatchedAlbums = new HashSet<>(Collections.emptySet());
        HashSet<AlbumModel> genreMatchedAlbums = new HashSet<>(Collections.emptySet());
        if (StringUtils.isEmpty(text) && genres.length == 0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (!StringUtils.isEmpty(text)) {
            textMatchedAlbums = albumService.searchByKeyword(text);
        }
        if (genres.length > 0) {
            if (exactMatch) {
                //search for albums that have EVERY one of the genre tags provided
                genreMatchedAlbums = albumService.searchByGenreMatchAll(genres);
            }
            else {
                //search for albums that have ANY OF the genre tags provided
                genreMatchedAlbums = albumService.searchByGenre(genres);
            }
        }
        if (textMatchedAlbums.isEmpty()) {
            return new ResponseEntity<>(genreMatchedAlbums, HttpStatus.CREATED);
        }
        else if (genreMatchedAlbums.isEmpty()) {
            return new ResponseEntity<>(textMatchedAlbums, HttpStatus.CREATED);
        }
        else {
            //return intersection of the 2 search results
            return new ResponseEntity<>(genreMatchedAlbums.retainAll(textMatchedAlbums), HttpStatus.CREATED);
        }
    }

//    @GetMapping("/get/{albumTitle}")
//    public AlbumModel getAlbum(@PathVariable("title") String title) throws IOException{
//        Optional<AlbumModel> album = albumRepo.findByTitle(title);
//        AlbumModel albumModel = new AlbumModel(album.get().getFilename(),album.get().getTitle(),
//                album.get().getDescription(), decompress(album.get().getPicture()), album.get().getGenres(),
//                album.get().getType(), album.get().getArtist());
//        return albumModel;
//    }


}
