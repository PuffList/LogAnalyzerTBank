package backend.academy.parser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.experimental.UtilityClass;

/**
 * Парсер логов NGINX. Преобразует строки логов в объекты {@link LogRecord}.
 * Поддерживает чтение из локальных файлов и URL.
 */
@UtilityClass
public class LogParser {
    
    private static final int IP_ADRESS_INDEX = 1;
    private static final int DATE_TIME_INDEX = 2;
    private static final int RESOURCE_GROUP_INDEX = 4;
    private static final int STATUS_CODE_INDEX = 5;
    private static final int RESPONSE_SIZE_INDEX = 6;
    private static final Logger LOGGER = Logger.getLogger(LogParser.class.getName());
    private static final Pattern LOG_PATTERN = Pattern.compile(
        "^(\\S+) \\S+ \\S+ \\[(.+?)] \"(\\S+) (\\S+) \\S+\" (\\d{3}) (\\d+) \"(.*?)\" \"(.*?)\""
    );
    private static final DateTimeFormatter DATE_FORMATTER =
        DateTimeFormatter.ofPattern("dd/MMM/yyyy:HH:mm:ss Z", Locale.ENGLISH);

    /**
     * Парсит лог-файлы по указанному пути или URL и возвращает список записей.
     *
     * @param pathOrUrl путь к файлу логов или URL.
     * @param from начальная дата диапазона анализа (может быть null).
     * @param to конечная дата диапазона анализа (может быть null).
     * @return список объектов {@link LogRecord}.
     */
    public static List<LogRecord> parse(String pathOrUrl, LocalDateTime from, LocalDateTime to) {
        List<LogRecord> logs = new ArrayList<>();

        try {
            if (pathOrUrl.startsWith("http://") || pathOrUrl.startsWith("https://")) {
                URL url = new URL(pathOrUrl);
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        parseLine(line, logs, from, to);
                    }
                }
            } else {
                Path path = Path.of(pathOrUrl);
                Files.lines(path).forEach(line -> parseLine(line, logs, from, to));
            }
        } catch (Exception e) {
            LOGGER.severe("Ошибка при обработке ресурса: " + pathOrUrl);
        }

        return logs;
    }

    private static void parseLine(String line, List<LogRecord> logs, LocalDateTime from, LocalDateTime to) {
        Matcher matcher = LOG_PATTERN.matcher(line);

        if (matcher.find()) {
            try {
                OffsetDateTime timestamp = OffsetDateTime.parse(matcher.group(DATE_TIME_INDEX), DATE_FORMATTER);
                LocalDateTime logDate = timestamp.toLocalDateTime();
                if ((from != null && logDate.isBefore(from)) || (to != null && logDate.isAfter(to))) {
                    return;
                }
                String ipAddress = matcher.group(IP_ADRESS_INDEX);
                String resource = matcher.group(RESOURCE_GROUP_INDEX);
                int statusCode = Integer.parseInt(matcher.group(STATUS_CODE_INDEX));
                int responseSize = Integer.parseInt(matcher.group(RESPONSE_SIZE_INDEX));
                LogRecord record = new LogRecord(ipAddress, timestamp, resource, statusCode, responseSize);
                logs.add(record);
            } catch (Exception e) {
                LOGGER.severe("Ошибка парсинга строки: " + line);
            }
        }
    }

}
