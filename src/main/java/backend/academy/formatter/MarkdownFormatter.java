package backend.academy.formatter;

import backend.academy.analyzer.Statistics;
import backend.academy.util.HttpStatus;
import java.time.format.DateTimeFormatter;
import java.util.Map;

/**
 * Форматирует статистику логов в формате Markdown.
 */
public class MarkdownFormatter implements ReportFormatter {

    private static final String COLUMN_SEPARATOR = " | ";
    private static final String ROW_END = " |\n";
    private static final String TABLE_HEADER_DIVIDER = "|:---------------------:|-------------:|\n";
    private static final String NEW_LINE = "\n";
    private static final String HEADER_COUNT = "Количество";

    /**
     * Форматирует объект статистики в текстовый отчёт в формате Markdown.
     *
     * @param stats объект {@link Statistics}, содержащий данные для отчёта.
     * @return строка отчёта в формате Markdown.
     */
    @Override
    public String format(Statistics stats) {
        StringBuilder report = new StringBuilder();
        report.append("#### Общая информация").append(NEW_LINE).append(NEW_LINE)
            .append("| Метрика").append(COLUMN_SEPARATOR).append("Значение").append(ROW_END)
            .append(TABLE_HEADER_DIVIDER)
            .append("| Файл(-ы)").append(COLUMN_SEPARATOR).append(stats.path()).append(ROW_END)
            .append("| Начальная дата").append(COLUMN_SEPARATOR)
            .append(stats.from() != null
                ? stats.from().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
                : "-")
            .append(ROW_END)
            .append("| Конечная дата").append(COLUMN_SEPARATOR)
            .append(stats.to() != null
                ? stats.to().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
                : "-")
            .append(ROW_END)
            .append("| Количество запросов").append(COLUMN_SEPARATOR).append(stats.totalRequests()).append(ROW_END)
            .append("| Средний размер ответа").append(COLUMN_SEPARATOR)
            .append(String.format("%.2fb", stats.averageResponseSize()))
            .append(ROW_END)
            .append("| 95p размера ответа").append(COLUMN_SEPARATOR)
            .append((int) stats.percentile95ResponseSize()).append("b").append(ROW_END)
            .append("| Минимальный размер ответа").append(COLUMN_SEPARATOR)
            .append(stats.minResponseSize()).append(ROW_END)
            .append("| Количество ответов размера 0b").append(COLUMN_SEPARATOR)
            .append(stats.zeroResponseCount()).append(ROW_END)
            .append(NEW_LINE);
        report.append("#### Запрашиваемые ресурсы").append(NEW_LINE).append(NEW_LINE)
            .append("| Ресурс").append(COLUMN_SEPARATOR).append(HEADER_COUNT).append(ROW_END)
            .append(TABLE_HEADER_DIVIDER);

        for (Map.Entry<String, Integer> entry : stats.resourceCounts().entrySet()) {
            report.append("| `").append(entry.getKey()).append("`").append(COLUMN_SEPARATOR)
                .append(entry.getValue()).append(ROW_END);
        }
        report.append(NEW_LINE);

        report.append("#### Коды ответа").append(NEW_LINE).append(NEW_LINE)
            .append("| Код").append(COLUMN_SEPARATOR).append("Имя").append(COLUMN_SEPARATOR)
            .append(HEADER_COUNT).append(ROW_END)
            .append("|:---:|:---------------------:|-----------:|\n");

        for (Map.Entry<Integer, Integer> entry : stats.statusCounts().entrySet()) {
            report.append("| ").append(entry.getKey()).append(COLUMN_SEPARATOR)
                .append(HttpStatus.getDescriptionByCode(entry.getKey())).append(COLUMN_SEPARATOR)
                .append(entry.getValue()).append(ROW_END);
        }


        return report.toString();
    }
}
