package org.course_planner.utils.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.course_planner.utils.exceptions.GenericExceptionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExceptionResponseDTO {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionResponseDTO.class);

    private LocalDateTime timestamp;
    private String exceptionType;
    private String errorMessage;
    private HttpStatus status;
    private String error;
    private Throwable throwable;

    public ExceptionResponseDTO(GenericExceptionTemplate ex) {
        LOGGER.error("ExceptionResponseDTO: Constructing exception response: {}", ex);
        this.exceptionType = ex.getInstanceName();
        this.errorMessage = ex.getMessage();
        this.status = ex.getHttpStatus();
        this.timestamp = LocalDateTime.now();
        this.error = ex.getHttpStatus().getReasonPhrase();
        this.throwable = ex.getCause();
    }
}
