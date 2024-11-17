package backend.academy.app;

import backend.academy.analyzer.LogAnalyzer;
import backend.academy.analyzer.Statistics;
import backend.academy.config.AppConfig;
import backend.academy.formatter.AsciidocFormatter;
import backend.academy.formatter.MarkdownFormatter;
import backend.academy.formatter.ReportFormatter;
import backend.academy.parser.LogParser;
import backend.academy.parser.LogRecord;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.logging.Logger;

/**
 * Основное приложение для анализа логов.
 */
public class ParserApp {

    private static final Logger LOGGER = Logger.getLogger(ParserApp.class.getName());
    private final AppConfig config;
    private final OutputRender outputRender;
    private final Map<String, Supplier<ReportFormatter>> formatters;

    /**
     * Создаёт приложение с заданной конфигурацией и компонентами.
     *
     * @param config конфигурация приложения.
     * @param outputRender компонент для вывода результата.
     */
    public ParserApp(AppConfig config, OutputRender outputRender) {
        this.config = config;
        this.outputRender = outputRender;
        this.formatters = Map.of(
            "markdown", MarkdownFormatter::new,
            "adoc", AsciidocFormatter::new
        );
    }

    /**
     * Запускает приложение:
     * - Читает и парсит логи.
     * - Выполняет анализ данных.
     * - Форматирует отчёт.
     * - Выводит результат.
     */
    public void run() {
        try {
            List<LogRecord> logs = LogParser.parse(config.pathOrUrl(), config.from(), config.to());

            if (logs.isEmpty()) {
                outputRender.render("Ошибка: Логи отсутствуют или файл пуст.");
                return;
            }

            LogAnalyzer analyzer = new LogAnalyzer(logs);
            Statistics stats = analyzer.getStatistics();
            stats.path(config.pathOrUrl());
            stats.from(config.from());
            stats.to(config.to());
            ReportFormatter formatter = getFormatter(config.format());
            String report = formatter.format(stats);
            outputRender.render(report);

        } catch (Exception e) {
            LOGGER.severe("Ошибка: " + e.getMessage());
        }
    }

    /**
     * Возвращает форматтер для создания отчёта в заданном формате.
     *
     * @param format строка формата (markdown или adoc).
     * @return объект {@link ReportFormatter} для заданного формата.
     * @throws IllegalArgumentException если указанный формат не поддерживается.
     */
    private ReportFormatter getFormatter(String format) {
        return formatters.getOrDefault(format.toLowerCase(), () -> {
            throw new IllegalArgumentException(
                "Неизвестный формат: " + format + ". Доступные форматы: markdown, adoc."
            );
        }).get();
    }
}
