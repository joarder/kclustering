package main.java.com.jkamal.app;

import org.apache.commons.math3.random.RandomDataGenerator;

public class Main {
	public static RandomDataGenerator rdg;
	
	public static void main(String[] args) {
		rdg = new RandomDataGenerator();
		HgrClustering hgrClustering = null;
		
		//Read configuration parameters
		ConfigParameters params = ReadConfiguration.read();
		if(params == null) {
			System.out.println("Missing configuration file \"config.param\"!! Aborting ...");
			System.exit(0);
		}
		
		//Check workload generation argument
		boolean wrl = false;
		if(args.length >= 2){
			if(args[1].equals("-w"))
				wrl = true;			
			else
				System.out.println("Usage: \"-w\" to generate workload");			
		}
		
		//Perform workload generation and clustering
		switch(args[0]){
		case "-hgr":
			if(wrl) 
				HgrWorkloadGeneration.generate(params);
			else
				System.out.println("Starting k-way hypergraph clustering using existing workload files ...");
			
			hgrClustering = new HgrClustering();
			hgrClustering.runClustering(params);
			break;
		default:
			System.out.println("Usage: \"-gr\" (Graph Clustering), \"-hgr\"  (Hypergraph Clustering), \"-chg\" (Compressed Hypergraph Clustering)");
			break;
		}
	}
}