package com.darya.bookshelf.service;

import com.darya.bookshelf.entity.Book;
import com.darya.bookshelf.entity.BookProgress;
import com.darya.bookshelf.entity.User;
import com.darya.bookshelf.repository.BookRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
public class BookFileService {

    private final BookRepository bookRepository;
    private final BookConversionService conversionService;
    private final BookProgressService progressService;
    public BookFileService(BookRepository bookRepository, BookConversionService conversionService, BookProgressService progressService) {
        this.bookRepository = bookRepository;
        this.conversionService = conversionService;
        this.progressService = progressService;
    }

    public Book uploadBook(String title, String author, String description, MultipartFile file, User uploadedBy) throws IOException {
        Book book = new Book();
        book.setTitle(title);
        book.setAuthor(author);
        book.setDescription(description);
        book.setUploadedBy(uploadedBy);
        bookRepository.save(book);
        byte[] fileContent = file.getBytes();
        CompletableFuture<String> future = conversionService.convertToHtml(file.getOriginalFilename(), fileContent);
        future.thenAccept(path -> {
            book.setFilePath("books/" + file.getOriginalFilename());
            book.setHtmlFilePath(path);
            bookRepository.save(book);
            System.out.println("Book converted to HTML: " + path);
        }).exceptionally(ex -> {
            System.err.println("Conversion failed: " + ex.getMessage());
            return null;
        });
        return book;
    }

    public Book updateBook(Long id, String title, String author, String description) {
        Book book = bookRepository.findById(id).orElseThrow(() -> new RuntimeException("Book not found"));
        book.setTitle(title);
        book.setAuthor(author);
        book.setDescription(description);
        return bookRepository.save(book);
    }

    public void deleteBook(Long id) {bookRepository.deleteById(id);}
    public BookProgress saveProgress(User user, Book book, int pageNumber) {return progressService.saveProgress(user, book, pageNumber);}
    public BookProgress getProgress(User user, Book book) {return progressService.getProgress(user, book);}
    public List<Book> getAllBooks() {return bookRepository.findAll();}
    public Optional<Book> getBookById(Long id) {return bookRepository.findById(id);}
}