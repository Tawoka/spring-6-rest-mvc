package com.rmorgner.spring6restmvc.config;

import org.springframework.context.annotation.*;
import org.springframework.security.config.annotation.web.builders.*;
import org.springframework.security.web.*;

@Configuration
public class SpringSecConfig {

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.csrf(httpSecurityCsrfConfigurer ->
        httpSecurityCsrfConfigurer.ignoringRequestMatchers("/api/**"));
    return http.build();
  }

}
