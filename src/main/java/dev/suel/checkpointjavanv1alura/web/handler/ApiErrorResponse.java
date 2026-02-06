package dev.suel.checkpointjavanv1alura.web.handler;

import lombok.*;

import java.time.Instant;
import java.util.List;
import java.util.Map;

import static dev.suel.checkpointjavanv1alura.web.handler.ApiErrorType.UNMAPPED_ERROR;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiErrorResponse {
    private String message;
    private String path;
    private int code;
    private Map<String, List<String>> fieldErrors;
    private List<String> globalsError;
    private final Instant timestamp = Instant.now();

    @Builder.Default
    private ApiErrorType type = UNMAPPED_ERROR;
}
