package com.imagerepo.albumrepo;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
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
    public void testUploadAlbum() throws IOException {
        MockMultipartFile mockfile = new MockMultipartFile("data", "mockfile.png", "image/png", "testtest".getBytes());
        Assert.assertFalse(albumRepo.findByTitle("Test album").isPresent());
        albumService.uploadAlbum(mockfile, "Test album", "Description", new Genre[]{Genre.Pop}, "Bob Dylan");
        Assert.assertTrue(albumRepo.findByTitle("Test album").isPresent());
    }

    @Test
    public void testSearchByKeyword() throws IOException {
        Assert.assertTrue(albumService.searchByKeyword("keyword").isEmpty());
        MockMultipartFile mockfile = new MockMultipartFile("data", "mockfile.png", "image/png", "testtest".getBytes());
        albumService.uploadAlbum(mockfile, "Test album", "keyword", new Genre[]{Genre.Pop}, "Bob Dylan");
        Assert.assertEquals(albumService.searchByKeyword("keyword").size(), 1);
        albumService.uploadAlbum(mockfile, "keyword album", "asdasd", new Genre[]{Genre.Pop}, "Bob Dylan");
        Assert.assertEquals(albumService.searchByKeyword("keyword").size(), 2);
        albumService.uploadAlbum(mockfile, "album", "asdasd", new Genre[]{Genre.Pop}, "Bob Dylan");
        Assert.assertEquals(albumService.searchByKeyword("keyword").size(), 2);
    }

    @Test
    public void testSearchByGenre() throws IOException {
        Assert.assertTrue(albumService.searchByKeyword("keyword").isEmpty());
        MockMultipartFile mockfile = new MockMultipartFile("data", "mockfile.png", "image/png", "testtest".getBytes());
        albumService.uploadAlbum(mockfile, "Test album", "keyword", new Genre[]{Genre.Pop}, "Bob Dylan");
        Assert.assertEquals(albumService.searchByGenre(new Genre[]{Genre.Pop}).size(), 1);
        albumService.uploadAlbum(mockfile, "keyword album", "asdasd", new Genre[]{Genre.Pop, Genre.Classical}, "Bob Dylan");
        Assert.assertEquals(albumService.searchByGenre(new Genre[]{Genre.Pop}).size(), 2);
        albumService.uploadAlbum(mockfile, "album", "asdasd", new Genre[]{Genre.Rock}, "Bob Dylan");
        Assert.assertEquals(albumService.searchByGenre(new Genre[]{Genre.Pop}).size(), 2);
        Assert.assertEquals(albumService.searchByGenre(new Genre[]{Genre.Metal}).size(), 0);
    }

    @Test
    public void testSearchByGenreMatchAll() throws IOException {
        HashSet<AlbumModel> albums = null;
        Assert.assertTrue(albumService.searchByKeyword("keyword").isEmpty());
        MockMultipartFile mockfile = new MockMultipartFile("data", "mockfile.png", "image/png", "testtest".getBytes());
        albumService.uploadAlbum(mockfile, "Test album", "keyword", new Genre[]{Genre.Pop}, "Bob Dylan");
        albums = albumService.searchByGenreMatchAll(new Genre[]{Genre.Pop, Genre.Rock});
        Assert.assertEquals(albums.size(), 0);
        albumService.uploadAlbum(mockfile, "keyword album", "asdasd", new Genre[]{Genre.Pop, Genre.Rock}, "Bob Dylan");
        albums =albumService.searchByGenreMatchAll(new Genre[]{Genre.Pop, Genre.Rock});
        Assert.assertEquals(albums.size(), 1);
        albumService.uploadAlbum(mockfile, "album", "asdasd", new Genre[]{Genre.Rock}, "Bob Dylan");
        albums = albumService.searchByGenreMatchAll(new Genre[]{Genre.Pop, Genre.Rock});
        Assert.assertEquals(albums.size(), 1);
        albums =albumService.searchByGenreMatchAll(new Genre[]{Genre.Jazz});
        Assert.assertEquals(albums.size(), 0);
    }

    @Test
    public void testGetAll() throws IOException {
        Assert.assertTrue(albumService.getAll().isEmpty());
        MockMultipartFile mockfile = new MockMultipartFile("data", "mockfile.png", "image/png", "testtest".getBytes());
        albumService.uploadAlbum(mockfile, "Test album", "keyword", new Genre[]{Genre.Pop}, "Bob Dylan");
        albumService.uploadAlbum(mockfile, "keyword album", "asdasd", new Genre[]{Genre.Pop}, "Bob Dylan");
        Assert.assertEquals(albumService.getAll().size(), 2);
    }
}
