package com.trungbeso.controller;

import com.trungbeso.dto.BookCreateDto;
import com.trungbeso.entity.Book;
import com.trungbeso.services.BookService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/books")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class BookController {

    BookService bookService;

    @PostMapping("/create")
    public ResponseEntity<?> createBook(@RequestBody BookCreateDto red) {
        var book = bookService.findByTitle(red.getTitle());
        if (book != null) {
            return ResponseEntity.badRequest().body(HttpStatus.CONFLICT);
        }
        bookService.save(red);
        return ResponseEntity.ok().body(HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Book>> findAllBook() {
        return ResponseEntity.ok(bookService.findAll());
    }
}
