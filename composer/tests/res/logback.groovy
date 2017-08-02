// layout.setPattern("%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n");

import ch.qos.logback.classic.filter.*

appender("console", ConsoleAppender) {
    encoder(PatternLayoutEncoder) {
        pattern = "%d{yy/MM/dd HH:mm:ss.SSS} %thread [%level] %logger - %msg%n%ex{full}"
    }
}

root(INFO, ["console"])