package com.esprit.ms.blog.Entity;

import jakarta.persistence.*;
import java.util.Date;

@Entity
public class Blog {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String title;
	private String content;
	private String author;

	@Enumerated(EnumType.STRING)
	private Category category;

	@Temporal(TemporalType.TIMESTAMP)
	private Date createdAt = new Date();

	public Blog() {}

	public Blog(String title, String content, String author, Category category) {
		this.title = title;
		this.content = content;
		this.author = author;
		this.category = category;
	}

	public Long getId() { return id; }
	public void setId(Long id) { this.id = id; }
	public String getTitle() { return title; }
	public void setTitle(String title) { this.title = title; }
	public String getContent() { return content; }
	public void setContent(String content) { this.content = content; }
	public String getAuthor() { return author; }
	public void setAuthor(String author) { this.author = author; }
	public Category getCategory() { return category; }
	public void setCategory(Category category) { this.category = category; }
	public Date getCreatedAt() { return createdAt; }

}
