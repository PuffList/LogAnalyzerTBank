package backend.academy.samples;

import backend.academy.analyzer.Statistics;
import backend.academy.formatter.AsciidocFormatter;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AsciidocFormatterTest {

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
        assertTrue(report.contains("| Файл(-ы) | access.log"));
        assertTrue(report.contains("| Количество запросов | 100"));
    }
}
