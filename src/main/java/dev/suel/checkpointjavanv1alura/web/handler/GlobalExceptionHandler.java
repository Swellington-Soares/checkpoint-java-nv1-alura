package dev.suel.checkpointjavanv1alura.web.handler;


import dev.suel.checkpointjavanv1alura.exception.ResourceNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<ApiErrorResponse> methodArgumentNotValidHandler(MethodArgumentNotValidException ex,
                                                                   HttpServletRequest request) {
        var apiResponse = new ApiErrorResponse();
        if (ex.getBindingResult().hasFieldErrors()) {
            Map<String, List<String>> errors = ex.getBindingResult().getFieldErrors().stream()
                    .collect(
                            Collectors.groupingBy(
                                    FieldError::getField,
                                    Collectors.mapping(
                                            FieldError::getDefaultMessage,
                                            Collectors.toList()
                                    )
                            )
                    );
            apiResponse.setFieldErrors(errors);
        }

        if (ex.getBindingResult().hasGlobalErrors()) {
            List<String> errors = ex.getBindingResult().getGlobalErrors().stream().map(
                    DefaultMessageSourceResolvable::getDefaultMessage
            ).toList();
            apiResponse.setGlobalsError(errors);
        }
        apiResponse.setMessage("Verifique os campos e tente novamente.");
        apiResponse.setCode(HttpStatus.BAD_REQUEST.value());
        apiResponse.setPath(request.getRequestURI());
        apiResponse.setType(ApiErrorType.VALIDATION_FIELD_ERROR);
        return ResponseEntity.badRequest().body(apiResponse);
    }


    @ExceptionHandler(IllegalArgumentException.class)
    ResponseEntity<ApiErrorResponse> illegalArgumentExceptionHandle(IllegalArgumentException ex, HttpServletRequest request) {
        return ResponseEntity.badRequest().body(ApiErrorResponse.builder()
                .type(ApiErrorType.BUSINESS_ERROR)
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .code(HttpStatus.BAD_REQUEST.value())
                .build());

    }

    @ExceptionHandler(ResourceNotFoundException.class)
    ResponseEntity<Void> resourceNotFoundExceptionHandle() {
        return ResponseEntity.notFound().build();
    }
}
