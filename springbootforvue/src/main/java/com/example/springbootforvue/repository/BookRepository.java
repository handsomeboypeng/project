package com.example.springbootforvue.repository;

import com.example.springbootforvue.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book,Integer> {
}
