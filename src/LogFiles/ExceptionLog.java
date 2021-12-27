package LogFiles;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;  
import java.util.Date;  

public class ExceptionLog {
	    
	    public static void addLog(String message, String fichierLog, boolean append){
	        File logFile = new File(fichierLog);
	        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss ");  
	        Date date = new Date();  
	        try{
	            FileWriter fw = new FileWriter(logFile, append);
	            BufferedWriter bw = new BufferedWriter(fw);
	            bw.write(formatter.format(date));
	            bw.write(message);
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
	
	


