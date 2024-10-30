package com.SpringBoot.TestingHW.controller;

import com.SpringBoot.TestingHW.dto.AuthorDTO;
import com.SpringBoot.TestingHW.entity.AuthorEntity;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AuthorControllerTestIT extends AbstractIntegrationTest{

    @Test
    void testGetAllAuthors_success(){
        //we saved the author to get it afterward
        AuthorEntity savedAuthor = authorRepository.save(mockAuthorEntity);

        webTestClient.get()
                .uri("/author")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.data.[0].id").isEqualTo(savedAuthor.getId())
                .jsonPath("$.data.[0].name").isEqualTo(savedAuthor.getName());
    }

    @Test
    void testGetAuthorById_success() {
        AuthorEntity savedAuthor = authorRepository.save(mockAuthorEntity);

        webTestClient.get()
                .uri("/author/{authorId}",savedAuthor.getId())
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.data.id").isEqualTo(savedAuthor.getId())
                .jsonPath("$.data.name").isEqualTo(savedAuthor.getName());
    }

    @Test
    void testGetAuthorById_failure() {
        webTestClient.get()
                .uri("/author/999")
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void testCreateNewAuthor_whenValidAuthor_thenCreate(){
        webTestClient.post()
                .uri("/author")
                .bodyValue(mockAuthorDto)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.data.name").isEqualTo(mockAuthorDto.getName());
    }

    @Test
    void testCreateNewAuthor_whenInvalidAuthor_thenReturnBadRequest(){
        //create an invalid author here just 2 characters whereas there should be 3 minimum
        AuthorDTO authorDTO = AuthorDTO.builder()
                .name("Jo")
                .build();

        webTestClient.post()
                .uri("/author")
                .bodyValue(authorDTO)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.apiError.subErrors[0]").isEqualTo("Name of the author must be at least 3 characters");
    }

    @Test
    void testUpdateAuthorById_whenValidDto_thenReturnUpdatedAuthor() {
        //save dummy author
        AuthorEntity savedAuthor = authorRepository.save(mockAuthorEntity);

        //now update it
        AuthorDTO updatedAuthorDto = AuthorDTO.builder()
                .id(savedAuthor.getId())
                .name("UPDATED NAME")
                .build();

        webTestClient.put()
                .uri("/author/{authorId}", savedAuthor.getId())
                .bodyValue(updatedAuthorDto)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.data.name").isEqualTo(updatedAuthorDto.getName());
    }

    @Test
    void testUpdateAuthorById_whenInvalidDto_thenReturnBadRequest() {
        AuthorEntity savedAuthor = authorRepository.save(mockAuthorEntity);

        AuthorDTO invalidAuthorDto = AuthorDTO.builder()
                .id(savedAuthor.getId())
                .name("JO") // Invalid name
                .build();

        webTestClient.put()
                .uri("/author/{authorId}", savedAuthor.getId())
                .bodyValue(invalidAuthorDto)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.apiError.subErrors[0]").isEqualTo("Name of the author must be at least 3 characters");
    }

    @Test
    void testDeleteAuthorById_success() {
        AuthorEntity savedAuthor = authorRepository.save(mockAuthorEntity);

        webTestClient.delete()
                .uri("/author/{authorId}", savedAuthor.getId())
                .exchange()
                .expectStatus().isNoContent();

        webTestClient.get()
                .uri("/author/{authorId}", savedAuthor.getId())
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void testDeleteAuthorById_failure() {
        webTestClient.delete()
                .uri("/authors/999")
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void testGetAuthorsByName_success() {
        AuthorEntity savedAuthor = authorRepository.save(mockAuthorEntity);

        webTestClient.get()
                .uri("/author/name/{name}", savedAuthor.getName())
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.data.[0].id").isEqualTo(savedAuthor.getId())
                .jsonPath("$.data.[0].name").isEqualTo(savedAuthor.getName());
    }

    @Test
    void testGetAuthorsByName_emptyResult() {
        webTestClient.get()
                .uri("/author/name/NonExistentName")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.data").isEmpty();
    }
}