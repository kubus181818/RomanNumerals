package com.adobe.RomanNumerals;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.stream.Stream;

import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.documentationConfiguration;


@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RomanNumeralsTests {

    private RequestSpecification spec;

    @LocalServerPort
    int port;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
    }

    @BeforeEach
    public void setUp(RestDocumentationContextProvider restDocumentation) {
        this.spec = new RequestSpecBuilder().addFilter(documentationConfiguration(restDocumentation))
                .build();
    }

    @Test
    void testRomanConversionCorrectness() {
        RestAssured.given(this.spec)
                .accept("application/json")
                .param("query", "4")
                .filter(document("index",
                        requestParameters(
                                parameterWithName("query").description("Arabic number to convert")
                        ),
                        responseFields(
                                fieldWithPath("input").description("Input arabic number"),
                                fieldWithPath("output").description("Output roman number"))
                ))
                .when().get("romannumeral")
                .then().assertThat().statusCode(200)
                .and().body("input", Matchers.equalTo("4"))
                .and().body("output", Matchers.equalTo("IV"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"-1933219038219083190", "-1", "0", "256", "test", "2.4", "null", ""})
    void testValidationException(String input) {
        RestAssured.given(this.spec)
                .accept("application/json")
                .param("query", input)
                .when().get("romannumeral")
                .then().assertThat().statusCode(400)
                .and().body("code", Matchers.equalTo(2)) ;
    }

}
