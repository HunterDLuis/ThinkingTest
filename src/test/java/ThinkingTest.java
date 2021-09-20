import io.restassured.http.ContentType;
import io.restassured.response.Response;
import model.contact.PostContactRequest;
import model.user.PostAddUserRequest;
import model.user.PostLoginUserRequest;
import org.apache.http.HttpStatus;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static io.restassured.path.json.JsonPath.from;
import static org.hamcrest.Matchers.*;

public class ThinkingTest extends BaseApi {
    private String userToken = getUserToken();

    /*POST Add User*/
    @Test
    public void addUserSuccessfullyTest() {

        PostAddUserRequest userRequest = new PostAddUserRequest("Carla Test4", "Apellido Test4", "carlatest4@gmail.com", "myPassword");

        Response responseUser = genericMethodPostAddUser(userRequest, "users");

        int statusCode = responseUser.getStatusCode();
        String body = responseUser.getBody().asString();

        assertThat(statusCode, equalTo(HttpStatus.SC_CREATED));

        String token = from(body).get("token");
        assertThat(token, notNullValue());
    }

    @Test
    public void responseCodeBadRequestInAddUserWhenSendMandatoryFieldsEmptyTest() {

        PostAddUserRequest userRequest = new PostAddUserRequest("", "", "", "");

        Response responseUser = genericMethodPostAddUser(userRequest, "users");

        int statusCode = responseUser.getStatusCode();
        String body = responseUser.getBody().asString();

        assertThat(statusCode, equalTo(HttpStatus.SC_BAD_REQUEST));

        String message = from(body).get("message");
        assertThat(message, notNullValue());
        assertThat(message, equalTo("User validation failed: firstName: Path `firstName` is required., lastName: Path `lastName` is required., email: Email is invalid, password: Path `password` is required."));
    }

    @Test
    public void getFirsNameWhenAddUserSuccessfullyTest() {

        PostAddUserRequest userRequest = new PostAddUserRequest("Luis34", "Villa34", "luis34@gmail.com", "myPassword");

        Response responseUser = genericMethodPostAddUser(userRequest, "users");

        int statusCode = responseUser.getStatusCode();
        String body = responseUser.getBody().asString();

        assertThat(statusCode, equalTo(HttpStatus.SC_CREATED));

        String userId = from(body).get("user._id");
        String userFirstName = from(body).get("user.firstName");

        assertThat(userId, notNullValue());
        assertThat(userFirstName, equalTo("Luis34"));
    }

    /*Post Login User*/
    @Test
    public void LoginUserWithValidCredentialsTest() {
        PostLoginUserRequest loginUserRequest = new PostLoginUserRequest("carla@gmail.com", "Password");

        Response responseLoginUser = genericPostLoginUserRequest(loginUserRequest, "users/login");

        int statusCode = responseLoginUser.getStatusCode();
        String body = responseLoginUser.getBody().asString();
        String token = from(body).get("token");

        assertThat(statusCode, equalTo(HttpStatus.SC_OK));
        assertThat(token, notNullValue());
    }

    @Test
    public void loginUserWithInvalidCredentialsTest() {
        PostLoginUserRequest loginUserRequest = new PostLoginUserRequest("carla1@gmail.com", "Password1");

        Response responseLoginUser = genericPostLoginUserRequest(loginUserRequest, "users/login");

        int statusCode = responseLoginUser.getStatusCode();
        assertThat(statusCode, equalTo(HttpStatus.SC_UNAUTHORIZED));
    }

    @Test
    public void verifyIfMatchUserWithValidCredentialsTest() {
        PostLoginUserRequest loginUserRequest = new PostLoginUserRequest("carla@gmail.com", "Password");

        Response responseLoginUser = genericPostLoginUserRequest(loginUserRequest, "users/login");

        int statusCode = responseLoginUser.getStatusCode();
        String body = responseLoginUser.getBody().asString();
        String userEmail = from(body).getString("user.email");

        assertThat(statusCode, equalTo(HttpStatus.SC_OK));
        assertThat(userEmail, equalTo("carla@gmail.com"));
    }

    /*Get User*/
    @Test
    public void verifyStatusCode200WithValidCredentialsTest() {
        Response response = genericGetRequest(userToken, "users/me");
        int statusCode = response.getStatusCode();
        String body = response.getBody().asString();
        String userEmail = from(body).getString("email");

        assertThat(statusCode, equalTo(HttpStatus.SC_OK));
        assertThat(userEmail, equalTo("carla@gmail.com"));
    }

    @Test
    public void verifyStatusCode401WithInvalidCredentialsTest() {
        Response response = genericGetRequest(userToken + "0", "users/me");
        int statusCode = response.getStatusCode();

        assertThat(statusCode, equalTo(HttpStatus.SC_UNAUTHORIZED));
    }

    @Test
    public void verifyReceivesAMessageWhenSendWithoutTokenTest() {
        Response response = genericGetRequest("", "users/me");
        int statusCode = response.getStatusCode();
        String body = response.getBody().asString();
        String errorMessage = from(body).getString("error");

        assertThat(statusCode, equalTo(HttpStatus.SC_UNAUTHORIZED));
        assertThat(errorMessage, equalTo("Please authenticate."));
    }

    /*PATH user*/
    @Test
    public void UpdateUserFirstName() {
        PostAddUserRequest userRequest = new PostAddUserRequest("Carlos", "Lopez", "carla@gmail.com", "Password");

        Response response = genericPathUserRequest(userToken, userRequest, "/users/me");

        String body = response.getBody().asString();
        String updateFirstName = from(body).get("firstName");

        assertThat(updateFirstName, equalTo("Carlos"));

    }

    /*POST Log out User*/
    @Test
    public void verifyStatusCodeWhenUseValidTokenTest() {

        /*Login User*/
        String userToken = given()
                .header("Content-Type", "application/json")
                .body("{\n" +
                        "    \"email\": \"luis20@gmail.com\",\n" +
                        "    \"password\": \"myPassword\"\n" +
                        "}")
                .post("users/login")
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .body("token", notNullValue())
                .extract().jsonPath().getString("token");

        Response responseUser = genericMethodPostLogOutUser(userToken, "users/logout");

        int statusCode = responseUser.getStatusCode();
        assertThat(statusCode, equalTo(HttpStatus.SC_OK));
    }

    @Test
    public void verifyStatusCodeWhenUseInvalidTokenTest() {

        /*Login User*/
        String userToken = given()
                .header("Content-Type", "application/json")
                .body("{\n" +
                        "    \"email\": \"luis20@gmail.com\",\n" +
                        "    \"password\": \"myPassword\"\n" +
                        "}")
                .post("users/login")
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .body("token", notNullValue())
                .extract().jsonPath().getString("token");
        Response responseUser = genericMethodPostLogOutUser(userToken + "a", "users/logout");

        int statusCode = responseUser.getStatusCode();
        assertThat(statusCode, equalTo(HttpStatus.SC_UNAUTHORIZED));
    }

    @Test
    public void verifyIfReceiveErrorMessageWhenUseTokenEmptyTest() {
        Response responseUser = genericMethodPostLogOutUser("", "users/logout");

        int statusCode = responseUser.getStatusCode();
        String body = responseUser.getBody().asString();
        String errorMessage = from(body).get("error");

        assertThat(statusCode, equalTo(HttpStatus.SC_UNAUTHORIZED));
        assertThat(errorMessage, equalTo("Please authenticate."));
    }

    /*POST Add Contact*/
    @Test
    public void addContactSuccessfullyTest() {
        PostContactRequest contactRequest = new PostContactRequest("Rita", "Vidal", "1990-01-01", "rita@gmail.com", "8005555555",
                "1 Main St.", "Apartment A", "Anytown", "KS", "12345", "USA");

        /*Login User*/
        String userToken = given()
                .header("Content-Type", "application/json")
                .body("{\n" +
                        "    \"email\": \"luis20@gmail.com\",\n" +
                        "    \"password\": \"myPassword\"\n" +
                        "}")
                .post("users/login")
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .body("token", notNullValue())
                .extract().jsonPath().getString("token");

        String validateResponse = genericMethodPostAddContact(userToken, contactRequest, "contacts", HttpStatus.SC_CREATED);
        assertThat(validateResponse, notNullValue());
    }

    @Test
    public void verifyResponseOfAddContactWhenSendMandatoryFieldsEmptyTest() {
        PostContactRequest contactRequest = new PostContactRequest("", "", "1990-01-01", "rita@gmail.com", "8005555555",
                "1 Main St.", "Apartment A", "Anytown", "KS", "12345", "USA");

        /*Login User*/
        String userToken = given()
                .header("Content-Type", "application/json")
                .body("{\n" +
                        "    \"email\": \"luis20@gmail.com\",\n" +
                        "    \"password\": \"myPassword\"\n" +
                        "}")
                .post("users/login")
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .body("token", notNullValue())
                .extract().jsonPath().getString("token");

        String validateResponse = genericMethodPostAddContact(userToken, contactRequest, "contacts", HttpStatus.SC_BAD_REQUEST);
        String message = from(validateResponse).get("message");
        assertThat(message, notNullValue());
        assertThat(message, equalTo("Contact validation failed: firstName: Path `firstName` is required., lastName: Path `lastName` is required."));
    }

    @Test
    public void verifyResponseWhenUseInvalidTokenTest() {
        PostContactRequest contactRequest = new PostContactRequest("Luis", "Dimitri", "1990-01-01", "rita@gmail.com", "8005555555",
                "1 Main St.", "Apartment A", "Anytown", "KS", "12345", "USA");

        /*Login User*/
        String userToken = given()
                .header("Content-Type", "application/json")
                .body("{\n" +
                        "    \"email\": \"luis20@gmail.com\",\n" +
                        "    \"password\": \"myPassword\"\n" +
                        "}")
                .post("users/login")
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .body("token", notNullValue())
                .extract().jsonPath().getString("token");

        String validateResponse = genericMethodPostAddContact(userToken + "a", contactRequest, "contacts", HttpStatus.SC_UNAUTHORIZED);
        String errorMessage = from(validateResponse).get("error");
        assertThat(errorMessage, notNullValue());
        assertThat(errorMessage, equalTo("Please authenticate."));
    }

    @Test
    public void getResponseWhenAddContactSuccessfullyTest() {
        PostContactRequest contactRequest = new PostContactRequest("Java java", "Du", "1990-01-01", "rita@gmail.com", "8005555555",
                "1 Main St.", "Apartment A", "Anytown", "KS", "12345", "USA");

        /*Login User*/
        String userToken = given()
                .header("Content-Type", "application/json")
                .body("{\n" +
                        "    \"email\": \"luis20@gmail.com\",\n" +
                        "    \"password\": \"myPassword\"\n" +
                        "}")
                .post("users/login")
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .body("token", notNullValue())
                .extract().jsonPath().getString("token");

        String validateResponse = genericMethodPostAddContact(userToken, contactRequest, "contacts", HttpStatus.SC_CREATED);

        String contactId = from(validateResponse).get("_id");
        assertThat(contactId, notNullValue());
        String contactBirthdate = from(validateResponse).get("birthdate");
        assertThat(contactBirthdate, equalTo("1990-01-01"));
    }



    /*GET CONTACT LIST */

    @Test
    public void getContactListUserWithValidCredentialsTest() {
        Response response = genericGetRequest(userToken, "contacts");

        int statusCode = response.getStatusCode();
        assertThat(statusCode, equalTo(HttpStatus.SC_OK));
    }

    @Test
    public void getContactListUserWithInvalidCredentialsTest() {
        Response response = genericGetRequest(userToken + "0", "contacts");
        int statusCode = response.getStatusCode();

        assertThat(statusCode, equalTo(HttpStatus.SC_UNAUTHORIZED));
    }

    @Test
    public void  getAllContactListTest2() {
        Response response = genericGetRequest(userToken, "contacts");

        String body = response.getBody().asString();
        int v = from(body).getInt("[0].__v");
        String email = from(body).getString("[0].email");

        assertThat(v, equalTo(0 ));
        assertThat(email, equalTo("juan@gmail.com"));

        System.out.println("logeo: " + v);
        System.out.println("Email :" + email);
    }
    @Test
    public void  getAllContactListTest3() {
        Response response = genericGetRequest(userToken, "contacts");

        String body = response.getBody().asString();
        String country = from(body).getString("[1].country");
        String email = from(body).getString("[1].email");

        assertThat(country, equalTo("USA"));
        assertThat(email, equalTo("jdoe1@fake.com"));

    }


    /*Refactor Method*/
    //AddUser
    private Response genericMethodPostAddUser(PostAddUserRequest userRequest, String path) {
        Response response = given()
                .contentType(ContentType.JSON)
                .body(userRequest)
                .post(path);
        return response;
    }

    //LoginUser
    private Response genericPostLoginUserRequest(PostLoginUserRequest loginUserRequest, String path) {
        Response response = given()
                .contentType(ContentType.JSON)
                .body(loginUserRequest)
                .post(path);

        return response;
    }

    //GetUser
    private Response genericGetRequest(String token, String path) {
        Response response = given()
                .auth()
                .oauth2(token)
                .when()
                .get(path);
        return response;
    }

    //PathUser
    private Response genericPathUserRequest(String token, PostAddUserRequest userRequest, String path) {
        Response response = given()
                .auth()
                .oauth2(token)
                .contentType(ContentType.JSON)
                .body(userRequest)
                .patch(path);
        return response;
    }

    //Log out User
    private Response genericMethodPostLogOutUser(String token, String path) {
        Response response = given()
                .auth()
                .oauth2(token)
                .post(path);
        return response;
    }

    //Contact
    private String genericMethodPostAddContact(String token, PostContactRequest contactRequest, String request, int codeStatus) {
        String response = given()
                .auth()
                .oauth2(token)
                .contentType(ContentType.JSON)
                .body(contactRequest)
                .post(request)
                .then()
                .assertThat()
                .statusCode(codeStatus)
                .extract().body().asString();
        return response;
    }

    //GetToken
    private String getUserToken() {
        PostLoginUserRequest loginUserRequest = new PostLoginUserRequest("carla@gmail.com", "Password");

        Response responseLoginUser = genericPostLoginUserRequest(loginUserRequest, "users/login");

        String body = responseLoginUser.getBody().asString();
        String userToken = from(body).getString("token");
        assertThat(userToken, notNullValue());

        return userToken;
    }

    //Get Contact List
//    private Response genericGetContactListRequest(String token, String path) {
//        Response response = given()
//                .auth()
//                .oauth2(token)
//                .when()
//                .get(path);
//        return response;
//
//    }
}
