package dev.hiwa.iblog.domain.dto.request;

import dev.hiwa.iblog.domain.enums.PostStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdatePostRequest {
    @NotNull
    private UUID id;

    @NotBlank
    @Size(min = 3, max = 200, message = "Title must be between {min} and {max} characters")
    private String title;

    @NotBlank
    @Size(min = 10, max = 50000, message = "Content must be between {min} and {max} characters")
    private String content;

    @NotNull
    private PostStatus status;

    @NotNull
    private UUID categoryId;

    @Builder.Default
    @NotNull
    @Size(max = 10, message = "Maximum {max} tags allowed")
    private Set<UUID> tagIds = new HashSet<>();

}
