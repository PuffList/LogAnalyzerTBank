package backend.academy.app;

import backend.academy.config.AppConfig;
import lombok.experimental.UtilityClass;

/**
 * Главный класс запуска приложения анализа логов.
 * Создаёт конфигурацию и запускает приложение.
 */
@UtilityClass
public class Main {

    /**
     * Точка входа в приложение.
     *
     * @param args аргументы командной строки.
     */
    public static void main(String[] args) {
        AppConfig config = new AppConfig(args);
        OutputRender outputRenderer = new ConsoleOutputRender();
        ParserApp app = new ParserApp(config, outputRenderer);

        app.run();
    }
}
