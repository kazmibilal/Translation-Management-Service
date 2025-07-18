package com.digitaltolk.tms.controller;

import com.digitaltolk.tms.entity.Translation;
import com.digitaltolk.tms.service.TranslationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/translations")
@CrossOrigin(origins = "*")
public class TranslationController {

    @Autowired
    TranslationService translationService;

    @GetMapping
    public ResponseEntity<List<Translation>> getAllTranslations() {
        List<Translation> translations = translationService.getAllTranslations();
        return ResponseEntity.ok(translations);
    }

    @GetMapping("/{keyName}/{languageCode}")
    public ResponseEntity<Translation> getTranslation(@PathVariable String keyName,
                                                      @PathVariable String languageCode) {
        Optional<Translation> translation = translationService.getTranslation(keyName, languageCode);
        return translation.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/by-language/{languageCode}")
    public ResponseEntity<List<Translation>> getTranslationsByLanguage(@PathVariable String languageCode) {
        List<Translation> translations = translationService.getTranslationsByLanguage(languageCode);
        return ResponseEntity.ok(translations);
    }

    @GetMapping("/by-key/{keyName}")
    public ResponseEntity<List<Translation>> getTranslationsByKey(@PathVariable String keyName) {
        List<Translation> translations = translationService.getTranslationsByKey(keyName);
        return ResponseEntity.ok(translations);
    }

    @GetMapping("/by-tag/{tagName}")
    public ResponseEntity<List<Translation>> getTranslationsByTag(@PathVariable String tagName) {
        List<Translation> translations = translationService.getTranslationsByTag(tagName);
        return ResponseEntity.ok(translations);
    }

    @GetMapping("/by-tag/{tagName}/language/{languageCode}")
    public ResponseEntity<List<Translation>> getTranslationsByTagAndLanguage(@PathVariable String tagName,
                                                                             @PathVariable String languageCode) {
        List<Translation> translations = translationService.getTranslationsByTagAndLanguage(tagName, languageCode);
        return ResponseEntity.ok(translations);
    }

    @PostMapping
    public ResponseEntity<Translation> createTranslation(@RequestBody TranslationRequest request) {
        try {
            Translation translation = translationService.createTranslation(
                    request.getKeyName(),
                    request.getLanguageCode(),
                    request.getContent()
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(translation);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{keyName}/{languageCode}")
    public ResponseEntity<Translation> updateTranslation(@PathVariable String keyName,
                                                         @PathVariable String languageCode,
                                                         @RequestBody TranslationUpdateRequest request) {
        try {
            Translation translation = translationService.updateTranslation(keyName, languageCode, request.getContent());
            return ResponseEntity.ok(translation);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/create-or-update")
    public ResponseEntity<Translation> createOrUpdateTranslation(@RequestBody TranslationRequest request) {
        try {
            Translation translation = translationService.createOrUpdateTranslation(
                    request.getKeyName(),
                    request.getLanguageCode(),
                    request.getContent()
            );
            return ResponseEntity.ok(translation);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{keyName}/{languageCode}")
    public ResponseEntity<Void> deleteTranslation(@PathVariable String keyName,
                                                  @PathVariable String languageCode) {
        try {
            translationService.deleteTranslation(keyName, languageCode);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<Translation>> searchTranslations(@RequestParam String keyword) {
        List<Translation> translations = translationService.searchTranslations(keyword);
        return ResponseEntity.ok(translations);
    }

    @GetMapping("/search-content")
    public ResponseEntity<List<Translation>> searchTranslationsByContent(@RequestParam String content) {
        List<Translation> translations = translationService.searchTranslationsByContent(content);
        return ResponseEntity.ok(translations);
    }

    // DTOs
    public static class TranslationRequest {
        private String keyName;
        private String languageCode;
        private String content;

        // getters and setters
        public String getKeyName() { return keyName; }
        public void setKeyName(String keyName) { this.keyName = keyName; }
        public String getLanguageCode() { return languageCode; }
        public void setLanguageCode(String languageCode) { this.languageCode = languageCode; }
        public String getContent() { return content; }
        public void setContent(String content) { this.content = content; }
    }

    public static class TranslationUpdateRequest {
        private String content;

        // getters and setters
        public String getContent() { return content; }
        public void setContent(String content) { this.content = content; }
    }
}