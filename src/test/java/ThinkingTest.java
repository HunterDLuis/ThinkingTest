import io.qameta.allure.*;
import io.qameta.allure.junit4.DisplayName;
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

//    /*CAMBIAR TODO EL TIEMPO*/
//    @Test
//    public void addUserSuccessfullyTest(){
//
//        UserRequest userRequest = new UserRequest("Test120", "Test120", "test120@gmail.com", "Password");
//
//        Response responseUser = genericMethodPostAddUser(userRequest, "users");
//
//        int statusCode = responseUser.getStatusCode();
//        String body = responseUser.getBody().asString();
//
//        assertThat(statusCode, equalTo(HttpStatus.SC_CREATED));
//
//        String token = from(body).get("token");
//        assertThat(token, notNullValue());
//    }
//




    @Test
    @DisplayName("RESPUESTA  401 CUANDO LOS CAMPOS SON VACIOS")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Resultado 401 ")
    @TmsLink("http://190.104.11.22:83/testlink/linkto.php?tprojectPrefix=TT&item=testcase&id=TT-1")

    public void responseCodeBadRequestInAddUserWhenSendMandatoryFieldsEmptyTest() {

        UserRequest userRequest = new UserRequest("", "", "", "");

        Response responseUser = genericMethodPostAddUser(userRequest, "users");

        int statusCode = responseUser.getStatusCode();
        String body = responseUser.getBody().asString();

        assertThat(statusCode, equalTo(HttpStatus.SC_BAD_REQUEST));

        String message = from(body).get("message");
        assertThat(message, notNullValue());
        assertThat(message, equalTo("User validation failed: firstName: Path `firstName` is required., lastName: Path `lastName` is required., email: Email is invalid, password: Path `password` is required."));
    }

//    /*CAMBIAR TODO EL TIEMPO*/
//
//    @Test
//    public void getFirsNameWhenAddUserSuccessfullyTest(){
//
//        UserRequest userRequest = new UserRequest("Luis340", "Villa34", "luis3400@gmail.com", "myPassword");
//
//        Response responseUser = genericMethodPostAddUser(userRequest, "users");
//
//        int statusCode = responseUser.getStatusCode();
//        String body = responseUser.getBody().asString();
//
//        assertThat(statusCode, equalTo(HttpStatus.SC_CREATED));
//
//        String userId = from(body).get("user._id");
//        String userFirstName = from(body).get("user.firstName");
//
//        assertThat(userId, notNullValue());
//        assertThat(userFirstName, equalTo("Luis340"));
//    }

    /*Post Login User*/

    //    @Story(" LOGIN USER ")

    @Test
    @Severity(SeverityLevel.MINOR)
    @DisplayName("RESPUESTA  200 cuando se logea")
    @Description("Resultado 200 ")

    @Link("https://thinking-tester-contact-list.herokuapp.com/")



    public void LoginUserWithValidCredentialsTest() {
        PostLoginUserRequest loginUserRequest = new PostLoginUserRequest("test0@gmail.com", "Password");

        Response responseLoginUser = genericPostLoginUserRequest(loginUserRequest, "users/login");

        int statusCode = responseLoginUser.getStatusCode();
        String body = responseLoginUser.getBody().asString();
        String token = from(body).get("token");

        assertThat(statusCode, equalTo(HttpStatus.SC_OK));
        assertThat(token, notNullValue());
    }

    //    @Test
//    public void loginUserWithInvalidCredentialsTest(){
//        PostLoginUserRequest loginUserRequest = new PostLoginUserRequest("test1@gmail.com", "Password");
//
//        Response responseLoginUser = genericPostLoginUserRequest(loginUserRequest, "users/login");
//
//        int statusCode = responseLoginUser.getStatusCode();
//        assertThat(statusCode, equalTo(HttpStatus.SC_UNAUTHORIZED));
//    }
//
//    @Test
//    public void verifyIfMatchUserWithValidCredentialsTest(){
//        PostLoginUserRequest loginUserRequest = new PostLoginUserRequest("test@gmail.com", "Password");
//
//        Response responseLoginUser = genericPostLoginUserRequest(loginUserRequest, "users/login");
//
//        int statusCode = responseLoginUser.getStatusCode();
//        String body = responseLoginUser.getBody().asString();
//        String userEmail = from(body).getString("user.email");
//
//        assertThat(statusCode, equalTo(HttpStatus.SC_OK));
//        assertThat(userEmail, equalTo("test0@gmail.com"));
//    }
//    /*Get User*/
//    @Test
//    public void verifyStatusCode200WithValidCredentialsTest(){
//        Response response = genericGetRequest(getUserTokenFromLoginUser(), "users/me");
//        int statusCode = response.getStatusCode();
//        String body = response.getBody().asString();
//        String userEmail = from(body).getString("email");
//
//        assertThat(statusCode, equalTo(HttpStatus.SC_OK));
//        assertThat(userEmail, equalTo("test0@gmail.com"));
//    }
//    @Test
//    public void verifyStatusCode401WithInvalidCredentialsTest(){
//        Response response = genericGetRequest(getUserTokenFromLoginUser()+"0", "users/me");
//        int statusCode = response.getStatusCode();
//
//        assertThat(statusCode, equalTo(HttpStatus.SC_UNAUTHORIZED));
//    }
//
//    @Test
//    public void verifyReceivesAMessageWhenSendWithoutTokenTest(){
//        Response response = genericGetRequest("", "users/me");
//        int statusCode = response.getStatusCode();
//        String body = response.getBody().asString();
//        String errorMessage = from(body).getString("error");
//
//        assertThat(statusCode, equalTo(HttpStatus.SC_UNAUTHORIZED));
//        assertThat(errorMessage, equalTo("Please authenticate."));
//    }
//
//    /*POST Add Contact*/
//    @Test
//    public void addContactSuccessfullyTest(){
//        ContactRequest contactRequest = new ContactRequest("Rita", "Vidal", "1990-01-01", "rita@gmail.com", "8005555555",
//                "1 Main St.", "Apartment A", "Anytown", "KS", "12345", "USA");
//
//        String validateResponse =  genericMethodPostAddContact(getUserTokenFromLoginUser(), contactRequest, "contacts", HttpStatus.SC_CREATED);
//        assertThat(validateResponse, notNullValue());
//    }
//
//    @Test
//    public void verifyResponseOfAddContactWhenSendMandatoryFieldsEmptyTest(){
//        ContactRequest contactRequest = new ContactRequest("", "", "1990-01-01", "rita@gmail.com", "8005555555",
//                "1 Main St.", "Apartment A", "Anytown", "KS", "12345", "USA");
//
//        String validateResponse = genericMethodPostAddContact(getUserTokenFromLoginUser(), contactRequest, "contacts", HttpStatus.SC_BAD_REQUEST);
//        String message = from(validateResponse).get("message");
//        assertThat(message, notNullValue());
//        assertThat(message, equalTo("Contact validation failed: firstName: Path `firstName` is required., lastName: Path `lastName` is required."));
//    }
//
//    @Test
//    public void verifyResponseWhenUseInvalidTokenTest(){
//        ContactRequest contactRequest = new ContactRequest("Luis", "Dimitri", "1990-01-01", "rita@gmail.com", "8005555555",
//                "1 Main St.", "Apartment A", "Anytown", "KS", "12345", "USA");
//
//        String validateResponse = genericMethodPostAddContact(getUserTokenFromLoginUser()+"a", contactRequest, "contacts", HttpStatus.SC_UNAUTHORIZED);
//        String errorMessage = from(validateResponse).get("error");
//        assertThat(errorMessage, notNullValue());
//        assertThat(errorMessage, equalTo("Please authenticate."));
//    }
//
//    @Test
//    public void getResponseWhenAddContactSuccessfullyTest(){
//        ContactRequest contactRequest = new ContactRequest("Java java", "Du", "1990-01-01", "rita@gmail.com", "8005555555",
//                "1 Main St.", "Apartment A", "Anytown", "KS", "12345", "USA");
//
//        String validateResponse = genericMethodPostAddContact(getUserTokenFromLoginUser(), contactRequest, "contacts", HttpStatus.SC_CREATED);
//
//        String contactId = from(validateResponse).get("_id");
//        assertThat(contactId, notNullValue());
//        String contactBirthdate = from(validateResponse).get("birthdate");
//        assertThat(contactBirthdate, equalTo("1990-01-01"));
//    }
//
//    /*GET CONTACT LIST */
//
//    @Test
//    public void getContactListUserWithValidCredentialsTest() {
//        Response response = genericGetRequest(getUserTokenFromLoginUser(), "contacts");
//
//        int statusCode = response.getStatusCode();
//        assertThat(statusCode, equalTo(HttpStatus.SC_OK));
//    }
//
//    @Test
//    public void getContactListUserWithInvalidCredentialsTest() {
//        Response response = genericGetRequest(getUserTokenFromLoginUser() + "0", "contacts");
//        int statusCode = response.getStatusCode();
//
//        assertThat(statusCode, equalTo(HttpStatus.SC_UNAUTHORIZED));
//    }
//
//    @Test
//    public void  getAllContactListTest2() {
//        Response response = genericGetRequest(getUserTokenFromLoginUser(), "contacts");
//
//        String body = response.getBody().asString();
//        int v = from(body).getInt("[0].__v");
//        String email = from(body).getString("[0].email");
//
//
//        System.out.println("Email:" + email);
//        System.out.println("logeo: " + v);
//
//
//        assertThat(v, equalTo(0 ));
//        //assertThat(email, equalTo("rita@gmail.com"));
//    }
//    @Test
//    public void  getAllContactListCountryTest3() {
//        Response response = genericGetRequest(getUserTokenFromLoginUser(), "contacts");
//
//        String body = response.getBody().asString();
//       // String country = from(body).getString("country");
//
//        List<Map> country= from(body).get(" findAll { list -> list.country  == 'USA' }");
//        System.out.println("*********************" );
//        System.out.println("Country :" + country);
//        System.out.println("Country size :" + country.size());
//
//       // assertThat(country, equalTo( "USA"));
//
//
//
//
//    }
//
//    /*PATH user*/
//    @Test
//    public void verifyResponseStatus200WhenUpdateUserTest(){
//        UserRequest userRequest = new UserRequest("Carlos", "Lopez", "test0@gmail.com", "Password");
//
//        Response response = genericPathUserRequest(getUserTokenFromLoginUser(), userRequest, "/users/me");
//
//        int status = response.getStatusCode();
//        assertThat(status, equalTo(HttpStatus.SC_OK));
//    }
//
//    @Test
//    public void UpdateUserFirstName(){
//        UserRequest userRequest = new UserRequest("Carla", "Lopez", "test0@gmail.com", "Password");
//
//        Response response = genericPathUserRequest(getUserTokenFromLoginUser(), userRequest, "/users/me");
//
//        String body = response.getBody().asString();
//        String updateFirstName = from(body).get("firstName");
//
//        assertThat(updateFirstName, equalTo("Carla"));
//    }
//
//    @Test
//    public void verifyResponseJsonIsNotNullTest(){
//        UserRequest userRequest = new UserRequest("Ana", "Lopez", "test0@gmail.com", "Password");
//
//        Response response = genericPathUserRequest(getUserTokenFromLoginUser(), userRequest, "/users/me");
//
//        String body = response.getBody().asString();
//        assertThat(body, notNullValue());
//    }
//
//    @Test
//    public void verifyResponseStatusIs401WhenInvalidTokenTest(){
//        UserRequest userRequest = new UserRequest("Ana", "Lopez", "test0@gmail.com", "Password");
//
//        Response response = genericPathUserRequest(getUserTokenFromLoginUser()+"0", userRequest, "/users/me");
//
//        int status = response.getStatusCode();
//        assertThat(status, equalTo(HttpStatus.SC_UNAUTHORIZED));
//    }
//
//    @Test
//    public void verifyErrorMessageWhenInvalidTokenTest(){
//        UserRequest userRequest = new UserRequest("Ana", "Lopez", "test0@gmail.com", "Password");
//
//        Response response = genericPathUserRequest(getUserTokenFromLoginUser()+"0", userRequest, "/users/me");
//
//        String body = response.getBody().asString();
//        String errorMessage = from(body).get("error");
//        assertThat(errorMessage, equalTo("Please authenticate."));
//    }
//
//    /*POST Log out User*/
//    @Test
//    public void verifyStatusCodeWhenUseValidTokenTest(){
//
//        Response responseUser = genericMethodPostLogOutUser(getUserTokenFromLoginUser(), "users/logout");
//
//        int statusCode = responseUser.getStatusCode();
//        assertThat(statusCode, equalTo(HttpStatus.SC_OK));
//    }
//
//    @Test
//    public void verifyStatusCodeWhenUseInvalidTokenTest(){
//
//        Response responseUser = genericMethodPostLogOutUser(getUserTokenFromLoginUser()+"a", "users/logout");
//
//        int statusCode = responseUser.getStatusCode();
//        assertThat(statusCode, equalTo(HttpStatus.SC_UNAUTHORIZED));
//    }
//
//    @Test
//    public void verifyIfReceiveErrorMessageWhenUseTokenEmptyTest(){
//        Response responseUser = genericMethodPostLogOutUser("", "users/logout");
//
//        int statusCode = responseUser.getStatusCode();
//        String body = responseUser.getBody().asString();
//        String errorMessage = from(body).get("error");
//
//        assertThat(statusCode, equalTo(HttpStatus.SC_UNAUTHORIZED));
//        assertThat(errorMessage, equalTo("Please authenticate."));
//    }
//
//    /*PUT CONTACT*/
//    @Test
//    public void verifyResponseStatus200WhenUpdateContactTest(){
//        String token = getUserTokenFromLoginUser();
//        Response response = genericGetRequest(token, "contacts");
//
//        String body = response.getBody().asString();
//        String idContact = from(body).getString("[0]._id");
//        assertThat(idContact, notNullValue());
//
//        ContactRequest contactRequest = new ContactRequest("Valeria", "Cartagena", "1990-01-01", "valeria@gmail.com", "8005555555",
//                "1 Main St.", "Apartment A", "Anytown", "KS", "12345", "Crocia");
//
//        Response responseUpdateContact = responseUpdateContact(contactRequest, token,"contacts/", idContact);
//
//        int status = responseUpdateContact.getStatusCode();
//        assertThat(status, equalTo(HttpStatus.SC_OK));
//    }
//    @Test
//    public void verifyResponseStatus401WhenEmptyTokenTest(){
//        Response response = genericGetRequest(getUserTokenFromLoginUser(), "contacts");
//
//        String body = response.getBody().asString();
//        String idContact = from(body).getString("[0]._id");
//        assertThat(idContact, notNullValue());
//
//        ContactRequest contactRequest = new ContactRequest("Valeria", "Cartagena", "1990-01-01", "valeria@gmail.com", "8005555555",
//                "1 Main St.", "Apartment A", "Anytown", "KS", "12345", "Crocia");
//
//        Response responseUpdateContact = responseUpdateContact(contactRequest, "","contacts/", idContact);
//
//        int status = responseUpdateContact.getStatusCode();
//        assertThat(status, equalTo(HttpStatus.SC_UNAUTHORIZED));
//    }
//
//    @Test
//    public void verifyResponseStatus401WhenInvalidIdTest(){
//        String token = getUserTokenFromLoginUser();
//        Response response = genericGetRequest(token, "contacts");
//
//        String body = response.getBody().asString();
//        String idContact = from(body).getString("[0]._id");
//        assertThat(idContact, notNullValue());
//
//        ContactRequest contactRequest = new ContactRequest("Valeria", "Cartagena", "1990-01-01", "valeria@gmail.com", "8005555555",
//                "1 Main St.", "Apartment A", "Anytown", "KS", "12345", "Crocia");
//
//        Response responseUpdateContact = responseUpdateContact(contactRequest, token,"contacts/", idContact+"0");
//
//        int status = responseUpdateContact.getStatusCode();
//        assertThat(status, equalTo(HttpStatus.SC_BAD_REQUEST));
//    }
//
//    /*Delete Contact*/
//
////    @Test
////    public void deleteContactWithIdTest(){
////        String token = getUserTokenFromLoginUser();
////        Response response = genericGetRequest(token, "contacts");
////
////        String body = response.getBody().asString();
////        String idContact = from(body).getString("[0]._id");
////
////        Response responseDeleteContact = responseDeleteContact(token, "contacts/", idContact);
////
////        int status = responseDeleteContact.getStatusCode();
////        assertThat(status, equalTo(HttpStatus.SC_OK));
////    }
//
//    @Test
//    public void deleteContactWithoutTokenUserTest(){
//        String token = getUserTokenFromLoginUser();
//        Response response = genericGetRequest(token, "contacts");
//
//        String body = response.getBody().asString();
//        String idContact = from(body).getString("[0]._id");
//
//        Response responseDeleteContact = responseDeleteContact("", "contacts/", idContact);
//
//        int status = responseDeleteContact.getStatusCode();
//        assertThat(status, equalTo(HttpStatus.SC_UNAUTHORIZED));
//    }
//
//    @Test
//    public void deleteContactWhitInvalidTokenUserTest(){
//        String token = getUserTokenFromLoginUser();
//        Response response = genericGetRequest(token, "contacts");
//
//        String body = response.getBody().asString();
//        String idContact = from(body).getString("[0]._id");
//
//        Response responseDeleteContact = responseDeleteContact(token+"0", "contacts/", idContact);
//
//        int status = responseDeleteContact.getStatusCode();
//        assertThat(status, equalTo(HttpStatus.SC_UNAUTHORIZED));
//    }
//
//    @Test
//    public void deleteContactWithoutIdTest(){
//        String token = getUserTokenFromLoginUser();
//        Response response = genericGetRequest(token, "contacts");
//
//        Response responseDeleteContact = responseDeleteContact(token, "contacts/", "");
//
//        int status = responseDeleteContact.getStatusCode();
//        assertThat(status, equalTo(HttpStatus.SC_SERVICE_UNAVAILABLE));
//    }
//
//    /*Delete User*/
////    @Test
////    public void deleteUserTest(){
////        Response response = responseDeleteUser(getUserTokenFromLoginUser(), "/users/me");
////        int statusCode = response.getStatusCode();
////        assertThat(statusCode, equalTo(HttpStatus.SC_OK));
////    }
//
//    @Test
//    public void deleteUserWithInvalidTokenTest(){
//        Response response = responseDeleteUser(getUserTokenFromLoginUser()+"0", "/users/me");
//        int statusCode = response.getStatusCode();
//        assertThat(statusCode, equalTo(HttpStatus.SC_UNAUTHORIZED));
//    }
//
//    @Test
//    public void deleteUserWhitEmptyTokenTest(){
//        Response response = responseDeleteUser("", "/users/me");
//        int statusCode = response.getStatusCode();
//        assertThat(statusCode, equalTo(HttpStatus.SC_UNAUTHORIZED));
//    }
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
    private Response genericPathUserRequest(String token, UserRequest userRequest, String path) {
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
    private String getUserTokenFromLoginUser() {
        PostLoginUserRequest loginUserRequest = new PostLoginUserRequest("test0@gmail.com", "Password");

        Response responseLoginUser = genericPostLoginUserRequest(loginUserRequest, "users/login");

        String body = responseLoginUser.getBody().asString();
        String userToken = from(body).getString("token");
        assertThat(userToken, notNullValue());

        return userToken;
    }

    //PUT CONTACT
    private Response responseUpdateContact(ContactRequest requestJson, String token, String path, String idContact) {
        Response response = given()
                .auth()
                .oauth2(token)
                .contentType(ContentType.JSON)
                .body(requestJson)
                .put(path + idContact);
        return response;
    }

    //Delete contact
    public Response responseDeleteContact(String token, String path, String idContact) {
        Response response = given()
                .auth()
                .oauth2(token)
                .when()
                .delete(path + idContact);
        return response;
    }

    //Delete User
    public Response responseDeleteUser(String token, String path) {
        Response response = given()
                .auth()
                .oauth2(token)
                .when()
                .delete(path);
        return response;
    }

}
