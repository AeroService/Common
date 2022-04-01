import de.natrox.common.consumer.ThrowingConsumer;
import de.natrox.common.function.ThrowingFunction;
import de.natrox.common.runnable.ThrowingRunnable;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FunctionalInterfaceTest {

    public void test() {
        var runnable = new ThrowingRunnable<IOException>() {
            @Override
            public void run() throws IOException {
                Files.createDirectories(Path.of(""));
            }
        };

        var consumer = new ThrowingConsumer<String, IOException>() {
            @Override
            public void accept(String s) throws IOException {
                Files.createDirectories(Path.of(""));
            }
        };

        var function = new ThrowingFunction<String, String, IOException>() {
            @Override
            public String apply(String s) throws IOException {
                Files.createDirectories(Path.of(""));
                return "null";
            }
        };
    }

}
