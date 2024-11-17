package backend.academy.formatter;

import backend.academy.analyzer.Statistics;
import backend.academy.util.HttpStatus;
import java.time.format.DateTimeFormatter;
import java.util.Map;

/**
 * Форматирует статистику логов в формате Markdown.
 */
public class MarkdownFormatter implements ReportFormatter {

    /**
     * Форматирует объект статистики в текстовый отчёт в формате Markdown.
     *
     * @param stats объект {@link Statistics}, содержащий данные для отчёта.
     * @return строка отчёта в формате Markdown.
     */
    @Override
    public String format(Statistics stats) {
        StringBuilder report = new StringBuilder();
        report.append("#### Общая информация\n\n");
        report.append("| Метрика | Значение |\n");
        report.append("|:---------------------:|-------------:|\n");
        report.append("| Файл(-ы) | `").append(stats.path()).append("` |\n");
        report.append("| Начальная дата | ")
            .append(stats.from() != null ? stats.from().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) : "-")
            .append(" |\n");
        report.append("| Конечная дата | ")
            .append(stats.to() != null ? stats.to().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) : "-")
            .append(" |\n");
        report.append("| Количество запросов | ").append(stats.totalRequests()).append(" |\n");
        report.append("| Средний размер ответа | ").append(String.format("%.2fb", stats.averageResponseSize())).append(" |\n");
        report.append("| 95p размера ответа | ").append((int) stats.percentile95ResponseSize()).append("b |\n\n");

        // Запрашиваемые ресурсы
        report.append("#### Запрашиваемые ресурсы\n\n");
        report.append("| Ресурс | Количество |\n");
        report.append("|:---------------:|-----------:|\n");
        for (Map.Entry<String, Integer> entry : stats.resourceCounts().entrySet()) {
            report.append("| `").append(entry.getKey()).append("` | ").append(entry.getValue()).append(" |\n");
        }

        // Коды ответа
        report.append("\n#### Коды ответа\n\n");
        report.append("| Код | Имя | Количество |\n");
        report.append("|:---:|:---------------------:|-----------:|\n");
        for (Map.Entry<Integer, Integer> entry : stats.statusCounts().entrySet()) {
            report.append("| ").append(entry.getKey()).append(" | ")
                .append(HttpStatus.getDescriptionByCode(entry.getKey())).append(" | ")
                .append(entry.getValue()).append(" |\n");
        }

        return report.toString();
    }
}
