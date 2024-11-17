package backend.academy.formatter;

import backend.academy.analyzer.Statistics;
import backend.academy.util.HttpStatus;
import java.time.format.DateTimeFormatter;
import java.util.Map;

/**
 * Форматирует статистику логов в формате AsciiDoc.
 */
public class AsciidocFormatter implements ReportFormatter {

    private static final String TABLE_START = "|===\n";
    private static final String TABLE_END = "|===\n\n";
    private static final String COLUMN_SEPARATOR = " | ";
    private static final String NEW_LINE = "\n";
    private static final String HEADER_COUNT = "Количество";

    /**
     * Форматирует объект статистики в текстовый отчёт в формате AsciiDoc.
     *
     * @param stats объект {@link Statistics}, содержащий данные для отчёта.
     * @return строка отчёта в формате AsciiDoc.
     */
    @Override
    public String format(Statistics stats) {
        StringBuilder report = new StringBuilder();
        report.append("== Общая информация").append(NEW_LINE).append(NEW_LINE)
            .append(TABLE_START)
            .append("| Метрика").append(COLUMN_SEPARATOR).append("Значение").append(NEW_LINE)
            .append(COLUMN_SEPARATOR).append("Файл(-ы)").append(COLUMN_SEPARATOR)
            .append(stats.path()).append(NEW_LINE)
            .append(COLUMN_SEPARATOR).append("Начальная дата").append(COLUMN_SEPARATOR)
            .append(stats.from() != null
                ? stats.from().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
                : "-")
            .append(NEW_LINE)
            .append(COLUMN_SEPARATOR).append("Конечная дата").append(COLUMN_SEPARATOR)
            .append(stats.to() != null
                ? stats.to().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
                : "-")
            .append(NEW_LINE)
            .append(COLUMN_SEPARATOR).append("Количество запросов").append(COLUMN_SEPARATOR)
            .append(stats.totalRequests()).append(NEW_LINE)
            .append(COLUMN_SEPARATOR).append("Средний размер ответа").append(COLUMN_SEPARATOR)
            .append(String.format("%.2fb", stats.averageResponseSize()))
            .append(NEW_LINE)
            .append(COLUMN_SEPARATOR).append("95p размера ответа").append(COLUMN_SEPARATOR)
            .append((int) stats.percentile95ResponseSize()).append("b")
            .append(NEW_LINE)
            .append(TABLE_END);
        report.append("== Запрашиваемые ресурсы").append(NEW_LINE).append(NEW_LINE)
            .append(TABLE_START)
            .append("| Ресурс").append(COLUMN_SEPARATOR).append(HEADER_COUNT).append(NEW_LINE);

        for (Map.Entry<String, Integer> entry : stats.resourceCounts().entrySet()) {
            report.append(COLUMN_SEPARATOR).append(entry.getKey()).append(COLUMN_SEPARATOR)
                .append(entry.getValue()).append(NEW_LINE);
        }

        report.append(TABLE_END);
        report.append("== Коды ответа").append(NEW_LINE).append(NEW_LINE)
            .append(TABLE_START)
            .append("| Код").append(COLUMN_SEPARATOR).append("Имя")
            .append(COLUMN_SEPARATOR).append(HEADER_COUNT).append(NEW_LINE);

        for (Map.Entry<Integer, Integer> entry : stats.statusCounts().entrySet()) {
            report.append(COLUMN_SEPARATOR).append(entry.getKey()).append(COLUMN_SEPARATOR)
                .append(HttpStatus.getDescriptionByCode(entry.getKey())).append(COLUMN_SEPARATOR)
                .append(entry.getValue()).append(NEW_LINE);
        }

        report.append(TABLE_END);

        return report.toString();
    }
}
