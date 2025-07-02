package dev.hiwa.iblog.repositories;

import dev.hiwa.iblog.domain.entities.Post;
import dev.hiwa.iblog.domain.entities.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PostRepository extends JpaRepository<Post, UUID> {}
