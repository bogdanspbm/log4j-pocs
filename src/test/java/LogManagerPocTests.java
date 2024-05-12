import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.net.URI;

public class LogManagerPocTests {
    @Test
    public void getFormatterLoggerLogManagerTest(){
        URI configUri = new File("config.json").toURI();
        LogManager.getLogger("a");
    }
}
