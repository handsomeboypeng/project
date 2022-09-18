package com.example.springbootforvue.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity //把Book和数据库中的Book表绑定起来
@Data
public class Book {
    @Id //主键绑定
    @GeneratedValue(strategy = GenerationType.IDENTITY) //id自增
    private Integer id;
    private String name;
    private String author;
}
