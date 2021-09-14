import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.junit.BeforeClass;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class ThinkingTest {
    @BeforeClass
    public void setup(){
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
    }
    /*POST Add User*/
    @Test
    public void createUserSuccessfullyTest(){
        given()
                .contentType(ContentType.JSON)
                .body("{\n" +
                        "    \"firstName\": \"Luis20\",\n" +
                        "    \"lastName\": \"Villa20\",\n" +
                        "    \"email\": \"luis20@gmail.com\",\n" +
                        "    \"password\": \"myPassword\"\n" +
                        "}")
                .post("https://thinking-tester-contact-list.herokuapp.com/users")
                .then()
                .statusCode(HttpStatus.SC_CREATED);

    }
    @Test
    public void responseCodeBadRequestWhenAddInvalidUserTest(){
        given()
                .contentType(ContentType.JSON)
                .log().all()
                .body("{\n" +
                        "    \"firstName\": \"\",\n" +
                        "    \"lastName\": \"\",\n" +
                        "    \"email\": \"\",\n" +
                        "    \"password\": \"\"\n" +
                        "}")
                .post("https://thinking-tester-contact-list.herokuapp.com/users")
                .then()
                .log().all()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body("message", notNullValue());

    }

    @Test
    public void getResponseWhenAddUserSuccessfullyTest(){
        String userFirstName = given()
                .contentType(ContentType.JSON)
                .log().all()
                .body("{\n" +
                        "    \"firstName\": \"Luis21\",\n" +
                        "    \"lastName\": \"Villa21\",\n" +
                        "    \"email\": \"luis21@gmail.com\",\n" +
                        "    \"password\": \"myPassword\"\n" +
                        "}")
                .post("https://thinking-tester-contact-list.herokuapp.com/users")
                .then()
                .log().all()
                .statusCode(HttpStatus.SC_CREATED)
                .body("token", notNullValue())
                .body("user._id", notNullValue())
                .extract().jsonPath().getString("firstName");

        assertThat(userFirstName, equalTo("Luis21"));
    }
    /**/
}
