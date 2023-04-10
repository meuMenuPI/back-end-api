package meumenu.application.meumenu.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition
@Configuration
public class SwaggerConfig {


    @Bean
    public OpenAPI baseOpenAPI() {

        return new OpenAPI()
                .info(new Info().title("Meu Menu doc").version("1.0.0").description("Meu Menu doc"));

    }
}