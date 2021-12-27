package bean;
import java.util.InputMismatchException;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Scanner;
import Exceptions.*;
import LogFiles.ExceptionLog;
import test.Test;


public class User {

	public int id; //autoInc
	public String login;
	public String pwd;
	public double solde;
	public LinkedList<String> historique;
	
	public static int nbU = 1;
	public static LinkedList<User> userList = new LinkedList<>(); //liste de tous les utilisateurs (admins inclus)
	public static Scanner scS = new Scanner(System.in);
	public static Scanner scN = new Scanner(System.in);
	
	
	public User() {
		this.id = nbU ++;
		this.historique = new LinkedList<>();
	}
	
	public User(String login, String pwd, double solde) {
		this.id = nbU ++;
		this.login = login;
		this.pwd = pwd;
		this.solde = solde;
		this.historique = new LinkedList<>();
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", login=" + login + ", pwd=" + pwd + ", solde=" + solde + ", historique="
				+ historique + "]";
	}

	
	//recherche par LOGIN: unique
	public static int exist(String login) {
		int position = -1;
		ListIterator<User> i = userList.listIterator();
		
		//recherche a l'aide d'iterateur
		while(i.hasNext()) {
			if(i.next().login.equals(login)) {
				position = i.nextIndex() - 1;
				break;
			}
		}
		return position;
	}
	
	
		// CAS : -1=>compte inexistant   0=>tentatives epuisees    1=>succès d'auth==>retourne le type de l'object
	public static String auth(String login) throws CompteInexException {
		int attempts = 3;
		int auth = -1;
		String type = null;
		
		//verifier que le compte existe par le login
		int pos = exist(login);
		
		do {
			if (pos != -1) { //le compte existe
				System.out.print("\nPassword :");
				String pwd = scS.nextLine();
				if (userList.get(pos).pwd.equals(pwd)) { //si le pwd est correct
					auth = 1;
				}else { //sinon repeter jusqu'a plus de tentatives possibles
					System.out.println("Password incorrect");
					attempts--;
					if(attempts > 0) System.out.println("Réessayez"); 
					else {
						auth = 0;
						System.out.println("Tentatives épuisées"); 
						break;
					}
				}
			}else  throw new CompteInexException(); //le compte n'existe pas
		} while (auth != 1 && attempts > 0);
		
		if(auth == 1) { //distinction entre user et admin
			if(userList.get(pos) instanceof Admin) type = "admin";
			else if(userList.get(pos) instanceof User) type = "user";
		}
			
		return type; //retourne le type du user créé
	}
		
		
	//ajout de paiement
	public static User ajoutPaiement(User u) throws ChampsVidesInvalidesException{
		boolean done = false;
		double montant = 0;
		String desc;
		
		while(done != true) { 
			System.out.println("Saisissez le montant du paiement");
			montant = scN.nextDouble();
			System.out.println("Saisissez la description du paiement");
			desc = scS.nextLine();
			
			if(montant <= 0 || desc.isEmpty()) { 
				throw new ChampsVidesInvalidesException();
			}else {
				if(montant > u.solde) {  //solde insuff
					System.out.println("Solde insuffisant. Voulez-vous abandonner le paiement ? 1 = oui / 0 = non");
					int choix = scN.nextInt();
					if(choix == 1) done = true;
				}else {
					//update des infos 
					u.solde -= montant;
					u.historique.add(desc + "(" + montant + "MAD)");
					System.out.println("Paiement effectué avec succès");
					done = true;
				}
			}
		}
		return u; 
	}
	
	//recharge de solde
	public static User rechargerSolde(User u) throws ChampsVidesInvalidesException{
		boolean done = false;
		double montant = 0;
		
		while(done != true) { 
			System.out.println("Saisissez le montant de rechargement");
			montant = scN.nextDouble();
			if(montant <= 0) { 
				throw new ChampsVidesInvalidesException();
			}else {
					//update des infos 
					u.solde += montant;
					done = true;
			}
		}
		return u; 
	}


	
	//consultation de solde
	public static void consult(User u) throws CompteInexException{
		int pos = exist(u.login);
		if(pos != -1) {
			System.out.println("Votre solde est de : " + userList.get(pos).solde);
		}
		else {
			System.out.println("Compte inexistant. Pas de solde a consulter");
			throw new CompteInexException();
		}
	}
	
	
	// menu utilisateur
	public static void userMenu() {
			String login = null;
			String auth = null;
			//Exception : wrong login => compte inexistant
			int temp = 0;
			do {
				temp = 0;
				try {
					System.out.println("Authentification : \n");
					System.out.print("\nLogin : ");
					login = scS.nextLine();
					auth = auth(login);
				}catch (CompteInexException ce) {
					ExceptionLog.addLog(ce.getMessage(), Test.LOGFILE, true);
					System.out.println("Compte inexistant: \n1 - Créez un nouveau compte. \n2 - Réessayer.");
					temp = scN.nextInt();
					if(temp == 1) {
						boolean done = false;
						do {
							
							try {
								done = Admin.createUserAcc();
							} catch (ChampsVidesInvalidesException cve) {
								ExceptionLog.addLog(cve.getMessage(), Test.LOGFILE, true);
								System.out.println("Informations non valides. Réessayez");
							}
						}while(done != true);
						userMenu(); //nouvelle chance d'authentification
					}
				}
			}while(temp == 2);

			if(auth != null) {
				User u = userList.get(exist(login)); //recuperer l'objet du user en cours 
				if(auth.equalsIgnoreCase("user")) {
					
					int choix = 0;
					while(choix != 4) {
						System.out.println("\n\n***************************************\n"
								+ "\nMenu E-wallet : \n1 - Ajout de paiement.\n2 - Consultation de solde.\n3 - Recharger solde.\n4 - Quitter. " );
						choix = scN.nextInt();
						
						switch (choix) {
						case 1:
							try {
								ajoutPaiement(u);
							}catch(InputMismatchException input) {
								ExceptionLog.addLog("SysErr : Input mismatch", Test.LOGFILE, true);
								System.out.println("Informations non valides. Réessayez");
							}catch (ChampsVidesInvalidesException cve) {
								ExceptionLog.addLog(cve.getMessage(), Test.LOGFILE, true);
								System.out.println("Informations non valides. Réessayez");
								choix = 1; //pour réessayer le paiement
							}
							break;
						case 2:
							try {
								consult(u);
							} catch (CompteInexException e) {
								ExceptionLog.addLog(e.getMessage(), Test.LOGFILE, true);
							}
							break;
						case 3:
							try {
								rechargerSolde(u);
							}catch(InputMismatchException input) {
								ExceptionLog.addLog("SysErr : Input mismatch", Test.LOGFILE, true);
								System.out.println("Informations non valides. Réessayez");
							}catch (ChampsVidesInvalidesException cve) {
								ExceptionLog.addLog(cve.getMessage(), Test.LOGFILE, true);
								System.out.println("Informations non valides. Réessayez");
								choix = 3; //pour réessayer le paiement
							}
							break;
						case 4:
							System.out.println("Au revoir !");
							break;
						default:
							System.out.println("Choix incorrect. Réessayez");
							break;
						}
						scN.nextLine();
					
					}
				}else if(auth.equalsIgnoreCase("admin")) Admin.adminMenu(u);
				else System.out.println("Echec de l'authentification");
			}
		}
}