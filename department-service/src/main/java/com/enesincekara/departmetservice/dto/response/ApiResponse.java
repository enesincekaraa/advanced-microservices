package com.enesincekara.departmetservice.dto.response;

import java.time.LocalDateTime;

public record ApiResponse<T>(
        String message,
        boolean success,
        LocalDateTime timestamp,
        int statusCode,
        T data
) {

}
