package com.SpringBoot.TestingHW.service;

import com.SpringBoot.TestingHW.dto.AuthorDTO;
import com.SpringBoot.TestingHW.entity.AuthorEntity;
import com.SpringBoot.TestingHW.exceptions.ResourceNotFoundException;
import com.SpringBoot.TestingHW.repository.AuthorRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthorService {
    private final AuthorRepository authorRepository;
    private final ModelMapper modelMapper;


    public List<AuthorDTO> getAllAuthors() {
        log.info("Fetching all authors");
        List<AuthorEntity> authors = authorRepository.findAll();
        List<AuthorDTO> authorDtoList = authors.stream()
                .map((author) -> modelMapper.map(author, AuthorDTO.class))
                .collect(Collectors.toList());
        log.info("Successfully fetched all authors");
        return authorDtoList;
    }


    public AuthorDTO getAuthorById(Long authorId) {
        log.info("Fetching author by id: {}",authorId);
        AuthorEntity author = authorRepository.findById(authorId).orElseThrow(() ->
        {   log.error("Author not found by id {}",authorId);
            return new ResourceNotFoundException("Author not found by id:"+authorId);
        });
        log.info("Successfully fetched author by id: {}",authorId);
        return modelMapper.map(author, AuthorDTO.class);
    }

    public AuthorDTO createNewAuthor(@Valid AuthorDTO authorDto) {
        log.info("Creating new author by name: {}",authorDto.getName());
        authorDto.setName(authorDto.getName().toUpperCase());
        AuthorEntity savedAuthor = authorRepository.save(modelMapper.map(authorDto,AuthorEntity.class));
        log.info("Successfully Created new author by name: {}",authorDto.getName());
        return modelMapper.map(savedAuthor, AuthorDTO.class);
    }


    public AuthorDTO updateAuthorById(Long authorId, @Valid AuthorDTO authorDto) {
        log.info("Updating author by id: {}",authorId);
        boolean isExists = authorRepository.existsById(authorId);
        if (!isExists){
            log.error("Author not found by id: {}",authorId);
            throw new ResourceNotFoundException("Author not found by id:"+authorId);
        }
        authorDto.setId(authorId);
        authorDto.setName(authorDto.getName().toUpperCase());
        log.info("Successfully Updated author by id: {}",authorId);
        return modelMapper.map(authorRepository.save(modelMapper.map(authorDto,AuthorEntity.class)), AuthorDTO.class);
    }


    public void deleteAuthorById(Long authorId) {
        log.info("Deleting author by id: {}",authorId);
        boolean isExists = authorRepository.existsById(authorId);
        if (!isExists){
            log.error("Author not found by id: {}",authorId);
            throw new ResourceNotFoundException("Author not found by id:"+authorId);
        }
        log.info("Successfully deleted author by id: {}",authorId);
        authorRepository.deleteById(authorId);
    }


    public List<AuthorDTO> getAuthorsByName(String name) {
        log.info("Fetching authors by name: {}",name);
        List<AuthorEntity> authors = authorRepository.findByName(name.toUpperCase());
        List<AuthorDTO> authorDtoList = authors.stream()
                .map((element) -> modelMapper.map(element, AuthorDTO.class))
                .collect(Collectors.toList());
        log.info("Successfully fetched all authors by name: {}",name);
        return authorDtoList;
    }
}
