package com.SpringBoot.TestingHW.service;

import com.SpringBoot.TestingHW.dto.BookDTO;
import com.SpringBoot.TestingHW.entity.AuthorEntity;
import com.SpringBoot.TestingHW.entity.bookEntity;
import com.SpringBoot.TestingHW.exceptions.ResourceNotFoundException;
import com.SpringBoot.TestingHW.repository.AuthorRepository;
import com.SpringBoot.TestingHW.repository.BookRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookService {
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final ModelMapper modelMapper;


    public List<BookDTO> getAllBooks() {
        log.info("Fetching all books");
        List<bookEntity> books = bookRepository.findAll();
        log.info("Successfully fetched all books");
        return books.stream()
                .map(element ->
                modelMapper.map(element, BookDTO.class))
                .collect(Collectors.toList());
    }

    public BookDTO getBookById(Long bookId) {
        log.info("Fetching book by id: {}",bookId);
        bookEntity book = bookRepository.findById(bookId)
                .orElseThrow(() ->{
                    log.error("Book not found by id: {}",bookId);
                    return new ResourceNotFoundException("Book not found by id:"+bookId);
                });
        return modelMapper.map(book, BookDTO.class);
    }

    public BookDTO createNewBook(@Valid BookDTO bookDto) {
        log.info("Creating new book with title: {}",bookDto.getTitle());
        bookDto.setTitle(bookDto.getTitle().toUpperCase());
        bookEntity savedBook = bookRepository.save(modelMapper.map(bookDto,bookEntity.class));
        log.info("Successfully created new book with title: {}",savedBook.getTitle());
        return modelMapper.map(savedBook, BookDTO.class);
    }

    public BookDTO updateBookById(Long id, @Valid BookDTO bookDto) {
        log.info("Updating book by id: {}",id);
        boolean isExists = bookRepository.existsById(id);
        if (!isExists){
            log.error("Book not found by id: {}",id);
            throw new ResourceNotFoundException("Book not found by id:"+id);
        }
        bookDto.setId(id);
        bookDto.setTitle(bookDto.getTitle().toUpperCase());
        log.info("Successfully updated book by id: {}",id);
        return modelMapper.map(
                bookRepository.save(modelMapper.map(bookDto, bookEntity.class)), BookDTO.class);
    }

    public void deleteBookById(Long bookId) {
        log.info("Deleting book by id: {}",bookId);
        boolean isExists = bookRepository.existsById(bookId);
        if (!isExists){
            log.error("Book not found by id: {}",bookId);
            throw new ResourceNotFoundException("Book not found by id:"+bookId);
        }
        log.info("Successfully Deleted book by id: {}",bookId);
        bookRepository.deleteById(bookId);
    }

    public List<BookDTO> getBooksPublishedAfterDate(LocalDate date) {
        log.info("Fetching books published after data: {}",date);
        List<bookEntity> books = bookRepository.findByPublishedOnAfter(date);
        log.info("Successfully fetched books published after date: {}",date);
        return books.stream()
                .map((element) ->
                modelMapper.map(element, BookDTO.class))
                .collect(Collectors.toList());

    }

    public List<BookDTO> getBooksByTitle(String title) {
        log.info("Fetching books by title: {}",title);
        List<bookEntity> books = bookRepository.findByTitle(title.toUpperCase());
        log.info("Successfully fetched books by title: {}",title);
        return books.stream().map((element) ->
                modelMapper.map(element, BookDTO.class))
                .collect(Collectors.toList());
    }

    public List<BookDTO> getBooksAuthoredBy(Long authorId) {
        log.info("Fetching books created by author id: {}",authorId);
        AuthorEntity author = authorRepository.findById(authorId)
                .orElseThrow(() -> {
                    log.error("Author not found by id: {}",authorId);
                    return new ResourceNotFoundException("Author not found by id:"+authorId);
                });
        List<bookEntity> books = bookRepository.findByAuthoredBy(author);
        log.info("Successfully fetched books by author id: {}",authorId);
        return books.stream().map((element) ->
                modelMapper.map(element, BookDTO.class))
                .collect(Collectors.toList());
    }

    public BookDTO assignAuthorToBook(Long bookId, Long authorId) {
        log.info("Assigning author with id: {} to book with id: {} ",authorId,bookId);
        // Step 1: Retrieve the existing book and author from the database
        bookEntity book = bookRepository.findById(bookId)
                .orElseThrow(() -> {
                    log.error("Book not found by id: {}",bookId);
                    return new ResourceNotFoundException("Book not found by id:"+bookId);
                });

        AuthorEntity author = authorRepository.findById(authorId)
                .orElseThrow(() -> {
                    log.error("Author not found by id: {}",authorId);
                    return new ResourceNotFoundException("Author not found by id:"+authorId);
                });

        // Step 2: Assign the author to the book
        book.setAuthoredBy(author);

        // Step 3: Save the updated book (and relationship) in the database
        bookEntity savedBook = bookRepository.save(book);
        log.info("Successfully Assigned author with id: {} to book with id: {} ",authorId,bookId);
        return modelMapper.map(savedBook,BookDTO.class);
    }
}
