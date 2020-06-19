package com.kosign.push.configs;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.GrantType;
import springfox.documentation.service.OAuth;
import springfox.documentation.service.ResourceOwnerPasswordCredentialsGrant;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import springfox.documentation.builders.ApiInfoBuilder;

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
  private String OAuthServerUri = "http://localhost:8080";
  String securitySchemaOAuth2 = "oauth2schema";
  Predicate<String> SWAGGER_PATHS = PathSelectors.regex("^(?!.*error$).*");

  private OAuth securitySchema() {
    // 这里设置 client 的 scope
    final AuthorizationScope authorizationScope = new AuthorizationScope("openid", "允许测试阶段访问的所有接口");
    final GrantType grantType = new ResourceOwnerPasswordCredentialsGrant(OAuthServerUri + "/oauth/token");
    return new OAuth(securitySchemaOAuth2, Arrays.asList(authorizationScope), Arrays.asList(grantType));
  }
  // private ApiInfo apiInfo() {
  // return new ApiInfo(
  // "Api Documentation",
  // "Some custom description of API.",
  // "API TOS",
  // "Terms of service",
  // new Contact("John Doe", "www.example.com", "myeaddress@company.com"),
  // "License of API", "API license URL", Collections.emptyList());
  // }

  private SecurityContext securityContext() {
    return SecurityContext.builder().securityReferences(this.defaultAuth()).forPaths(SWAGGER_PATHS).build();
  }

  @Bean
  public Docket api() {
    return new Docket(DocumentationType.SWAGGER_2).select().apis(RequestHandlerSelectors.any())
        .paths(PathSelectors.ant("/api/public/**")).build().securitySchemes(Collections.singletonList(securitySchema()))
//            .paths(PathSelectors.any()).build().securitySchemes(Collections.singletonList(securitySchema()))
        .securityContexts(Collections.singletonList(securityContext()));
  }

  private List<SecurityReference> defaultAuth() {
    final AuthorizationScope authorizationScope = new AuthorizationScope("openid", "允许测试阶段访问的所有接口");
    final AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
    authorizationScopes[0] = authorizationScope;
    return Arrays.asList(new SecurityReference(securitySchemaOAuth2, authorizationScopes));
  }

}