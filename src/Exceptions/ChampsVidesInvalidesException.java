package Exceptions;

public class ChampsVidesInvalidesException extends Exception{

	
	public String getMessage() {
		return "Err 002 : Champs vides ou invalides";
	}
	
	public void printStackTrace() {
		super.printStackTrace();
		System.err.println("Champs vides ou invalides");
	}
}


