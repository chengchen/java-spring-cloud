package io.opentracing.contrib.spring.cloud.feign;

import io.opentracing.Tracer;
import org.junit.Test;

import org.springframework.boot.test.util.EnvironmentTestUtils;
import org.springframework.cloud.netflix.feign.FeignContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

/**
 * @author Eddú Meléndez
 */
public class FeignTracingAutoConfigurationTest {

  @Test
  public void loadFeignTracingByDefault() {
    AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
    context.register(FeignContextConfig.class, TracerConfig.class, FeignTracingAutoConfiguration.class);
    context.refresh();
    FeignContext feignContext = context.getBean(TraceFeignContext.class);
    assertNotNull(feignContext);
  }

  @Test
  public void disableFeignTracing() {
    AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
    context.register(TracerConfig.class, FeignTracingAutoConfiguration.class);
    EnvironmentTestUtils.addEnvironment(context, "opentracing.spring.cloud.feign.enabled:false");
    context.refresh();
    String[] feignContextBeans = context.getBeanNamesForType(TraceFeignContext.class);
    assertThat(feignContextBeans.length, is(0));
  }

  @Configuration
  static class TracerConfig {

    @Bean
    public Tracer tracer() {
      return mock(Tracer.class);
    }
  }

  @Configuration
  static class FeignContextConfig {

    @Bean
    public TraceFeignContext feignContext() {
      return mock(TraceFeignContext.class);
    }
  }
}