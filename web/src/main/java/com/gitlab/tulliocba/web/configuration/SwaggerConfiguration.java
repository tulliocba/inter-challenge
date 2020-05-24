package com.gitlab.tulliocba.web.configuration;

import com.fasterxml.classmate.TypeResolver;
import com.gitlab.tulliocba.web.exceptionhandler.ApiError;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.service.Tag;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableSwagger2
public class SwaggerConfiguration {

    public static final String UNIQUE_NUMBERS_TAG = "uniqueNumbers";
    public static final String USERS_TAG = "users";
    private static final String ERROR_MODEL = "Error";

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.withClassAnnotation(RestController.class))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo())
                .useDefaultResponseMessages(false)
                .additionalModels(new TypeResolver().resolve(ApiError.class))
                .tags(new Tag(UNIQUE_NUMBERS_TAG, "Operations pertaining to UniqueNumbers."),
                        new Tag(USERS_TAG, "Operations pertaining to Users."))
                .globalResponseMessage(RequestMethod.POST, globalPostResponseMessages())
                .globalResponseMessage(RequestMethod.GET, globalGetAndPutResponseMessages())
                .globalResponseMessage(RequestMethod.DELETE, globalDeleteResponseMessages())
                .globalResponseMessage(RequestMethod.PUT, globalGetAndPutResponseMessages());
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Inter Challenge API")
                .description("This is an API that implements the inter bank's interview challenge.")
                .version("1.0")
                .contact(new Contact("Tulio Gabriel", "https://gitlab.com/tulliocba/",
                        "tulliocba@gmail.com")).build();
    }

    private List<ResponseMessage> globalGetAndPutResponseMessages() {
        return Arrays.asList(internalServerError(), badRequest(), ok(), notFound());
    }

    private List<ResponseMessage> globalDeleteResponseMessages() {
        return Arrays.asList(internalServerError(), badRequest(), noContent());
    }

    private List<ResponseMessage> globalPostResponseMessages() {
        return Arrays.asList(internalServerError(), badRequest(), created());
    }

    private ResponseMessage noContent() {
        return new ResponseMessageBuilder()
                .code(HttpStatus.NO_CONTENT.value())
                .message("No content").build();
    }


    private ResponseMessage created() {
        return new ResponseMessageBuilder()
                .code(HttpStatus.CREATED.value())
                .message("Created").build();
    }


    private ResponseMessage ok() {
        return new ResponseMessageBuilder()
                .code(HttpStatus.OK.value())
                .message("OK").build();
    }

    private ResponseMessage badRequest() {
        return new ResponseMessageBuilder()
                .code(HttpStatus.BAD_REQUEST.value())
                .message("Bad request")
                .responseModel(new ModelRef(ERROR_MODEL))
                .build();
    }

    private ResponseMessage notFound() {
        return new ResponseMessageBuilder()
                .code(HttpStatus.NOT_FOUND.value())
                .message("Not found")
                .responseModel(new ModelRef(ERROR_MODEL))
                .build();
    }

    private ResponseMessage internalServerError() {
        return new ResponseMessageBuilder()
                .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message("Internal server error")
                .responseModel(new ModelRef(ERROR_MODEL))
                .build();
    }

    @Controller
    public class RedirectSwaggerConfiguration {

        @GetMapping("/")
        public String redirectToSwaggerUI() {
            return "redirect:/swagger-ui.html";
        }
    }
}
