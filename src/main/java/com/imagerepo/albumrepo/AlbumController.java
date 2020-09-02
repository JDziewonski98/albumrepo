package com.imagerepo.albumrepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Optional;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;

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
    public ResponseEntity<?> uploadImage(@RequestParam("imgFile") MultipartFile imgfile,
                                         @RequestParam("title") String title,
                                         @RequestParam("description") String description,
                                         @RequestParam("genres") Genre[] genres,
                                         @RequestParam("artist") String artist) {

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

//    @GetMapping("/get/{albumTitle}")
//    public AlbumModel getAlbum(@PathVariable("title") String title) throws IOException{
//        Optional<AlbumModel> album = albumRepo.findByTitle(title);
//        AlbumModel albumModel = new AlbumModel(album.get().getFilename(),album.get().getTitle(),
//                album.get().getDescription(), decompress(album.get().getPicture()), album.get().getGenres(),
//                album.get().getType(), album.get().getArtist());
//        return albumModel;
//    }


}
