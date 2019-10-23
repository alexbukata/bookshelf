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
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ru.desiolab.bookshelf.Application;
import ru.desiolab.bookshelf.dto.Author;
import ru.desiolab.bookshelf.dto.Book;
import ru.desiolab.bookshelf.dto.request.BookRequest;
import ru.desiolab.bookshelf.dto.response.BookResponse;
import ru.desiolab.bookshelf.repository.AuthorRepository;
import ru.desiolab.bookshelf.repository.BookRepository;

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
class BookControllerTest {

    @MockBean
    AuthorRepository authorRepository;

    @MockBean
    BookRepository bookRepository;

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
    void getBook() throws Exception {
        //arrange
        Author mockAuthor = new Author().name("mockAuthor");
        Book mockBook = new Book().author(mockAuthor).name("mockBook");
        when(bookRepository.findById(eq(2L))).thenReturn(Optional.of(mockBook));
        //act
        String responseJson = mockMvc.perform(get("/book/2"))
                .andReturn().getResponse().getContentAsString();
        BookResponse bookResponse = objectMapper.readValue(responseJson, BookResponse.class);
        //assert
        assertEquals(mockBook.name(), bookResponse.name());
        verify(bookRepository).findById(eq(2L));
    }

    @Test
    void getBook_notExists() throws Exception {
        //arrange
        //act
        mockMvc.perform(get("/book/1"))
                .andExpect(status().is(404));
        //assert
        verify(bookRepository).findById(eq(1L));
    }

    @Test
    void createBook() throws Exception {
        //arrange
        when(authorRepository.findById(eq(1L))).thenReturn(Optional.of(new Author().name("author1")));
        when(bookRepository.save(any())).then(invocationOnMock -> {
            Book toSave = invocationOnMock.getArgument(0);
            toSave.id(99L);
            return toSave;
        });
        //act
        BookRequest requestBook = new BookRequest().authorId(1L).name("book1");
        String requestBookJson = objectMapper.writeValueAsString(requestBook);
        String responseJson = mockMvc.perform(post("/book")
                .contentType(MediaType.APPLICATION_JSON).content(requestBookJson))
                .andDo(MockMvcResultHandlers.print())
                .andReturn().getResponse().getContentAsString();
        //assert
        assertEquals(Long.valueOf(99L), Long.valueOf(responseJson));
        var authorCaptor = ArgumentCaptor.forClass(Book.class);
        verify(bookRepository).save(authorCaptor.capture());
        Book actualBook = authorCaptor.getValue();
        assertEquals("book1", actualBook.name());
    }

    @Test
    void updateBook() throws Exception {
        //arrange
        Author mockAuthor = new Author().name("author1");
        when(authorRepository.findById(eq(1L))).thenReturn(Optional.of(mockAuthor));
        Book mockBook = new Book().author(mockAuthor).name("book1");
        when(bookRepository.findById(eq(10L))).thenReturn(Optional.of(mockBook));
        when(bookRepository.existsById(eq(10L))).thenReturn(true);
        //act
        BookRequest bookRequest = new BookRequest().authorId(1L).name("book2");
        String requestAuthorJson = objectMapper.writeValueAsString(bookRequest);
        mockMvc.perform(put("/book/10")
                .contentType(MediaType.APPLICATION_JSON).content(requestAuthorJson));
        //assert
        verify(authorRepository).findById(eq(1L));
        verify(bookRepository).existsById(eq(10L));
        var bookCaptor = ArgumentCaptor.forClass(Book.class);
        verify(bookRepository).save(bookCaptor.capture());
        Book actualAuthor = bookCaptor.getValue();
        assertEquals("book2", actualAuthor.name());
    }

    @Test
    void updateBook_notExists() throws Exception {
        //arrange
        when(bookRepository.existsById(eq(10L))).thenReturn(false);
        //act
        BookRequest bookRequest = new BookRequest();
        String requestAuthorJson = objectMapper.writeValueAsString(bookRequest);
        mockMvc.perform(put("/book/10")
                .contentType(MediaType.APPLICATION_JSON).content(requestAuthorJson))
                .andExpect(status().is(404));
        //assert
        verify(bookRepository).existsById(eq(10L));
        verifyNoInteractions(authorRepository);
    }

    @Test
    void deleteBook() throws Exception {
        //arrange
        when(bookRepository.existsById(eq(10L))).thenReturn(true);
        //act
        mockMvc.perform(delete("/book/10"));
        //assert
        verify(bookRepository).existsById(eq(10L));
        verify(bookRepository).deleteById(eq(10L));
    }

    @Test
    void deleteBook_notExists() throws Exception {
        //arrange
        when(bookRepository.existsById(eq(10L))).thenReturn(false);
        //act
        mockMvc.perform(delete("/book/10"))
                .andExpect(status().is(404));
        //assert
        verify(bookRepository).existsById(eq(10L));
        verify(bookRepository, times(0)).deleteById(eq(10L));
    }
}