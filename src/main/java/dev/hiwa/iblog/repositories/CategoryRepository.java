package dev.hiwa.iblog.repositories;

import dev.hiwa.iblog.domain.entities.Category;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CategoryRepository extends JpaRepository<Category, UUID> {

    @EntityGraph(attributePaths = "posts")
    @Query("SELECT c FROM Category c")
    List<Category> findAlWithPosts();

    //    Alternative to Above
    //    @Query("SELECT c FROM Category c LEFT JOIN FETCH c.posts")
    //    List<Category> findAlWithPosts();

    //    @Query("SELECT DISTINCT c FROM Category c LEFT JOIN FETCH c.posts p WHERE p.status =
    //    'published'")
    //    List<Category> findAlWithPublishedPosts();

    boolean existsByNameIgnoreCase(String name);

}
