package backend.academy.app;

/**
 * Интерфейс для вывода результатов анализа.
 */
public interface OutputRender {

    /**
     * Выводит текстовый отчёт.
     *
     * @param output текст отчёта.
     */
    void render(String output);
}
