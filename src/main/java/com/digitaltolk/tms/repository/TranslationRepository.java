package com.digitaltolk.tms.repository;

import com.digitaltolk.tms.entity.Language;
import com.digitaltolk.tms.entity.Translation;
import com.digitaltolk.tms.entity.TranslationKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TranslationRepository extends JpaRepository<Translation, Long> {

    Optional<Translation> findByTranslationKeyAndLanguage(TranslationKey translationKey, Language language);

    Optional<Translation> findByTranslationKey_KeyNameAndLanguage_Code(String keyName, String languageCode);

    List<Translation> findByLanguage_Code(String languageCode);

    List<Translation> findByTranslationKey_KeyName(String keyName);

    @Query("SELECT t FROM Translation t WHERE t.translationKey.id = :keyId")
    List<Translation> findByTranslationKeyId(Long keyId);

    @Query("SELECT t FROM Translation t WHERE t.language.id = :languageId")
    List<Translation> findByLanguageId(Long languageId);

    @Query("SELECT t FROM Translation t WHERE t.content LIKE %:content%")
    List<Translation> findByContentContaining(String content);

    @Query("SELECT t FROM Translation t JOIN t.translationKey tk JOIN tk.tags tag WHERE tag.name = :tagName")
    List<Translation> findByTagName(String tagName);

    @Query("SELECT t FROM Translation t JOIN t.translationKey tk JOIN tk.tags tag WHERE tag.name IN :tagNames")
    List<Translation> findByTagNameIn(List<String> tagNames);

    @Query("SELECT t FROM Translation t JOIN t.translationKey tk JOIN tk.tags tag WHERE tag.name = :tagName AND t.language.code = :languageCode")
    List<Translation> findByTagNameAndLanguageCode(String tagName, String languageCode);

    // For JSON export - get all translations grouped by language
    @Query("SELECT t FROM Translation t JOIN FETCH t.translationKey JOIN FETCH t.language WHERE t.language.active = true")
    List<Translation> findAllWithKeyAndLanguage();

    @Query("SELECT t FROM Translation t JOIN FETCH t.translationKey JOIN FETCH t.language WHERE t.language.code = :languageCode")
    List<Translation> findByLanguageCodeWithKeyAndLanguage(String languageCode);

    // Search across multiple fields
    @Query("SELECT DISTINCT t FROM Translation t JOIN t.translationKey tk JOIN tk.tags tag " +
            "WHERE tk.keyName LIKE %:keyword% OR t.content LIKE %:keyword% OR tag.name LIKE %:keyword%")
    List<Translation> searchByKeyword(String keyword);

    // Check if translation exists
    boolean existsByTranslationKey_KeyNameAndLanguage_Code(String keyName, String languageCode);

    // Delete by key name and language code
    @Modifying
    @Query("DELETE FROM Translation t WHERE t.translationKey.keyName = :keyName AND t.language.code = :languageCode")
    void deleteByKeyNameAndLanguageCode(String keyName, String languageCode);
}

