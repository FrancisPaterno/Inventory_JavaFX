package application.utility;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataValidator {

	public static final Boolean isDouble(String value) {
		try {
			Double.parseDouble(value);
			return true;
		} catch (NumberFormatException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public static final Boolean isInteger(String value) {
		try {
			Integer.parseInt(value);
			return true;
		} catch (NumberFormatException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public static final Boolean isValidEmail(String value) {
		final String regex = "^[a-zA-Z0-9_!#$%&�*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
		Matcher matcher;
		try {
			Pattern pattern = Pattern.compile(regex);
			matcher = pattern.matcher(value);
			return matcher.matches();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}
