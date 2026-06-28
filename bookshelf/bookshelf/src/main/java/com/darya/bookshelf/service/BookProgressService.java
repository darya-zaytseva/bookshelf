package com.darya.bookshelf.service;

import com.darya.bookshelf.entity.Book;
import com.darya.bookshelf.entity.BookProgress;
import com.darya.bookshelf.entity.User;
import com.darya.bookshelf.repository.BookProgressRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

@Service
@Transactional
public class BookProgressService {

    private final BookProgressRepository progressRepository;
    public BookProgressService(BookProgressRepository progressRepository) {this.progressRepository = progressRepository;}

    public BookProgress saveProgress(User user, Book book, int pageNumber) {
        BookProgress progress = progressRepository.findByUserAndBook(user, book).orElseGet(() -> {
                    BookProgress newProgress = new BookProgress();
                    newProgress.setUser(user);
                    newProgress.setBook(book);
                    return newProgress;
                });
        progress.setPageNumber(pageNumber);
        progress.setLastReadAt(LocalDateTime.now());
        return progressRepository.save(progress);
    }

    @Transactional(readOnly = true)
    public BookProgress getProgress(User user, Book book) {return progressRepository.findByUserAndBook(user, book).orElse(null);}
}