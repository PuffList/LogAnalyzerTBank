package backend.academy.analyzer;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;

/**
 * Класс для хранения статистики по логам:
 * - Количество запросов.
 * - Популярные ресурсы.
 * - Частота кодов ответа.
 * - Средний размер ответа.
 * - 95-й перцентиль размера ответа.
 * - Минимальный размер ответа.
 * - Количество ответов размера 0b
 */
@Getter
public class Statistics {

    @Setter
    private String path;
    @Setter
    private LocalDateTime from;
    @Setter
    private LocalDateTime to;
    @Setter
    private int totalRequests;
    private Map<String, Integer> resourceCounts;
    private Map<Integer, Integer> statusCounts;
    @Setter
    private double averageResponseSize;
    @Setter
    private double percentile95ResponseSize;
    @Setter
    private int minResponseSize;
    @Setter
    private long zeroResponseCount;

    public Statistics() {
        this.resourceCounts = new HashMap<>();
        this.statusCounts = new HashMap<>();
    }

    /**
     * Увеличивает счётчик для указанного ресурса.
     *
     * @param resource запрашиваемый ресурс.
     */
    public void incrementResourceCount(String resource) {
        resourceCounts.put(resource, resourceCounts.getOrDefault(resource, 0) + 1);
    }

    /**
     * Увеличивает счётчик для указанного кода ответа.
     *
     * @param statusCode код ответа HTTP.
     */
    public void incrementStatusCount(int statusCode) {
        statusCounts.put(statusCode, statusCounts.getOrDefault(statusCode, 0) + 1);
    }
}
