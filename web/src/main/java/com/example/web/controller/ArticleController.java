package com.example.web.controller;

import com.example.web.pojo.Article;
import com.example.web.pojo.PageBean;
import com.example.web.pojo.Result;
import com.example.web.service.ArticleService;
import com.example.web.utils.JwtUtil;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.tomcat.util.http.parser.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/article")
public class ArticleController {
     @Autowired
    private ArticleService articleService;

     @PostMapping
    public Result add(@RequestBody @Validated(Article.Add.class) Article article){
         articleService.add(article);
         return Result.success();
     }
     @GetMapping("/detail")
    public Result<Article> detail(Integer id){
         Article a=articleService.findById(id);
         return Result.success(a);
     }

     @PutMapping
    public Result update(@RequestBody @Validated(Article.Update.class) Article article){
         articleService.update(article);
         return Result.success();
     }

     @DeleteMapping
    public Result delete(Integer id){
         articleService.delete(id);
         return Result.success();
     }

     @GetMapping
    public  Result<PageBean<Article>> list(
            Integer pageNum,
            Integer pageSize,
            @RequestParam(required = false) Integer categoryId,
            @RequestParam(required = false) String state
     ){

         PageBean<Article> pb=articleService.list(pageNum,pageSize,categoryId,state);
         return Result.success(pb);
     }
}
