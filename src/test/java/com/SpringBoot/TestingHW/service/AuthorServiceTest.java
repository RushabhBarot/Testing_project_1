package com.SpringBoot.TestingHW.service;

import com.SpringBoot.TestingHW.dto.AuthorDTO;
import com.SpringBoot.TestingHW.entity.AuthorEntity;
import com.SpringBoot.TestingHW.exceptions.ResourceNotFoundException;
import com.SpringBoot.TestingHW.repository.AuthorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthorServiceTest {

    @Mock
    private AuthorRepository authorRepository;

    @InjectMocks
    private AuthorService authorService;

    @Spy
    private ModelMapper modelMapper;

    private AuthorDTO mockAuthorDTO;
    private AuthorEntity mockAuthorEntity;

    @BeforeEach
    void setUp() {
        mockAuthorEntity = AuthorEntity.builder()
                .id(1L)
                .name("JOHN DOE")
                .build();

        mockAuthorDTO = AuthorDTO.builder()
                .id(1L)
                .name("JOHN DOE")
                .build();
    }

    @Test
    void testGetAllAuthors_whenAuthorsArePresent_thenReturnListOfAuthorDTOS() {
        // Arrange, Given
        when(authorRepository.findAll()).thenReturn(List.of(mockAuthorEntity));

        // Act, When
        List<AuthorDTO> authorDtoList = authorService.getAllAuthors();

        // Assert, Then
        assertThat(authorDtoList).isNotNull();
        assertThat(authorDtoList).hasSize(1);
        assertThat(authorDtoList.get(0).getName()).isEqualTo(mockAuthorEntity.getName());
        verify(authorRepository, only()).findAll();
    }

    @Test
    void testGetAuthorById_whenAuthorIsPresent_thenReturnAuthorDTO(){

        //assign
        when(authorRepository.findById(anyLong())).thenReturn(Optional.of(mockAuthorEntity));

        //act
        AuthorDTO authorDto = authorService.getAuthorById(1L);

        //assertThat
        assertThat(authorDto).isNotNull();
        assertThat(authorDto.getName()).isEqualTo(mockAuthorEntity.getName());
        verify(authorRepository, only()).findById(1L);
    }

    @Test
    void testGetAuthorById_whenAuthorIsNotPresent_thenThrowException(){
        //assign
        when(authorRepository.findById(anyLong())).thenReturn(Optional.empty());

        //act nd assert
        assertThatThrownBy(()->authorService.getAuthorById(1L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Author not found by id:1");

        verify(authorRepository).findById(1L);
    }

    @Test
    void testCreateNewAuthor_whenValidAuthor_thenCreateNewAuthor(){
        //assign
        when(authorRepository.save(any(AuthorEntity.class))).thenReturn(mockAuthorEntity);

        //act
        AuthorDTO authorDTO = authorService.createNewAuthor(mockAuthorDTO);

        //assert
        assertThat(authorDTO).isNotNull();
        assertThat(authorDTO.getName()).isEqualTo(mockAuthorDTO.getName().toUpperCase());

        verify(authorRepository).save(any(AuthorEntity.class));
    }

    @Test
    void testUpdateAuthorById_whenAuthorExists_thenReturnAuthorDTO(){
        //assign
        when(authorRepository.existsById(anyLong())).thenReturn(true);
        when(authorRepository.save(any(AuthorEntity.class))).thenReturn(mockAuthorEntity);

        //act
        AuthorDTO authorDTO = authorService.updateAuthorById(mockAuthorDTO.getId(),mockAuthorDTO);

        //assert
        assertThat(authorDTO).isNotNull();
        assertThat(authorDTO.getName()).isEqualTo(mockAuthorDTO.getName().toUpperCase());
        assertThat(authorDTO.getId()).isEqualTo(mockAuthorDTO.getId());
        verify(authorRepository,times(1)).save(any(AuthorEntity.class));
        verify(authorRepository,times(1)).existsById(anyLong());
    }

    @Test
    void testUpdateAuthorById_whenAuthorDoesNotExists_thenThrowException(){
        //assign
        when(authorRepository.existsById(anyLong())).thenReturn(false);

        //act nd assert
        assertThatThrownBy(()->authorService.updateAuthorById(1L,mockAuthorDTO))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Author not found by id:1");
    }

    @Test
    void testDeleteAuthorById_whenAuthorExists_thenDeleteAuthor(){
        // Arrange, Given
        when(authorRepository.existsById(anyLong())).thenReturn(true);

        // Act, When
        authorService.deleteAuthorById(1L);

        // Assert, Then
        verify(authorRepository).deleteById(1L);
    }

    @Test
    void  testDeleteAuthorById_whenAuthorDoesNotExist_thenThrowException() {
        // Arrange
        when(authorRepository.existsById(anyLong())).thenReturn(false);

        // Act & Assert, When & Then
        assertThatThrownBy(() -> authorService.deleteAuthorById(1L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Author not found by id:1");

        verify(authorRepository, never()).deleteById(anyLong());
    }

    @Test
    void testGetAuthorsByName_whenAuthorsPresent_thenReturnListOfAuthorDTOS(){
        //assign
        when(authorRepository.findByName(mockAuthorEntity.getName().toUpperCase()))
                .thenReturn(List.of(mockAuthorEntity));

        //act
        List<AuthorDTO> authorDTOS = authorService.getAuthorsByName(mockAuthorEntity.getName());

        //assert
        assertThat(authorDTOS).hasSize(1);
        assertThat(authorDTOS).isNotNull();
        assertThat(authorDTOS.get(0).getName()).isEqualTo(mockAuthorDTO.getName().toUpperCase());

        verify(authorRepository,times(1)).findByName(mockAuthorEntity.getName().toUpperCase());

    }

}