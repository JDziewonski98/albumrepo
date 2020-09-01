package com.imagerepo.albumrepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Optional;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping(path = "api/v1")
public class AlbumController {

    private final AlbumRepo albumRepo;

    @Autowired
    public AlbumController(AlbumRepo albumRepo) {
        this.albumRepo = albumRepo;
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadImage(@RequestParam("imgFile") MultipartFile imgfile,
                                         @RequestParam("title") String title,
                                         @RequestParam("description") String description,
                                         @RequestParam("genres") Genre[] genres,
                                         @RequestParam("artist") String artist) throws IOException {

        AlbumModel albumModel = new AlbumModel(imgfile.getOriginalFilename(), title, description, imgfile.getBytes(),
                genres, imgfile.getContentType(),artist);
        albumRepo.save(albumModel);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/get/{albumTitle}")
    public AlbumModel getAlbum(@PathVariable("title") String title) throws IOException{
        Optional<AlbumModel> album = albumRepo.findByTitle(title);
        AlbumModel albumModel = new AlbumModel(album.get().getFilename(),album.get().getTitle(),
                album.get().getDescription(), decompress(album.get().getPicture()), album.get().getGenres(),
                album.get().getType(), album.get().getArtist());
        return albumModel;
    }

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
