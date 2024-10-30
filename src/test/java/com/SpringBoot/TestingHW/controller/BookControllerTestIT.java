package com.SpringBoot.TestingHW.controller;

import com.SpringBoot.TestingHW.dto.BookDTO;
import com.SpringBoot.TestingHW.entity.AuthorEntity;
import com.SpringBoot.TestingHW.entity.bookEntity;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class BookControllerTestIT extends AbstractIntegrationTest{

    @Test
    void testGetAllBooks_whenBooksExist_thenReturnBookList() {
        bookEntity book = bookRepository.save(mockBookEntity);

        webTestClient.get()
                .uri("/book")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.data.[0].id").isEqualTo(book.getId())
                .jsonPath("$.data.[0].title").isEqualTo(book.getTitle());
    }

    @Test
    void testGetAllBooks_whenBooksNotPresent_thenReturnEmptyList(){
        webTestClient.get()
                .uri("/book")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.data").isEmpty();
    }

    @Test
    void testGetBookById_whenBookExists_thenReturnBook(){
        bookEntity book = bookRepository.save(mockBookEntity);
        webTestClient.get()
                .uri("/book/{bookId}",book.getId())
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.data.id").isEqualTo(book.getId());
    }

    @Test
    void testGetBookById_whenBookNotPresent_thenThrowResourceNotFoundError(){

        webTestClient.get()
                .uri("/book/{bookId}",100)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.apiError.message").isEqualTo("Book not found by id:"+100);
    }

    @Test
    void testCreateNewBook_whenValidDto_thenReturnCreatedBook() {

        webTestClient.post()
                .uri("/book")
                .bodyValue(mockBookDto)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.data.title").isEqualTo(mockBookEntity.getTitle());
    }

    @Test
    void testCreateNewBook_whenPublishedOnDateIsNotPastOrPresent_thenThrowMethodArgumentError() {

        mockBookDto.setPublishedOn(LocalDate.now().plusDays(10));

        webTestClient.post()
                .uri("/book")
                .bodyValue(mockBookDto)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.apiError.subErrors[0]").isEqualTo("Book publish date should be Past or Present");
    }

    @Test
    void testUpdateBookById_whenValidDto_thenReturnUpdatedBook() {
        bookEntity savedBook = bookRepository.save(mockBookEntity);

        BookDTO updatedBookDto = BookDTO.builder()
                .title("UPDATED TITLE")
                .description("UPDATED DESCRIPTION")
                .publishedOn(LocalDate.now().minusDays(2))
                .build();

        webTestClient.put()
                .uri("/book/{bookId}", savedBook.getId())
                .bodyValue(updatedBookDto)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.data.title").isEqualTo(updatedBookDto.getTitle());
    }

    @Test
    void testUpdateBookById_whenBookNotFound_thenThrowResourceNotFoundError() {

        BookDTO updatedBookDto = BookDTO.builder()
                .title("UPDATED TITLE")
                .description("UPDATED DESCRIPTION")
                .publishedOn(LocalDate.now().minusDays(2))
                .build();

        webTestClient.put()
                .uri("/book/{bookId}", 100)
                .bodyValue(updatedBookDto)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.apiError.message").isEqualTo("Book not found by id:"+100);
    }

    @Test
    void testDeleteBookById_whenValidId_thenDeleteBook() {
        bookEntity savedBook = bookRepository.save(mockBookEntity);

        webTestClient.delete()
                .uri("/book/{bookId}", savedBook.getId())
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    void testDeleteBookById_whenBookNotPresent_thenThrowResourceNotFoundError() {

        webTestClient.delete()
                .uri("/book/{bookId}", 100)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.apiError.message").isEqualTo("Book not found by id:"+100);
    }

    @Test
    void testGetBooksPublishedAfterDate_whenBooksExist_thenReturnBooksAfterDate(){
        bookEntity book1 = bookEntity.builder()
                .title("BOOK1")
                .publishedOn(LocalDate.of(2023,1,1))
                .build();
        bookEntity book2 = bookEntity.builder()
                .title("BOOK2")
                .publishedOn(LocalDate.of(2024,1,1))
                .build();

        bookEntity savedBook1 = bookRepository.save(book1);
        bookEntity savedBook2 = bookRepository.save(book2);

        webTestClient.get()
                .uri("/book/getAfterDate/{date}", LocalDate.of(2023, 12, 31))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.data.[0].title").isEqualTo("BOOK2");
    }

    @Test
    void testGetBooksByTitle_whenBooksExist_thenReturnBooks() {

        bookEntity savedBook = bookRepository.save(mockBookEntity);

        webTestClient.get()
                .uri("/book/title/{title}", savedBook.getTitle())
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.data.[0].title").isEqualTo(savedBook.getTitle());
    }

    @Test
    void testGetBooksByTitle_whenBooksNotPresent_thenReturnEmptyListOfBooks() {

        webTestClient.get()
                .uri("/book/title/{title}", "FIRST BOOK")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.data").isEmpty();
    }

    @Test
    void testGetBooksByCreatedBy_whenBooksPresent_thenReturnBooks() {
        //save dummy author
        AuthorEntity savedAuthor = authorRepository.save(mockAuthorEntity);


        mockBookEntity.setAuthoredBy(savedAuthor);

        bookEntity savedBook = bookRepository.save(mockBookEntity);

        webTestClient.get()
                .uri("/book/createdBy/{authorId}", savedAuthor.getId())
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.data.[0].authoredBy.name").isEqualTo(savedAuthor.getName());
    }

    @Test
    void testGetBooksByCreatedBy_whenAuthorNotFound_thenThrowResourceNotFoundError() {
        AuthorEntity savedAuthor = authorRepository.save(mockAuthorEntity);

        mockBookEntity.setAuthoredBy(savedAuthor);
        bookEntity savedBook = bookRepository.save(mockBookEntity);

        webTestClient.get()
                .uri("/book/createdBy/{authorId}", 100)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.apiError.message").isEqualTo("Author not found by id:"+100);
    }

    @Test
    void testAssignAuthorToBook_whenAuthorAndBookBothPresent_thenReturnBookWithAuthor(){
        AuthorEntity savedAuthor = authorRepository.save(mockAuthorEntity);

        mockBookEntity.setAuthoredBy(savedAuthor);
        bookEntity savedBook = bookRepository.save(mockBookEntity);

        webTestClient.put()
                .uri("/book/{bookId}/assignAuthorToBook/{authorId}",savedBook.getId(),savedAuthor.getId())
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.data.id").isEqualTo(savedBook.getId())
                .jsonPath("$.data.authoredBy.id").isEqualTo(savedAuthor.getId());
    }

    @Test
    void testAssignAuthorToBook_whenAuthorNotPresent_thenReturnResourceNotFoundError(){
        AuthorEntity savedAuthor = authorRepository.save(mockAuthorEntity);
        mockBookEntity.setAuthoredBy(savedAuthor);
        bookEntity savedBook = bookRepository.save(mockBookEntity);

        webTestClient.put()
                .uri("/book/{bookId}/assignAuthorToBook/{authorId}",savedBook.getId(),100)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.apiError.message").isEqualTo("Author not found by id:"+100);
    }

    @Test
    void testAssignAuthorToBook_whenBookNotPresent_thenReturnRsourceNotFoundError(){
        AuthorEntity savedAuthor = authorRepository.save(mockAuthorEntity);
        mockBookEntity.setAuthoredBy(savedAuthor);
        bookEntity savedBook = bookRepository.save(mockBookEntity);

        webTestClient.put()
                .uri("/book/{bookId}/assignAuthorToBook/{authorId}",100,savedAuthor.getId())
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.apiError.message").isEqualTo("Book not found by id:"+100);

    }
}