import io.qameta.allure.*;
import io.qameta.allure.junit4.*;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import model.contact.ContactRequest;
import model.user.UserRequest;
import model.user.PostLoginUserRequest;
import org.apache.http.HttpStatus;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static io.restassured.path.json.JsonPath.from;
import static org.hamcrest.Matchers.*;

public class ThinkingTest extends BaseApi {
    /*POST Add User*/
    @Test
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Post add user response 201")
    @Description("Verify that the user receives status code 201 when their register is successful.")
    public void addUserSuccessfullyTest(){
        UserRequest userRequest = UserRequest.builder()
                .firstName("Luis32")
                .lastName("VIlla32")
                .email("luis32@gmail.com")
                .password("myPassword")
                .build();

        Response responseUser = genericMethodPostAddUser(userRequest, "users");

        int statusCode = responseUser.getStatusCode();
        String body = responseUser.getBody().asString();

        assertThat(statusCode, equalTo(HttpStatus.SC_CREATED));

        String token = from(body).get("token");
        assertThat(token, notNullValue());
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Post add user response 400")
    @Description("Verify that the user receive status code 400 when user register with mandatory fields empty")
    @Story("Add User")
    public void matchMessageAndStatusCode400WhenMandatoryFieldsEmptyTest(){
        UserRequest userRequest = UserRequest.builder()
                .firstName("")
                .lastName("")
                .email("")
                .password("")
                .build();

        Response responseUser = genericMethodPostAddUser(userRequest, "users");

        int statusCode = responseUser.getStatusCode();
        String body = responseUser.getBody().asString();

        assertThat(statusCode, equalTo(HttpStatus.SC_BAD_REQUEST));

        String message = from(body).get("message");
        assertThat(message, notNullValue());
        assertThat(message, equalTo("User validation failed: firstName: Path `firstName` is required., lastName: Path `lastName` is required., email: Email is invalid, password: Path `password` is required."));
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Post add user - verify firstName")
    @Description("Verify that the json response contains the same firstName that the user registered ")
    @Story("Add User")
    public void matchFirsNameWhenAddUserSuccessfullyTest(){
        UserRequest userRequest = UserRequest.builder()
                .firstName("Luis34")
                .lastName("Villa34")
                .email("Luis34@gmail.com")
                .password("myPassword")
                .build();

        Response responseUser = genericMethodPostAddUser(userRequest, "users");

        int statusCode = responseUser.getStatusCode();
        String body = responseUser.getBody().asString();
        assertThat(statusCode, equalTo(HttpStatus.SC_CREATED));

        String userFirstName = from(body).get("user.firstName");
        assertThat(userFirstName, equalTo("Luis34"));
    }

    /*Post Login User*/
    @Test
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Post login user with valid credentials")
    @Description("Verify that user receives a status code 200  when request login with valid Credentials")
    @Story("Login User")
    public void loginUserWithValidCredentialsTest(){
        PostLoginUserRequest loginUserRequest = PostLoginUserRequest.builder()
                .email("Luis34@gmail.com")
                .password("myPassword")
                .build();

        System.out.println(loginUserRequest);

        Response responseLoginUser = genericPostLoginUserRequest(loginUserRequest, "users/login");

        int statusCode = responseLoginUser.getStatusCode();
        String body = responseLoginUser.getBody().asString();
        String token = from(body).get("token");

        assertThat(statusCode, equalTo(HttpStatus.SC_OK));
        assertThat(token, notNullValue());
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Post login user with invalid credentials")
    @Description("Verify that user receives a status code 401 when request login with invalid Credentials")
    @Story("Login User")
    public void loginUserWithInvalidCredentialsTest(){
        PostLoginUserRequest loginUserRequest = PostLoginUserRequest.builder()
                .email("Luis341@gmail.com")
                .password("myPassword")
                .build();

        Response responseLoginUser = genericPostLoginUserRequest(loginUserRequest, "users/login");

        int statusCode = responseLoginUser.getStatusCode();
        assertThat(statusCode, equalTo(HttpStatus.SC_UNAUTHORIZED));
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Post login user - Verify user email")
    @Description("Verify that Json response contains the same email  that the user entered.")
    @Story("Login User")
    public void verifyEmailWithValidCredentialsTest(){
        PostLoginUserRequest loginUserRequest = PostLoginUserRequest.builder()
                .email("Luis34@gmail.com")
                .password("myPassword")
                .build();

        Response responseLoginUser = genericPostLoginUserRequest(loginUserRequest, "users/login");

        int statusCode = responseLoginUser.getStatusCode();
        String body = responseLoginUser.getBody().asString();
        String userEmail = from(body).getString("user.email");

        assertThat(statusCode, equalTo(HttpStatus.SC_OK));
        assertThat(userEmail, equalTo("luis34@gmail.com"));
    }
    /*Get User*/
    @Test
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Get user with valid token")
    @Description("Verify that user receives a status code 200 when sends a request with the valid token ")
    @Story("Get User")
    public void verifyStatusCode200WithValidCredentialsTest(){
        Response response = genericGetRequest(getUserTokenFromLoginUser(), "users/me");
        int statusCode = response.getStatusCode();

        assertThat(statusCode, equalTo(HttpStatus.SC_OK));
    }
    @Test
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Get user with invalid token")
    @Description("Verify that the user receives a status code 401 when  sends a request with the invalid token")
    @Story("Get User")
    public void verifyStatusCode401WithInvalidCredentialsTest(){
        Response response = genericGetRequest(getUserTokenFromLoginUser()+"0", "users/me");
        int statusCode = response.getStatusCode();

        assertThat(statusCode, equalTo(HttpStatus.SC_UNAUTHORIZED));
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Receives error message when send token empty")
    @Description("Verify that the user receives a message when sends a request without the token")
    @Story("Get User")
    public void errorMessageWhenSendWithoutTokenTest(){
        Response response = genericGetRequest("", "users/me");
        int statusCode = response.getStatusCode();
        String body = response.getBody().asString();
        String errorMessage = from(body).getString("error");

        assertThat(statusCode, equalTo(HttpStatus.SC_UNAUTHORIZED));
        assertThat(errorMessage, equalTo("Please authenticate."));
    }

    /*POST Add Contact*/
    @Test
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Add contact successfully")
    @Description("Verify that the user receive valid status code 201 when register a contact successfully")
    @Story("Post contact")
    public void addContactSuccessfullyTest(){
        ContactRequest contactRequest = ContactRequest
                .builder()
                .firstName("Diego")
                .lastName("Cadima")
                .birthdate("1991/01/01")
                .email("diego@gmail.com")
                .phone("74896512")
                .postalCode("1524")
                .build();

        String validateResponse =  genericMethodPostAddContact(getUserTokenFromLoginUser(), contactRequest, "contacts", HttpStatus.SC_CREATED);
        assertThat(validateResponse, notNullValue());
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Error message when mandatory fields empty")
    @Description("Verify that user receive invalid status code 400 when register a contact with the mandatory fields empty")
    @Story("Post contact")
    public void errorMessageWhenSendMandatoryFieldsEmptyTest(){
        ContactRequest contactRequest = ContactRequest
                .builder()
                .firstName("")
                .lastName("")
                .birthdate("")
                .email("")
                .phone("")
                .postalCode("")
                .build();

        String validateResponse = genericMethodPostAddContact(getUserTokenFromLoginUser(), contactRequest, "contacts", HttpStatus.SC_BAD_REQUEST);
        String message = from(validateResponse).get("message");
        assertThat(message, notNullValue());
        assertThat(message, equalTo("Contact validation failed: firstName: Path `firstName` is required., lastName: Path `lastName` is required., birthdate: Birthdate is invalid, email: Expected a string but received a null, phone: Expected a string but received a null, postalCode: Expected a string but received a null"));
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Error message when send invalid token")
    @Description("Verify that user receive invalid status code 401 when use invalid token")
    @Story("Post contact")
    public void verifyResponseWhenUseInvalidTokenTest(){
        ContactRequest contactRequest = ContactRequest
                .builder()
                .firstName("Richar")
                .lastName("Cadima")
                .build();

        String validateResponse = genericMethodPostAddContact(getUserTokenFromLoginUser()+"a", contactRequest, "contacts", HttpStatus.SC_UNAUTHORIZED);
        String errorMessage = from(validateResponse).get("error");
        assertThat(errorMessage, notNullValue());
        assertThat(errorMessage, equalTo("Please authenticate."));
    }

    @Test
    @Severity(SeverityLevel.MINOR)
    @DisplayName("Response json contain the same birthdate")
    @Description("Verify that the response contains the same birthdate as the contact registered")
    @Story("Post contact")
    public void getResponseWhenAddContactSuccessfullyTest(){
        ContactRequest contactRequest = ContactRequest
                .builder()
                .firstName("Susana")
                .lastName("Gimenes")
                .birthdate("1991/01/01")
                .email("susana@gmail.com")
                .phone("74896512")
                .postalCode("1524")
                .build();

        String validateResponse = genericMethodPostAddContact(getUserTokenFromLoginUser(), contactRequest, "contacts", HttpStatus.SC_CREATED);

        String contactId = from(validateResponse).get("_id");
        assertThat(contactId, notNullValue());
        String contactBirthdate = from(validateResponse).get("birthdate");
        assertThat(contactBirthdate, equalTo("1991/01/01"));
    }

    /*GET CONTACT LIST */

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Response status code 200 with valid token")
    @Description("Verify that user receives a status code 200 when sends a request  Contact list with  the valid token")
    @Story("Get contacts")
    public void getContactListUserWithValidTokenTest() {
        Response response = genericGetRequest(getUserTokenFromLoginUser(), "contacts");

        int statusCode = response.getStatusCode();
        assertThat(statusCode, equalTo(HttpStatus.SC_OK));
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Response status code 401 with invalid token")
    @Description("Verify that the user receives a status code 401 when sends a request  Contact list   invalid token.")
    @Story("Get contacts")
    public void getInvalidStatusCodeWithInvalidTokenTest() {
        Response response = genericGetRequest(getUserTokenFromLoginUser() + "0", "contacts");
        int statusCode = response.getStatusCode();

        assertThat(statusCode, equalTo(HttpStatus.SC_UNAUTHORIZED));
    }

    @Test
    @Severity(SeverityLevel.MINOR)
    @DisplayName("Session number in contact is integer")
    @Description("Verify that the session number of a new contact is integer")
    @Story("Get contacts")
    public void  verifyThatSessionNumberInContactIsIntegerTest() {
        Response response = genericGetRequest(getUserTokenFromLoginUser(), "contacts");

        String body = response.getBody().asString();
        int v = from(body).getInt("[0].__v");

        assertThat(v, equalTo(0 ));

        System.out.println("logeo: " + v);
    }
    @Test
    @Severity(SeverityLevel.MINOR)
    @DisplayName("Filter contact list")
    @Description("Verify that is possible to get all contact list information of user  using page filter")
    @Story("Get contacts")
    public void  getAllContactListTest3() {
        Response response = genericGetRequest(getUserTokenFromLoginUser(), "contacts");

        String body = response.getBody().asString();

        List<Map> country= from(body).get(" findAll { list -> list.country  == 'USA' }");
        System.out.println("*********************" );
        System.out.println("Country :" + country);
        System.out.println("Country size :" + country.size());
    }

    /*PATH user*/
    @Test
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Status code 200 when update user first name")
    @Description("Verify that the user  receives response  status code 200 when sends a request with a valid token.")
    @Story("Path user")
    public void verifyResponseStatus200WhenUpdateUserTest(){
        UserRequest userRequest = UserRequest.builder()
                .firstName("Carlos").build();

        Response response = genericPathUserRequest(getUserTokenFromLoginUser(), userRequest, "/users/me");

        int status = response.getStatusCode();
        assertThat(status, equalTo(HttpStatus.SC_OK));
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Match user first name with valid token")
    @Description("Verify that the response json content the same first name that the user updated")
    @Story("Path user")
    public void UpdateUserFirstName(){
        UserRequest userRequest = UserRequest.builder()
                .firstName("Carlita").build();

        Response response = genericPathUserRequest(getUserTokenFromLoginUser(), userRequest, "/users/me");

        String body = response.getBody().asString();
        String updateFirstName = from(body).get("firstName");

        assertThat(updateFirstName, equalTo("Carlita"));
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Response json is not null")
    @Description("Verify that the user receives  json when sends a update with a valid token.")
    @Story("Path user")
    public void verifyResponseJsonIsNotNullTest(){
        UserRequest userRequest = UserRequest.builder()
                .firstName("Carla").build();

        Response response = genericPathUserRequest(getUserTokenFromLoginUser(), userRequest, "/users/me");

        String body = response.getBody().asString();
        assertThat(body, notNullValue());
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Status code 401 when send invalid token")
    @Description("Verify that the user receives response status code 401 when sends a request with a invalid token.")
    @Story("Path user")
    public void verifyStatusIs401WhenInvalidTokenTest(){
        UserRequest userRequest = UserRequest.builder()
                .firstName("Carlita").build();

        Response response = genericPathUserRequest(getUserTokenFromLoginUser()+"0", userRequest, "/users/me");

        int status = response.getStatusCode();
        assertThat(status, equalTo(HttpStatus.SC_UNAUTHORIZED));
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Error message when send invalid token")
    @Description("Verify that the user receives a message when sends a request with an invalid  token.")
    @Story("Path user")
    public void verifyErrorMessageWhenInvalidTokenTest(){
        UserRequest userRequest = UserRequest.builder()
                .firstName("Carlita").build();

        Response response = genericPathUserRequest(getUserTokenFromLoginUser()+"0", userRequest, "/users/me");

        String body = response.getBody().asString();
        String errorMessage = from(body).get("error");
        assertThat(errorMessage, equalTo("Please authenticate."));
    }

    /*POST Log out User*/
    @Test
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Status code 200 when use valid token")
    @Description("Verify that user authenticated receive response with status code 200 when logout with valid token")
    @Story("Post log out user")
    public void verifyStatusCode200WhenUseValidTokenTest(){
        Response responseUser = genericMethodPostLogOutUser(getUserTokenFromLoginUser(), "users/logout");

        int statusCode = responseUser.getStatusCode();
        assertThat(statusCode, equalTo(HttpStatus.SC_OK));
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Status code 401 when use invalid token")
    @Description("Verify that user receives a status code 401 when request logout with invalid token")
    @Story("Post log out user")
    public void verifyStatusCodeWhenUseInvalidTokenTest(){

        Response responseUser = genericMethodPostLogOutUser(getUserTokenFromLoginUser()+"a", "users/logout");

        int statusCode = responseUser.getStatusCode();
        assertThat(statusCode, equalTo(HttpStatus.SC_UNAUTHORIZED));
    }

    @Test
    @Severity(SeverityLevel.MINOR)
    @DisplayName("Error message when use token empty")
    @Description("Verify that the user receive a message when use an token empty for request logout")
    @Story("Post log out user")
    public void verifyIfReceiveErrorMessageWhenUseTokenEmptyTest(){
        Response responseUser = genericMethodPostLogOutUser("", "users/logout");

        int statusCode = responseUser.getStatusCode();
        String body = responseUser.getBody().asString();
        String errorMessage = from(body).get("error");

        assertThat(statusCode, equalTo(HttpStatus.SC_UNAUTHORIZED));
        assertThat(errorMessage, equalTo("Please authenticate."));
    }

    /*PUT CONTACT*/
    @Test
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Statud code 200 when updated a contact")
    @Description("Verify that the user receives status code 200 when the request update is successful.")
    @Story("Put contact")
    public void verifyResponseStatus200WhenUpdateContactTest(){
        String token = getUserTokenFromLoginUser();
        Response response = genericGetRequest(token, "contacts");

        String body = response.getBody().asString();
        String idContact = from(body).getString("[0]._id");
        assertThat(idContact, notNullValue());

        ContactRequest contactRequest = ContactRequest
                .builder()
                .birthdate("1990-01-01")
                .phone("74859632")
                .build();

        Response responseUpdateContact = responseUpdateContact(contactRequest, token,"contacts/", idContact);

        int status = responseUpdateContact.getStatusCode();
        assertThat(status, equalTo(HttpStatus.SC_OK));
    }
    @Test
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Status code 401 when token is empty")
    @Description("Verify that the user receives status code 401 when the Token  is empty.")
    @Story("Put contact")
    public void verifyResponseStatus401WhenEmptyTokenTest(){
        Response response = genericGetRequest(getUserTokenFromLoginUser(), "contacts");

        String body = response.getBody().asString();
        String idContact = from(body).getString("[0]._id");
        assertThat(idContact, notNullValue());

        ContactRequest contactRequest = ContactRequest
                .builder()
                .birthdate("1990-01-01")
                .phone("74859632")
                .build();

        Response responseUpdateContact = responseUpdateContact(contactRequest, "","contacts/", idContact);

        int status = responseUpdateContact.getStatusCode();
        assertThat(status, equalTo(HttpStatus.SC_UNAUTHORIZED));
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Status code 400 when invalid id")
    @Description("Verify that the user receives status code 400 when the Id  is invalid.")
    @Story("Put contact")
    public void verifyResponseStatus400WhenInvalidIdTest(){
        String token = getUserTokenFromLoginUser();
        Response response = genericGetRequest(token, "contacts");

        String body = response.getBody().asString();
        String idContact = from(body).getString("[0]._id");
        assertThat(idContact, notNullValue());

        ContactRequest contactRequest = ContactRequest
                .builder()
                .birthdate("1990-01-01")
                .build();

        Response responseUpdateContact = responseUpdateContact(contactRequest, token,"contacts/", idContact+"0");

        int status = responseUpdateContact.getStatusCode();
        assertThat(status, equalTo(HttpStatus.SC_BAD_REQUEST));
    }

    /*Delete Contact*/

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Delete contact - Status code 200 whit valid id")
    @Description("Verify that the user receives status code 200 when sends a request delete with a valid token.")
    @Story("Delete a contact")
    public void deleteContactWithIdTest(){
        String token = getUserTokenFromLoginUser();
        Response response = genericGetRequest(token, "contacts");

        String body = response.getBody().asString();
        String idContact = from(body).getString("[0]._id");

        Response responseDeleteContact = responseDeleteContact(token, "contacts/", idContact);

        int status = responseDeleteContact.getStatusCode();
        assertThat(status, equalTo(HttpStatus.SC_OK));
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Delete contact - Status code 401 whit valid id")
    @Description("Verify that the user receives status code 401 if request delete a contact with a empty token")
    //verificar que el usuario reciva a status code 401 if solicita eliminar un contacto con un token vacio
    @Story("Delete a contact")
    public void deleteContactWithoutTokenUserTest(){
        String token = getUserTokenFromLoginUser();
        Response response = genericGetRequest(token, "contacts");

        String body = response.getBody().asString();
        String idContact = from(body).getString("[0]._id");

        Response responseDeleteContact = responseDeleteContact("", "contacts/", idContact);

        int status = responseDeleteContact.getStatusCode();
        assertThat(status, equalTo(HttpStatus.SC_UNAUTHORIZED));
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Delete contact - Status code 401 whit invalid token")
    @Description("Verify that the user receives  status code 401 if requested to delete a contact  with invalid token.")
    @Story("Delete a contact")
    public void deleteContactWhitInvalidTokenUserTest(){
        String token = getUserTokenFromLoginUser();
        Response response = genericGetRequest(token, "contacts");

        String body = response.getBody().asString();
        String idContact = from(body).getString("[0]._id");

        Response responseDeleteContact = responseDeleteContact(token+"0", "contacts/", idContact);

        int status = responseDeleteContact.getStatusCode();
        assertThat(status, equalTo(HttpStatus.SC_UNAUTHORIZED));
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Delete contact- Status code 503 whit empty id")
    @Description("Verify that the user receives  status code 503 if requested to delete a contact  with empty id.")
    @Story("Delete a contact")
    public void deleteContactWithoutIdTest(){
        String token = getUserTokenFromLoginUser();
        Response response = genericGetRequest(token, "contacts");

        Response responseDeleteContact = responseDeleteContact(token, "contacts/", "");

        int status = responseDeleteContact.getStatusCode();
        assertThat(status, equalTo(HttpStatus.SC_SERVICE_UNAVAILABLE));
    }

    /*Delete User*/
    @Test
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Delete User - Status code 200 whit a valid token")
    @Description("Verify that the user receives status code 200 when sends a DELETE user request with a valid token")
    @Story("Delete user")
    public void deleteUserTest(){
        Response response = responseDeleteUser(getUserTokenFromLoginUser(), "/users/me");
        int statusCode = response.getStatusCode();
        assertThat(statusCode, equalTo(HttpStatus.SC_OK));
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Delete User - Status code 401 whit a invalid token")
    @Description("Verify that the user receives status code 401 when sends DELETE user request with a invalid token")
    @Story("Delete user")
    public void deleteUserWithInvalidTokenTest(){
        Response response = responseDeleteUser(getUserTokenFromLoginUser()+"0", "/users/me");
        int statusCode = response.getStatusCode();
        assertThat(statusCode, equalTo(HttpStatus.SC_UNAUTHORIZED));
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Delete User - Status code 401 whit empty token")
    @Description("Verify that the user receives status code 401 when sends DELETE user request with empty token")
    @Story("Delete user")
    public void deleteUserWhitEmptyTokenTest(){
        Response response = responseDeleteUser("", "/users/me");
        int statusCode = response.getStatusCode();
        assertThat(statusCode, equalTo(HttpStatus.SC_UNAUTHORIZED));
    }
    /*Generic Method*/
    //AddUser
    private Response genericMethodPostAddUser(UserRequest userRequest, String path) {
        Response response = given()
                .contentType(ContentType.JSON)
                .body(userRequest)
                .post(path);
        return response;
    }
    //LoginUser
    private Response genericPostLoginUserRequest(PostLoginUserRequest loginUserRequest, String path){
        Response response = given()
                .contentType(ContentType.JSON)
                .body(loginUserRequest)
                .post(path);

        return response;
    }
    //GetUser
    private Response genericGetRequest(String token, String path){
        Response response = given()
                .auth()
                .oauth2(token)
                .when()
                .get(path);
        return response;
    }
    //PathUser
    private Response genericPathUserRequest(String token, UserRequest userRequest, String path){
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
    private String genericMethodPostAddContact(String token, ContactRequest contactRequest, String request, int codeStatus) {
        String response =  given()
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
    private String getUserTokenFromLoginUser(){
        PostLoginUserRequest loginUserRequest = PostLoginUserRequest.builder()
                .email("luis32@gmail.com")
                .password("myPassword")
                .build();

        Response responseLoginUser = genericPostLoginUserRequest(loginUserRequest, "users/login");

        String body = responseLoginUser.getBody().asString();
        String userToken = from(body).getString("token");
        assertThat(userToken, notNullValue());

        return userToken;
    }

    //PUT CONTACT
    private Response responseUpdateContact(ContactRequest requestJson, String token, String path, String idContact){
        Response response = given()
                .auth()
                .oauth2(token)
                .contentType(ContentType.JSON)
                .body(requestJson)
                .put(path+idContact);
        return response;
    }

    //Delete contact
    public Response responseDeleteContact(String token, String path, String idContact){
        Response response = given()
                .auth()
                .oauth2(token)
                .when()
                .delete(path+idContact);
        return response;
    }
    //Delete User
    public Response responseDeleteUser(String token, String path){
        Response response = given()
                .auth()
                .oauth2(token)
                .when()
                .delete(path);
        return response;
    }

}
