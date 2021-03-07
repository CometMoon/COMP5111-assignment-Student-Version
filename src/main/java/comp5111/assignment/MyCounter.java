package comp5111.assignment;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.*;

/* The counter class */
public class MyCounter {
	
	/* the counter, initialize to zero */
	private static HashMap<String, Integer> statements;
	private static HashMap<String, String> statementClass;
	private static HashMap<String, Integer> branches;
	private static HashMap<String, String> branchClass;
	private static HashMap<String, Integer> lines;
	private static HashMap<String, String> lineClass;
	private static String lastIf;
	
	static {
		statements = new HashMap<>();
		statementClass = new HashMap<>();
		branches = new HashMap<>();
		branchClass = new HashMap<>();
		lines = new HashMap<>();
		lineClass = new HashMap<>();
		lastIf = null;
	}
	
	/**
	* increases the counter of specific statement
	*/
	public static synchronized void increaseStatementCounter(String className, String statementID) {
		
		if (!statements.containsKey(statementID)) {
			
            statements.put(statementID, 0);
            
        }
		
		statements.put(statementID, statements.get(statementID) + 1);
		
		if (!statementClass.containsKey(statementID)) {
			
			statementClass.put(statementID, className);
            
        }
		
	}
	
	/**
	* increases the counter of specific branch
	*/
	public static synchronized void recordIf(String statementID) {
		
		lastIf = statementID;
		
	}
	
	/**
	* increases the counter of specific branch
	*/
	public static synchronized void checkBranch(String className, String statementID) {
		
		if (lastIf == null) {
			return;
		}
		
		String branchID = lastIf + "-" + statementID;
		
		if (!branches.containsKey(branchID)) {
			
			branches.put(branchID, 0);
            
        }
		
		branches.put(branchID, branches.get(branchID) + 1);
		
		if (!branchClass.containsKey(branchID)) {
			
			branchClass.put(branchID, className);
            
        }
		
		lastIf = null;
		
	}
	
	/**
	* increases the counter of specific line
	*/
	public static synchronized void increaseLineCounter(String className, String lineNumber) {
		
		if (!lines.containsKey(lineNumber)) {
			
			lines.put(lineNumber, 0);
            
        }
		
		lines.put(lineNumber, lines.get(lineNumber) + 1);
		
		if (!lineClass.containsKey(lineNumber)) {
			
			lineClass.put(lineNumber, className);
            
        }
		
	}
	
	/**
	* reports the counter content.
	*/
	public static synchronized void reportCounter() {
		
		/**
		 * report statement counter for each class
		 */
		Map<String, Integer> statementCounterPerClass = new HashMap<String, Integer>();
		
		for (Map.Entry<String, String> entry : statementClass.entrySet()) {
			
			String statementID = entry.getKey();
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
		
		/**
		 * report branch counter for each class
		 */
		Map<String, Integer> branchCounterPerClass = new HashMap<String, Integer>();
		
		for (Map.Entry<String, String> entry : branchClass.entrySet()) {
			
		    String branchID = entry.getKey();
		    String className = entry.getValue();
		    
		    if (!branchCounterPerClass.containsKey(className)) {
				
		    	branchCounterPerClass.put(className, 0);
	            
	        }
		    
		    branchCounterPerClass.put(className, branchCounterPerClass.get(className) + 1);
		      
		}
		
		for (Map.Entry<String, Integer> entry : branchCounterPerClass.entrySet()) {
			
			String className = entry.getKey();
			int counter = entry.getValue();
			
			try {
				
				// Create folder for storing number of branch in each class
				File oDir = new File("executedBranch/");
				oDir.mkdirs();
				
				File ofile = new File("executedBranch/" + className + "_" + "branch_count.txt");
				
				BufferedWriter writer = new BufferedWriter(new FileWriter(ofile));
				
				writer.write(Integer.toString(counter));
				
				writer.close();
				
	        } catch (Exception e) {

	        	System.err.println("Write file error...");
	        	
	        }
		
		}
		
		/**
		 * report line counter for each class
		 */
		Map<String, Integer> lineCounterPerClass = new HashMap<String, Integer>();
		
		for (Map.Entry<String, String> entry : lineClass.entrySet()) {
			
		    String lineNumber = entry.getKey();
		    String className = entry.getValue();
		    
		    if (!lineCounterPerClass.containsKey(className)) {
				
		    	lineCounterPerClass.put(className, 0);
	            
	        }
		    
		    lineCounterPerClass.put(className, lineCounterPerClass.get(className) + 1);
		      
		}
		
		for (Map.Entry<String, Integer> entry : lineCounterPerClass.entrySet()) {
			
			String className = entry.getKey();
			int counter = entry.getValue();
			
			try {
				
				// Create folder for storing number of branch in each class
				File oDir = new File("executedLine/");
				oDir.mkdirs();
				
				File ofile = new File("executedLine/" + className + "_" + "line_count.txt");
				
				BufferedWriter writer = new BufferedWriter(new FileWriter(ofile));
				
				writer.write(Integer.toString(counter));
				
				writer.close();
				
	        } catch (Exception e) {

	        	System.err.println("Write file error...");
	        	
	        }
		
		}
	
	}
}