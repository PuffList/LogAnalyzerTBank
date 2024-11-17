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
public class AppConfig {

    private static final DateTimeFormatter ISO8601_FORMATTER = DateTimeFormatter.ISO_DATE_TIME;
    @Getter
    private String pathOrUrl;
    @Getter
    private String format;
    @Getter
    private LocalDateTime from;
    @Getter
    private LocalDateTime to;

    /**
     * Создаёт конфигурацию приложения на основе аргументов командной строки.
     *
     * @param args массив аргументов командной строки.
     * @throws IllegalArgumentException если обязательные параметры отсутствуют или некорректны.
     */
    public AppConfig(String[] args) {
        Map<String, String> argMap = parseArgs(args);

        if (argMap.containsKey("path")) {
            this.pathOrUrl = argMap.get("path");
        } else {
            throw new IllegalArgumentException("Параметр --path обязателен.");
        }

        this.format = argMap.getOrDefault("format", "markdown");

        if (argMap.containsKey("from")) {
            this.from = parseDate(argMap.get("from"));
        }
        if (argMap.containsKey("to")) {
            this.to = parseDate(argMap.get("to"));
        }
    }

    /**
     * Парсит аргументы командной строки в карту ключ-значение.
     *
     * @param args массив аргументов командной строки.
     * @return карта аргументов, где ключи — это параметры без "--", а значения — их значения.
     */
    private Map<String, String> parseArgs(String[] args) {
        Map<String, String> argMap = new HashMap<>();
        for (int i = 0; i < args.length; i++) {
            if (args[i].startsWith("--")) {
                String key = args[i].substring(2);
                String value = (i + 1 < args.length && !args[i + 1].startsWith("--")) ? args[++i] : null;
                argMap.put(key, value);
            }
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
