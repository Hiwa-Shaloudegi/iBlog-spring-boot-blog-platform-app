package dev.hiwa.iblog.repositories;

import dev.hiwa.iblog.domain.entities.Post;
import dev.hiwa.iblog.domain.enums.PostStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PostRepository extends JpaRepository<Post, UUID> {

    List<Post> findAllByStatus(PostStatus postStatus);

    List<Post> findAllByStatusAndCategory_Id(PostStatus postStatus, UUID categoryId);

    @Query("SELECT DISTINCT p FROM Post p JOIN p.tags t where t.id = :tagId and p.status = :status")
    List<Post> findAllByStatusAndTagId(
            @Param("status") PostStatus postStatus, @Param("tagId") UUID tagId
    );

    @Query("SELECT DISTINCT p FROM Post p JOIN p.tags t WHERE p.status = :status AND p.category.id = " +
            ":categoryId AND t.id = :tagId")
    List<Post> findAllByStatusAndCategoryIdAndTagId(
            @Param("status") PostStatus status,
            @Param("categoryId") UUID categoryId,
            @Param("tagId") UUID tagId
    );
}
