package api.auth;

import config.Path;
import config.RestAssuredConfig;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import io.restassured.response.Validatable;
import io.restassured.response.ValidatableResponse;

import java.io.File;

import static io.restassured.RestAssured.given;

/**
 * GET https://stellarburgers.nomoreparties.site/api/auth/user
 */
public class User {

    @Step("Создание пользователя с телом {0}")
    public Validatable<ValidatableResponse, Response> createUser(File body) {

        return given()
                .spec(RestAssuredConfig.requestSpecification())
                .body(body)
                .when()
                .post(Path.REGISTER.getTitle());
    }

    @Step("Авторизация пользователя с телом {0}")
    public Validatable<ValidatableResponse, Response> login(File body) {

        return given()
                .spec(RestAssuredConfig.requestSpecification())
                .body(body)
                .when()
                .post(Path.LOGIN.getTitle());
    }


    @Step("Запрос на пользователя с токеном {0} и телом {1}")
    public Validatable<ValidatableResponse, Response> user(String accessToken, File body) {

        return given()
                .header("Authorization", accessToken)
                .spec(RestAssuredConfig.requestSpecification())
                .body(body)
                .when()
                .patch(Path.USER.getTitle());
    }

    @Step("Запрос на пользователя с телом {0}")
    public Validatable<ValidatableResponse, Response> user(File body) {

        return given()
                .spec(RestAssuredConfig.requestSpecification())
                .body(body)
                .when()
                .patch(Path.USER.getTitle());
    }

    @Step("Удалить пользователя с токеном {0}")
    public Validatable<ValidatableResponse, Response> deleteUser(String token) {
        return given()
                .header("Authorization", token)
                .spec(RestAssuredConfig.requestSpecification())
                .when()
                .delete(Path.USER.getTitle());
    }
}
