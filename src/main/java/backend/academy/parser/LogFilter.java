package backend.academy.parser;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Класс LogFilter представляет параметры фильтрации логов.
 * Используется для указания временного диапазона и дополнительных фильтров,
 * таких как HTTP-метод или User-Agent.
 *
 * Поля класса:
 * - from: Начальная дата и время анализа (включительно).
 * - to: Конечная дата и время анализа (включительно).
 * - filterField: Поле для фильтрации (например, "method" или "agent").
 * - filterValue: Значение фильтра для указанного поля (может включать шаблоны, такие как "Mozilla*").
 *
 * Конструктор принимает все параметры фильтрации.
 */
@Getter
@AllArgsConstructor
public class LogFilter {

    private LocalDateTime from;
    private LocalDateTime to;
    private String filterField;
    private String filterValue;
}
