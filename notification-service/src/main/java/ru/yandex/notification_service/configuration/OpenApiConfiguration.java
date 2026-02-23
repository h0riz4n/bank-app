package ru.yandex.notification_service.configuration;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.OAuthFlow;
import io.swagger.v3.oas.models.security.OAuthFlows;
import io.swagger.v3.oas.models.security.Scopes;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import ru.yandex.notification_service.property.ApplicationProperty;

@Configuration
@EnableConfigurationProperties(ApplicationProperty.class)
public class OpenApiConfiguration {

    private final ApplicationProperty appProperty;
    private final String issuerUri;

    public OpenApiConfiguration(
        ApplicationProperty applicationProperty,
        @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}") String issuerUri
    ) {
        this.appProperty = applicationProperty;
        this.issuerUri = issuerUri;
    }

    @Bean
    OpenAPI openAPI() {
        return new OpenAPI()
            .info(
                new Info()
                    .title(appProperty.getInfo().getTitle())
                    .version(appProperty.getInfo().getVersion())
                    .description(appProperty.getInfo().getDescription())
            )
            .servers(
                List.of(
                    new Server()
                        .url(appProperty.getServer().getUrl())
                        .description(appProperty.getServer().getDescription())
                )
            )
            .components(getComponents());
    }

    private Components getComponents() {
        Components components = new Components();
        components.addSecuritySchemes(
            "OAuth",
            new SecurityScheme().type(SecurityScheme.Type.OAUTH2).flows(new OAuthFlows().clientCredentials(oauthFlow(issuerUri)))
        );
        return components;
    }

    private OAuthFlow oauthFlow(String issuerUri) {
        Scopes scopes = new Scopes();
        scopes.addString("openid", "openid");
        return new OAuthFlow()
            .scopes(scopes)
            .authorizationUrl("%s%s".formatted(issuerUri, "/protocol/openid-connect/auth"))
            .tokenUrl("%s%s".formatted(issuerUri, "/protocol/openid-connect/token"))
            .refreshUrl("%s%s".formatted(issuerUri, "/protocol/openid-connect/token"));
    }

}
