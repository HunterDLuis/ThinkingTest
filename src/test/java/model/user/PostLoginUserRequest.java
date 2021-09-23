package model.user;

import lombok.*;

@Data
@RequiredArgsConstructor
@NoArgsConstructor
public class PostLoginUserRequest{
	@NonNull
	private String email;
	@NonNull
	private String password;
}
