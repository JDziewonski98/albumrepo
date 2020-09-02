package com.imagerepo.albumrepo;

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

import java.io.IOException;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class AlbumControllerTest {
    private MockMvc mockMvc;
    @Autowired
    WebApplicationContext webApplicationContext;
    @MockBean
    private AlbumService mockAlbumService;



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
}
