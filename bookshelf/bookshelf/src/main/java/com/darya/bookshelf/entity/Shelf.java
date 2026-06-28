package com.darya.bookshelf.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "shelves")
public class Shelf {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "shelf_books", joinColumns = @JoinColumn(name = "shelf_id"), inverseJoinColumns = @JoinColumn(name = "book_id"))
    private List<Book> books = new ArrayList<>();
    public Shelf() {}

    public Shelf(String name, User user) {
        this.name = name;
        this.user = user;
    }

    @PrePersist
    public void prePersist() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }
    public Long getId() {return id;}
    public String getName() {return name;}
    public void setName(String name) {this.name = name;}
    public LocalDateTime getCreatedAt() {return createdAt;}
    public void setCreatedAt(LocalDateTime createdAt) {this.createdAt = createdAt;}
    public User getUser() {return user;}
    public void setUser(User user) {this.user = user;}
    public List<Book> getBooks() {return books;}
    public void setBooks(List<Book> books) {this.books = books;}
}