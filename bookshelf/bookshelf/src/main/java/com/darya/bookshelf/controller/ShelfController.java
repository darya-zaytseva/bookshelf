package com.darya.bookshelf.controller;

import com.darya.bookshelf.entity.Shelf;
import com.darya.bookshelf.entity.User;
import com.darya.bookshelf.service.ShelfService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/shelves")
@Tag(name = "Shelves", description = "API для управления полками и книгами на них")
public class ShelfController {

    private final ShelfService shelfService;
    public ShelfController(ShelfService shelfService) {this.shelfService = shelfService;}

    private User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && auth.getPrincipal() instanceof org.springframework.security.core.userdetails.UserDetails) {
            String email = ((org.springframework.security.core.userdetails.UserDetails) auth.getPrincipal()).getUsername();
            return null;
        }
        return null;
    }

    @PostMapping
    @Operation(summary = "Создать полку")
    public ResponseEntity<?> createShelf(@RequestParam String name) {
        // User user = getCurrentUser();
        // if (user == null) return ResponseEntity.status(401).body(Map.of("error", "Unauthorized"));
        // Shelf shelf = shelfService.createShelf(user.getId(), name);
        return ResponseEntity.ok(Map.of("message", "Shelf created"));
    }

    @GetMapping
    @Operation(summary = "Получить мои полки")
    public ResponseEntity<?> getMyShelves() {
        // User user = getCurrentUser();
        // if (user == null) return ResponseEntity.status(401).body(Map.of("error", "Unauthorized"));
        // List<Shelf> shelves = shelfService.getUserShelves(user.getId());
        return ResponseEntity.ok(List.of());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить полку по ID")
    public ResponseEntity<?> getShelf(@PathVariable Long id) {
        return ResponseEntity.ok(Map.of("message", "Shelf retrieved"));
    }

    @PostMapping("/{shelfId}/books/{bookId}")
    @Operation(summary = "Добавить книгу на полку")
    public ResponseEntity<?> addBook(@PathVariable Long shelfId, @PathVariable Long bookId) {
        return ResponseEntity.ok(Map.of("message", "Book added to shelf"));
    }

    @DeleteMapping("/{shelfId}/books/{bookId}")
    @Operation(summary = "Удалить книгу с полки")
    public ResponseEntity<?> removeBook(@PathVariable Long shelfId, @PathVariable Long bookId) {
        return ResponseEntity.ok(Map.of("message", "Book removed from shelf"));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить полку")
    public ResponseEntity<?> deleteShelf(@PathVariable Long id) {
        return ResponseEntity.ok(Map.of("message", "Shelf deleted"));
    }
}