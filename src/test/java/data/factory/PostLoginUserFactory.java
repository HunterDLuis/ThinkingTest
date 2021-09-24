package data.factory;

import com.github.javafaker.Faker;
import model.user.PostLoginUserRequest;
import org.apache.commons.lang3.StringUtils;

import static data.builder.PostLoginUserRequestBuilder.loginUser;


public class PostLoginUserFactory {
    private static final Faker faker = new Faker();
    private static final String DEFAULT_EMAIL = "luis34@gmail.com";
    private static final String DEFAULT_PASSWORD = "myPassword";

    public static PostLoginUserRequest missingAllInformation(){
        return loginUser()
                .withEmail(StringUtils.EMPTY)
                .withPassword(StringUtils.EMPTY)
                .build();
    }

    public static PostLoginUserRequest nullInformation(){
        return loginUser()
                .withEmail(null)
                .withPassword(null)
                .build();
    }

    public static PostLoginUserRequest defaultUser(){
        return loginUser()
                .withEmail(DEFAULT_EMAIL)
                .withPassword(DEFAULT_PASSWORD)
                .build();
    }

    public static PostLoginUserRequest invalidCredential(){
        return loginUser()
                .withEmail(faker.internet().emailAddress())
                .withPassword(faker.internet().password())
                .build();
    }
}
