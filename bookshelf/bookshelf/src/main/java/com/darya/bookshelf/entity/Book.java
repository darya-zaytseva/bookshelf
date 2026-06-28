package com.darya.bookshelf.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "books")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String author;
    @Column(length = 2000)
    private String description;
    @Column(name = "file_path")
    private String filePath;
    @Column(name = "html_file_path")
    private String htmlFilePath;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uploaded_by")
    private User uploadedBy;
    public Book() {}
    public Book(Long id, String title, String author, String description) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.description = description;
    }
    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}
    public String getTitle() {return title;}
    public void setTitle(String title) {this.title = title;}
    public String getAuthor() {return author;}
    public void setAuthor(String author) {this.author = author;}
    public String getDescription() {return description;}
    public void setDescription(String description) {this.description = description;}
    public String getFilePath() {return filePath;}
    public void setFilePath(String filePath) {this.filePath = filePath;}
    public String getHtmlFilePath() {return htmlFilePath;}
    public void setHtmlFilePath(String htmlFilePath) {this.htmlFilePath = htmlFilePath;}
    public User getUploadedBy() {return uploadedBy;}
    public void setUploadedBy(User uploadedBy) {this.uploadedBy = uploadedBy;}
}