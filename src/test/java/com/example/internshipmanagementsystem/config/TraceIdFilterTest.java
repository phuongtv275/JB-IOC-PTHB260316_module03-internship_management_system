package com.example.internshipmanagementsystem.config;

import static org.assertj.core.api.Assertions.assertThat;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;

class TraceIdFilterTest {

  @AfterEach
  void clearSecurityContext() {
    SecurityContextHolder.clearContext();
  }

  @Test
  void shouldLogRequestMetadata_withoutQueryValuesOrSensitiveData() throws Exception {
    Logger logger = (Logger) LoggerFactory.getLogger(TraceIdFilter.class);
    ListAppender<ILoggingEvent> appender = new ListAppender<>();
    appender.start();
    logger.addAppender(appender);
    MockHttpServletRequest request = new MockHttpServletRequest("GET", "/api/users");
    request.setQueryString("token=secret-value");
    request.setAttribute(TraceIdFilter.AUTHENTICATED_ROLE_ATTRIBUTE, "ADMIN");
    MockHttpServletResponse response = new MockHttpServletResponse();

    try {
      new TraceIdFilter().doFilter(request, response, (req, res) -> response.setStatus(200));
    } finally {
      logger.detachAppender(appender);
    }

    assertThat(appender.list)
        .extracting(ILoggingEvent::getFormattedMessage)
        .anySatisfy(
            message -> {
              assertThat(message).contains("IMS_REQUEST METHOD=GET PATH=/api/users STATUS=200");
              assertThat(message).contains("ROLE=ADMIN");
              assertThat(message).doesNotContain("secret-value");
            });
  }
}
