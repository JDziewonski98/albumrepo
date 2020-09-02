package com.imagerepo.albumrepo;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.Arrays;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AlbumServiceTest {
    @Autowired
    AlbumRepo albumRepo;
    AlbumService albumService;
    private final Genre[] genres = new Genre[]{Genre.Pop, Genre.Jazz};
    private final Genre[] genres2 = new Genre[]{Genre.Metal};
    private final AlbumModel albumModelToSave = new AlbumModel("testimg", "testalbum",
            "test description", new byte[0], Arrays.asList(genres), ".png", "testartist" );
    @Before
    public void clearDB() {
        albumRepo.deleteAll();
        albumService = new AlbumService(albumRepo);
    }

    @Test
    public void testSaveAlbum() throws IOException {
        MockMultipartFile mockfile = new MockMultipartFile("data", "mockfile.png", "image/png", "testtest".getBytes());
        Assert.assertFalse(albumRepo.findByTitle("Test album").isPresent());
        albumService.uploadAlbum(mockfile, "Test album", "Description", new Genre[]{Genre.Pop}, "Bob Dylan");
        Assert.assertTrue(albumRepo.findByTitle("Test album").isPresent());
    }
}
