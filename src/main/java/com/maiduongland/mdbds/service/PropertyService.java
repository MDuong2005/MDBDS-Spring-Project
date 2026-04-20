package com.maiduongland.mdbds.service;

import com.maiduongland.mdbds.entity.Property;
import com.maiduongland.mdbds.entity.Category;
import com.maiduongland.mdbds.repository.PropertyRepository;
import com.maiduongland.mdbds.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.annotation.PostConstruct;

import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class PropertyService {

    @Autowired
    private PropertyRepository repository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Transactional
    @PostConstruct
    public void seedCategories() {
        System.out.println(">>> SEEDING CATEGORIES...");
        String[][] cats = {
            {"Đất nền", "dat-nen"},
            {"Nhà ở", "nha-o"},
            {"Chung cư", "chung-cu"},
            {"Dự án", "extra-du-an"}
        };
        for (String[] cat : cats) {
            try {
                if (categoryRepository.findBySlug(cat[1]) == null) {
                    Category c = new Category();
                    c.setName(cat[0]);
                    c.setSlug(cat[1]);
                    categoryRepository.save(c);
                    System.out.println(">>> Seeded: " + cat[0]);
                }
            } catch (Exception e) {
                System.err.println(">>> Error seeding " + cat[0] + ": " + e.getMessage());
            }
        }
    }

    public List<Property> getAll() {
        return repository.findAll();
    }

    public Property getBySlug(String slug) {
        return repository.findBySlug(slug).orElse(null);
    }

    public List<Property> getHotPropertiesLimit5() {
        return repository.findTop5ByIsHotTrueOrderByCreatedAtDesc();
    }

    public List<Property> searchProperties(String q, Long catId, Double min, Double max) {
        return repository.searchProperties(q, catId, min, max);
    }

    public Property save(Property property) {
        return repository.save(property);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public Property getById(Long id) {
        return repository.findById(id).orElse(null);
    }
}
