package data.builder;

import model.user.PostLoginUserRequest;

public class PostLoginUserRequestBuilder {
    private PostLoginUserRequest postLoginUserRequest;

    private PostLoginUserRequestBuilder(){
        postLoginUserRequest = new PostLoginUserRequest();
    }

    public static PostLoginUserRequestBuilder loginUser(){
        return new PostLoginUserRequestBuilder();
    }

    public PostLoginUserRequestBuilder withPassword(String password){
        this.postLoginUserRequest.setPassword(password);
        return this;
    }

    public PostLoginUserRequestBuilder withEmail(String email){
        this.postLoginUserRequest.setEmail(email);
        return this;
    }

    public PostLoginUserRequest build(){
        return postLoginUserRequest;
    }
}
