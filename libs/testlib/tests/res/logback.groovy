appender("console", ConsoleAppender) {
  encoder(PatternLayoutEncoder) {
    pattern = "%d{yy/MM/dd HH:mm:ss.SSS} %thread [%level] %logger - %msg%n%ex{full}"
  }
}

root(INFO, ["console"])
