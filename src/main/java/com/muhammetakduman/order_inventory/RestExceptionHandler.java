package com.muhammetakduman.order_inventory;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Tüm controller'lar için merkezi hata yakalayıcı.
 * Her zaman aynı JSON şemasında döner; UI için tutarlı.
 */
@ControllerAdvice
public class RestExceptionHandler {

    /** Hata cevabı zarfı (UI için standart). */
    public static final class ApiError {
        public final boolean success;
        public final int status;
        public final String error;
        public final String message;
        public final Map<String, String> errors; // alan bazlı validation hataları
        public final Instant timestamp;

        public ApiError(boolean success, int status, String error, String message,
                        Map<String, String> errors, Instant timestamp) {
            this.success = success;
            this.status = status;
            this.error = error;
            this.message = message;
            this.errors = errors;
            this.timestamp = timestamp;
        }
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiError> handleNotFound(EntityNotFoundException ex) {
        ApiError body = new ApiError(
                false,
                HttpStatus.NOT_FOUND.value(),
                "Not Found",
                ex.getMessage(),
                null,
                Instant.now()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }

    // Domain'e özgü örnek: Yetersiz stok
    // Bu sınıf yoksa önce ekleyebilirsin:
    // public class NotEnoughStockException extends RuntimeException { public NotEnoughStockException(String m){ super(m); } }
    @ExceptionHandler(NotEnoughStockException.class)
    public ResponseEntity<ApiError> handleNotEnoughStock(NotEnoughStockException ex) {
        ApiError body = new ApiError(
                false,
                HttpStatus.CONFLICT.value(),
                "Conflict",
                ex.getMessage(),  // UI: "Yetersiz stok" gibi kullanıcı dostu mesaj verebilirsin
                null,
                Instant.now()
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiError> handleBadRequest(IllegalArgumentException ex) {
        ApiError body = new ApiError(
                false,
                HttpStatus.BAD_REQUEST.value(),
                "Bad Request",
                ex.getMessage(),
                null,
                Instant.now()
        );
        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> fieldErrors = new LinkedHashMap<>();
        for (org.springframework.validation.ObjectError error : ex.getBindingResult().getAllErrors()) {
            FieldError fe = (FieldError) error;
            fieldErrors.put(fe.getField(), error.getDefaultMessage());
        }
        ApiError body = new ApiError(
                false,
                HttpStatus.BAD_REQUEST.value(),
                "Validation Error",
                "Input validation failed",
                fieldErrors,
                Instant.now()
        );
        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiError> handleMalformedJson(HttpMessageNotReadableException ex) {
        String detail = ex.getMostSpecificCause() != null
                ? ex.getMostSpecificCause().getMessage()
                : ex.getMessage();
        ApiError body = new ApiError(
                false,
                HttpStatus.BAD_REQUEST.value(),
                "Malformed JSON",
                detail,
                null,
                Instant.now()
        );
        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGeneric(Exception ex) {
        ApiError body = new ApiError(
                false,
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Internal Server Error",
                ex.getMessage(),
                null,
                Instant.now()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }
}
