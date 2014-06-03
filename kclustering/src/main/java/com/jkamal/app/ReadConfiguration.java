package main.java.com.jkamal.app;

import java.io.FileInputStream;
import java.io.IOException;

import org.apache.commons.configuration.AbstractFileConfiguration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

public class ReadConfiguration {
	public static ConfigParameters read() {		
		FileInputStream config_file = null; 
	    AbstractFileConfiguration config_param = null;
	    ConfigParameters params = null;	    
	    
	    try {
	    	//Access configuration file
	    	try {
	    		config_file = new FileInputStream("./config.param");
	    	} catch(IOException e){
	    		e.printStackTrace();
	    		System.out.println("Configuration file \"config.param\" is missing in the working directory !!");
	    		return null;
	    	}
	    	
		    //Load configuration parameters
	    	config_param = new PropertiesConfiguration();
			config_param.load(config_file);
			
			//Read configuration parameters
			params = new ConfigParameters();
			
			//Read transaction dimensions
			for(Object param : config_param.getList("transaction.dimension"))
		    	params.getDimensions().add(Integer.parseInt((String) param));
			//System.out.println(params.getDimensions());

			//Read transaction numbers
			for(Object param : config_param.getList("transaction.number"))
		    	params.getTransactions().add(Integer.parseInt((String) param));
			//System.out.println(params.getTransactions());
			
			//Read cluster sizes
			for(Object param : config_param.getList("cluster.size"))
		    	params.getClusters().add(Integer.parseInt((String) param));
			//System.out.println(params.getClusters());
		 
			//Read run numbers
			params.setRuns(Integer.parseInt((String) config_param.getProperty("run.numbers")));
			//System.out.println(params.getRuns());
			
		} catch (ConfigurationException e) {
			e.printStackTrace();
		} finally {
			if(config_file != null) {
				try {
					config_file.close();			
				} catch (IOException e) {
					e.printStackTrace();
				}			
			}
		}	    

	    return params;
	}
}