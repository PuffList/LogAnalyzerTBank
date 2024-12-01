package backend.academy.samples;

import backend.academy.parser.LogFilter;
import backend.academy.parser.LogParser;
import backend.academy.parser.LogRecord;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.nio.file.Files;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class LogParserTest {

    @Test
    void testParseSingleLogRecord() throws Exception {
        File tempFile = File.createTempFile("test", ".log");
        Files.write(tempFile.toPath(), List.of(
            "127.0.0.1 - - [01/Jan/2024:12:00:00 +0000] \"GET /index.html HTTP/1.1\" 200 1234 \"-\" \"Mozilla/5.0\""
        ));
        LogFilter filter = new LogFilter(null, null, null, null);
        List<LogRecord> records = LogParser.parse(tempFile.getAbsolutePath(), filter);

        assertEquals(1, records.size());
        LogRecord record = records.get(0);
        assertEquals("127.0.0.1", record.ipAddress());
        assertEquals("/index.html", record.resource());
        assertEquals(200, record.statusCode());
        assertEquals(1234, record.responseSize());

        tempFile.delete();
    }

    @Test
    void testParseFromUrl() {
        String url = "https://raw.githubusercontent.com/elastic/examples/master/Common%20Data%20Formats/nginx_logs/nginx_logs";
        LogFilter filter = new LogFilter(null, null, null, null);
        List<LogRecord> records = LogParser.parse(url, filter);

        assertEquals(false, records.isEmpty());
    }

    @Test
    void testLogParserCorrectness() throws Exception {
        File tempFile = File.createTempFile("test", ".log");
        Files.write(tempFile.toPath(), List.of(
            "127.0.0.1 - - [01/Jan/2024:12:00:00 +0000] \"GET /index.html HTTP/1.1\" 200 1234 \"-\" \"Mozilla/5.0\""
        ));
        LogFilter filter = new LogFilter(null, null, null, null);
        List<LogRecord> records = LogParser.parse(tempFile.getAbsolutePath(), filter);
        assertEquals(1, records.size());
        LogRecord record = records.get(0);
        assertEquals("127.0.0.1", record.ipAddress());
        assertEquals("/index.html", record.resource());
        assertEquals(200, record.statusCode());
        assertEquals(1234, record.responseSize());
        tempFile.delete();
    }

}
