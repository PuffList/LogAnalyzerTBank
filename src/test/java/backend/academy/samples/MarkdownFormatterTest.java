package backend.academy.samples;

import backend.academy.analyzer.Statistics;
import backend.academy.formatter.MarkdownFormatter;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

public class MarkdownFormatterTest {

    @Test
    void testMarkdownFormatter() {
        Statistics stats = Mockito.mock(Statistics.class);

        when(stats.path()).thenReturn("access.log");
        when(stats.totalRequests()).thenReturn(100);
        when(stats.averageResponseSize()).thenReturn(500.0);
        when(stats.percentile95ResponseSize()).thenReturn(950.0);

        MarkdownFormatter formatter = new MarkdownFormatter();
        String report = formatter.format(stats);

        assertTrue(report.contains("#### Общая информация"));
        assertTrue(report.contains("| Файл(-ы) | access.log |"));
        assertTrue(report.contains("| Количество запросов | 100 |"));
        assertTrue(report.contains("| Средний размер ответа | 500"));
        assertTrue(report.contains("| 95p размера ответа | 950"));
    }

    @Test
    void testMarkdownFormatterWithNullTimeParameters() {
        Statistics stats = Mockito.mock(Statistics.class);

        when(stats.from()).thenReturn(null);
        when(stats.to()).thenReturn(null);

        MarkdownFormatter formatter = new MarkdownFormatter();
        String report = formatter.format(stats);

        assertTrue(report.contains("| Начальная дата | -"));
        assertTrue(report.contains("| Конечная дата | -"));
    }
}
