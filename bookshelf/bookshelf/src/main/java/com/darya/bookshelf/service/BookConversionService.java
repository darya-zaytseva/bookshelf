package com.darya.bookshelf.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.CompletableFuture;

@Service
public class BookConversionService {

    private static final String BOOKS_DIR = "books";
    private static final String HTML_DIR = "books/html";
    public BookConversionService() {
        new File(BOOKS_DIR).mkdirs();
        new File(HTML_DIR).mkdirs();
    }

    @Async
    public CompletableFuture<String> convertToHtml(String originalFilename, byte[] fileContent) {
        try {
            Path originalPath = Paths.get(BOOKS_DIR, originalFilename);
            Files.write(originalPath, fileContent);
            String htmlContent = "<html><body><h1>" + originalFilename + "</h1>" + "<p>Book content converted to HTML...</p>" + "</body></html>";
            String htmlFilename = originalFilename.substring(0, originalFilename.lastIndexOf('.')) + ".html";
            Path htmlPath = Paths.get(HTML_DIR, htmlFilename);
            Files.write(htmlPath, htmlContent.getBytes());
            Thread.sleep(2000);
            return CompletableFuture.completedFuture(htmlPath.toString());
        } catch (IOException | InterruptedException e) {
            return CompletableFuture.failedFuture(e);
        }
    }
}