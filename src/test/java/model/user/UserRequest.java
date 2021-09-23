package model.user;

import lombok.*;

@Data
@RequiredArgsConstructor
@NoArgsConstructor
public class UserRequest {
	@NonNull
	private String firstName;
	@NonNull
	private String lastName;
	@NonNull
	private String email;
	@NonNull
	private String password;
}
