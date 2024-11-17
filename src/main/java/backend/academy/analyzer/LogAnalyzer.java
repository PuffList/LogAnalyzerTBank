package backend.academy.analyzer;

import backend.academy.parser.LogRecord;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Класс для анализа логов. Вычисляет ключевые метрики, такие как:
 * - Общее количество запросов.
 * - Наиболее популярные ресурсы.
 * - Частота кодов ответа.
 * - Средний размер ответа.
 * - 95-й перцентиль размера ответа.
 */
public class LogAnalyzer {

    private static final double PERCENTILE_95 = 0.95;
    private final List<LogRecord> logs;

    /**
     * Создаёт анализатор логов на основе списка записей.
     *
     * @param logs список объектов {@link LogRecord}, представляющих записи логов.
     */
    public LogAnalyzer(List<LogRecord> logs) {
        this.logs = logs;
    }

    /**
     * Анализирует список записей логов и возвращает статистику.
     *
     * @return объект {@link Statistics}, содержащий результаты анализа логов.
     */
    public Statistics getStatistics() {
        Statistics stats = new Statistics();
        stats.totalRequests(logs.size());
        long totalResponseSize = 0;

        List<Integer> responseSizes = logs.stream()
            .map(log -> {
                stats.incrementResourceCount(log.resource());
                stats.incrementStatusCount(log.statusCode());

                return log.responseSize();
            })
            .collect(Collectors.toList());

        totalResponseSize = responseSizes.stream().mapToLong(Integer::longValue).sum();
        stats.averageResponseSize((double) totalResponseSize / logs.size());
        Collections.sort(responseSizes);
        int index95Percentile = (int) Math.ceil(PERCENTILE_95 * responseSizes.size()) - 1;
        stats.percentile95ResponseSize(responseSizes.get(index95Percentile));

        return stats;
    }
}
