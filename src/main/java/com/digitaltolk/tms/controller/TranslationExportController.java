package com.digitaltolk.tms.controller;

import com.digitaltolk.tms.service.TranslationExportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/export")
@CrossOrigin(origins = "*")
public class TranslationExportController {

    @Autowired
    TranslationExportService exportService;

    @GetMapping("/translations")
    public ResponseEntity<Map<String, Map<String, String>>> exportAllTranslations() {
        Map<String, Map<String, String>> translations = exportService.exportAllTranslations();
        return ResponseEntity.ok(translations);
    }

    @GetMapping("/translations/{languageCode}")
    public ResponseEntity<Map<String, String>> exportTranslationsByLanguage(@PathVariable String languageCode) {
        Map<String, String> translations = exportService.exportTranslationsByLanguage(languageCode);
        return ResponseEntity.ok(translations);
    }

    @GetMapping("/translations/tag/{tagName}")
    public ResponseEntity<Map<String, Map<String, String>>> exportTranslationsByTag(@PathVariable String tagName) {
        Map<String, Map<String, String>> translations = exportService.exportTranslationsByTag(tagName);
        return ResponseEntity.ok(translations);
    }

    @GetMapping("/translations/tag/{tagName}/language/{languageCode}")
    public ResponseEntity<Map<String, String>> exportTranslationsByTagAndLanguage(@PathVariable String tagName,
                                                                                  @PathVariable String languageCode) {
        Map<String, String> translations = exportService.exportTranslationsByTagAndLanguage(tagName, languageCode);
        return ResponseEntity.ok(translations);
    }
}