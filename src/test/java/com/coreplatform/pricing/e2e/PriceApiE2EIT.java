package com.coreplatform.pricing.e2e;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PriceApiE2EIT {

    @LocalServerPort int port;

    @BeforeEach
    void setup() { RestAssured.baseURI = "http://localhost"; RestAssured.port = port; }

    @Test void case1_2020_06_14_10_00() {
        given().param("productId", 35455).param("brandId", 1).param("applicationDate", "2020-06-14T10:00:00")
                .when().get("/prices")
                .then().statusCode(200).body("priceList", equalTo(1)).body("price", equalTo(35.50F));
    }

    @Test void case2_2020_06_14_16_00() {
        given().param("productId", 35455).param("brandId", 1).param("applicationDate", "2020-06-14T16:00:00")
                .when().get("/prices")
                .then().statusCode(200).body("priceList", equalTo(2)).body("price", equalTo(25.45F));
    }

    @Test void case3_2020_06_14_21_00() {
        given().param("productId", 35455).param("brandId", 1).param("applicationDate", "2020-06-14T21:00:00")
                .when().get("/prices")
                .then().statusCode(200).body("priceList", equalTo(1)).body("price", equalTo(35.50F));
    }

    @Test void case4_2020_06_15_10_00() {
        given().param("productId", 35455).param("brandId", 1).param("applicationDate", "2020-06-15T10:00:00")
                .when().get("/prices")
                .then().statusCode(200).body("priceList", equalTo(3)).body("price", equalTo(30.50F));
    }

    @Test void case5_2020_06_16_21_00() {
        given().param("productId", 35455).param("brandId", 1).param("applicationDate", "2020-06-16T21:00:00")
                .when().get("/prices")
                .then().statusCode(200).body("priceList", equalTo(4)).body("price", equalTo(38.95F));
    }

    @Test void returns404_whenNoPriceForDate() {
        given().param("productId", 35455).param("brandId", 1).param("applicationDate", "2019-01-01T00:00:00")
                .when().get("/prices")
                .then().statusCode(404);
    }

    @Test void returns400_whenInvalidDateFormat() {
        given().param("productId", 35455).param("brandId", 1).param("applicationDate", "fecha-mala")
                .when().get("/prices")
                .then().statusCode(400);
    }
}
