package dev.hiwa.iblog.services;

import dev.hiwa.iblog.domain.dto.request.CreatePostRequest;
import dev.hiwa.iblog.domain.dto.request.UpdatePostRequest;
import dev.hiwa.iblog.domain.dto.response.PostDto;
import dev.hiwa.iblog.domain.entities.Post;
import dev.hiwa.iblog.domain.entities.Tag;
import dev.hiwa.iblog.domain.enums.PostStatus;
import dev.hiwa.iblog.exceptions.ResourceNotFoundException;
import dev.hiwa.iblog.mappers.CategoryMapper;
import dev.hiwa.iblog.mappers.PostMapper;
import dev.hiwa.iblog.mappers.TagMapper;
import dev.hiwa.iblog.repositories.PostRepository;
import dev.hiwa.iblog.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PostService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final PostMapper postMapper;

    private final CategoryService categoryService;
    private final CategoryMapper categoryMapper;

    private final TagService tagService;
    private final TagMapper tagMapper;


    @Transactional(readOnly = true)
    public List<PostDto> getAllPublishedPosts(UUID categoryId, UUID tagId) {
        List<Post> posts = new ArrayList<>();

        if (categoryId != null && tagId != null) {
            posts = postRepository.findAllByStatusAndCategoryIdAndTagId(PostStatus.PUBLISHED,
                                                                        categoryId,
                                                                        tagId
            );
        }
        else if (categoryId != null) {
            posts = postRepository.findAllByStatusAndCategory_Id(PostStatus.PUBLISHED, categoryId);
        }
        else if (tagId != null) {
            posts = postRepository.findAllByStatusAndTagId(PostStatus.PUBLISHED, tagId);
        }
        else {
            posts = postRepository.findAllByStatus(PostStatus.PUBLISHED);
        }

        return posts.stream().map(postMapper::toDto).toList();

    }

    @Transactional(readOnly = true)
    public List<PostDto> getDraftPostsForUser(String userEmail) {
        var user = userRepository
                .findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", userEmail));

        List<Post> draftPosts =
                postRepository.findAllByStatusAndAuthor_Id(PostStatus.DRAFT, user.getId());

        return draftPosts.stream().map(postMapper::toDto).toList();
    }

    @Transactional
    public PostDto createPost(CreatePostRequest request, String userEmail) {
        var user = userRepository
                .findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", userEmail));
        var categoryDto = categoryService.getCategoryById(request.getCategoryId());
        var tagDtos = tagService.getTagByIds(request.getTagIds());

        var post = postMapper.toEntity(request);
        post.setAuthor(user);
        post.setCategory(categoryMapper.toEntity(categoryDto));
        post.setTags(tagDtos.stream().map(tagMapper::toEntity).collect(Collectors.toSet()));
        post.setReadingTime(_calculateReadingTime(request.getContent()));

        var savedPost = postRepository.save(post);

        return postMapper.toDto(savedPost);
    }

    @Transactional
    public PostDto updatePost(UUID id, UpdatePostRequest request) {
        var existingPost = postRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "id", id.toString()));

        existingPost.setTitle(request.getTitle());
        existingPost.setContent(request.getContent());
        existingPost.setStatus(request.getStatus());

        existingPost.setReadingTime(_calculateReadingTime(request.getContent()));
        if (!existingPost.getCategory().getId().equals(request.getCategoryId())) {
            var categoryDto = categoryService.getCategoryById(request.getCategoryId());
            existingPost.setCategory(categoryMapper.toEntity(categoryDto));
        }

        Set<UUID> existingTagIds = existingPost.getTags().stream().map(Tag::getId).collect(Collectors.toSet());
        if (!existingTagIds.equals(request.getTagIds())) {
            var tagDtos = tagService.getTagByIds(request.getTagIds());
            existingPost.setTags(tagDtos.stream().map(tagMapper::toEntity).collect(Collectors.toSet()));
        }

        var savedPost = postRepository.save(existingPost);

        return postMapper.toDto(savedPost);
    }

    @Transactional(readOnly = true)
    public PostDto getPostById(UUID id) {
        Post post = postRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "id", id.toString()));

        return postMapper.toDto(post);
    }


    private Integer _calculateReadingTime(String content) {
        int words = content.trim().split("\\s+").length;
        int wordsPerMinute = 200;
        return Math.max(1, words / wordsPerMinute);
    }

    @Transactional
    public void deletePostById(UUID id) {
        getPostById(id);

        postRepository.deleteById(id);
    }
}
