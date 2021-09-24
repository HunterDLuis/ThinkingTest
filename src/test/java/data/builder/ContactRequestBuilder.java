package data.builder;

import lombok.NonNull;
import model.contact.ContactRequest;

public class ContactRequestBuilder {
    private ContactRequest contactRequest;

    private ContactRequestBuilder(){
        contactRequest = new ContactRequest();
    }

    public static ContactRequestBuilder aContact(){
        return new ContactRequestBuilder();
    }

    public ContactRequestBuilder withFirstName(String firstName){
        this.contactRequest.setFirstName(firstName);
        return this;
    }

    public ContactRequestBuilder withLastName(String lastName){
        this.contactRequest.setLastName(lastName);
        return this;
    }

    public ContactRequestBuilder withBirthdate(String birthdate){
        this.contactRequest.setBirthdate(birthdate);
        return this;
    }

    public ContactRequestBuilder withEmail(String email){
        this.contactRequest.setEmail(email);
        return this;
    }

    public ContactRequestBuilder withPhone(String phone){
        this.contactRequest.setPhone(phone);
        return this;
    }

    public ContactRequestBuilder withPostalCode(String postalCode){
        this.contactRequest.setPostalCode(postalCode);
        return this;
    }

    public ContactRequest build(){
        return contactRequest;
    }
}
