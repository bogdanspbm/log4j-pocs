import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.net.URI;

public class ContextPocTests {
    @Test
    public void getContextPocTest(){
        URI configUri = new File("config.json").toURI();
        LoggerContext context = (LoggerContext) LogManager.getContext(null, false, configUri);
    }

}
