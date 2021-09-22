package model.user;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserRequest {
	private String firstName;
	private String lastName;
	private String email;
	private String password;
}
