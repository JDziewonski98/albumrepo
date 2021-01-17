package com.imagerepo.albumrepo;

import com.imagerepo.albumrepo.controllers.AlbumController;
import com.imagerepo.albumrepo.enums.Genre;
import com.imagerepo.albumrepo.models.AlbumModel;
import com.imagerepo.albumrepo.repositories.AlbumRepo;
import com.imagerepo.albumrepo.services.AlbumService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Random;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class AlbumControllerTest {
    private MockMvc mockMvc;
    @Autowired
    WebApplicationContext webApplicationContext;
    @MockBean
    private AlbumService mockAlbumService;
    private final Genre[] genres = new Genre[]{Genre.Pop, Genre.Jazz};


    @Autowired
    AlbumRepo albumrepo;
    AlbumService albumService;
    AlbumController albumController;

    @Before
    public void clearDB() {
        this.albumService = new AlbumService(albumrepo);
        this.albumController = new AlbumController(albumService);
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .alwaysDo(MockMvcResultHandlers.print())
                .build();
    }

    @Test
    public void testUpload() throws Exception {
        MockMultipartFile mockfile = new MockMultipartFile("data", "thing.txt", "text/plain", "testtest".getBytes());

        Mockito.doNothing().when(mockAlbumService).uploadAlbum(Mockito.any(),Mockito.any(),Mockito.any(),Mockito.any(),Mockito.any());

        MvcResult res = this.mockMvc.perform(MockMvcRequestBuilders.multipart("/api/v1/upload")
                .file("imgfile", mockfile.getBytes())
                .param("title","asd")
                .param("description","description")
                .param("genres", "Pop")
                .param("artist", "artist")
        ).andExpect(status().is(201)).andReturn();

        res = this.mockMvc.perform(MockMvcRequestBuilders.multipart("/api/v1/upload")
                .file("imgfile", mockfile.getBytes())
                .param("title","")
                .param("description","description")
                .param("genres", "Pop")
                .param("artist", "artist")
        ).andExpect(status().is(400)).andReturn();

        Mockito.doThrow(new IOException()).when(mockAlbumService).uploadAlbum(Mockito.any(),Mockito.any(),Mockito.any(),Mockito.any(),Mockito.any());
        res = this.mockMvc.perform(MockMvcRequestBuilders.multipart("/api/v1/upload")
                .file("imgfile", mockfile.getBytes())
                .param("title","asd")
                .param("description","description")
                .param("genres", "Pop")
                .param("artist", "artist")
        ).andExpect(status().is(500)).andReturn();
    }

    @Test
    public void testSearch() throws Exception {

        AlbumModel album = new AlbumModel("testname", "testtitle", "desc",
                this.getRandomByteArray(), Arrays.asList(genres), "png", "bob");
        AlbumModel album2 = new AlbumModel("testname2", "testtitle2", "desc2",
                this.getRandomByteArray(), Arrays.asList(genres), "png2", "bob2");
        HashSet<AlbumModel> hs1 = new HashSet<>(Collections.emptySet());
        hs1.add(album);
        HashSet<AlbumModel> hs2 = new HashSet<>(Collections.emptySet());
        hs2.add(album2);
        HashSet<AlbumModel> hs3= new HashSet<>(Collections.emptySet());
        hs3.add(album);
        hs3.add(album2);

        Mockito
                .when(mockAlbumService.searchByKeyword(Mockito.any()))
                .thenReturn(hs1);
        Mockito
                .when(mockAlbumService.searchByGenre(Mockito.any()))
                .thenReturn(hs2);
        Mockito
                .when(mockAlbumService.searchByGenreMatchAll(Mockito.any()))
                .thenReturn(hs3);

        MvcResult res = this.mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/search")
                .param("text","")
                .param("genres", "")
                .param("exactMatch","true")
        ).andExpect(status().is(400)).andReturn();

        res = this.mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/search")
                .param("text","test")
                .param("genres", "")
                .param("exactMatch","true")
        ).andExpect(status().is(201)).andReturn();

        res = this.mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/search")
                .param("text","")
                .param("genres", "Pop")
                .param("exactMatch","true")
        ).andExpect(status().is(201)).andReturn();

        res = this.mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/search")
                .param("text","test")
                .param("genres", "Pop")
                .param("exactMatch","false")
        ).andExpect(status().is(201)).andReturn();

    }

    private byte[] getRandomByteArray() {
        byte[] b = new byte[20];
        new Random().nextBytes(b);
        return b;
    }
}
