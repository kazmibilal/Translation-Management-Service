package com.digitaltolk.tms.controller;

import com.digitaltolk.tms.entity.Language;
import com.digitaltolk.tms.service.LanguageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/languages")
@CrossOrigin(origins = "*")
public class LanguageController {

    @Autowired
    LanguageService languageService;

    @GetMapping
    public ResponseEntity<List<Language>> getAllLanguages() {
        List<Language> languages = languageService.getAllLanguages();
        return ResponseEntity.ok(languages);
    }

    @GetMapping("/{code}")
    public ResponseEntity<Language> getLanguageByCode(@PathVariable String code) {
        Optional<Language> language = languageService.getLanguageByCode(code);
        return language.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Language> createLanguage(@RequestBody LanguageRequest request) {
        try {
            Language language = languageService.createLanguage(request.getCode(), request.getName());
            return ResponseEntity.status(HttpStatus.CREATED).body(language);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Language> updateLanguage(@PathVariable Long id,
                                                   @RequestBody LanguageUpdateRequest request) {
        try {
            Language language = languageService.updateLanguage(id, request.getName(), request.getActive());
            return ResponseEntity.ok(language);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLanguage(@PathVariable Long id) {
        try {
            languageService.deleteLanguage(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/active")
    public ResponseEntity<List<Language>> getActiveLanguages() {
        List<Language> languages = languageService.getActiveLanguages();
        return ResponseEntity.ok(languages);
    }

    // DTOs
    public static class LanguageRequest {
        private String code;
        private String name;

        // getters and setters
        public String getCode() { return code; }
        public void setCode(String code) { this.code = code; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
    }

    public static class LanguageUpdateRequest {
        private String name;
        private Boolean active;

        // getters and setters
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public Boolean getActive() { return active; }
        public void setActive(Boolean active) { this.active = active; }
    }
}
