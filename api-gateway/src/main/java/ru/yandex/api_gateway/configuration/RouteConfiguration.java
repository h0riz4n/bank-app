package ru.yandex.api_gateway.configuration;

import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
            });
  		return routes.build();
 	}
}
