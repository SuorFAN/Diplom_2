import api.auth.User;
import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class LoginTest {

    User user = new User();
    File bodyLogin = new File("src/test/resources/userLogin.json");
    File bodyRegister = new File("src/test/resources/user.json");
    File bodyBadUser = new File("src/test/resources/badUserLogin.json");


    @Before
    public void beforeTest() {
        user.createUser(bodyRegister)
                .then()
                .statusCode(200)
                .body("success", equalTo(true));
    }


    @Test
    @DisplayName("логин с неверным логином и паролем")
    public void badLoginUser() {
        user.login(bodyBadUser)
                .then()
                .statusCode(401)
                .body("success", equalTo(false))
                .body("message", equalTo("email or password are incorrect"));
    }

    @Test
    @DisplayName("логин под существующим пользователем")
    public void loginUser() {
        user.login(bodyLogin)
                .then()
                .statusCode(200)
                .body("success", equalTo(true));
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
