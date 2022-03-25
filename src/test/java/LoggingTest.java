import de.natrox.common.logger.LogManager;
import de.natrox.common.logger.Logger;

public class LoggingTest {

    public static void main(String[] args) {
        System.setProperty("logger.debug", "true");

        Logger logger = LogManager.rootLogger();

        logger.debug("Test");
    }

}
