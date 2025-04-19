package com.esprit.ms.blog.Controller;

import com.esprit.ms.blog.service.BlogAlreadyExistsException;
import com.esprit.ms.blog.Entity.Blog;
import com.esprit.ms.blog.Entity.Category;
import com.esprit.ms.blog.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/Blogs")
public class BlogRestAPI {

    @Autowired
    private BlogService blogService;

    @Autowired
    private TranslateService translateService;

    @PostMapping("/add")
    public ResponseEntity<Blog> addBlog(@RequestBody Blog blog) {
        try {
            Blog createdBlog = blogService.addBlog(blog);
            return ResponseEntity.ok(createdBlog);
        } catch (BlogAlreadyExistsException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Blog> getBlogById(@PathVariable Long id) {
        try {
            return ResponseEntity.of(blogService.getBlogById(id));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @GetMapping
    public ResponseEntity<Page<Blog>> getAllBlogs(@RequestParam(defaultValue = "0") int page,
                                                  @RequestParam(defaultValue = "5") int size) {
        Page<Blog> blogs = blogService.getAllBlogs(page, size);
        return ResponseEntity.ok(blogs);
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<Page<Blog>> getBlogsByCategory(@PathVariable Category category,
                                                         @RequestParam(defaultValue = "0") int page,
                                                         @RequestParam(defaultValue = "5") int size) {
        Page<Blog> blogs = blogService.getBlogsByCategory(category, page, size);
        return ResponseEntity.ok(blogs);
    }

    @GetMapping("/search/{title}")
    public ResponseEntity<Page<Blog>> searchBlogsByTitle(
            @PathVariable String title,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        return ResponseEntity.ok(blogService.searchBlogs(title, page, size));
    }


    @PutMapping("/{id}")
    public ResponseEntity<Blog> updateBlog(@PathVariable Long id, @RequestBody Blog updatedBlog) {
        try {
            return ResponseEntity.of(blogService.updateBlog(id, updatedBlog));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBlog(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(blogService.deleteBlog(id));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid ID provided.");
        }
    }
    @GetMapping("/translate/{id}/{lang}")
    public ResponseEntity<Blog> translateBlogContent(@PathVariable Long id, @PathVariable String lang) {
        return blogService.getBlogById(id)
                .map(blog -> {
                    String translatedTitle = translateService.translateText(blog.getTitle(), "en", lang);
                    String translatedContent = translateService.translateText(blog.getContent(), "en", lang);
                    String translatedAuthor = translateService.translateText(blog.getAuthor(), "en", lang);
                    String translatedCategory = translateService.translateText(blog.getCategory().name(), "en", lang);

                    Blog translatedBlog = new Blog();
                    translatedBlog.setId(blog.getId());
                    translatedBlog.setTitle(translatedTitle);
                    translatedBlog.setContent(translatedContent);
                    translatedBlog.setAuthor(translatedAuthor);

                    try {
                        translatedBlog.setCategory(Category.valueOf(translatedCategory));
                    } catch (IllegalArgumentException e) {
                        translatedBlog.setCategory(blog.getCategory());
                    }

                    return ResponseEntity.ok(translatedBlog);
                })
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }


}
