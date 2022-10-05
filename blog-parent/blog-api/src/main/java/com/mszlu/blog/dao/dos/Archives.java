package com.mszlu.blog.dao.dos;

import lombok.Data;

@Data
public class Archives {
    //返回文章归档的时候三个值这三个是在数据库中没有的，也就是无需持久化的
    private Integer year;

    private Integer month;

    private Long count;
}
