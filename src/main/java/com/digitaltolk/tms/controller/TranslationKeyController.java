package com.digitaltolk.tms.controller;

import com.digitaltolk.tms.entity.TranslationKey;
import com.digitaltolk.tms.service.TranslationKeyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/translation-keys")
@CrossOrigin(origins = "*")
public class TranslationKeyController {

    @Autowired
    TranslationKeyService translationKeyService;

    public TranslationKeyController(TranslationKeyService translationKeyService) {
        this.translationKeyService = translationKeyService;
    }

    @GetMapping
    public ResponseEntity<List<TranslationKey>> getAllTranslationKeys() {
        List<TranslationKey> keys = translationKeyService.getAllTranslationKeys();
        return ResponseEntity.ok(keys);
    }

    @GetMapping("/{keyName}")
    public ResponseEntity<TranslationKey> getTranslationKeyByName(@PathVariable String keyName) {
        Optional<TranslationKey> key = translationKeyService.getTranslationKeyByName(keyName);
        return key.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<TranslationKey> createTranslationKey(@RequestBody TranslationKeyRequest request) {
        try {
            TranslationKey key = translationKeyService.createTranslationKey(
                    request.getKeyName(),
                    request.getDescription(),
                    request.getTagNames()
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(key);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<TranslationKey> updateTranslationKey(@PathVariable Long id,
                                                               @RequestBody TranslationKeyUpdateRequest request) {
        try {
            TranslationKey key = translationKeyService.updateTranslationKey(
                    id,
                    request.getKeyName(),
                    request.getDescription(),
                    request.getTagNames()
            );
            return ResponseEntity.ok(key);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTranslationKey(@PathVariable Long id) {
        try {
            translationKeyService.deleteTranslationKey(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/by-tag/{tagName}")
    public ResponseEntity<List<TranslationKey>> getTranslationKeysByTag(@PathVariable String tagName) {
        List<TranslationKey> keys = translationKeyService.getTranslationKeysByTag(tagName);
        return ResponseEntity.ok(keys);
    }

    @GetMapping("/search")
    public ResponseEntity<List<TranslationKey>> searchTranslationKeys(@RequestParam String keyword) {
        List<TranslationKey> keys = translationKeyService.searchTranslationKeys(keyword);
        return ResponseEntity.ok(keys);
    }

    // DTOs
    public static class TranslationKeyRequest {
        private String keyName;
        private String description;
        private List<String> tagNames;

        // getters and setters
        public String getKeyName() { return keyName; }
        public void setKeyName(String keyName) { this.keyName = keyName; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public List<String> getTagNames() { return tagNames; }
        public void setTagNames(List<String> tagNames) { this.tagNames = tagNames; }
    }

    public static class TranslationKeyUpdateRequest {
        private String keyName;
        private String description;
        private List<String> tagNames;

        // getters and setters
        public String getKeyName() { return keyName; }
        public void setKeyName(String keyName) { this.keyName = keyName; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public List<String> getTagNames() { return tagNames; }
        public void setTagNames(List<String> tagNames) { this.tagNames = tagNames; }
    }
}