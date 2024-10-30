package com.SpringBoot.TestingHW.controller;

import com.SpringBoot.TestingHW.dto.BookDTO;
import com.SpringBoot.TestingHW.service.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(path = "/book")
@RequiredArgsConstructor
@Slf4j
public class BookController {
    private final BookService bookService;

    @GetMapping
    public ResponseEntity<List<BookDTO>> getAllBooks(){
        return ResponseEntity.ok(bookService.getAllBooks());
    }

    @GetMapping("/{bookId}")
    public ResponseEntity<BookDTO> getBookById(@PathVariable Long bookId){
        return ResponseEntity.ok(bookService.getBookById(bookId));
    }

    @PostMapping
    public ResponseEntity<BookDTO> createNewBook(@RequestBody @Valid BookDTO bookDto){
        return new ResponseEntity(bookService.createNewBook(bookDto), HttpStatus.CREATED);
    }

    @PutMapping("/{bookId}")
    public ResponseEntity<BookDTO> updateBookById(@RequestBody @Valid BookDTO bookDto, @PathVariable Long bookId){
        return ResponseEntity.ok(bookService.updateBookById(bookId,bookDto));
    }

    @DeleteMapping("/{bookId}")
    public ResponseEntity<?> deleteBookById(@PathVariable Long bookId){
        bookService.deleteBookById(bookId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/getAfterDate/{date}")
    public ResponseEntity<List<BookDTO>> getBooksPublishedAfterDate(@PathVariable LocalDate date){
        return ResponseEntity.ok(bookService.getBooksPublishedAfterDate(date));
    }

    @GetMapping("/title/{title}")
    public ResponseEntity<List<BookDTO>> getBooksByTitle(@PathVariable String title){
        return ResponseEntity.ok(bookService.getBooksByTitle(title));
    }

    @GetMapping("/createdBy/{authorId}")
    public ResponseEntity<List<BookDTO>> getBookByAuthor(@PathVariable Long authorId){
        List<BookDTO> bookDTOS = bookService.getBooksAuthoredBy(authorId);
        log.info("Fetched books:{}",bookDTOS);
        return ResponseEntity.ok(bookDTOS);
    }

    @PutMapping("{bookId}/assignAuthorToBook/{authorId}")
    public ResponseEntity<BookDTO> assignAuthorToBook(@PathVariable Long bookId,@PathVariable Long authorId){
        return ResponseEntity.ok(bookService.assignAuthorToBook(bookId,authorId));
    }

}
