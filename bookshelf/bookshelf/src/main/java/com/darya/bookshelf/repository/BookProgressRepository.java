package com.darya.bookshelf.repository;

import com.darya.bookshelf.entity.Book;
import com.darya.bookshelf.entity.BookProgress;
import com.darya.bookshelf.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface BookProgressRepository extends JpaRepository<BookProgress, Long> {
    Optional<BookProgress> findByUserAndBook(User user, Book book);
}