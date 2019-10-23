package ru.desiolab.bookshelf.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ru.desiolab.bookshelf.Application;
import ru.desiolab.bookshelf.dto.Author;
import ru.desiolab.bookshelf.dto.request.AuthorRequest;
import ru.desiolab.bookshelf.dto.response.AuthorResponse;
import ru.desiolab.bookshelf.repository.AuthorRepository;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = Application.class)
@ActiveProfiles("test")
@AutoConfigureMockMvc
class AuthorControllerTest {

    @MockBean
    AuthorRepository authorRepository;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    WebApplicationContext webApplicationContext;

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void getAuthor() throws Exception {
        //arrange
        Author mockAuthor = new Author().name("mock");
        when(authorRepository.findById(eq(1L))).thenReturn(Optional.of(mockAuthor));
        //act
        String responseJson = mockMvc.perform(get("/author/1"))
                .andReturn().getResponse().getContentAsString();
        AuthorResponse author = objectMapper.readValue(responseJson, AuthorResponse.class);
        //assert
        assertEquals(mockAuthor.name(), author.name());
        verify(authorRepository).findById(eq(1L));
    }

    @Test
    void getAuthor_notExists() throws Exception {
        //arrange
        //act
        mockMvc.perform(get("/author/1"))
                .andExpect(status().is(404));
        //assert
        verify(authorRepository).findById(eq(1L));
    }

    @Test
    void createAuthor() throws Exception {
        //arrange
        when(authorRepository.save(any())).then(invocationOnMock -> {
            Author toSave = invocationOnMock.getArgument(0);
            toSave.id(99L);
            return toSave;
        });
        //act
        AuthorRequest requestAuthor = new AuthorRequest().name("author1");
        String requestAuthorJson = objectMapper.writeValueAsString(requestAuthor);
        String responseJson = mockMvc.perform(post("/author")
                .contentType(MediaType.APPLICATION_JSON).content(requestAuthorJson))
                .andReturn().getResponse().getContentAsString();
        //assert
        assertEquals(Long.valueOf(99L), Long.valueOf(responseJson));
        var authorCaptor = ArgumentCaptor.forClass(Author.class);
        verify(authorRepository).save(authorCaptor.capture());
        Author actualAuthor = authorCaptor.getValue();
        assertEquals("author1", actualAuthor.name());
    }

    @Test
    void updateAuthor() throws Exception {
        //arrange
        Author mockAuthor = new Author().name("author1");
        when(authorRepository.findById(eq(1L))).thenReturn(Optional.of(mockAuthor));
        when(authorRepository.existsById(eq(1L))).thenReturn(true);
        //act
        AuthorRequest requestAuthor = new AuthorRequest().name("author2");
        String requestAuthorJson = objectMapper.writeValueAsString(requestAuthor);
        mockMvc.perform(put("/author/1")
                .contentType(MediaType.APPLICATION_JSON).content(requestAuthorJson));
        //assert
        verify(authorRepository).existsById(eq(1L));
        verify(authorRepository).findById(eq(1L));
        var authorCaptor = ArgumentCaptor.forClass(Author.class);
        verify(authorRepository).save(authorCaptor.capture());
        Author actualAuthor = authorCaptor.getValue();
        assertEquals("author2", actualAuthor.name());
    }

    @Test
    void updateAuthor_notExists() throws Exception {
        //arrange
        when(authorRepository.existsById(eq(1L))).thenReturn(false);
        //act
        AuthorRequest requestAuthor = new AuthorRequest().name("author2");
        String requestAuthorJson = objectMapper.writeValueAsString(requestAuthor);
        mockMvc.perform(put("/author/1")
                .contentType(MediaType.APPLICATION_JSON).content(requestAuthorJson))
                .andExpect(status().is(404));
        //assert
        verify(authorRepository).existsById(eq(1L));
        verify(authorRepository, times(0)).findById(eq(1L));
        verify(authorRepository, times(0)).save(any());
    }

    @Test
    void deleteAuthor() throws Exception {
        //arrange
        when(authorRepository.existsById(eq(1L))).thenReturn(true);
        //act
        mockMvc.perform(delete("/author/1"));
        //assert
        verify(authorRepository).existsById(eq(1L));
        verify(authorRepository).deleteById(eq(1L));
    }

    @Test
    void deleteAuthor_notExists() throws Exception {
        //arrange
        when(authorRepository.existsById(eq(1L))).thenReturn(false);
        //act
        AuthorRequest requestAuthor = new AuthorRequest().name("author2");
        String requestAuthorJson = objectMapper.writeValueAsString(requestAuthor);
        mockMvc.perform(delete("/author/1")
                .contentType(MediaType.APPLICATION_JSON).content(requestAuthorJson))
                .andExpect(status().is(404));
        //assert
        verify(authorRepository).existsById(eq(1L));
        verify(authorRepository, times(0)).findById(eq(1L));
        verify(authorRepository, times(0)).save(any());
    }
}