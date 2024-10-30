package com.SpringBoot.TestingHW.controller;

import com.SpringBoot.TestingHW.entity.AuthorEntity;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AuthorControllerTestIT extends AbstractIntegrationTest{

    @Test
    void testGetAllAuthors_success(){
        //we saved the author to get it afterward
        AuthorEntity savedAuthor = authorRepository.save(mockAuthorEntity);

    }
}