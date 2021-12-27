package LogFiles;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;  
import java.util.Date;

import bean.User;  

public class UserLogCreation {
	    
	    public static void addLog(User user, String userLog, boolean append){
	        File logFile = new File(userLog);
	        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss ");  
	        Date date = new Date();  
	        try{
	            FileWriter fw = new FileWriter(logFile, append);
	            BufferedWriter bw = new BufferedWriter(fw);
	            bw.write("Creation date : " + formatter.format(date));
	            bw.write(user.toString());
	            bw.newLine();
	            bw.flush();
	            bw.close();
	            fw.close();
	        }catch(IOException e){
	            e.printStackTrace();
	            System.err.println("Problème de lecture de fichier");
	        }
	    }
	}
	
	


