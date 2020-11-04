package com.kosign.push.configs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.service.Parameter;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.Contact;
import springfox.documentation.service.GrantType;
import springfox.documentation.service.OAuth;
import springfox.documentation.service.ResourceOwnerPasswordCredentialsGrant;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;

import springfox.documentation.swagger.web.ApiKeyVehicle;
import springfox.documentation.swagger.web.SecurityConfiguration;

/**
 * File Name : File Path : File Description : File Author : Created Date :
 * Developed By : Modified Date : Modified By :
 */

@Configuration
@EnableSwagger2
public class SpringFoxConfig {
  /**
   * |----------------------------------------------------------------------------------------
   * | Method Name => | Parameters => String username | Developed By => | Created
   * Date => 17-May-2020 | Updated By => | Updated Date =>
   * |----------------------------------------------------------------------------------------
   * | | |
   */
  @Value("${base.url}")
  private String OAuthServerUri;
  String securitySchemaOAuth2 = "oauth2schema";
  Predicate<String> SWAGGER_PATHS = PathSelectors.regex("^(?!.*error$).*");

  private OAuth securitySchema() {
    final AuthorizationScope authorizationScope = new AuthorizationScope("openid", "");
    final GrantType grantType = new ResourceOwnerPasswordCredentialsGrant("/oauth/token");
    return new OAuth(securitySchemaOAuth2, Arrays.asList(authorizationScope), Arrays.asList(grantType));
  }
  private ApiInfo apiInfo() {
  return new ApiInfo(
    "KOSIGN Push APIs",
    "API Documentation",
    "",
    "",
      new Contact("", "", ""),
      "", "", Collections.emptyList());
  }

  private SecurityContext securityContext() {
    return SecurityContext.builder().securityReferences(this.defaultAuth()).forPaths(SWAGGER_PATHS).build();
  }

  @Bean
  public Docket api() {
    ParameterBuilder aParameterBuilder = new ParameterBuilder();
    aParameterBuilder.name("Authorization")                 // name of header
                      .modelRef(new ModelRef("string"))
                      .parameterType("header")               // type - header
                      .defaultValue("Optional<Basic em9uZTpteXBhc3N3b3Jk>")        // based64 of - zone:mypassword
                      .required(false)                // for compulsory
                      .build();
    List<Parameter> aParameters = new ArrayList<>();
                  aParameters.add(aParameterBuilder.build());    

    return new Docket(DocumentationType.SWAGGER_2)
    
        .select().apis(RequestHandlerSelectors.any())
        .paths(PathSelectors.ant("/api/**"))
        .build()
        // .pathMapping("/api/public/**")
        // .globalOperationParameters(aParameters)
        .apiInfo(this.apiInfo())
        .securitySchemes(Collections.singletonList(securitySchema()))
//            .paths(PathSelectors.any()).build().securitySchemes(Collections.singletonList(securitySchema()))
        .securityContexts(Collections.singletonList(securityContext()));
  }

  private List<SecurityReference> defaultAuth() {
    final AuthorizationScope authorizationScope = new AuthorizationScope("openid   ", "");
    final AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
    authorizationScopes[0] = authorizationScope;
    return Arrays.asList(new SecurityReference(securitySchemaOAuth2, authorizationScopes));
  }

}