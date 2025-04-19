package com.esprit.ms.blog.Repository;

import com.esprit.ms.blog.Entity.Category;
import com.esprit.ms.blog.Entity.Blog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlogRepository extends JpaRepository<Blog, Long> {

    Page<Blog> findByCategory(Category category, Pageable pageable);
    Page<Blog> findByTitleContainingIgnoreCase(String title, Pageable pageable);
}
