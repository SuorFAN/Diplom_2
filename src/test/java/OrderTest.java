import api.Orders;
import api.auth.User;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class OrderTest {
    Orders orders = new Orders();
    User user = new User();
    String token;
    String userName;

    File bodyLogin = new File("src/test/resources/userLogin.json");
    File bodyRegister = new File("src/test/resources/user.json");

    File bodyOrder = new File("src/test/resources/order.json");
    File bodyBadOrder = new File("src/test/resources/badOrder.json");
    File bodyEmptyOrder = new File("src/test/resources/emptyOrder.json");

    @Before
    public void beforeTest() {
        user.createUser(bodyRegister)
                .then()
                .statusCode(200)
                .body("success", equalTo(true));

        Response r = user.login(bodyLogin)
                .then()
                .statusCode(200)
                .body("accessToken", notNullValue())
                .extract()
                .response();
        token = r.path("accessToken");
        userName = r.path("user.name");
    }

    @Test
    @DisplayName("Создание заказа с неверным хешем ингредиентов")
    public void createBadOrder() {
        orders.createOrders(bodyBadOrder)
                .then()
                .statusCode(500);
    }

    @Test
    @DisplayName("Создание заказа с авторизацией")
    public void createOrderWithAuth() {
        orders.createOrders(token, bodyOrder)
                .then()
                .statusCode(200)
                .body("success", equalTo(true))
                .body("name", equalTo("Экзо-плантаго space бессмертный флюоресцентный бургер"))
                .body("order.number", notNullValue())
                .body("order.owner.name", equalTo(userName));
    }

    @Test
    @DisplayName("Создание заказа с авторизацией")
    public void createOrderWithoutAuth() {
        orders.createOrders(bodyOrder)
                .then()
                .statusCode(200)
                .body("success", equalTo(true))
                .body("name", equalTo("Экзо-плантаго space бессмертный флюоресцентный бургер"))
                .body("order.number", notNullValue());
    }


    @Test
    @DisplayName("Создание заказа без ингредиентов")
    public void createOrderWithoutIngridients() {
        orders.createOrders(bodyEmptyOrder)
                .then()
                .statusCode(400)
                .body("success", equalTo(false))
                .body("message", equalTo("Ingredient ids must be provided"));
    }

    @Test
    @DisplayName("Получение заказов конкретного пользователя авторизованный пользователь")
    public void getOrdersAuthUser() {
        orders.getOrders(token)
                .then()
                .statusCode(200)
                .body("success", equalTo(true))
                .body("orders", notNullValue());
    }

    @Test
    @DisplayName("Получение заказов конкретного пользователя неавторизованный пользователь")
    public void getOrdersNotAuthUser() {
        orders.getOrders()
                .then()
                .statusCode(401)
                .body("success", equalTo(false))
                .body("message", equalTo("You should be authorised"));
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
