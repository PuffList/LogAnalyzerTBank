package backend.academy.samples;

import backend.academy.formatter.AsciidocFormatter;
import backend.academy.formatter.MarkdownFormatter;
import backend.academy.parser.LogParser;
import backend.academy.parser.LogRecord;
import backend.academy.analyzer.LogAnalyzer;
import backend.academy.analyzer.Statistics;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LogParserTest {

    @Test
    void testParseLocalFile() throws Exception {
        File tempFile = File.createTempFile("test", ".log");
        Files.write(tempFile.toPath(), List.of(
            "127.0.0.1 - - [01/Jan/2024:12:00:00 +0000] \"GET /index.html HTTP/1.1\" 200 1234 \"-\" \"Mozilla/5.0\""
        ));
        List<LogRecord> records = LogParser.parse(tempFile.getAbsolutePath(), null, null);
        assertEquals(1, records.size());
        assertEquals("127.0.0.1", records.get(0).ipAddress());
        assertEquals("/index.html", records.get(0).resource());
        tempFile.delete();
    }

    @Test
    void testParseFromUrl() throws Exception {
        String url = "https://raw.githubusercontent.com/elastic/examples/master/Common%20Data%20Formats/nginx_logs/nginx_logs";
        List<LogRecord> records = LogParser.parse(url, null, null);
        assertFalse(records.isEmpty());
    }

    @Test
    void testLogParserCorrectness() throws Exception {
        File tempFile = File.createTempFile("test", ".log");
        Files.write(tempFile.toPath(), List.of(
            "127.0.0.1 - - [01/Jan/2024:12:00:00 +0000] \"GET /index.html HTTP/1.1\" 200 1234 \"-\" \"Mozilla/5.0\""
        ));
        List<LogRecord> records = LogParser.parse(tempFile.getAbsolutePath(), null, null);
        assertEquals(1, records.size());
        LogRecord record = records.get(0);
        assertEquals("127.0.0.1", record.ipAddress());
        assertEquals("/index.html", record.resource());
        assertEquals(200, record.statusCode());
        assertEquals(1234, record.responseSize());
        tempFile.delete();
    }

    @Test
    void testLogFilteringByDate() throws Exception {
        File tempFile = File.createTempFile("test", ".log");
        Files.write(tempFile.toPath(), List.of(
            "127.0.0.1 - - [01/Jan/2024:12:00:00 +0000] \"GET /index.html HTTP/1.1\" 200 1234 \"-\" \"Mozilla/5.0\"",
            "127.0.0.1 - - [02/Jan/2024:12:00:00 +0000] \"GET /about.html HTTP/1.1\" 200 5678 \"-\" \"Mozilla/5.0\""
        ));
        LocalDateTime from = LocalDateTime.of(2024, 1, 2, 0, 0);
        LocalDateTime to = LocalDateTime.of(2024, 1, 2, 23, 59);
        List<LogRecord> records = LogParser.parse(tempFile.getAbsolutePath(), null, null);
        List<LogRecord> filtered = records.stream()
            .filter(record -> {
                LocalDateTime timestamp = record.timestamp().toLocalDateTime();
                return (from == null || !timestamp.isBefore(from)) &&
                    (to == null || !timestamp.isAfter(to));
            })
            .collect(Collectors.toList());
        assertEquals(1, filtered.size());
        assertEquals("/about.html", filtered.get(0).resource());
        tempFile.delete();
    }

    @Test
    void testStatisticsCalculation() {
        List<LogRecord> records = List.of(
            new LogRecord("127.0.0.1", OffsetDateTime.now(), "/index.html", 200, 500),
            new LogRecord("127.0.0.1", OffsetDateTime.now(), "/index.html", 200, 1000),
            new LogRecord("127.0.0.1", OffsetDateTime.now(), "/about.html", 404, 300)
        );
        LogAnalyzer analyzer = new LogAnalyzer(records);
        Statistics stats = analyzer.getStatistics();
        assertEquals(3, stats.totalRequests());
        assertEquals(2, stats.resourceCounts().get("/index.html"));
        assertEquals(1, stats.resourceCounts().get("/about.html"));
        assertEquals(1000, stats.percentile95ResponseSize());
    }

    @Test
    void testMarkdownFormatter() {
        Statistics stats = new Statistics();
        stats.path("access.log");
        stats.totalRequests(100);
        stats.averageResponseSize(500);
        stats.percentile95ResponseSize(950);
        MarkdownFormatter formatter = new MarkdownFormatter();
        String report = formatter.format(stats);
        assertTrue(report.contains("#### Общая информация"));
        assertTrue(report.contains("| Файл(-ы) | `access.log` |"));
        assertTrue(report.contains("| Количество запросов | 100 |"));
    }

    @Test
    void testAsciiDocFormatter() {
        Statistics stats = new Statistics();
        stats.path("access.log");
        stats.totalRequests(100);
        stats.averageResponseSize(500);
        stats.percentile95ResponseSize(950);
        AsciidocFormatter formatter = new AsciidocFormatter();
        String report = formatter.format(stats);
        assertTrue(report.contains("== Общая информация"));
        assertTrue(report.contains("| Файл(-ы) | `access.log`"));
        assertTrue(report.contains("| Количество запросов | 100"));
    }


}
