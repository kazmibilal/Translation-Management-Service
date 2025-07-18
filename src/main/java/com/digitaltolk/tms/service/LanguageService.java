package com.digitaltolk.tms.service;

import com.digitaltolk.tms.entity.Language;
import com.digitaltolk.tms.entity.Tag;
import com.digitaltolk.tms.repository.LanguageRepository;
import com.digitaltolk.tms.repository.TagRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class LanguageService {

    @Autowired
    LanguageRepository languageRepository;

    @Transactional(readOnly = true)
    public List<Language> getAllLanguages() {
        return languageRepository.findAllActiveOrderByName();
    }

    @Transactional(readOnly = true)
    public Optional<Language> getLanguageByCode(String code) {
        return languageRepository.findByCode(code);
    }

    @Transactional(readOnly = true)
    public Language getLanguageByCodeOrThrow(String code) {
        return languageRepository.findByCode(code)
                .orElseThrow(() -> new EntityNotFoundException("Language not found with code: " + code));
    }

    public Language createLanguage(String code, String name) {
        if (languageRepository.existsByCode(code)) {
            throw new IllegalArgumentException("Language already exists with code: " + code);
        }

        Language language = new Language();
        language.setCode(code.toLowerCase());
        language.setName(name);
        language.setActive(true);

        return languageRepository.save(language);
    }

    public Language updateLanguage(Long id, String name, Boolean active) {
        Language language = languageRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Language not found with id: " + id));

        if (name != null) {
            language.setName(name);
        }
        if (active != null) {
            language.setActive(active);
        }

        return languageRepository.save(language);
    }

    public void deleteLanguage(Long id) {
        if (!languageRepository.existsById(id)) {
            throw new EntityNotFoundException("Language not found with id: " + id);
        }
        languageRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<Language> getActiveLanguages() {
        return languageRepository.findByActiveTrue();
    }
}