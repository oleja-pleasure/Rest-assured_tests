import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ReqresTests {
    @Test
    public void getUsersFirstName() {
        given()
                .get("https://reqres.in/api/users/2")
                .then()
                .statusCode(200)
                .body("data.first_name", is("Janet"));
    }

    @Test
    public void createUser() {
        given()
                .contentType(JSON)
                .body("{\"name\":\"morpheus\",\"job\":\"leader\"}")
                .when()
                .post("https://reqres.in/api/users")
                .then()
                .statusCode(201)
                .body("name", is("morpheus"), "job", is("leader"));
    }

    @Test
    public void updateUser() {
        String response =
                given()
                        .contentType(JSON)
                        .body("{\"name\":\"morpheus\",\"job\":\"leader\"}")
                        .when()
                        .post("https://reqres.in/api/users")
                        .then()
                        .statusCode(201)
                        .body("name", is("morpheus"), "job", is("leader"))
                        .extract().path("id");
        given()
                .contentType(JSON)
                .body("{\"name\":\"morpheus\",\"job\":\"zion resident\"}")
                .when()
                .put("https://reqres.in/api/users/" + response)
                .then()
                .statusCode(200)
                .body("name", is("morpheus"), "job", is("zion resident"));
    }

    @Test
    public void deleteUser() {
        String response =
                given()
                        .contentType(JSON)
                        .body("{\"name\":\"morpheus\",\"job\":\"leader\"}")
                        .when()
                        .post("https://reqres.in/api/users")
                        .then()
                        .statusCode(201)
                        .body("name", is("morpheus"), "job", is("leader"))
                        .extract().path("id");
        given()
                .delete("https://reqres.in/api/users/" + response)
                .then()
                .statusCode(204);
    }

    @Test
    public void unsuccessfulLogin() {
        String response =
                given()
                        .contentType(JSON)
                        .body("{\"email\":\"morpheus@zion.com\"}")
                        .when()
                        .post("https://reqres.in/api/login")
                        .then()
                        .statusCode(400)
                        .extract().path("error");
        assertEquals("Missing password", response);
    }
}