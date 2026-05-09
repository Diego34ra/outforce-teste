package com.outforce.coupon.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OpenApiConfig {

    @Bean
    public OpenAPI myOpenAPI() {

        Server devServer = new Server();
        devServer.setUrl("http://localhost:8081");
        devServer.setDescription("Development environment server URL");

        Contact contact = new Contact();
        contact.setEmail("diegoribeiro13ra@hotmail.com");
        contact.setName("Diego Ribeiro Araújo");
        contact.setUrl("mailto:diegoribeiro13ra@hotmail.com");

        Info info = new Info()
                .title("OutForce API")
                .version("1.0")
                .contact(contact)
                .description("This API exposes the endpoints developed for the OutForce technical challenge.")
                .termsOfService("");

        return new OpenAPI()
                .info(info)
                .servers(List.of(devServer));
    }

}
