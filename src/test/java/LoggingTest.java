import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggingTest {

    public static void main(String[] args) {
        System.setProperty("logger.debug", "true");

        Logger logger = LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);

        logger.debug("Test");
    }

}
