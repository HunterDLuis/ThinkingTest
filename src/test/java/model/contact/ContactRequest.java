package model.contact;

import lombok.*;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@NoArgsConstructor
public class ContactRequest {
	@NonNull
	private String firstName;
	@NonNull
	private String lastName;
	@NonNull
	private String birthdate;
	@NonNull
	private String email;
	@NonNull
	private String phone;
	private String street1;
	private String street2;
	private String city;
	private String stateProvince;
	@NonNull
	private String postalCode;
	private String country;
}
