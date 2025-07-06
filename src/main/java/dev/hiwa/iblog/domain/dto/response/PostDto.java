package dev.hiwa.iblog.domain.dto.response;

import dev.hiwa.iblog.domain.enums.PostStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostDto {

    private UUID id;
    private String title;
    private String content;
    private PostStatus status;
    private Integer readingTime;
    private AuthorDto author;
    private CategoryDto category;
    private Set<TagDto> tags;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
