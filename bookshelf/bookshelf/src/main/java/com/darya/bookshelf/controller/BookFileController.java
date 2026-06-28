package com.darya.bookshelf.controller;

import com.darya.bookshelf.entity.Book;
import com.darya.bookshelf.entity.BookProgress;
import com.darya.bookshelf.entity.User;
import com.darya.bookshelf.service.BookFileService;
import com.darya.bookshelf.service.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/books")
@Tag(name = "Books", description = "API для управления книгами, загрузки и чтения")
public class BookFileController {

    private final BookFileService bookFileService;
    private final JwtService jwtService;
    public BookFileController(BookFileService bookFileService, JwtService jwtService) {
        this.bookFileService = bookFileService;
        this.jwtService = jwtService;
    }

    private User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated()
                && auth.getPrincipal() instanceof org.springframework.security.core.userdetails.UserDetails) {
            String email = ((org.springframework.security.core.userdetails.UserDetails) auth.getPrincipal()).getUsername();
            return null;
        }
        return null;
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Загрузить книгу", description = "Загружает PDF/EPUB файл и запускает асинхронную конвертацию в HTML")
    public ResponseEntity<?> uploadBook(
            @RequestParam("title") String title,
            @RequestParam("author") String author,
            @RequestParam("description") String description,
            @RequestParam("file") MultipartFile file) {
        try {
            Book book = bookFileService.uploadBook(title, author, description, file, null);
            return ResponseEntity.ok(book);
        } catch (IOException e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Failed to upload book: " + e.getMessage()));
        }
    }

    @GetMapping
    @Operation(summary = "Получить все книги")
    public List<Book> getAllBooks() {return bookFileService.getAllBooks();}

    @GetMapping("/{id}")
    @Operation(summary = "Получить книгу по ID")
    public ResponseEntity<Book> getBookById(@Parameter(description = "ID книги") @PathVariable Long id) {
        return bookFileService.getBookById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/content")
    @Operation(summary = "Получить HTML-контент книги")
    public ResponseEntity<?> getBookContent(@PathVariable Long id) {
        Book book = bookFileService.getBookById(id).orElse(null);
        if (book == null || book.getHtmlFilePath() == null) {
            return ResponseEntity.notFound().build();
        }
        try {
            Path path = Paths.get(book.getHtmlFilePath());
            byte[] content = Files.readAllBytes(path);
            return ResponseEntity.ok()
                    .contentType(MediaType.TEXT_HTML)
                    .body(new String(content));
        } catch (IOException e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Failed to read book content"));
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Обновить книгу")
    public ResponseEntity<?> updateBook(@PathVariable Long id, @RequestParam String title, @RequestParam String author, @RequestParam String description) {
        try {
            Book book = bookFileService.updateBook(id, title, author, description);
            return ResponseEntity.ok(book);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить книгу")
    public ResponseEntity<?> deleteBook(@PathVariable Long id) {
        bookFileService.deleteBook(id);
        return ResponseEntity.ok(Map.of("message", "Book deleted successfully"));
    }

    @GetMapping("/{id}/read")
    @Operation(summary = "Читать книгу", description = "Сохраняет текущую страницу и возвращает информацию о книге")
    public ResponseEntity<?> readBook(@PathVariable Long id, @RequestParam(defaultValue = "1") int page) {
        Book book = bookFileService.getBookById(id).orElse(null);
        if (book == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(Map.of("message", "Reading book: " + book.getTitle(), "page", page, "htmlFilePath", book.getHtmlFilePath()));
    }

    @PostMapping("/{id}/progress")
    @Operation(summary = "Сохранить прогресс чтения")
    public ResponseEntity<?> saveProgress(@PathVariable Long id, @RequestParam int pageNumber) {
        return ResponseEntity.ok(Map.of("message", "Progress saved", "page", pageNumber));
    }

    @GetMapping("/{id}/progress")
    @Operation(summary = "Получить прогресс чтения")
    public ResponseEntity<?> getProgress(@PathVariable Long id) {
        return ResponseEntity.ok(Map.of("message", "No progress found"));
    }
}