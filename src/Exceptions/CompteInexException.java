/*
 * Is thrown when account is not found
 * */
package Exceptions;

public class CompteInexException extends Exception {

	public String getMessage() {
		return "Err 001 : Compte inexistant";
	}
	
	public void printStackTrace() {
		super.printStackTrace();
		System.err.println("Error 001 : Compte inexistant");
	}
}


