package backend.academy.formatter;

import backend.academy.analyzer.Statistics;
import backend.academy.util.HttpStatus;
import java.time.format.DateTimeFormatter;
import java.util.Map;

/**
 * Форматирует статистику логов в формате AsciiDoc.
 */
public class AsciidocFormatter implements ReportFormatter {

    /**
     * Форматирует объект статистики в текстовый отчёт в формате AsciiDoc.
     *
     * @param stats объект {@link Statistics}, содержащий данные для отчёта.
     * @return строка отчёта в формате AsciiDoc.
     */
    @Override
    public String format(Statistics stats) {
        StringBuilder report = new StringBuilder();

        // Общая информация
        report.append("== Общая информация\n\n");
        report.append("|===\n");
        report.append("| Метрика | Значение\n");
        report.append("| Файл(-ы) | `").append(stats.path()).append("`\n");
        report.append("| Начальная дата | ")
            .append(stats.from() != null ? stats.from().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) : "-")
            .append("\n");
        report.append("| Конечная дата | ")
            .append(stats.to() != null ? stats.to().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) : "-")
            .append("\n");
        report.append("| Количество запросов | ").append(stats.totalRequests()).append("\n");
        report.append("| Средний размер ответа | ").append(String.format("%.0fb", stats.averageResponseSize())).append("\n");
        report.append("| 95p размера ответа | ").append((int) stats.percentile95ResponseSize()).append("b\n");
        report.append("|===\n\n");

        // Запрашиваемые ресурсы
        report.append("== Запрашиваемые ресурсы\n\n");
        report.append("|===\n");
        report.append("| Ресурс | Количество\n");
        for (Map.Entry<String, Integer> entry : stats.resourceCounts().entrySet()) {
            report.append("| `").append(entry.getKey()).append("` | ").append(entry.getValue()).append("\n");
        }
        report.append("|===\n\n");

        // Коды ответа
        report.append("== Коды ответа\n\n");
        report.append("|===\n");
        report.append("| Код | Имя | Количество\n");
        for (Map.Entry<Integer, Integer> entry : stats.statusCounts().entrySet()) {
            report.append("| ").append(entry.getKey()).append(" | ")
                .append(HttpStatus.getDescriptionByCode(entry.getKey())).append(" | ")
                .append(entry.getValue()).append("\n");
        }
        report.append("|===\n");

        return report.toString();
    }
}
