package com.chamados.API.config;

import com.chamados.API.security.CustomAuthentication;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Duration;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Component
@EnableWebSecurity
public class AuthorizationServerConfiguration {

    @Bean
    @Order(1)
    public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http) throws Exception {

        OAuth2AuthorizationServerConfigurer authorizationServerConfigurer =
                new OAuth2AuthorizationServerConfigurer();

        authorizationServerConfigurer.oidc(Customizer.withDefaults());

        RequestMatcher endpointsMatcher = authorizationServerConfigurer
                .getEndpointsMatcher();

        http
                .securityMatcher(endpointsMatcher)
                .authorizeHttpRequests(authorize -> authorize
                        .anyRequest().authenticated()
                )
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers(endpointsMatcher)
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(Customizer.withDefaults())
                )
                .formLogin(Customizer.withDefaults()
                )
                .with(authorizationServerConfigurer, Customizer.withDefaults());

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

    @Bean
    public TokenSettings tokenSettings() {
        return TokenSettings.builder()
                .accessTokenFormat(OAuth2TokenFormat.SELF_CONTAINED)
                .accessTokenTimeToLive(Duration.ofMinutes(60))
                .accessTokenTimeToLive(Duration.ofMinutes(90))
                .build();
    }

    @Bean
    public JWKSource<SecurityContext> jwkSource()  throws Exception {
        RSAKey rsaKey = generateKeyRSA();
        JWKSet jwkSet = new JWKSet(rsaKey);
        return new ImmutableJWKSet<>(jwkSet);
    }

    private RSAKey generateKeyRSA() throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();

        RSAPublicKey publickKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();

        return new RSAKey
                .Builder(publickKey)
                .privateKey(privateKey)
                .keyID(UUID.randomUUID().toString())
                .build();

    }

    @Bean
    public JwtDecoder jwtDecoder(JWKSource<SecurityContext> jwtSource) {
        return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwtSource);
    }

    public AuthorizationServerSettings authorizationServerSettings(){
        return AuthorizationServerSettings.builder()
                // obter token
                .tokenEndpoint("/oauth/token")
                // para consultar status do token
                .tokenIntrospectionEndpoint("/oauth2/introspect")
                // revogar
                .tokenRevocationEndpoint("/oauth2/revoke")
                // authorization endpoint
                .authorizationEndpoint("/oauth2/authorize")
                // informações do usuário OPEN ID CONNECT
                .oidcUserInfoEndpoint("/oauth2/userinfo")
                // obter a chave púvlica para verificar a assinatura do token
                .jwkSetEndpoint("/oauth2/jwks")
                // logout
                .oidcLogoutEndpoint("/oauth2/logout")
                .build();
    }

    public OAuth2TokenCustomizer<JwtEncodingContext> tokenCustomizer(){
        return context -> {
            var principal = context.getPrincipal();

            if (principal instanceof CustomAuthentication authentication){
                OAuth2TokenType tokenType = context.getTokenType();

                if (OAuth2TokenType.ACCESS_TOKEN.equals(tokenType)){
                    Collection<GrantedAuthority> authorities = authentication.getAuthorities();
                    List<String> authoritiesList =
                            authorities.stream().map(GrantedAuthority::getAuthority).toList();

                    context
                            .getClaims()
                            .claim("authorities", authoritiesList)
                            .claim("username", authentication.getUser().getUsername());
                }
            }
        };
    }




































}
