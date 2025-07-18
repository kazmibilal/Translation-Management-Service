package com.digitaltolk.tms.service;

import com.digitaltolk.tms.entity.Translation;
import com.digitaltolk.tms.repository.TranslationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class TranslationExportService {

    @Autowired
    TranslationRepository translationRepository;

    @Autowired
    LanguageService languageService;

    public Map<String, Map<String, String>> exportAllTranslations() {
        List<Translation> translations = translationRepository.findAllWithKeyAndLanguage();
        return buildTranslationMap(translations);
    }

    public Map<String, String> exportTranslationsByLanguage(String languageCode) {
        List<Translation> translations = translationRepository.findByLanguageCodeWithKeyAndLanguage(languageCode);
        return translations.stream()
                .collect(Collectors.toMap(
                        t -> t.getTranslationKey().getKeyName(),
                        Translation::getContent,
                        (existing, replacement) -> replacement
                ));
    }

    public Map<String, Map<String, String>> exportTranslationsByTag(String tagName) {
        List<Translation> translations = translationRepository.findByTagName(tagName);
        return buildTranslationMap(translations);
    }

    public Map<String, String> exportTranslationsByTagAndLanguage(String tagName, String languageCode) {
        List<Translation> translations = translationRepository.findByTagNameAndLanguageCode(tagName, languageCode);
        return translations.stream()
                .collect(Collectors.toMap(
                        t -> t.getTranslationKey().getKeyName(),
                        Translation::getContent,
                        (existing, replacement) -> replacement
                ));
    }

    private Map<String, Map<String, String>> buildTranslationMap(List<Translation> translations) {
        return translations.stream()
                .collect(Collectors.groupingBy(
                        t -> t.getLanguage().getCode(),
                        Collectors.toMap(
                                t -> t.getTranslationKey().getKeyName(),
                                Translation::getContent,
                                (existing, replacement) -> replacement
                        )
                ));
    }
}
