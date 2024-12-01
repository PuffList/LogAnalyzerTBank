package backend.academy.samples;

import backend.academy.parser.LogFilter;
import backend.academy.parser.LogParser;
import backend.academy.parser.LogRecord;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class LogFilterTest {

    @Test
    void testLogFilteringByDate() throws Exception {
        File tempFile = File.createTempFile("test", ".log");
        Files.write(tempFile.toPath(), List.of(
            "127.0.0.1 - - [01/Jan/2024:12:00:00 +0000] \"GET /index.html HTTP/1.1\" 200 1234 \"-\" \"Mozilla/5.0\"",
            "127.0.0.1 - - [02/Jan/2024:12:00:00 +0000] \"GET /about.html HTTP/1.1\" 200 5678 \"-\" \"Mozilla/5.0\""
        ));
        LocalDateTime from = LocalDateTime.of(2024, 1, 2, 0, 0);
        LocalDateTime to = LocalDateTime.of(2024, 1, 2, 23, 59);
        LogFilter filter = new LogFilter(from, to, null, null);
        List<LogRecord> records = LogParser.parse(tempFile.getAbsolutePath(), filter);
        assertEquals(1, records.size());
        assertEquals("/about.html", records.get(0).resource());
        tempFile.delete();
    }

    @Test
    void testLogFilteringByMethod() throws Exception {
        File tempFile = File.createTempFile("test", ".log");
        Files.write(tempFile.toPath(), List.of(
            "127.0.0.1 - - [01/Jan/2024:12:00:00 +0000] \"GET /index.html HTTP/1.1\" 200 1234 \"-\" \"Mozilla/5.0\"",
            "127.0.0.1 - - [01/Jan/2024:12:00:01 +0000] \"POST /submit HTTP/1.1\" 200 5678 \"-\" \"Mozilla/5.0\""
        ));
        LogFilter filter = new LogFilter(null, null, "method", "GET");
        List<LogRecord> records = LogParser.parse(tempFile.getAbsolutePath(), filter);
        assertEquals(1, records.size());
        assertEquals("/index.html", records.get(0).resource());
        tempFile.delete();
    }

}
