import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.DefaultConfiguration;
import org.apache.logging.log4j.core.layout.PatternLayout;
import org.apache.logging.log4j.core.osgi.BundleContextSelector;
import org.apache.logging.log4j.core.selector.ClassLoaderContextSelector;
import org.apache.logging.log4j.core.selector.NamedContextSelector;
import org.junit.jupiter.api.Test;

import javax.naming.Context;
import java.io.File;
import java.lang.module.Configuration;
import java.net.URI;

public class ContextPocTests {
    @Test
    public void getContextLogManagerTest(){
        URI configUri = new File("config.json").toURI();
        LoggerContext context = (LoggerContext) LogManager.getContext(null, false, configUri);
    }

    @Test
    public void getContextClassLoaderContextSelectorTest(){
        ClassLoaderContextSelector contextSelector = new ClassLoaderContextSelector();
        URI configUri = new File("config.json").toURI();
        LoggerContext context = contextSelector.getContext( ClassLoaderContextSelector.class.getName() ,null, false, configUri );
        context.setConfigLocation(null);
        contextSelector.getContext( ClassLoaderContextSelector.class.getName() ,null, false, configUri );
    }


    @Test
    // Здесь не очень понятно как подать Bundle пока что, но если подать любой Bundle то сработает POC
    public void getContextBundleContextSelectorTest(){
       BundleContextSelector contextSelector = new BundleContextSelector();
       URI configUri = new File("config.json").toURI();
       LoggerContext context = contextSelector.getContext( BundleContextSelector.class.getName() ,null, false, configUri );
        context.setConfigLocation(null);
        contextSelector.getContext( BundleContextSelector.class.getName() ,null, false, configUri );
    }



}
