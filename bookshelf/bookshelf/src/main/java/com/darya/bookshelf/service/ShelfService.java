package com.darya.bookshelf.service;

import com.darya.bookshelf.entity.Book;
import com.darya.bookshelf.entity.Shelf;
import com.darya.bookshelf.entity.User;
import com.darya.bookshelf.repository.BookRepository;
import com.darya.bookshelf.repository.ShelfRepository;
import com.darya.bookshelf.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class ShelfService {

    private final ShelfRepository shelfRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;

    public ShelfService(ShelfRepository shelfRepository,
                        UserRepository userRepository,
                        BookRepository bookRepository) {
        this.shelfRepository = shelfRepository;
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
    }

    @Transactional
    public Shelf createShelf(Long userId, String name) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Shelf shelf = new Shelf(name, user);
        return shelfRepository.save(shelf);
    }

    public List<Shelf> getUserShelves(Long userId) {
        return shelfRepository.findByUserId(userId);
    }

    public Shelf getShelf(Long shelfId, Long userId) {
        return shelfRepository.findByIdAndUserId(shelfId, userId)
                .orElseThrow(() -> new RuntimeException("Shelf not found"));
    }

    @Transactional
    public Shelf addBookToShelf(Long shelfId, Long bookId, Long userId) {
        Shelf shelf = getShelf(shelfId, userId);
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found"));
        if (!shelf.getBooks().contains(book)) {
            shelf.getBooks().add(book);
        }
        return shelfRepository.save(shelf);
    }

    @Transactional
    public void removeBookFromShelf(Long shelfId, Long bookId, Long userId) {
        Shelf shelf = getShelf(shelfId, userId);
        shelf.getBooks().removeIf(book -> book.getId().equals(bookId));
        shelfRepository.save(shelf);
    }

    @Transactional
    public void deleteShelf(Long shelfId, Long userId) {
        Shelf shelf = getShelf(shelfId, userId);
        shelfRepository.delete(shelf);
    }
}