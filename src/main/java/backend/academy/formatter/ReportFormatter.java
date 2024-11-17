package backend.academy.formatter;

import backend.academy.analyzer.Statistics;

/**
 * Интерфейс для форматирования статистики в отчёты.
 */
public interface ReportFormatter {

    /**
     * Форматирует объект статистики в текстовый отчёт.
     *
     * @param stats объект {@link Statistics}, содержащий данные для отчёта.
     * @return строка отчёта в заданном формате.
     */
    String format(Statistics stats);
}
