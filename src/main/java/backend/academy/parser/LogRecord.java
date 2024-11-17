package backend.academy.parser;

import java.time.OffsetDateTime;
import lombok.Getter;

/**
 * Представляет запись лога NGINX, содержащую данные о запросе.
 */
@Getter
public class LogRecord {

    private String ipAddress;
    private OffsetDateTime timestamp;
    private String resource;
    private int statusCode;
    private int responseSize;

    /**
     * Создаёт объект записи лога.
     *
     * @param ipAddress IP-адрес клиента.
     * @param timestamp Временная метка запроса.
     * @param resource Запрашиваемый ресурс.
     * @param statusCode HTTP-код ответа.
     * @param responseSize Размер ответа в байтах.
     */
    public LogRecord(String ipAddress, OffsetDateTime timestamp, String resource, int statusCode, int responseSize) {
        this.ipAddress = ipAddress;
        this.timestamp = timestamp;
        this.resource = resource;
        this.statusCode = statusCode;
        this.responseSize = responseSize;
    }
}
