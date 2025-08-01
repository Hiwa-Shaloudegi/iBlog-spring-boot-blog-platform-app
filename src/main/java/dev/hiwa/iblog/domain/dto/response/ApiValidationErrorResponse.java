package dev.hiwa.iblog.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApiValidationErrorResponse {

    private int status;
    private String message;
    private Map<String, String> errors;
}
