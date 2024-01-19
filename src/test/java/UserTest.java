import api.auth.User;
import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class UserTest {

    String token;
    User user = new User();
    File bodyLogin = new File("src/test/resources/userLogin.json");
    File bodyRegister = new File("src/test/resources/user.json");
    File bodyChangeUser = new File("src/test/resources/newCredits.json");
    String userEmail = "new_gfhjds111@yandex.ru";
    String userName = "new_AFedorov";


    @Before
    public void beforeTest() {
        user.createUser(bodyRegister)
                .then()
                .statusCode(200)
                .body("success", equalTo(true));
    }

    @Test @DisplayName("Изменение данных пользователя с авторизацией")
    public void changeNameAndEmailOfUser() {
        token = user.login(bodyLogin)
                .then()
                .statusCode(200)
                .body("accessToken", notNullValue())
                .extract()
                .response().path("accessToken");
        user.user(token, bodyChangeUser)
                .then()
                .statusCode(200)
                .body("success", equalTo(true))
                .body("user.email", equalTo(userEmail))
                .body("user.name", equalTo(userName));
    }

    @Test @DisplayName("Изменение данных пользователя без авторизации")
    public void changeData() {
        user.user(bodyChangeUser)
                .then()
                .statusCode(401)
                .body("success", equalTo(false))
                .body("message", equalTo("You should be authorised"));
    }

    @Before
    @After
    public void deleteUser() {
        if (user.login(bodyChangeUser).then().extract().response().statusCode() == 200) {
            user.deleteUser(user.login(bodyChangeUser)
                            .then()
                            .statusCode(200)
                            .body("accessToken", notNullValue())
                            .extract()
                            .response().path("accessToken"))
                    .then()
                    .statusCode(202)
                    .body("success", equalTo(true));
        } else if (user.login(bodyLogin).then().extract().response().statusCode() == 200) {
            user.deleteUser(user.login(bodyLogin)
                            .then()
                            .statusCode(200)
                            .body("accessToken", notNullValue())
                            .extract()
                            .response().path("accessToken"))
                    .then()
                    .statusCode(202)
                    .body("success", equalTo(true));
        }
    }
}
