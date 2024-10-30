package com.SpringBoot.TestingHW.repository;

import com.SpringBoot.TestingHW.dto.AuthorDTO;
import com.SpringBoot.TestingHW.entity.AuthorEntity;
import com.SpringBoot.TestingHW.entity.bookEntity;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.testcontainers.utility.TestcontainersConfiguration;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@Import(TestcontainersConfiguration.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class BookRepositoryTest {
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private AuthorRepository authorRepository;

    @Test
    void testPublishedOnAfter_thenReturnBooksPublishedAfter(){
        // Arrange, Given
        bookEntity book1 = new bookEntity();
        book1.setTitle("Future Book");
        book1.setPublishedOn(LocalDate.now().plusDays(10));
        bookRepository.save(book1);

        bookEntity book2 = new bookEntity();
        book1.setTitle("Past Book");
        book1.setPublishedOn(LocalDate.now().minusDays(10));
        bookRepository.save(book2);

        //act
        List<bookEntity> bookEntityList = bookRepository.findByPublishedOnAfter(LocalDate.now());

        //assert
        assertThat(bookEntityList).isNotNull();
        assertThat(bookEntityList).isNotEmpty();
        assertThat(bookEntityList).hasSize(1);
        assertThat(bookEntityList.get(0).getTitle()).isEqualTo("Future Book");
    }

    @Test
    void testWhenAuthorIsPresent_thenReturnBooksWrittenByThatAuthor(){
        // Arrange, Given
        AuthorEntity author = new AuthorEntity();
        author.setName("John Doe");
        authorRepository.save(author);

        bookEntity book1 = new bookEntity();
        book1.setId(1L);
        book1.setTitle("Book by John Doe");
        book1.setAuthoredBy(author);
        bookRepository.save(book1);

        // Act, When
        List<bookEntity> books = bookRepository.findByAuthoredBy(author);

        // Assert, Then
        assertThat(books).isNotNull();
        assertThat(books).isNotEmpty();
        assertThat(books).hasSize(1);
        assertThat(books.get(0).getAuthoredBy().getName()).isEqualTo("John Doe");
    }

    @Test
    void testFindByTitle_whenTitleIsPresent_thenReturnBooksWithTitle(){
        //assign
        bookEntity book1 = new bookEntity();
        book1.setTitle("Book1");
        bookRepository.save(book1);

        //act
        List<bookEntity> books = bookRepository.findByTitle("Book1");

        // Assert, Then
        assertThat(books).isNotNull();
        assertThat(books).isNotEmpty();
        assertThat(books).hasSize(1);
        assertThat(books.get(0).getTitle()).isEqualTo("Book1");
    }

    @Test
    void testFindByTitle_whenTitleIsNotFound_thenReturnEmptyBookList(){
        // Act, When
        List<bookEntity> books = bookRepository.findByTitle("Nonexistent Title");

        // Assert, Then
        assertThat(books).isNotNull();
        assertThat(books).isEmpty();
    }

    @Test
    void testFindByPublishOnAfter_whenNoBooksAfterDate_thenReturnEmptyBookList(){
        //assign
        bookEntity book1 = new bookEntity();
        book1.setTitle("Past Book");
        book1.setPublishedOn(LocalDate.now().minusDays(10));
        bookRepository.save(book1);

        // Act, When
        List<bookEntity> books = bookRepository.findByPublishedOnAfter(LocalDate.now());

        //assert
        assertThat(books).isNotNull();
        assertThat(books).isEmpty();
    }

    @Test
    void testFindByCreatedBy_whenAuthorIsNotPresent_thenReturnEmptyBookList() {
        // Arrange, Given
        AuthorEntity unknownAuthor = new AuthorEntity();
        unknownAuthor.setName("Unknown Author");
        authorRepository.save(unknownAuthor);

        // Act, When
        List<bookEntity> books = bookRepository.findByAuthoredBy(unknownAuthor);

        // Assert, Then
        assertThat(books).isNotNull();
        assertThat(books).isEmpty();
    }

}