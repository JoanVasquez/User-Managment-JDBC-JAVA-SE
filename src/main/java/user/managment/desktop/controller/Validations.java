package user.managment.desktop.controller;

import java.util.HashMap;
import java.util.Map;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import user.managment.desktop.model.Error;

//VALIDATION CLASS
public class Validations {

	//VALIDATE USER DATA
	public static HashMap<String, Error> userValidation(String firstName, String lastName, String email,
			String password) {

		Map<String, Error> errors;

		errors = new HashMap<>();
		errors.clear();

		if (!firstName.matches("^[a-zA-z ]*$")) {
			Error error = new Error();
			error.setMessage("The First Name must be only letters!");
			errors.put("firstNameError", error);
		}else if(firstName.isEmpty()) {
			Error error = new Error();
			error.setMessage("First Name cannot be empty");
			errors.put("firstNameError", error);
		}

		if (!lastName.matches("^[a-zA-z ]*$")) {
			Error error = new Error();
			error.setMessage("The Last Name must be only letters!");
			errors.put("lastNameError", error);
		}else if(lastName.equalsIgnoreCase("")) {
			Error error = new Error();
			error.setMessage("Last Name cannot be empty");
			errors.put("lastNameError", error);
		}

		try {
			InternetAddress internetAddress = new InternetAddress(email);
			internetAddress.validate();
		} catch (AddressException ae) {
			Error error = new Error();
			error.setMessage("Wrong email format!");
			errors.put("emailError", error);
		}
		
		if(password.isEmpty()) {
			Error error = new Error();
			error.setMessage("Password cannot be empty");
			errors.put("passwordError", error);
		}

		return (HashMap<String, Error>) errors;
	}

}
