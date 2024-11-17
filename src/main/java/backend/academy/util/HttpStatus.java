package backend.academy.util;

import java.util.Arrays;
import lombok.Getter;

/**
 * Enum для представления HTTP-кодов ответа и их описаний.
 */
public enum HttpStatus {

    OK(200, "OK"),
    NOT_FOUND(404, "Not Found"),
    INTERNAL_SERVER_ERROR(500, "Internal Server Error"),
    FORBIDDEN(403, "Forbidden"),
    NOT_MODIFIED(304, "Not Modified"),
    PARTIAL_CONTENT(206, "Partial Content"),
    UNKNOWN(-1, "Unknown");

    private final int code;
    @Getter
    private final String description;

    HttpStatus(int code, String description) {
        this.code = code;
        this.description = description;
    }

    /**
     * Возвращает описание HTTP-кода по его числовому значению.
     *
     * @param code числовое значение HTTP-кода.
     * @return строка описания HTTP-кода.
     */
    public static String getDescriptionByCode(int code) {
        return Arrays.stream(HttpStatus.values())
            .filter(status -> status.code == code)
            .findFirst()
            .orElse(HttpStatus.UNKNOWN)
            .description();
    }
}
