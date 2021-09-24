package data.builder;

import lombok.NonNull;
import model.user.UserRequest;

public class UserRequestBuilder {
    private UserRequest userRequest;

    private UserRequestBuilder(){
        userRequest = new UserRequest();
    }

    public static UserRequestBuilder anUser(){
        return new UserRequestBuilder();
    }

    public UserRequestBuilder withPassword(String password){
        this.userRequest.setPassword(password);
        return this;
    }

    public UserRequestBuilder withEmail(String email){
        this.userRequest.setEmail(email);
        return this;
    }

    public UserRequestBuilder withFirstName(String firstName){
        this.userRequest.setFirstName(firstName);
        return this;
    }

    public UserRequestBuilder withLastName(String lastName){
        this.userRequest.setLastName(lastName);
        return this;
    }


    public UserRequest build(){
        return userRequest;
    }
}
