package comp5111.assignment;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.*;

/* The counter class */
public class MyCounter {
	
	/* the counter, initialize to zero */
	private static HashMap<Integer, Integer> c;
	private static HashMap<Integer, String> statementClass;
	
	static {
		c = new HashMap<>();
		statementClass = new HashMap<>();
	}
	
	/**
	* increases the counter by <pre>howmany</pre>
	* @param howmany, the increment of the counter.
	*/
	public static synchronized void increaseStatementCounter(String className, int statementID) {
		
		if (!c.containsKey(statementID)) {
			
            c.put(statementID, 0);
            
        }
		
		c.put(statementID, c.get(statementID) + 1);
		
		if (!statementClass.containsKey(statementID)) {
			
			statementClass.put(statementID, className);
            
        }
		
		
	}
	
	/**
	* reports the counter content.
	*/
	public static synchronized void reportStatementCounter() {
		
		Map<String, Integer> statementCounterPerClass = new HashMap<String, Integer>();
		
		for (Map.Entry<Integer, String> entry : statementClass.entrySet()) {
			
		    int statementID = entry.getKey();
		    String className = entry.getValue();
		    
		    if (!statementCounterPerClass.containsKey(className)) {
				
		    	statementCounterPerClass.put(className, 0);
	            
	        }
		    
		    statementCounterPerClass.put(className, statementCounterPerClass.get(className) + 1);
		      
		}
		
		for (Map.Entry<String, Integer> entry : statementCounterPerClass.entrySet()) {
			
			String className = entry.getKey();
			int counter = entry.getValue();
			
			try {
				
				// Create folder for storing number of statement in each class
				File oDir = new File("executedStatement/");
				oDir.mkdirs();
				
				File ofile = new File("executedStatement/" + className + "_" + "statement_count.txt");
				
				BufferedWriter writer = new BufferedWriter(new FileWriter(ofile));
				
				writer.write(Integer.toString(counter));
				
				writer.close();
				
	        } catch (Exception e) {

	        	System.err.println("Write file error...");
	        	
	        }
		
		}
	
	}
}