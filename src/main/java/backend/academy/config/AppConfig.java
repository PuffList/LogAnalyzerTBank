package backend.academy.config;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;

/**
 * Класс для обработки и хранения конфигурации приложения.
 * Обрабатывает аргументы командной строки и предоставляет доступ к параметрам.
 */
@Getter
public class AppConfig {

    private static final String ARG_PATH = "path";
    private static final String ARG_FROM = "from";
    private static final String ARG_TO = "to";
    private static final String ARG_FILTER_FIELD = "filter-field";
    private static final String ARG_FILTER_VALUE = "filter-value";
    private static final DateTimeFormatter ISO8601_FORMATTER = DateTimeFormatter.ISO_DATE_TIME;
    private String pathOrUrl;
    private String format;
    private LocalDateTime from;
    private LocalDateTime to;
    private String filterField;
    private String filterValue;

    /**
     * Создаёт конфигурацию приложения на основе аргументов командной строки.
     *
     * @param args массив аргументов командной строки.
     * @throws IllegalArgumentException если обязательные параметры отсутствуют или некорректны.
     */
    public AppConfig(String[] args) {
        Map<String, String> argMap = parseArgs(args);

        if (argMap.containsKey(ARG_PATH)) {
            this.pathOrUrl = argMap.get(ARG_PATH);
        } else {
            throw new IllegalArgumentException("Параметр --path обязателен.");
        }

        this.format = argMap.getOrDefault("format", "markdown");

        if (argMap.containsKey(ARG_FROM)) {
            this.from = parseDate(argMap.get(ARG_FROM));
        }
        if (argMap.containsKey(ARG_TO)) {
            this.to = parseDate(argMap.get(ARG_TO));
        }

        this.filterField = argMap.get(ARG_FILTER_FIELD);
        this.filterValue = argMap.get(ARG_FILTER_VALUE);
    }

    /**
     * Парсит аргументы командной строки в карту ключ-значение.
     *
     * @param args массив аргументов командной строки.
     * @return карта аргументов, где ключи — это параметры без "--", а значения — их значения.
     */
    private Map<String, String> parseArgs(String[] args) {
        Map<String, String> argMap = new HashMap<>();
        for (int index = 0; index < args.length;) {
            if (args[index].startsWith("--")) {
                String key = args[index].substring(2);
                String value = (index + 1 < args.length && !args[index + 1].startsWith("--")) ? args[++index] : null;
                argMap.put(key, value);
            }
            index++;
        }
        return argMap;
    }

    /**
     * Парсит строку даты в объект {@link LocalDateTime}.
     *
     * @param dateStr строка даты в формате ISO8601 или "yyyy-MM-dd".
     * @return объект {@link LocalDateTime}, представляющий дату и время.
     * @throws IllegalArgumentException если формат строки даты некорректен.
     */
    private LocalDateTime parseDate(String dateStr) {
        try {
            if (dateStr.matches("\\d{4}-\\d{2}-\\d{2}")) {
                return LocalDate.parse(dateStr, DateTimeFormatter.ISO_DATE).atStartOfDay();
            }
            return LocalDateTime.parse(dateStr, ISO8601_FORMATTER);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Неверный формат даты: " + dateStr + ". Ожидается формат ISO8601.");
        }
    }
}
