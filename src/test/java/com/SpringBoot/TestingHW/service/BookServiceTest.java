package com.SpringBoot.TestingHW.service;

import com.SpringBoot.TestingHW.dto.AuthorDTO;
import com.SpringBoot.TestingHW.dto.BookDTO;
import com.SpringBoot.TestingHW.entity.AuthorEntity;
import com.SpringBoot.TestingHW.entity.bookEntity;
import com.SpringBoot.TestingHW.exceptions.ResourceNotFoundException;
import com.SpringBoot.TestingHW.repository.AuthorRepository;
import com.SpringBoot.TestingHW.repository.BookRepository;
import lombok.Builder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;
    @Mock
    private AuthorRepository authorRepository;
    @InjectMocks
    private BookService bookService;
    @Spy
    private ModelMapper modelMapper;

    private AuthorEntity mockAuthorEntity;
    private AuthorDTO mockAuthorDTO;
    private bookEntity mockBookEntity;
    private BookDTO mockBookDTO;

    @BeforeEach
    void setUp(){
        mockAuthorEntity = AuthorEntity.builder()
                .id(1L)
                .name("JOHN DOE")
                .build();

        mockAuthorDTO = modelMapper.map(mockAuthorEntity,AuthorDTO.class);

        mockBookEntity = bookEntity.builder()
                .id(1L)
                .title("FIRST BOOK")
                .publishedOn(LocalDate.now())
                .build();
        mockBookDTO = modelMapper.map(mockBookEntity, BookDTO.class);
    }

    @Test
    void testGetAllBooks_whenBooksArePresent_thenReturnListOfBookDTOS() {
        // Arrange
        when(bookRepository.findAll()).thenReturn(List.of(mockBookEntity));
        // Act
        List<BookDTO> bookDtoList = bookService.getAllBooks();

        // Assert
        assertThat(bookDtoList).isNotNull();
        assertThat(bookDtoList).hasSize(1);
        assertThat(bookDtoList.get(0).getTitle()).isEqualTo(mockBookEntity.getTitle());
        verify(bookRepository, only()).findAll();
    }

    @Test
    void testGetBookById_whenBookExists_thenReturnBookDto() {
        // Arrange
        Long bookId = 1L;
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(mockBookEntity));

        // Act
        BookDTO bookDto = bookService.getBookById(bookId);

        // Assert
        assertThat(bookDto).isNotNull();
        assertThat(bookDto.getTitle()).isEqualTo(mockBookEntity.getTitle());
        verify(bookRepository, only()).findById(bookId);
    }

    @Test
    void testGetBookById_whenBookDoesNotExist_thenThrowResourceNotFoundException() {
        // Arrange
        Long bookId = 1L;
        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> bookService.getBookById(bookId));
        verify(bookRepository, only()).findById(bookId);
    }

    @Test
    void testCreateNewBook_whenValidBookDto_thenReturnSavedBookDto() {
        // Arrange
        when(bookRepository.save(any(bookEntity.class))).thenReturn(mockBookEntity);

        // Act
        BookDTO savedBookDto = bookService.createNewBook(mockBookDTO);

        // Assert
        ArgumentCaptor<bookEntity> bookArgumentCaptor = ArgumentCaptor.forClass(bookEntity.class);
        assertThat(savedBookDto).isNotNull();
        assertThat(savedBookDto.getTitle()).isEqualTo(mockBookEntity.getTitle());
        verify(bookRepository, only()).save(bookArgumentCaptor.capture());
    }

    @Test
    void testUpdateBookById_whenBookExists_thenReturnUpdatedBookDto() {
        // Arrange
        Long bookId = 1L;
        when(bookRepository.existsById(bookId)).thenReturn(true);
        when(modelMapper.map(mockBookDTO, bookEntity.class)).thenReturn(mockBookEntity);
        when(bookRepository.save(mockBookEntity)).thenReturn(mockBookEntity);
        when(modelMapper.map(mockBookEntity, BookDTO.class)).thenReturn(mockBookDTO);

        // Act
        BookDTO updatedBookDto = bookService.updateBookById(bookId, mockBookDTO);

        // Assert
        assertThat(updatedBookDto).isNotNull();
        assertThat(updatedBookDto.getTitle()).isEqualTo(mockBookEntity.getTitle());
        verify(bookRepository, times(1)).existsById(bookId);
        verify(bookRepository, times(1)).save(mockBookEntity);
    }

    @Test
    void testUpdateBookById_whenBookDoesNotExist_thenThrowResourceNotFoundException() {
        // Arrange
        Long bookId = 1L;
        when(bookRepository.existsById(bookId)).thenReturn(false);

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> bookService.updateBookById(bookId, mockBookDTO));
        verify(bookRepository, only()).existsById(bookId);
    }

    @Test
    void testDeleteBookById_whenBookExists_thenBookIsDeleted() {
        // Arrange
        Long bookId = 1L;
        when(bookRepository.existsById(bookId)).thenReturn(true);

        // Act
        bookService.deleteBookById(bookId);

        // Assert
        verify(bookRepository, times(1)).existsById(bookId);
        verify(bookRepository, times(1)).deleteById(bookId);
    }

    @Test
    void testDeleteBookById_whenBookDoesNotExist_thenThrowResourceNotFoundException() {
        // Arrange
        Long bookId = 1L;
        when(bookRepository.existsById(bookId)).thenReturn(false);

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> bookService.deleteBookById(bookId));
        verify(bookRepository, only()).existsById(bookId);
    }

    @Test
    void testGetBooksPublishedAfterDate_whenBooksArePresent_thenReturnListOfBookDTOS() {
        // Arrange
        LocalDate date = LocalDate.of(2020, 1, 1);
        when(bookRepository.findByPublishedOnAfter(date)).thenReturn(List.of(mockBookEntity));
        when(modelMapper.map(mockBookEntity, BookDTO.class)).thenReturn(mockBookDTO);

        // Act
        List<BookDTO> bookDtoList = bookService.getBooksPublishedAfterDate(date);

        // Assert
        assertThat(bookDtoList).isNotNull();
        assertThat(bookDtoList).hasSize(1);
        assertThat(bookDtoList.get(0).getTitle()).isEqualTo(mockBookEntity.getTitle());
        verify(bookRepository, only()).findByPublishedOnAfter(date);
    }

    @Test
    void testGetBooksByTitle_whenBooksArePresent_thenReturnListOfBookDTOS() {
        // Arrange
        String title = "Test Title";
        when(bookRepository.findByTitle(title.toUpperCase())).thenReturn(List.of(mockBookEntity));

        // Act
        List<BookDTO> bookDtoList = bookService.getBooksByTitle(title);

        // Assert
        assertThat(bookDtoList).isNotNull();
        assertThat(bookDtoList).hasSize(1);
        assertThat(bookDtoList.get(0).getTitle()).isEqualTo(mockBookEntity.getTitle());
        verify(bookRepository, only()).findByTitle(title.toUpperCase());
    }

    @Test
    void testGetBooksAuthoredBy_whenAuthorExists_thenReturnListOfBookDTOS() {
        // Arrange
        Long authorId = 1L;
        when(authorRepository.findById(authorId)).thenReturn(Optional.of(mockAuthorEntity));
        when(bookRepository.findByAuthoredBy(mockAuthorEntity)).thenReturn(List.of(mockBookEntity));
        when(modelMapper.map(mockBookEntity, BookDTO.class)).thenReturn(mockBookDTO);

        // Act
        List<BookDTO> bookDtoList = bookService.getBooksAuthoredBy(authorId);

        // Assert
        assertThat(bookDtoList).isNotNull();
        assertThat(bookDtoList).hasSize(1);
        assertThat(bookDtoList.get(0).getTitle()).isEqualTo(mockBookEntity.getTitle());
        verify(authorRepository, times(1)).findById(authorId);
        verify(bookRepository, times(1)).findByAuthoredBy(mockAuthorEntity);
    }

    @Test
    void testGetBooksAuthoredBy_whenAuthorDoesNotExist_thenThrowResourceNotFoundException() {
        // Arrange
        Long authorId = 1L;
        when(authorRepository.findById(authorId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> bookService.getBooksAuthoredBy(authorId));
        verify(authorRepository, only()).findById(authorId);
    }

    @Test
    void testAssignAuthorToBook_whenBookAndAuthorExist_thenReturnUpdatedBookDto() {
        // Arrange
        Long bookId = 1L;
        Long authorId = 1L;
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(mockBookEntity));
        when(authorRepository.findById(authorId)).thenReturn(Optional.of(mockAuthorEntity));
        mockBookEntity.setAuthoredBy(mockAuthorEntity);
        when(bookRepository.save(mockBookEntity)).thenReturn(mockBookEntity);

        // Act
        BookDTO updatedBookDto = bookService.assignAuthorToBook(bookId, authorId);

        // Assert
        assertThat(updatedBookDto).isNotNull();
        assertThat(updatedBookDto.getTitle()).isEqualTo(mockBookEntity.getTitle());
        //assertThat(updatedBookDto.getAuthored_by().getId()).isEqualTo(authorId);
        verify(bookRepository, times(1)).findById(bookId);
        verify(authorRepository, times(1)).findById(authorId);
        verify(bookRepository, times(1)).save(mockBookEntity);
    }

    @Test
    void testAssignAuthorToBook_whenBookDoesNotExist_thenThrowResourceNotFoundException() {
        // Arrange
        Long bookId = 1L;
        Long authorId = 1L;
        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> bookService.assignAuthorToBook(bookId, authorId));
        verify(bookRepository, only()).findById(bookId);
    }

    @Test
    void testAssignAuthorToBook_whenAuthorDoesNotExist_thenThrowResourceNotFoundException() {
        // Arrange
        Long bookId = 1L;
        Long authorId = 1L;
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(mockBookEntity));
        when(authorRepository.findById(authorId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> bookService.assignAuthorToBook(bookId, authorId));
        verify(authorRepository, only()).findById(authorId);
    }
}