import api.auth.User;
import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class RegisterTest {
    String token;
    User user = new User();
    File bodyLogin = new File("src/test/resources/userLogin.json");
    File bodyRegister = new File("src/test/resources/user.json");
    File bodyBadRegister = new File("src/test/resources/badUser.json");


    @Test
    @DisplayName("создать пользователя, который уже зарегистрирован")
    public void createDuplicateUser() {
        user.createUser(bodyRegister)
                .then()
                .statusCode(200)
                .body("success", equalTo(true));
        user.createUser(bodyRegister).then()
                .statusCode(403)
                .body("success", equalTo(false))
                .body("message", equalTo("User already exists"));
    }

    @Test
    @DisplayName("создать уникального пользователя")
    public void createUser() {
        token = user.createUser(bodyRegister)
                .then()
                .statusCode(200)
                .body("success", equalTo(true))
                .extract().path("accessToken");
    }

    @Test
    @DisplayName("создать пользователя и не заполнить одно из обязательных полей")
    public void createBadUser() {
        user.createUser(bodyBadRegister)
                .then()
                .statusCode(403)
                .body("success", equalTo(false))
                .body("message", equalTo("Email, password and name are required fields"));
    }

    @Before
    @After
    public void deleteUser() {
        if (user.login(bodyLogin).then().extract().response().statusCode() == 200) {
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
