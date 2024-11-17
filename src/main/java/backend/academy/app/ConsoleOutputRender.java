package backend.academy.app;

import java.io.PrintStream;

/**
 * Реализация {@link OutputRender} для вывода в консоль.
 */
public class ConsoleOutputRender implements OutputRender {

    private static final PrintStream OUT = System.out;

    @Override
    public void render(String output) {
        OUT.println(output);
    }
}
