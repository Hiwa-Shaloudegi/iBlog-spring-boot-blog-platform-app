package dev.hiwa.iblog.domain.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateCategoryRequest {

    @NotBlank
    @Size(min = 2, max = 50, message = "Category name must be between {min} and {max} characters")
    @Pattern(
            regexp = "^[\\w\\s- ]+$",
            message = "Category name can only contain letters, numbers, spaces, and hyphens"
    )
    private String name;
}
