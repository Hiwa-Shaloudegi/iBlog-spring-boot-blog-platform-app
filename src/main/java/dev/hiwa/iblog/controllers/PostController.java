package dev.hiwa.iblog.controllers;

import dev.hiwa.iblog.domain.dto.response.PostDto;
import dev.hiwa.iblog.services.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/posts")
public class PostController {

    private final PostService postService;

    @GetMapping
    public ResponseEntity<List<PostDto>> getAllPublishedPosts(
            @RequestParam(name = "category-id", required = false) UUID categoryId,
            @RequestParam(name = "tag-id", required = false) UUID tagId
    ) {
        List<PostDto> posts = postService.getAllPosts(categoryId, tagId);

        return ResponseEntity.ok(posts);
    }

}
