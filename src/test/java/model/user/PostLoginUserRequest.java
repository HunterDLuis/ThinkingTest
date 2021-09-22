package model.user;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class PostLoginUserRequest{
	private String email;
	private String password;
}
