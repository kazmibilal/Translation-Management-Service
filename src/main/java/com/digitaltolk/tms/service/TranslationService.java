package com.digitaltolk.tms.service;

import com.digitaltolk.tms.entity.Language;
import com.digitaltolk.tms.entity.Translation;
import com.digitaltolk.tms.entity.TranslationKey;
import com.digitaltolk.tms.repository.TranslationRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TranslationService {

    @Autowired
    TranslationRepository translationRepository;

    @Autowired
    TranslationKeyService translationKeyService;

    @Autowired
    private final LanguageService languageService;

    public TranslationService(TranslationRepository translationRepository,
                              TranslationKeyService translationKeyService,
                              LanguageService languageService) {
        this.translationRepository = translationRepository;
        this.translationKeyService = translationKeyService;
        this.languageService = languageService;
    }

    @Transactional(readOnly = true)
    public List<Translation> getAllTranslations() {
        return translationRepository.findAllWithKeyAndLanguage();
    }

    @Transactional(readOnly = true)
    public Optional<Translation> getTranslation(String keyName, String languageCode) {
        return translationRepository.findByTranslationKey_KeyNameAndLanguage_Code(keyName, languageCode);
    }

    @Transactional(readOnly = true)
    public List<Translation> getTranslationsByLanguage(String languageCode) {
        return translationRepository.findByLanguageCodeWithKeyAndLanguage(languageCode);
    }

    @Transactional(readOnly = true)
    public List<Translation> getTranslationsByKey(String keyName) {
        return translationRepository.findByTranslationKey_KeyName(keyName);
    }

    @Transactional(readOnly = true)
    public List<Translation> getTranslationsByTag(String tagName) {
        return translationRepository.findByTagName(tagName);
    }

    @Transactional(readOnly = true)
    public List<Translation> getTranslationsByTagAndLanguage(String tagName, String languageCode) {
        return translationRepository.findByTagNameAndLanguageCode(tagName, languageCode);
    }

    public Translation createTranslation(String keyName, String languageCode, String content) {
        TranslationKey translationKey = translationKeyService.getTranslationKeyByNameOrThrow(keyName);
        Language language = languageService.getLanguageByCodeOrThrow(languageCode);

        // Check if translation already exists
        if (translationRepository.existsByTranslationKey_KeyNameAndLanguage_Code(keyName, languageCode)) {
            throw new IllegalArgumentException("Translation already exists for key: " + keyName + " and language: " + languageCode);
        }

        Translation translation = new Translation();
        translation.setTranslationKey(translationKey);
        translation.setLanguage(language);
        translation.setContent(content);

        return translationRepository.save(translation);
    }

    public Translation updateTranslation(String keyName, String languageCode, String content) {
        Translation translation = translationRepository.findByTranslationKey_KeyNameAndLanguage_Code(keyName, languageCode)
                .orElseThrow(() -> new EntityNotFoundException("Translation not found for key: " + keyName + " and language: " + languageCode));

        translation.setContent(content);
        return translationRepository.save(translation);
    }

    public Translation createOrUpdateTranslation(String keyName, String languageCode, String content) {
        Optional<Translation> existingTranslation = translationRepository.findByTranslationKey_KeyNameAndLanguage_Code(keyName, languageCode);

        if (existingTranslation.isPresent()) {
            Translation translation = existingTranslation.get();
            translation.setContent(content);
            return translationRepository.save(translation);
        } else {
            return createTranslation(keyName, languageCode, content);
        }
    }

    public void deleteTranslation(String keyName, String languageCode) {
        if (!translationRepository.existsByTranslationKey_KeyNameAndLanguage_Code(keyName, languageCode)) {
            throw new EntityNotFoundException("Translation not found for key: " + keyName + " and language: " + languageCode);
        }
        translationRepository.deleteByKeyNameAndLanguageCode(keyName, languageCode);
    }

    @Transactional(readOnly = true)
    public List<Translation> searchTranslations(String keyword) {
        return translationRepository.searchByKeyword(keyword);
    }

    @Transactional(readOnly = true)
    public List<Translation> searchTranslationsByContent(String content) {
        return translationRepository.findByContentContaining(content);
    }
}
