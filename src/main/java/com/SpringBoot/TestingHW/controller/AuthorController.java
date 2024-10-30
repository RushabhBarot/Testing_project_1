package com.SpringBoot.TestingHW.controller;

import com.SpringBoot.TestingHW.dto.AuthorDTO;
import com.SpringBoot.TestingHW.service.AuthorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/author")
@RequiredArgsConstructor
@Slf4j
public class AuthorController {
    private final AuthorService authorService;

    @GetMapping
    public ResponseEntity<List<AuthorDTO>> getAllAuthors(){
        return ResponseEntity.ok(authorService.getAllAuthors());
    }

    @GetMapping("/{authorId}")
    public ResponseEntity<AuthorDTO> getAuthorById(@PathVariable Long authorId){
        return ResponseEntity.ok(authorService.getAuthorById(authorId));
    }

    @PostMapping
    public ResponseEntity<AuthorDTO> createNewAuthor(@RequestBody @Valid AuthorDTO authorDto){
        return new ResponseEntity(authorService.createNewAuthor(authorDto), HttpStatus.CREATED);
    }

    @PutMapping("/{authorId}")
    public ResponseEntity<AuthorDTO> updateAuthorById(@RequestBody @Valid AuthorDTO authorDto,@PathVariable Long authorId){
        return ResponseEntity.ok(authorService.updateAuthorById(authorId,authorDto));
    }

    @DeleteMapping("/{authorId}")
    public ResponseEntity<?> deleteAuthorById(@PathVariable Long authorId){
        authorService.deleteAuthorById(authorId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<List<AuthorDTO>> getAuthorsByName(@PathVariable String name){
        return ResponseEntity.ok(authorService.getAuthorsByName(name));
    }
}
