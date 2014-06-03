package main.java.com.jkamal.app;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import org.apache.commons.lang.StringUtils;

public class HgrWorkloadGeneration {
	private static Map<Integer, Integer> vertexMap = null;
	private static Map<Integer, Integer> vertexWeight = null;
	
	public static void generate(ConfigParameters params){
		int lower = 0;
		int upper = 10000;		
		long startTime;
		long duration;
		int w_id = 0;
		
		for(int i = 0; i <= params.getClusters().size()-1; i++){//cluster_size
			for(int j = 0; j <= params.getTransactions().size()-1; j++){//transaction_numbers
				for(int k = 0; k <= params.getDimensions().size()-1; k++){//transactional_dimensions
					++w_id;
										
					startTime = System.nanoTime();
					generateWorkload(w_id, params.getTransactions().get(j), params.getDimensions().get(k), lower, upper);			        
					duration = (System.nanoTime() - startTime);			        

					System.out.println("Workload file "+w_id+".hgr has been generated in "+duration + " nano sec.");
				}
			}
		}
	}
	
	private static Set<Integer> generateTransaction(int dim, int min, int max){
		Set<Integer> transaction = new TreeSet<Integer>();
		int step_size = max/dim;
		int lower_limit = min+1;
		int upper_limit = step_size;
		
		while(transaction.size() < dim){			
			transaction.add(Main.rdg.nextInt(lower_limit, upper_limit));
			lower_limit = upper_limit+1;
			upper_limit += step_size;
		}
		
		return transaction;
	}
	
	private static void generateWorkload(int w_id, int tr_numbers, int tr_dimension, int min, int max){		
		vertexMap = new TreeMap<Integer, Integer>();
		vertexWeight = new TreeMap<Integer, Integer>();
		int v_id = 0;
		int edges = tr_numbers;
		String content = "";
		
		//Creating hyper edges
		Main.rdg.reSeed(0);
		for(int i = 0; i < edges; i++){
			Set<Integer> edge = generateTransaction(tr_dimension, min, max);
			String e = Integer.toString(Main.rdg.nextInt(1, 2))+" "; // Temporal edge weight for two consecutive workload windows			
			
			for(Integer v : edge){
				if(!vertexMap.containsKey(v)){
					++v_id;
					vertexMap.put(v, v_id);
					vertexWeight.put(v_id, 1);
				}else{
					int weight = vertexWeight.get(vertexMap.get(v));
					vertexWeight.remove(vertexMap.get(v));
					vertexWeight.put(vertexMap.get(v), ++weight);
				}
				
				e += Integer.toString(vertexMap.get(v))+" ";
			}
			
			StringUtils.stripEnd(e, null);
			content += e+"\n";
		}
		
		//Creating vertices
		int new_line = vertexMap.size();
		for(int i = 1; i< vertexMap.size()+1; i++){
			content += Integer.toString(vertexWeight.get(i));
			
			--new_line;			
			if(new_line != 0)
				content += "\n";
		}
		
		//Writing in workload file
		File workloadFile = new File("./workload/hgr/"+w_id+".hgr");		
		try {
			workloadFile.createNewFile();
			Writer writer = null;			

			try {
				writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(workloadFile), "utf-8"));
				writer.write(edges+" "+vertexMap.size()+" "+Integer.toString(1)+""+Integer.toString(1)+"\n"+content);
			} catch(IOException e) {
				e.printStackTrace();
			}finally {
				writer.close();
			}
		} catch (IOException e) {		
			e.printStackTrace();
		}
	}
}