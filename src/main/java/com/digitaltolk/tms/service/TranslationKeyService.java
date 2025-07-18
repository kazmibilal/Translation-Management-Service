package com.digitaltolk.tms.service;

import com.digitaltolk.tms.entity.Language;
import com.digitaltolk.tms.entity.Tag;
import com.digitaltolk.tms.entity.TranslationKey;
import com.digitaltolk.tms.repository.LanguageRepository;
import com.digitaltolk.tms.repository.TranslationKeyRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
public class TranslationKeyService {

    @Autowired
    TranslationKeyRepository translationKeyRepository;

    @Autowired
    TagService tagService;

    @Transactional(readOnly = true)
    public List<TranslationKey> getAllTranslationKeys() {
        return translationKeyRepository.findAllOrderByKeyName();
    }

    @Transactional(readOnly = true)
    public Optional<TranslationKey> getTranslationKeyByName(String keyName) {
        return translationKeyRepository.findByKeyName(keyName);
    }

    @Transactional(readOnly = true)
    public TranslationKey getTranslationKeyByNameOrThrow(String keyName) {
        return translationKeyRepository.findByKeyName(keyName)
                .orElseThrow(() -> new EntityNotFoundException("Translation key not found: " + keyName));
    }

    public TranslationKey createTranslationKey(String keyName, String description, List<String> tagNames) {
        if (translationKeyRepository.existsByKeyName(keyName)) {
            throw new IllegalArgumentException("Translation key already exists: " + keyName);
        }

        TranslationKey translationKey = new TranslationKey();
        translationKey.setKeyName(keyName);
        translationKey.setDescription(description);

        // Add tags if provided
        if (tagNames != null && !tagNames.isEmpty()) {
            Set<Tag> tags = new HashSet<>(tagService.getTagsByNames(tagNames));
            translationKey.setTags(tags);
        }

        return translationKeyRepository.save(translationKey);
    }

    public TranslationKey updateTranslationKey(Long id, String keyName, String description, List<String> tagNames) {
        TranslationKey translationKey = translationKeyRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Translation key not found with id: " + id));

        if (keyName != null && !keyName.equals(translationKey.getKeyName())) {
            if (translationKeyRepository.existsByKeyName(keyName)) {
                throw new IllegalArgumentException("Translation key already exists: " + keyName);
            }
            translationKey.setKeyName(keyName);
        }

        if (description != null) {
            translationKey.setDescription(description);
        }

        // Update tags if provided
        if (tagNames != null) {
            Set<Tag> tags = new HashSet<>(tagService.getTagsByNames(tagNames));
            translationKey.setTags(tags);
        }

        return translationKeyRepository.save(translationKey);
    }

    public void deleteTranslationKey(Long id) {
        if (!translationKeyRepository.existsById(id)) {
            throw new EntityNotFoundException("Translation key not found with id: " + id);
        }
        translationKeyRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<TranslationKey> getTranslationKeysByTag(String tagName) {
        return translationKeyRepository.findByTagName(tagName);
    }

    @Transactional(readOnly = true)
    public List<TranslationKey> searchTranslationKeys(String keyword) {
        return translationKeyRepository.findByKeyNameOrDescriptionContaining(keyword);
    }
}