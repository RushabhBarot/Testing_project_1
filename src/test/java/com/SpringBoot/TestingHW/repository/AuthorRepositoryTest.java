package com.SpringBoot.TestingHW.repository;

import com.SpringBoot.TestingHW.entity.AuthorEntity;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.testcontainers.utility.TestcontainersConfiguration;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@Import(TestcontainersConfiguration.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class AuthorRepositoryTest {
    @Autowired
    private AuthorRepository authorRepository;

    @Test
    void testAuthorRepository_whenFindByName_thenReturnListOfAuthors(){
        //assign
        AuthorEntity author = new AuthorEntity();
        author.setName("JOHN DOE");
        authorRepository.save(author);

        AuthorEntity author1 = new AuthorEntity();
        author.setName("JOHN DOE");
        authorRepository.save(author1);

        //act
        List<AuthorEntity> authorEntityList = authorRepository.findByName("JOHN DOE");

        //assert
        assertThat(authorEntityList).isNotNull();
        assertThat(authorEntityList).isNotEmpty();
        assertThat(authorEntityList).hasSize(2);
        assertThat(authorEntityList).extracting(authorEntity -> authorEntity.getName()).contains("JOHN DOE");
    }

    @Test
    void testAuthorRepository_whenNameIsUnique_thenReturnUniqueName(){
        //assign
        AuthorEntity author = AuthorEntity.builder()
                .name("JOHN DOE")
                .build();
        authorRepository.save(author);

        //act
        List<AuthorEntity> authorEntityList = authorRepository.findByName("JOHN DOE");

        //assert
        assertThat(authorEntityList).isNotNull();
        assertThat(authorEntityList).hasSize(1);
        assertThat(authorEntityList).isNotEmpty();
        assertThat(authorEntityList.get(0).getName()).isEqualTo("JOHN DOE");
    }

    @Test
    void testAuthorRepository_whenNameIsNotFound_thenReturnEmptyAuthorList(){
        //assign
        String name = "anyName";

        //act
        List<AuthorEntity> authors = authorRepository.findByName("anyName");

        //assert
        assertThat(authors).hasSize(0);
        assertThat(authors).isEmpty();
        assertThat(authors).isNotNull();
    }

}