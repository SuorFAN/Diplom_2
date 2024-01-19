package api;

import config.Path;
import config.RestAssuredConfig;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import io.restassured.response.Validatable;
import io.restassured.response.ValidatableResponse;

import java.io.File;

import static io.restassured.RestAssured.given;

/**
 * POST https://stellarburgers.nomoreparties.site/api/orders
 */
public class Orders {

    @Step("Создание заказа с токеном {0}")
    public Validatable<ValidatableResponse, Response> getOrders(String accessToken) {
        return given()
                .spec(RestAssuredConfig.requestSpecification())
                .header("Authorization", accessToken)
                .when()
                .get(Path.ORDERS.getTitle());
    }

    @Step("Создание заказа")
    public Validatable<ValidatableResponse, Response> getOrders() {
        return given()
                .spec(RestAssuredConfig.requestSpecification())
                .when()
                .get(Path.ORDERS.getTitle());
    }

    @Step("Создание заказа с телом {0}")
    public Validatable<ValidatableResponse, Response> createOrders(File body) {

        return given()
                .spec(RestAssuredConfig.requestSpecification())
                .body(body)
                .when()
                .post(Path.ORDERS.getTitle());
    }

    @Step("Создание заказа с токеном {0} и телом {1}")
    public Validatable<ValidatableResponse, Response> createOrders(String accessToken, File body) {
        return given()
                .spec(RestAssuredConfig.requestSpecification())
                .header("Authorization", accessToken)
                .body(body)
                .when()
                .post(Path.ORDERS.getTitle());
    }
}
