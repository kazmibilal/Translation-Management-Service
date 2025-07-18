package com.digitaltolk.tms.repository;

import com.digitaltolk.tms.entity.TranslationKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TranslationKeyRepository extends JpaRepository<TranslationKey, Long> {

    Optional<TranslationKey> findByKeyName(String keyName);

    boolean existsByKeyName(String keyName);

    @Query("SELECT tk FROM TranslationKey tk JOIN tk.tags t WHERE t.name = :tagName")
    List<TranslationKey> findByTagName(String tagName);

    @Query("SELECT tk FROM TranslationKey tk JOIN tk.tags t WHERE t.name IN :tagNames")
    List<TranslationKey> findByTagNameIn(List<String> tagNames);

    @Query("SELECT tk FROM TranslationKey tk WHERE tk.keyName LIKE %:keyword% OR tk.description LIKE %:keyword%")
    List<TranslationKey> findByKeyNameOrDescriptionContaining(String keyword);

    @Query("SELECT tk FROM TranslationKey tk ORDER BY tk.keyName")
    List<TranslationKey> findAllOrderByKeyName();

    @Query("SELECT tk FROM TranslationKey tk JOIN tk.tags t WHERE t.id IN :tagIds")
    List<TranslationKey> findByTagIds(List<Long> tagIds);
}
