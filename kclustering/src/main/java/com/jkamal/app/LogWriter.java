package main.java.com.jkamal.app;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LogWriter {
	public static PrintWriter getWriter() {
		Date date = new Date() ;
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss") ;
		
		File logFile = new File("./log/"+dateFormat.format(date)+".log");
		PrintWriter writer = null;
		
		try {
			if(!logFile.exists())
				logFile.createNewFile();
			
			try {
				writer = new PrintWriter(new BufferedWriter(new FileWriter(logFile)));				
			} catch(IOException e) {
				e.printStackTrace();
			}
		} catch (IOException e) {		
			e.printStackTrace();
		}
		
		return writer;
	}
}