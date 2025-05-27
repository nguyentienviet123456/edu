package edu.vietgo.global.exam.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI quizApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Quiz API")
                        .description("API for managing quizzes and quiz attempts")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("VietGo Global")
                                .email("contact@vietgo.global")
                                .url("https://vietgo.global"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8080")
                                .description("Local Development Server"),
                        new Server()
                                .url("https://api.vietgo.global")
                                .description("Production Server")
                ));
    }
} 