import de.natrox.common.console.Console;

public class ConsoleTest {

    public static void main(String[] args) throws Exception {
        var console = Console
            .builder()
            .prompt(() -> "")
            .build();

    }

}
