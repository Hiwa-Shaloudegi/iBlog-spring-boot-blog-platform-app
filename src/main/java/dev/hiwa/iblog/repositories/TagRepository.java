package dev.hiwa.iblog.repositories;

import dev.hiwa.iblog.domain.entities.Tag;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Repository
public interface TagRepository extends JpaRepository<Tag, UUID> {

    @EntityGraph(attributePaths = "posts")
    @Query("SELECT t FROM Tag t")
    List<Tag> findAllWithPosts();

    @EntityGraph(attributePaths = "posts")
    Set<Tag> findAllByNameIn(Set<String> names);
}
