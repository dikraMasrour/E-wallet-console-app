package test;
import bean.*;



public class Test {
	public static String LOGFILE = "C:\\Users\\zbook\\Documents\\eclipse-workspace\\E-wallet\\src\\ExceptionLog"; //Exceptions Log
	public static String USERFILE = "C:\\Users\\zbook\\Documents\\eclipse-workspace\\E-wallet\\src\\UserLog"; //User create/delete Log
	public static void main(String[] args) {
			
		//creer un admin par defaut 
		User.userList.add(new Admin("admin", "admin", 10000));
		
				User.userMenu(); //authentification à travers "admin" "admin"
				User.userMenu();
			
			
				
			
			
		
	}
}

