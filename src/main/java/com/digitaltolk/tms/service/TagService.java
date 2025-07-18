package com.digitaltolk.tms.service;

import com.digitaltolk.tms.entity.Language;
import com.digitaltolk.tms.entity.Tag;
import com.digitaltolk.tms.repository.LanguageRepository;
import com.digitaltolk.tms.repository.TagRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TagService {

    @Autowired
    TagRepository tagRepository;

    @Transactional(readOnly = true)
    public List<Tag> getAllTags() {
        return tagRepository.findAllOrderByName();
    }

    @Transactional(readOnly = true)
    public Optional<Tag> getTagByName(String name) {
        return tagRepository.findByName(name);
    }

    @Transactional(readOnly = true)
    public Tag getTagByNameOrThrow(String name) {
        return tagRepository.findByName(name)
                .orElseThrow(() -> new EntityNotFoundException("Tag not found with name: " + name));
    }

    public Tag createTag(String name, String description) {
        if (tagRepository.existsByName(name)) {
            throw new IllegalArgumentException("Tag already exists with name: " + name);
        }

        Tag tag = new Tag();
        tag.setName(name);
        tag.setDescription(description);

        return tagRepository.save(tag);
    }

    public Tag updateTag(Long id, String name, String description) {
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tag not found with id: " + id));

        if (name != null && !name.equals(tag.getName())) {
            if (tagRepository.existsByName(name)) {
                throw new IllegalArgumentException("Tag already exists with name: " + name);
            }
            tag.setName(name);
        }

        if (description != null) {
            tag.setDescription(description);
        }

        return tagRepository.save(tag);
    }

    public void deleteTag(Long id) {
        if (!tagRepository.existsById(id)) {
            throw new EntityNotFoundException("Tag not found with id: " + id);
        }
        tagRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<Tag> getTagsByNames(List<String> names) {
        return tagRepository.findByNameIn(names);
    }
}