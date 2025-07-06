package dev.hiwa.iblog.services;

import dev.hiwa.iblog.domain.dto.response.PostDto;
import dev.hiwa.iblog.domain.entities.Post;
import dev.hiwa.iblog.domain.enums.PostStatus;
import dev.hiwa.iblog.exceptions.ResourceNotFoundException;
import dev.hiwa.iblog.mappers.PostMapper;
import dev.hiwa.iblog.repositories.PostRepository;
import dev.hiwa.iblog.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final PostMapper postMapper;
    private final CategoryService categoryService;
    private final TagService tagService;


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
}
