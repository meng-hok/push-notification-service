package com.kosign.push.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;

@Configuration
@EnableAuthorizationServer
public class AuthorizationConfiguration extends AuthorizationServerConfigurerAdapter {
    @Value("${admin.name}")
    String authName;
    @Value("${admin.password}")
    String authPassword;
    @Autowired 
    private AuthenticationManager authenticationManager;
    @Autowired 
    private PasswordEncoder password;
  
    /**
     |---------------------------------------------------------------------------------------- 
     |  Method Name              => 
     |  Parameters               => 
     |  Developed By             =>
     |  Created Date             =>
     |  Updated By               =>
     |  Updated Date             =>
     |----------------------------------------------------------------------------------------
     |
     |
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
                .withClient(authName)
                .secret(password.encode(authPassword))
                .scopes("read")
                .authorizedGrantTypes("password","refresh_token")
                .authorities("USER")
                .accessTokenValiditySeconds(1000)
                .refreshTokenValiditySeconds(1200);
    }
    
    /**
     * Using Authentication which inject from AuthenticationManager.java
     * 
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.tokenStore(tokenStore()).authenticationManager(authenticationManager);
    }

    @Bean
    public TokenStore tokenStore() { 
        return new  InMemoryTokenStore(); 
    }

    /**
     * To be used when logged out
     * 
     * @return
     */
    @Bean
    @Primary
    public DefaultTokenServices tokenServices() {
        DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
        defaultTokenServices.setTokenStore(tokenStore());
        defaultTokenServices.setSupportRefreshToken(true);
        return defaultTokenServices;
}

}