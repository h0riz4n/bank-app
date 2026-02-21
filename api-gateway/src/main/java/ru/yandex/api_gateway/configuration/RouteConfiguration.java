package ru.yandex.api_gateway.configuration;

import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class RouteConfiguration {

    private final DiscoveryClient discoveryClient;

    @Bean
	@RefreshScope
 	RouteLocator routeLocator(RouteLocatorBuilder builder) {
		log.debug("Refresh bean");
		RouteLocatorBuilder.Builder routes = builder.routes();
		discoveryClient.getServices()
            .forEach(service -> {
				routes.route(r -> r.path("/" + service).uri("lb://%s".formatted(service)));
				// routes.route(r -> r.path("/%s/api/**".formatted(service)).and().uri("lb://%s".formatted(service)));
                // routes.route(r -> r.path("/%s/v3/api-docs/swagger-config".formatted(service)).and().method(HttpMethod.GET).uri("lb://%s".formatted(service)));
                // routes.route(r -> r.path("/%s/v3/api-docs".formatted(service)).and().method(HttpMethod.GET).uri("lb://%s".formatted(service)));
            });
  		return routes.build();
 	}
}
