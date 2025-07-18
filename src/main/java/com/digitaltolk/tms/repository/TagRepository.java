package com.digitaltolk.tms.repository;

import com.digitaltolk.tms.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {

    Optional<Tag> findByName(String name);

    boolean existsByName(String name);

    @Query("SELECT t FROM Tag t WHERE t.name IN :names")
    List<Tag> findByNameIn(List<String> names);

    @Query("SELECT t FROM Tag t ORDER BY t.name")
    List<Tag> findAllOrderByName();
}
