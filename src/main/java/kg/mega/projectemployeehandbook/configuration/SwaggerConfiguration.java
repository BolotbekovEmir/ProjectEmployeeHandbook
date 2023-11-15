package kg.mega.projectemployeehandbook.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.time.LocalDate;
import java.time.LocalDateTime;

@EnableSwagger2
@Configuration
public class SwaggerConfiguration {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
            .select()
            .apis(RequestHandlerSelectors.basePackage("kg.mega.projectemployeehandbook"))
            .paths(PathSelectors.any())
            .build()
            .directModelSubstitute(LocalDate.class, java.sql.Date.class)
            .directModelSubstitute(LocalDateTime.class, java.util.Date.class)
            .apiInfo(apiInfo());
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
            .title("Employee handbook")
            .description("Application of viewing the employees and organizational structure.")
            .version("1.0")
            .contact(new Contact(
                "Emir Bolotbekov",
                "https://t.me/emirBolotbekov",
                "bolotbekove32@gmail.com"))
            .build();
    }

}