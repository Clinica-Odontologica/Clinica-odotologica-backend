package com.joao.dev.clinica_odontologica.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Builder
public class GlobalResponse<T> {
    private Boolean ok;
    private String message;
    private T data;
    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now();
    private String details;

    public static <T> GlobalResponse<T> success(T data, String message) {
        return GlobalResponse.<T>builder()
                .ok(true)
                .message(message)
                .data(data)
                .build();
    }

    public static <T> GlobalResponse<T> failure(String message, String details) {
        return GlobalResponse.<T>builder()
                .ok(false)
                .message(message)
                .details(details)
                .build();
    }
}
