package dev.hiwa.iblog.controllers;

import dev.hiwa.iblog.domain.dto.request.CreateTagsRequest;
import dev.hiwa.iblog.domain.dto.response.TagResponse;
import dev.hiwa.iblog.services.TagService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/tags")
public class TagController {

    private final TagService tagService;

    @GetMapping
    public ResponseEntity<List<TagResponse>> getAllTagsWithPosts() {
        List<TagResponse> tags = tagService.getAllTagsWithPosts();

        return ResponseEntity.ok(tags);
    }

    @PostMapping
    public ResponseEntity<List<TagResponse>> createTags(
            @Valid @RequestBody CreateTagsRequest request
    ) {
        List<TagResponse> tags = tagService.createTags(request);

        return new ResponseEntity<>(tags, HttpStatus.CREATED);
    }
}
