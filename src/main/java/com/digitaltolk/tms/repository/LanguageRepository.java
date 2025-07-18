package com.digitaltolk.tms.repository;

import com.digitaltolk.tms.entity.Language;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LanguageRepository extends JpaRepository<Language, Long> {

    Optional<Language> findByCode(String code);

    List<Language> findByActiveTrue();

    boolean existsByCode(String code);

    @Query("SELECT l FROM Language l WHERE l.active = true ORDER BY l.name")
    List<Language> findAllActiveOrderByName();
}