package main.java.com.jkamal.app;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class HgrClustering {	
	public void runClustering(ConfigParameters params){
		List<String> arg_list = null;
		String file_name = null;
		File wrl_file = null;		
		long startTime;
		long duration;
		int w_id = 0;
		
		//Logging
		PrintWriter writer = LogWriter.getWriter();
		
		//Detail Logging
		File console_log = new File("./log/"+(new SimpleDateFormat("yyyyMMddHHmmss")).format(new Date())+"-detail.log");
		FileOutputStream _fos = null;
		
		try {
			_fos = new FileOutputStream(console_log);
		} catch (FileNotFoundException e) {		
			e.printStackTrace();
		}
		
		//File output
		final PrintStream _ps_file = new PrintStream(_fos);		
		//Console output
		final PrintStream _ps_console = System.out;
		
		//Actual Clustering
		while(params.getRuns() != 0){
			w_id = 0;
			
			for(int i = 0; i <= params.getClusters().size()-1; i++){//cluster_size
				for(int j = 0; j <= params.getTransactions().size()-1; j++){//transaction_numbers
					for(int k = 0; k <= params.getDimensions().size()-1; k++){//transactional_dimensions			        
						++w_id;
		
						//Construct workload file name
						file_name = "./workload/hgr/"+w_id+".hgr";
						wrl_file = new File(file_name);
						
						if(!wrl_file.exists()){
							System.setOut(_ps_console);
							System.out.println("Workload file "+w_id+".hgr doesn't exist in the \"workload\" directory. Skipping ...");
						}else{						
							//Hyper-graph clustering parameters		
					        arg_list = new ArrayList<String>();
							arg_list.add("./metis/khmetis");
							arg_list.add(file_name);
							arg_list.add(Integer.toString(params.getClusters().get(i)));	// Nparts
							arg_list.add("5");					// UBfactor(>=5)
							arg_list.add("10");					// Nruns(>=1)
							arg_list.add("1");					// CType(1-5)
							arg_list.add("1");					// OType(1-2) -- 1: Minimizes the hyper edge cut, 2: Minimizes the sum of external degrees (SOED)
							arg_list.add("0");					// Vcycle(0-3)
							arg_list.add("0");					// dbglvl(0+1+2+4+8+16)
							
							String[] arg = arg_list.toArray(new String[arg_list.size()]);					
							ProcessBuilder pb = new ProcessBuilder(arg);
							
							System.setOut(_ps_console);
							System.out.println("["+w_id+"] Clustering workload file "+w_id+".hgr ...");					
							startTime = System.nanoTime();
					        					
							try {
								final Process p = pb.start();						
								
								new Thread(new Runnable(){
									public void run(){
							        	Scanner stdin = new Scanner(p.getInputStream());
							            while(stdin.hasNextLine()){
							            	System.setOut(_ps_file);
							            	System.out.println(stdin.nextLine());
							            }
							            
							            stdin.close();
									}
							    }).start();
								
								p.waitFor();						
							} catch (IOException | InterruptedException e) {						
								e.printStackTrace();
							}
							
							duration = (System.nanoTime() - startTime);		
							System.setOut(_ps_console);
					        System.out.println("Workload file "+w_id+". has been clustered in "+duration + " nano sec.");
					        
					        //Logging
					        writer.print((params.getDimensions().get(k))+" "+(params.getTransactions().get(j))+" "+(params.getClusters().get(i))+" "+duration);
					        //writer.print(duration);
					        writer.println();
					        writer.flush();
						}
					}
				}
			}
			
			params.setRuns(params.getRuns() - 1);
		}
		
		//Closing log
		writer.close();
		
		//Closing detail-log
		try {
			_fos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}