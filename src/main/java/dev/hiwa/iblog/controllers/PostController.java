package dev.hiwa.iblog.controllers;

import dev.hiwa.iblog.domain.dto.request.CreatePostRequest;
import dev.hiwa.iblog.domain.dto.request.UpdatePostRequest;
import dev.hiwa.iblog.domain.dto.response.PostDto;
import dev.hiwa.iblog.services.PostService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

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
        List<PostDto> posts = postService.getAllPublishedPosts(categoryId, tagId);

        return ResponseEntity.ok(posts);
    }


    @SecurityRequirement(name = "BearerAuth")
    @GetMapping("/drafts")
    public ResponseEntity<List<PostDto>> getDraftPosts() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();

        List<PostDto> draftPosts = postService.getDraftPostsForUser(userEmail);

        return ResponseEntity.ok(draftPosts);
    }

    @SecurityRequirement(name = "BearerAuth")
    @PostMapping
    public ResponseEntity<PostDto> createPost(
            @Valid @RequestBody CreatePostRequest request
    ){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();

        PostDto postDto = postService.createPost(request, userEmail);

        return new ResponseEntity<>(postDto, HttpStatus.CREATED);
    }

    @SecurityRequirement(name = "BearerAuth")
    @PutMapping("/{id}")
    public ResponseEntity<PostDto> updatePost(
            @PathVariable("id") UUID id,
            @Valid @RequestBody UpdatePostRequest request
    ){
        PostDto postDto = postService.updatePost(id, request);

        return ResponseEntity.ok(postDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostDto> getPostById(@PathVariable("id") UUID id) {
        PostDto postDto = postService.getPostById(id);

        return ResponseEntity.ok(postDto);
    }

    @SecurityRequirement(name = "BearerAuth")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePostById(@PathVariable("id") UUID id) {
        postService.deletePostById(id);

        return ResponseEntity.noContent().build();
    }

}
