package com.example.web.service;

import com.example.web.pojo.Article;
import com.example.web.pojo.PageBean;

public interface ArticleService {
    void add(Article article);

    Article findById(Integer id);

    void update(Article article);

    void delete(Integer id);

    PageBean<Article> list(Integer pageNum, Integer pageSize, Integer categoryId, String state);
}
