package com.soshiant.springbootexample.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile("dev")
@Configuration
public class SpringDocConfig {

  @Value("${application.version}")
  private String applicationVersion;

  @Value("${application.name}")
  private String applicationName;

  @Value("${application.description}")
  private String applicationDescription;


  @Bean
  public GroupedOpenApi publicApi() {
    return GroupedOpenApi.builder()
            .group("soshiant")
            .pathsToMatch("/soshiant/sbe/**")
            .build();
  }

  @Bean
  public OpenAPI springShopOpenAPI() {
    return new OpenAPI()
            .info(new Info()
                    .title(applicationName)
                    .description(applicationDescription)
                    .version(applicationVersion)
                    .license(new License().name("Apache 2.0").url("http://springdoc.org")))
            .externalDocs(new ExternalDocumentation()
                    .description("SpringShop Wiki Documentation")
                    .url("https://springshop.wiki.github.org/docs"));
  }
}
