package com.SpringBoot.TestingHW.controller;

import com.SpringBoot.TestingHW.dto.AuthorDTO;
import com.SpringBoot.TestingHW.dto.BookDTO;
import com.SpringBoot.TestingHW.entity.AuthorEntity;
import com.SpringBoot.TestingHW.entity.bookEntity;
import com.SpringBoot.TestingHW.repository.AuthorRepository;
import com.SpringBoot.TestingHW.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.utility.TestcontainersConfiguration;

import java.time.LocalDate;

@AutoConfigureWebTestClient(timeout = "100000")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(TestcontainersConfiguration.class)
public class AbstractIntegrationTest {

    @Autowired
    WebTestClient webTestClient;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    AuthorRepository authorRepository;

    @Autowired
    BookRepository bookRepository;

    AuthorEntity mockAuthorEntity;
    bookEntity mockBookEntity;
    AuthorDTO mockAuthorDto;
    BookDTO mockBookDto;

    @BeforeEach
    void setUp() {
        mockAuthorEntity = AuthorEntity.builder()
                .id(1L)
                .name("JOHN DOE")
                .build();

        mockAuthorDto = modelMapper.map(mockAuthorEntity,AuthorDTO.class);

        mockBookEntity = bookEntity.builder()
                .id(1L)
                .title("FIRST BOOK")
                .description("First description")
                .publishedOn(LocalDate.now())
                .build();
        mockBookDto = modelMapper.map(mockBookEntity, BookDTO.class);

        authorRepository.deleteAll();
        bookRepository.deleteAll();
    }
}
