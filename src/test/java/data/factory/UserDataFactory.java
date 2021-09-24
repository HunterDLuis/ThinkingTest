package data.factory;

import com.github.javafaker.Faker;
import lombok.NonNull;
import model.user.UserRequest;
import org.apache.commons.lang3.StringUtils;

import static data.builder.UserRequestBuilder.anUser;

public class UserDataFactory {
    private static final Faker faker = new Faker();
    private static final String DEFAULT_FIRSTNAME = "Luis34";
    private static final String DEFAULT_LASTNAME = "Villa34";
    private static final String DEFAULT_EMAIL = "luis34@gmail.com";
    private static final String DEFAULT_PASSWORD = "myPassword";

    public static UserRequest missingAllInformation(){
        return anUser()
                .withFirstName(StringUtils.EMPTY)
                .withLastName(StringUtils.EMPTY)
                .withEmail(StringUtils.EMPTY)
                .withPassword(StringUtils.EMPTY)
                .build();
    }

    public static UserRequest nullInformation(){
        return anUser()
                .withFirstName(null)
                .withLastName(null)
                .withEmail(null)
                .withPassword(null)
                .build();
    }

    public static UserRequest defaultAccount(){
        return anUser()
                .withFirstName(DEFAULT_FIRSTNAME)
                .withLastName(DEFAULT_LASTNAME)
                .withEmail(DEFAULT_EMAIL)
                .withPassword(DEFAULT_PASSWORD)
                .build();
    }

    public static UserRequest validAccount(){
        return anUser()
                .withFirstName(faker.name().firstName())
                .withLastName(faker.name().lastName())
                .withEmail(faker.internet().emailAddress())
                .withPassword(faker.internet().password())
                .build();
    }
    /*PATH user*/
    public static UserRequest updateFirstName(){
        return anUser()
                .withFirstName(faker.name().firstName())
                .withLastName(DEFAULT_LASTNAME)
                .withEmail(DEFAULT_EMAIL)
                .withPassword(DEFAULT_PASSWORD)
                .build();
    }
    public static UserRequest updateLastName(){
        return anUser()
                .withFirstName(DEFAULT_FIRSTNAME)
                .withLastName(faker.name().lastName())
                .withEmail(DEFAULT_EMAIL)
                .withPassword(DEFAULT_PASSWORD)
                .build();
    }
}
