package com.maiduongland.mdbds.repository;

import com.maiduongland.mdbds.entity.Property;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PropertyRepository extends JpaRepository<Property, Long> {
    List<Property> findByIsHotTrue();
    
    List<Property> findTop5ByIsHotTrueOrderByCreatedAtDesc();

    Optional<Property> findBySlug(String slug);

    @Query("SELECT p FROM Property p WHERE " +
           "(:q IS NULL OR :q = '' OR LOWER(p.title) LIKE LOWER(CONCAT('%', :q, '%')) OR LOWER(p.description) LIKE LOWER(CONCAT('%', :q, '%'))) AND " +
           "(:catId IS NULL OR p.category.id = :catId) AND " +
           "(:minPrice IS NULL OR p.price >= :minPrice) AND " +
           "(:maxPrice IS NULL OR p.price <= :maxPrice) " +
           "ORDER BY p.createdAt DESC")
    List<Property> searchProperties(@Param("q") String q, 
                                   @Param("catId") Long catId, 
                                   @Param("minPrice") Double minPrice, 
                                   @Param("maxPrice") Double maxPrice);
}
