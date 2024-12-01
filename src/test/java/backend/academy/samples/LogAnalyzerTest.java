package backend.academy.samples;

import backend.academy.analyzer.LogAnalyzer;
import backend.academy.analyzer.Statistics;
import backend.academy.parser.LogRecord;
import org.junit.jupiter.api.Test;
import java.time.OffsetDateTime;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class LogAnalyzerTest {

    @Test
    void testStatisticsCalculation() {
        List<LogRecord> records = List.of(
            new LogRecord("127.0.0.1", OffsetDateTime.now(), "/index.html", 200, 500),
            new LogRecord("127.0.0.1", OffsetDateTime.now(), "/index.html", 200, 1000),
            new LogRecord("127.0.0.1", OffsetDateTime.now(), "/about.html", 404, 300),
            new LogRecord("127.0.0.1", OffsetDateTime.now(), "/about.html", 200, 0)
        );

        LogAnalyzer analyzer = new LogAnalyzer(records);
        Statistics stats = analyzer.getStatistics();

        assertEquals(4, stats.totalRequests());
        assertEquals(2, stats.resourceCounts().get("/index.html"));
        assertEquals(2, stats.resourceCounts().get("/about.html"));
        assertEquals(1000, stats.percentile95ResponseSize());
        assertEquals(300, stats.minResponseSize());
        assertEquals(1, stats.zeroResponseCount());
    }
}
