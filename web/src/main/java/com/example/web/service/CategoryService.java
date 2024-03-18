package com.example.web.service;

import com.example.web.pojo.Category;

import java.util.List;

public interface CategoryService {

    //新增文章
    void add(Category category);


    List<Category> list();

    Category findById(Integer id);

    void update(Category category);

    void delete(Integer id);
}
