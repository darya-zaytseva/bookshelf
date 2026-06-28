package com.darya.bookshelf.repository;

import com.darya.bookshelf.entity.Shelf;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ShelfRepository extends JpaRepository<Shelf, Long> {
    List<Shelf> findByUserId(Long userId);
    Optional<Shelf> findByIdAndUserId(Long id, Long userId);
}