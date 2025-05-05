package com.trungbeso.dto;

import com.trungbeso.enums.BookType;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookCreateDto {
    String title;

    String author;

    LocalDateTime createDate;

    LocalDateTime lastModified;

    BookType bookType;
}
