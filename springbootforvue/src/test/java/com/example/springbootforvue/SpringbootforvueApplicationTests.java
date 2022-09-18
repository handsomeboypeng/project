package com.example.springbootforvue;

import com.example.springbootforvue.entity.Book;
import com.example.springbootforvue.repository.BookRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SpringbootforvueApplicationTests {
    @Autowired
    private BookRepository repository;

    @Test
    void contextLoads() {
    }

    @Test
    void save() {
        Book book = new Book();
        book.setName("spring boot");
        book.setAuthor("sam");
        Book newBook = repository.save(book);
        System.out.println(newBook);
    }

}
