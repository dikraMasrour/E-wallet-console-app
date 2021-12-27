package bean;

import java.util.InputMismatchException;

import Exceptions.ChampsVidesInvalidesException;
import Exceptions.CompteInexException;
import LogFiles.ExceptionLog;
import LogFiles.UserLogCreation;
import LogFiles.UserLogSuppr;
import test.Test;

public class Admin extends User{
	public String profil;

	
	
	public Admin() {
		super();
	}

	public Admin(String login, String pwd, double solde) {
		super(login, pwd, solde);
		this.profil = "admin";
	}

	@Override
	public String toString() {
		return "Admin [id=" + id + ", login=" + login + ", pwd=" + pwd + 
				", solde=" + solde + ", historique="
				+ historique + ", profil=" + profil + "]";
	}
	
	
	//creation d'utilisateurs
	public static boolean createUserAcc() throws ChampsVidesInvalidesException {
		boolean done = false;
		String confirm = "non";
		String login = null, pwd = null, profil = null;
		double solde = 0;
		boolean valide = false;
		
		//clean scanner's buffer
		scS.nextLine();
		scN.nextLine();
		
		while(!valide) {
			do{
				System.out.println("Création de compte e-Wallet :");
				System.out.print("\nLogin : ");
				login = scS.nextLine();
				System.out.print("\nPassword :");
				pwd = scS.nextLine();
				System.out.println("Solde initial :");
				solde = scN.nextDouble();
				System.out.println("Profil : ");
				profil = scS.nextLine();
				System.out.println("Confirmer vos informations : oui / non");
				confirm = scS.nextLine();
			}while(!confirm.equalsIgnoreCase("oui"));
			
			if (login.isEmpty() || pwd.isEmpty() || solde <= 0) throw new ChampsVidesInvalidesException();
			else if(exist(login) != -1) { //verifier l'unicite du login
				System.out.println("Login déjà utilisé. Choisissez un autre");
				valide = false;
			}
			else valide = true;
		}

		if (profil.equalsIgnoreCase("admin")) {
			User a = new Admin(login, pwd, solde);
			userList.add(a);
			UserLogCreation.addLog(a, Test.USERFILE, true);
			done = true;
			System.out.println("Compte créé avec succès !");
		}else if(profil.equalsIgnoreCase("user")) {
			User u = new User(login, pwd, solde);
			userList.add(u); //creation est enregistrement
			UserLogCreation.addLog(u, Test.USERFILE, true);
			done = true;
			System.out.println("Compte créé avec succès !");
		}
		else {
			done = false;
			System.out.println("Profil invalide");
		}
		return done;
	}

	
	//suppression d'un user/admin
	public static User deleteUser() throws CompteInexException {
		System.out.println("Login du compte à supprimer : ");
		String login = scS.nextLine();
		User u = null;
		
		//position du user a supprimer
		int pos = exist(login); 
		
		if (pos != -1) {
			u = userList.get(pos);
			userList.remove(pos);
			System.out.println("Compte supprimé avec succès");
			UserLogSuppr.addLog(u, Test.USERFILE, true);
		
		}else {
			System.out.println("Compte inexistant. Aucun compte n'a été supprimé");
			throw new CompteInexException();
		}
			return u;
	}
	
	
	//visualiser tous les users
	public static void showUsers() {
		for (int i = 0; i < User.userList.size(); i++) System.out.println(User.userList.get(i));
	}
	
	//menu Admin
	public static void adminMenu(User u) {
			int choix = 0;
			while(choix != 7) {
				System.out.println("\n\n***************************************\n"
						+ "\nMenu E-wallet Administrateur : \n1 - Créer un compte.\n2 - Visualiser tous les comptes"
						+ "\n3 - Supprimer un compte.\n4 - Ajout Paiement.\n5 - Consultation de solde.\n6 - Recharger solde.\n7 - Quitter " );
				choix = scN.nextInt();
				switch (choix) {
				case 1:
					boolean done = false;
					do {
						try {
							done = Admin.createUserAcc();
						} catch(InputMismatchException input) {
							ExceptionLog.addLog("SysErr : Input mismatch", Test.LOGFILE, true);
							System.out.println("Informations non valides. Réessayez");
						} catch (ChampsVidesInvalidesException cve) {
							ExceptionLog.addLog(cve.getMessage(), Test.LOGFILE, true);
							System.out.println("Informations non valides. Réessayez");
						} 
					}while(done != true);
					break;
				case 2:
					showUsers();
					break;
				case 3:
					User uTemp = null;
					try {
						uTemp = deleteUser();
					} catch (CompteInexException e) {
						ExceptionLog.addLog(e.getMessage(), Test.LOGFILE, true);
					}
					if(uTemp.equals(u)) {
						choix = 7;
						System.out.println("Votre compte a été supprimé. Deconnexion...");
					}
					break;
				case 4:
					try {
						ajoutPaiement(u);
					} catch(InputMismatchException input) {
						ExceptionLog.addLog("SysErr : Input mismatch", Test.LOGFILE, true);
						System.out.println("Informations non valides. Réessayez");
					} catch (ChampsVidesInvalidesException cve) {
						ExceptionLog.addLog(cve.getMessage(), Test.LOGFILE, true);
						System.out.println("Informations non valides. Réessayez");
						choix = 4; //pour réessayer le paiement
					}
					break;
				case 5: 
					try {
						consult(u);
					} catch (CompteInexException e) {
						ExceptionLog.addLog(e.getMessage(), Test.LOGFILE, true);
					}
					break;
				case 6:
					try {
						rechargerSolde(u);
					}catch(InputMismatchException input) {
						ExceptionLog.addLog("SysErr : Input mismatch", Test.LOGFILE, true);
						System.out.println("Informations non valides. Réessayez");
					}catch (ChampsVidesInvalidesException cve) {
						ExceptionLog.addLog(cve.getMessage(), Test.LOGFILE, true);
						System.out.println("Informations non valides. Réessayez");
						choix = 6; //pour réessayer le paiement
					}
					break;
				case 7:
					System.out.println("Au revoir !!");
					break;
				default:
					System.out.println("Choix incorrect. Réessayez");
					break;
				}
			
			}
		}
	}
	
	
	
	

