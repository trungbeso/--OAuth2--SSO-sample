package com.trungbeso.services;

import com.trungbeso.dto.BookCreateDto;
import com.trungbeso.entity.Book;
import com.trungbeso.repositories.IBookRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import static com.trungbeso.enums.BookType.EBOOK;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class BookService {

    IBookRepository bookRepository;

    public void save(BookCreateDto req) {
        var book = Book.builder()
                .title(req.getTitle())
                .author(req.getAuthor())
                .createDate(LocalDateTime.now())
                .lastModified(LocalDateTime.now())
                .bookType(EBOOK)
                .build();
        bookRepository.save(book);
    }

    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    public Book findByTitle(String title) {
        var book = bookRepository.findByTitle(title);
        if (book == null) {
            throw new RuntimeException("Book not found");
        }
        return book;
    }
}
