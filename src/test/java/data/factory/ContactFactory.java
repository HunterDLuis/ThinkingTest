package data.factory;

import com.github.javafaker.Faker;
import lombok.NonNull;
import model.contact.ContactRequest;
import org.apache.commons.lang3.StringUtils;

import static data.builder.ContactRequestBuilder.aContact;


public class ContactFactory {
    private static final Faker faker = new Faker();
    private static final String DEFAULT_BIRTHDATE = "1990-01-01";
    private static final String DEFAULT_PHONE = "74896512";
    private static final String DEFAULT_POSTAL_CODE = "1524";

    public static ContactRequest missingAllInformation(){
        return aContact()
                .withFirstName(StringUtils.EMPTY)
                .withLastName(StringUtils.EMPTY)
                .withBirthdate(StringUtils.EMPTY)
                .withEmail(StringUtils.EMPTY)
                .withPhone(StringUtils.EMPTY)
                .withPostalCode(StringUtils.EMPTY)
                .build();
    }

    public static ContactRequest nullInformation(){
        return aContact()
                .withFirstName(null)
                .withLastName(null)
                .withBirthdate(null)
                .withEmail(null)
                .withPhone(null)
                .withPostalCode(null)
                .build();
    }

    public static ContactRequest defaultInfoAccount(){
        return aContact()
                .withFirstName(faker.name().firstName())
                .withLastName(faker.name().lastName())
                .withBirthdate(DEFAULT_BIRTHDATE)
                .withEmail(faker.internet().emailAddress())
                .withPhone(DEFAULT_PHONE)
                .withPostalCode(DEFAULT_POSTAL_CODE)
                .build();
    }

    public static ContactRequest invalidInfoAccount(){
        return aContact()
                .withFirstName(faker.name().firstName())
                .withLastName(faker.name().lastName())
                .withBirthdate(faker.date().birthday().toString())
                .withEmail(faker.internet().emailAddress())
                .withPhone(faker.phoneNumber().toString())
                .withPostalCode(faker.address().countryCode())
                .build();
    }
}
