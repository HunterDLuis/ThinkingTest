import data.factory.ContactFactory;
import data.factory.PostLoginUserFactory;
import data.factory.UserDataFactory;
import io.qameta.allure.*;
import io.qameta.allure.junit4.*;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import lombok.NonNull;
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
    @DisplayName("Post add user - Status code 201 when user is registered.")
    @Description("Verify that the user receives status code 201 when their register is successful.")
    @Story("Add User")
    public void addUserSuccessfullyTest(){
        UserRequest userRequest = UserDataFactory.validAccount();
        Response responseUser = getResponsePostAddUser(userRequest, "users");

        int statusCode = responseUser.getStatusCode();
        String body = responseUser.getBody().asString();

        assertThat(statusCode, equalTo(HttpStatus.SC_CREATED));
        @NonNull
        String token = from(body).get("token");
        assertThat(token, notNullValue());
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Post add user - response status code 400 when send empty mandatory fields")
    @Description("Verify that the user receives status code 400 when register with empty mandatory fields in add user request")
    @Story("Add User")
    public void matchMessageAndStatusCode400WhenMandatoryFieldsEmptyTest(){
        UserRequest dataMissingUser = UserDataFactory.missingAllInformation();

        Response responseUser = getResponsePostAddUser(dataMissingUser, "users");

        int statusCode = responseUser.getStatusCode();
        String body = responseUser.getBody().asString();

        assertThat(statusCode, equalTo(HttpStatus.SC_BAD_REQUEST));
        @NonNull
        String message = from(body).get("_message");
        assertThat(message, equalTo("User validation failed"));
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Post add user - verify firstName")
    @Description("Verify that json response contains the same firstName that the user has been registered in add user request")
    @Story("Add User")
    public void matchFirsNameWhenAddUserSuccessfullyTest(){
        UserRequest userRequest = UserDataFactory.validAccount();

        Response responseUser = getResponsePostAddUser(userRequest, "users");

        int statusCode = responseUser.getStatusCode();
        String body = responseUser.getBody().asString();
        assertThat(statusCode, equalTo(HttpStatus.SC_CREATED));
        @NonNull
        String userFirstName = from(body).get("user.firstName");
        assertThat(userFirstName, equalTo(userRequest.getFirstName()));
    }

    /*Post Login User*/
    @Test
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Post login user - Status code 200 when send  with valid credentials")
    @Description("Verify that user receives a status code 200  when sends login request with valid Credentials")
    @Story("Login User")
    public void loginUserWithValidCredentialsTest(){
        PostLoginUserRequest loginUserRequest = PostLoginUserFactory.defaultUser();
        Response responseLoginUser = getResponsePostLoginUserRequest(loginUserRequest, "users/login");

        int statusCode = responseLoginUser.getStatusCode();
        assertThat(statusCode, equalTo(HttpStatus.SC_OK));
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Post login user- Status code 401 when send with invalid credentials")
    @Description("Verify that user receives a status code 401 when sends login request with invalid Credentials")
    @Story("Login User")
    public void loginUserWithInvalidCredentialsTest(){
        PostLoginUserRequest invalidCredential = PostLoginUserFactory.invalidCredential();
        Response responseLoginUser = getResponsePostLoginUserRequest(invalidCredential, "users/login");

        int statusCode = responseLoginUser.getStatusCode();
        assertThat(statusCode, equalTo(HttpStatus.SC_UNAUTHORIZED));
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Post login user - Verify user email")
    @Description("Verify that Json response contains the same email  that the user entered when sends login request.")
    @Story("Login User")
    public void verifyEmailWithValidCredentialsTest(){
        PostLoginUserRequest loginUserRequest = PostLoginUserFactory.defaultUser();

        Response responseLoginUser = getResponsePostLoginUserRequest(loginUserRequest, "users/login");

        int statusCode = responseLoginUser.getStatusCode();
        String body = responseLoginUser.getBody().asString();
        @NonNull
        String userEmail = from(body).getString("user.email");

        assertThat(statusCode, equalTo(HttpStatus.SC_OK));
        assertThat(userEmail, equalTo(loginUserRequest.getEmail()));
    }
    /*Get User*/
    @Test
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Get user - Status code 200 when send with valid token")
    @Description("Verify that user receives a status code 200 when sends get user request with the valid token ")
    @Story("Get User")
    public void verifyStatusCode200WithValidCredentialsTest(){
        Response response = getResponseGetRequest(getUserTokenFromLoginUser(), "users/me");
        int statusCode = response.getStatusCode();

        assertThat(statusCode, equalTo(HttpStatus.SC_OK));
    }
    @Test
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Get user - Status code 401 when send with invalid token")
    @Description("Verify that the user receives a status code 401 when  sends get user request with the invalid token")
    @Story("Get User")
    public void verifyStatusCode401WithInvalidCredentialsTest(){
        Response response = getResponseGetRequest(getUserTokenFromLoginUser()+"0", "users/me");
        int statusCode = response.getStatusCode();

        assertThat(statusCode, equalTo(HttpStatus.SC_UNAUTHORIZED));
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Get user - Receives error message when send token empty")
    @Description("Verify that the user receives a message when sends a request without the token")
    @Story("Get User")
    public void errorMessageWhenSendWithoutTokenTest(){
        Response response = getResponseGetRequest("", "users/me");
        int statusCode = response.getStatusCode();
        String body = response.getBody().asString();
        @NonNull
        String errorMessage = from(body).getString("error");

        assertThat(statusCode, equalTo(HttpStatus.SC_UNAUTHORIZED));
        assertThat(errorMessage, equalTo("Please authenticate."));
    }

    /*POST Add Contact*/
    @Test
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Post contact - Status code 201 when add contact successfully")
    @Description("Verify that the user receives  status code 201 when a contact is registered  successfully")
    @Story("Post contact")
    public void addContactSuccessfullyTest(){
        ContactRequest contactRequest = ContactFactory.defaultInfoAccount();
        Response validateResponse =  getResponsePostAddContact(getUserTokenFromLoginUser(), contactRequest, "contacts", HttpStatus.SC_CREATED);
        int statusCode = validateResponse.getStatusCode();

        assertThat(statusCode, equalTo(HttpStatus.SC_CREATED));
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Post contact - Error message and status code 400 when mandatory fields empty")
    @Description("Verify that user receives status code 400 when the mandatory fields of register contact are empty")
    @Story("Post contact")
    public void errorMessageWhenSendMandatoryFieldsEmptyTest(){
        ContactRequest dataMissingContact = ContactFactory.missingAllInformation();

        Response validateResponse = getResponsePostAddContact(getUserTokenFromLoginUser(), dataMissingContact, "contacts", HttpStatus.SC_BAD_REQUEST);
        int statusCode = validateResponse.getStatusCode();
        String body = validateResponse.getBody().asString();
        @NonNull
        String message = from(body).get("_message");

        assertThat(statusCode, equalTo(HttpStatus.SC_BAD_REQUEST));
        assertThat(message, equalTo("Contact validation failed"));
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Post contact - Error message when send invalid token")
    @Description("Verify that user receives  status code 401 when sends contact request with invalid token")
    @Story("Post contact")
    public void verifyResponseInAddContactWhenUseInvalidTokenTest(){
        ContactRequest contactRequest = ContactFactory.defaultInfoAccount();

        Response validateResponse = getResponsePostAddContact(getUserTokenFromLoginUser()+"a", contactRequest, "contacts", HttpStatus.SC_UNAUTHORIZED);
        String body = validateResponse.getBody().asString();
        @NonNull
        String errorMessage = from(body).get("error");
        assertThat(errorMessage, equalTo("Please authenticate."));
    }

    @Test
    @Severity(SeverityLevel.MINOR)
    @DisplayName("Post Contact - Response json contain the same birthdate")
    @Description("Verify that the response contains the same birthdate as the contact has been registered in add contact request")
    @Story("Post contact")
    public void getResponseWhenAddContactSuccessfullyTest(){
        ContactRequest contactRequest = ContactFactory.defaultInfoAccount();
        Response validateResponse = getResponsePostAddContact(getUserTokenFromLoginUser(), contactRequest, "contacts", HttpStatus.SC_CREATED);

        String  body = validateResponse.getBody().asString();
        @NonNull
        String contactBirthdate = from(body).get("birthdate");
        assertThat(contactBirthdate, equalTo(contactRequest.getBirthdate()));
    }

    /*GET CONTACT LIST */

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Get contact - status code 200 with valid token")
    @Description("Verify that user receives a status code 200 when sends get contact list request with  the valid token")
    @Story("Get contacts")
    public void getContactListUserWithValidTokenTest(){
        Response response = getResponseGetRequest(getUserTokenFromLoginUser(), "contacts");

        int statusCode = response.getStatusCode();
        assertThat(statusCode, equalTo(HttpStatus.SC_OK));
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Get contact - status code 401 with invalid token")
    @Description("Verify that the user receives a status code 401 when sends get contact list request  with  invalid token.")
    @Story("Get contacts")
    public void getInvalidStatusCodeWithInvalidTokenTest(){
        Response response = getResponseGetRequest(getUserTokenFromLoginUser() + "0", "contacts");
        int statusCode = response.getStatusCode();

        assertThat(statusCode, equalTo(HttpStatus.SC_UNAUTHORIZED));
    }

    @Test
    @Severity(SeverityLevel.MINOR)
    @DisplayName("Get contact - Session number in contact is integer")
    @Description("Verify that the session number of a new contact is integer when sends get contact request")
    @Story("Get contacts")
    public void  verifyThatSessionNumberInContactIsIntegerTest(){
        Response response = getResponseGetRequest(getUserTokenFromLoginUser(), "contacts");

        String body = response.getBody().asString();
        @NonNull
        int v = from(body).getInt("[0].__v");

        assertThat(v, equalTo(0 ));

        System.out.println("logeo: " + v);
    }
    @Test
    @Severity(SeverityLevel.MINOR)
    @DisplayName("Get contact - Filter contact list")
    @Description("Verify that user can filter all contacts that it has country equal USA ")
    @Story("Get contacts")
    public void  getAllContactListTest3(){
        Response response = getResponseGetRequest(getUserTokenFromLoginUser(), "contacts");

        String body = response.getBody().asString();

        List<Map> country= from(body).get(" findAll { list -> list.country  == 'USA' }");
        System.out.println("*********************" );
        System.out.println("Country :" + country);
        System.out.println("Country size :" + country.size());
    }

    /*PATH user*/
    @Test
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Patch user - Status code 200 when update user first name")
    @Description("Verify that the user  receives status code 200 when sends a path user request with a valid token.")
    @Story("Patch user")
    public void verifyResponseStatus200WhenUpdateUserTest(){
        UserRequest userRequest = UserDataFactory.updateFirstName();

        Response response = getResponsePathUserRequest(getUserTokenFromLoginUser(), userRequest, "/users/me");

        int status = response.getStatusCode();
        assertThat(status, equalTo(HttpStatus.SC_OK));
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Patch user - Match user first name with valid token")
    @Description("Verify that the response json contents the same first name that the user updated when sends path user request")
    @Story("Patch user")
    public void UpdateUserFirstName(){
        UserRequest userRequest = UserDataFactory.updateFirstName();
        Response response = getResponsePathUserRequest(getUserTokenFromLoginUser(), userRequest, "/users/me");

        String body = response.getBody().asString();
        @NonNull
        String updateFirstName = from(body).get("firstName");
        assertThat(updateFirstName, equalTo(userRequest.getFirstName()));
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Patch user - Response json is not null")
    @Description("Verify that the user receives json when sends path user request with valid token.")
    @Story("Patch user")
    public void verifyResponseJsonIsNotNullTest(){
        UserRequest userRequest = UserDataFactory.updateLastName();
        Response response = getResponsePathUserRequest(getUserTokenFromLoginUser(), userRequest, "/users/me");
        @NonNull
        String body = response.getBody().asString();
        String updateFirstName = from(body).get("lastName");
        assertThat(updateFirstName, equalTo(userRequest.getLastName()));
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Patch user-Status code 401 when send invalid token")
    @Description("Verify that the user receives  status code 401 when sends path user request with a invalid token.")
    @Story("Patch user")
    public void verifyStatusIs401WhenInvalidTokenTest(){
        UserRequest userRequest = UserDataFactory.defaultAccount();
        Response response = getResponsePathUserRequest(getUserTokenFromLoginUser()+"0", userRequest, "/users/me");
        @NonNull
        int status = response.getStatusCode();
        assertThat(status, equalTo(HttpStatus.SC_UNAUTHORIZED));
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Patch user -Error message when send invalid token")
    @Description("Verify that the user receives a message when sends a request with an invalid  token.")
    @Story("Patch user")
    public void verifyErrorMessageWhenInvalidTokenTest(){
        UserRequest userRequest =  UserDataFactory.defaultAccount();
        Response response = getResponsePathUserRequest(getUserTokenFromLoginUser()+"0", userRequest, "/users/me");

        String body = response.getBody().asString();
        @NonNull
        String errorMessage = from(body).get("error");
        assertThat(errorMessage, equalTo("Please authenticate."));
    }

    /*POST Log out User*/
    @Test
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Post log out user - Status code 200 when use valid token")
    @Description("Verify that user authenticated receives status code 200 when logout with valid token")
    @Story("Post log out user")
    public void verifyStatusCode200WhenUseValidTokenTest(){
        Response responseUser = getResponsePostLogOutUser(getUserTokenFromLoginUser(), "users/logout");
        @NonNull
        int statusCode = responseUser.getStatusCode();
        assertThat(statusCode, equalTo(HttpStatus.SC_OK));
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Post log out user - Status code 401 when use invalid token")
    @Description("Verify that user receives a status code 401 when sends log out request  with invalid token")
    @Story("Post log out user")
    public void verifyStatusCodeWhenUseInvalidTokenTest(){

        Response responseUser = getResponsePostLogOutUser(getUserTokenFromLoginUser()+"a", "users/logout");
        @NonNull
        int statusCode = responseUser.getStatusCode();
        assertThat(statusCode, equalTo(HttpStatus.SC_UNAUTHORIZED));
    }

    @Test
    @Severity(SeverityLevel.MINOR)
    @DisplayName("Post log out user - Error message when use empty")
    @Description("Verify that the user receives a message when use an empty token for request logout")
    @Story("Post log out user")
    public void verifyIfReceiveErrorMessageWhenUseTokenEmptyTest(){
        Response responseUser = getResponsePostLogOutUser("", "users/logout");
        @NonNull
        int statusCode = responseUser.getStatusCode();
        String body = responseUser.getBody().asString();
        @NonNull
        String errorMessage = from(body).get("error");

        assertThat(statusCode, equalTo(HttpStatus.SC_UNAUTHORIZED));
        assertThat(errorMessage, equalTo("Please authenticate."));
    }

    /*PUT CONTACT*/
    @Test
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Put contact- Status code 200 when updated a contact")
    @Description("Verify that the user receives status code 200 when  put contact request is successful.")
    @Story("Put contact")
    public void verifyResponseStatus200WhenUpdateContactTest(){
        String token = getUserTokenFromLoginUser();
        Response response = getResponseGetRequest(token, "contacts");

        String body = response.getBody().asString();
        String idContact = from(body).getString("[0]._id");
        assertThat(idContact, notNullValue());

        ContactRequest contactRequest = ContactFactory.defaultInfoAccount();

        Response responseUpdateContact = getResponseUpdateContact(contactRequest, token,"contacts/", idContact);
        int status = responseUpdateContact.getStatusCode();
        assertThat(status, equalTo(HttpStatus.SC_OK));
    }
    @Test
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Put contact - Status code 401 when token is empty")
    @Description("Verify that the user receives status code 401 when sends put contact request with empty token.")
    @Story("Put contact")
    public void verifyResponseStatus401WhenEmptyTokenTest(){
        Response response = getResponseGetRequest(getUserTokenFromLoginUser(), "contacts");

        String body = response.getBody().asString();
        String idContact = from(body).getString("[0]._id");
        assertThat(idContact, notNullValue());

        ContactRequest contactRequest = ContactFactory.defaultInfoAccount();

        Response responseUpdateContact = getResponseUpdateContact(contactRequest, "","contacts/", idContact);

        int status = responseUpdateContact.getStatusCode();
        assertThat(status, equalTo(HttpStatus.SC_UNAUTHORIZED));
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Put contact-Status code 400 when invalid id")
    @Description("Verify that the user receives status code 400 when the Id  is invalid.")
    @Story("Put contact")
    public void verifyResponseStatus400WhenInvalidIdTest(){
        String token = getUserTokenFromLoginUser();
        Response response = getResponseGetRequest(token, "contacts");

        String body = response.getBody().asString();
        String idContact = from(body).getString("[0]._id");
        assertThat(idContact, notNullValue());

        ContactRequest contactRequest = ContactFactory.defaultInfoAccount();
        Response responseUpdateContact = getResponseUpdateContact(contactRequest, token,"contacts/", idContact+"0");

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
        Response response = getResponseGetRequest(token, "contacts");

        String body = response.getBody().asString();
        String idContact = from(body).getString("[0]._id");

        Response responseDeleteContact = getResponseDeleteContact(token, "contacts/", idContact);

        int status = responseDeleteContact.getStatusCode();
        assertThat(status, equalTo(HttpStatus.SC_OK));
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Delete contact - Status code 401 whit valid id")
    @Description("Verify that the user receives status code 401 when sends delete contact request with empty token")
    @Story("Delete a contact")
    public void deleteContactWithoutTokenUserTest(){
        String token = getUserTokenFromLoginUser();
        Response response = getResponseGetRequest(token, "contacts");

        String body = response.getBody().asString();
        String idContact = from(body).getString("[0]._id");

        Response responseDeleteContact = getResponseDeleteContact("", "contacts/", idContact);

        int status = responseDeleteContact.getStatusCode();
        assertThat(status, equalTo(HttpStatus.SC_UNAUTHORIZED));
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Delete contact - Status code 401 with invalid token")
    @Description("Verify that the user receives  status code 401 when sends delete contact request with invalid token.")
    @Story("Delete a contact")
    public void deleteContactWhitInvalidTokenUserTest(){
        String token = getUserTokenFromLoginUser();
        Response response = getResponseGetRequest(token, "contacts");

        String body = response.getBody().asString();
        String idContact = from(body).getString("[0]._id");

        Response responseDeleteContact = getResponseDeleteContact(token+"0", "contacts/", idContact);

        int status = responseDeleteContact.getStatusCode();
        assertThat(status, equalTo(HttpStatus.SC_UNAUTHORIZED));
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Delete contact- Status code 503 with empty id")
    @Description("Verify that the user receives  status code 503 when sends delete contact request with empty id.")
    @Story("Delete a contact")
    public void deleteContactWithoutIdTest(){
        String token = getUserTokenFromLoginUser();
        Response responseDeleteContact = getResponseDeleteContact(token, "contacts/", "");

        int status = responseDeleteContact.getStatusCode();
        assertThat(status, equalTo(HttpStatus.SC_SERVICE_UNAVAILABLE));
    }

    /*Delete User*/
    @Test
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Delete User - Status code 200 with a valid token")
    @Description("Verify that the user receives status code 200 when sends delete user request with a valid token")
    @Story("Delete user")
    public void deleteUserTest(){
        UserRequest userRequest = UserDataFactory.validAccount();

        Response responseUser = getResponsePostAddUser(userRequest, "users");
        @NonNull
        int statusCodeCreate = responseUser.getStatusCode();
        String body = responseUser.getBody().asString();

        assertThat(statusCodeCreate, equalTo(HttpStatus.SC_CREATED));
        @NonNull
        String token = from(body).get("token");

        Response response = getResponseDeleteUser(token, "/users/me");
        int statusCodeDelete = response.getStatusCode();
        assertThat(statusCodeDelete, equalTo(HttpStatus.SC_OK));
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Delete User - Status code 401 with a invalid token")
    @Description("Verify that the user receives status code 401 when sends delete user request with a invalid token")
    @Story("Delete user")
    public void deleteUserWithInvalidTokenTest(){
        Response response = getResponseDeleteUser(getUserTokenFromLoginUser()+"0", "/users/me");
        int statusCode = response.getStatusCode();
        assertThat(statusCode, equalTo(HttpStatus.SC_UNAUTHORIZED));
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Delete User - Status code 401 with empty token")
    @Description("Verify that the user receives status code 401 when sends delete user request with empty token")
    @Story("Delete user")
    public void deleteUserWhitEmptyTokenTest(){
        Response response = getResponseDeleteUser("", "/users/me");
        int statusCode = response.getStatusCode();
        assertThat(statusCode, equalTo(HttpStatus.SC_UNAUTHORIZED));
    }
    /*Reuse Method*/
    //AddUser
    private Response getResponsePostAddUser(UserRequest userRequest, String path) {
        Response response = given()
                .contentType(ContentType.JSON)
                .body(userRequest)
                .post(path);
        return response;
    }
    //LoginUser
    private Response getResponsePostLoginUserRequest(PostLoginUserRequest loginUserRequest, String path){
        Response response = given()
                .contentType(ContentType.JSON)
                .body(loginUserRequest)
                .post(path);

        return response;
    }
    //GetUser
    private Response getResponseGetRequest(String token, String path){
        Response response = given()
                .auth()
                .oauth2(token)
                .when()
                .get(path);
        return response;
    }
    //PathUser
    private Response getResponsePathUserRequest(String token, UserRequest userRequest, String path){
        Response response = given()
                .auth()
                .oauth2(token)
                .contentType(ContentType.JSON)
                .body(userRequest)
                .patch(path);
        return response;
    }
    //Log out User
    private Response getResponsePostLogOutUser(String token, String path) {
        Response response = given()
                .auth()
                .oauth2(token)
                .post(path);
        return response;
    }

    //Contact
    private Response getResponsePostAddContact(String token, ContactRequest contactRequest, String request, int codeStatus) {
        Response response =  given()
                .auth()
                .oauth2(token)
                .contentType(ContentType.JSON)
                .body(contactRequest)
                .post(request);
        return response;
    }
    //GetToken
    private String getUserTokenFromLoginUser() {
        PostLoginUserRequest loginUserRequest = PostLoginUserFactory.defaultUser();

        Response responseLoginUser = getResponsePostLoginUserRequest(loginUserRequest, "users/login");

        String body = responseLoginUser.getBody().asString();
        @NonNull
        String userToken = from(body).getString("token");

        return userToken;
    }

    //PUT CONTACT
    private Response getResponseUpdateContact(ContactRequest requestJson, String token, String path, String idContact){
        Response response = given()
                .auth()
                .oauth2(token)
                .contentType(ContentType.JSON)
                .body(requestJson)
                .put(path+idContact);
        return response;
    }

    //Delete contact
    public Response getResponseDeleteContact(String token, String path, String idContact){
        Response response = given()
                .auth()
                .oauth2(token)
                .when()
                .delete(path+idContact);
        return response;
    }
    //Delete User
    public Response getResponseDeleteUser(String token, String path){
        Response response = given()
                .auth()
                .oauth2(token)
                .when()
                .delete(path);
        return response;
    }

}
