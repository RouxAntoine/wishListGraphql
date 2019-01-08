package com.main.webObject;

import lombok.*;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

/**
 * error DTO
 */
@Getter
@Setter
@Builder
@ToString
@EqualsAndHashCode
@AllArgsConstructor
public class ErrorDTO {

    private LocalDateTime timestamp;
    private final Integer status;
    private final String error;
    private final String message;
    private final String path;

    public ErrorDTO(Exception ex, String path, HttpStatus status) {
        this.message = ex.getLocalizedMessage();
        this.timestamp = LocalDateTime.now();
        this.path = path;
        this.error = status.getReasonPhrase();
        this.status = status.value();
    }
}
