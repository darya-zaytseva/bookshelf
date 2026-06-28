package com.darya.bookshelf.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "verification_tokens")
public class VerificationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String token;
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    public VerificationToken() {}
    public VerificationToken(String token, User user) {
        this.token = token;
        this.user = user;
    }
    public Long getId() {return id;}
    public String getToken() {return token;}
    public User getUser() {return user;}
    public void setToken(String token) {this.token = token;}
    public void setUser(User user) {this.user = user;}
}