package comp5111.assignment;
import soot.*;
import soot.jimple.internal.*;
import soot.tagkit.LineNumberTag;
import soot.tagkit.Tag;
import soot.jimple.*;
import soot.util.*;

import java.io.*;
import java.util.*;

public class InvokeInstrumenter extends BodyTransformer {

	static SootClass counterClass;
	static SootMethod increaseStatementCounter, recordIf, checkBranch, increaseLineCounter, reportCounter;
	static HashMap<String, Integer> classStatementCount;
	static HashMap<String, Integer> classBranchCount;
	static HashMap<String, Integer> classLineCount;
	static HashMap<String, Integer> lineCount;
	static HashMap<String, String> statementJimple;
	static int statementCounter = 1;

	static {
		counterClass = Scene.v().loadClassAndSupport("comp5111.assignment.MyCounter");
		increaseStatementCounter = counterClass.getMethod("void increaseStatementCounter(java.lang.String,java.lang.String)");
		recordIf = counterClass.getMethod("void recordIf(java.lang.String)");
		checkBranch = counterClass.getMethod("void checkBranch(java.lang.String,java.lang.String)");
		increaseLineCounter = counterClass.getMethod("void increaseLineCounter(java.lang.String,java.lang.String)");
		reportCounter = counterClass.getMethod("void reportCounter()");
		classStatementCount = new HashMap<>();
		classBranchCount = new HashMap<>();
		classLineCount = new HashMap<>();
		lineCount = new HashMap<>();
		statementJimple = new HashMap<>();
		Scene.v().setSootClassPath(null);
	}

	/*
	 * internalTransform goes through a method body and inserts counter
	 * instructions before each statement
	 */
	protected void internalTransform(Body body, String phase, Map options) {
		
		// body's method
		SootMethod method = body.getMethod();
		
		// Get class name
		String className = method.getDeclaringClass().toString();
		
		// Initialize statement counter for class
		if (!classStatementCount.containsKey(className)) {
			
			classStatementCount.put(className, 0);
            
        }
		
		// Initialize branch counter for class
		if (!classBranchCount.containsKey(className)) {
			
			classBranchCount.put(className, 0);
            
        }
		
		// Initialize line counter for class
		if (!classLineCount.containsKey(className)) {
			
			classLineCount.put(className, 0);
            
        }

		// debugging
		System.out.println("instrumenting method : " + method.getSignature());

		// get body's unit as a chain
		Chain units = body.getUnits();

		// get a snapshot iterator of the unit
		Iterator stmtIt = units.snapshotIterator();
		
		// iterating over each statement
		while (stmtIt.hasNext()) {
			
			// cast back to a statement.
			Stmt stmt = (Stmt) stmtIt.next();
			
			String statementID = className + ":" + statementCounter;

			if (!(stmt instanceof JIdentityStmt)) {
				
				statementJimple.put(statementID, String.valueOf(stmt));
				
				classStatementCount.put(className, classStatementCount.get(className)+1);
				
				// call Chain.insertBefore() to insert instructions
				InvokeExpr incExpr = Jimple.v().newStaticInvokeExpr(
						increaseStatementCounter.makeRef(), 
						StringConstant.v(className), 
						StringConstant.v(statementID));
	
				Stmt incStmt = Jimple.v().newInvokeStmt(incExpr);
	
				units.insertBefore(incStmt, stmt);
				
				statementCounter++;
				
				// call Chain.insertBefore() to insert instructions
				incExpr = Jimple.v().newStaticInvokeExpr(
						checkBranch.makeRef(), 
						StringConstant.v(className), 
						StringConstant.v(statementID));
	
				Stmt chkBranch = Jimple.v().newInvokeStmt(incExpr);
	
				units.insertBefore(chkBranch, stmt);
				
			}
			
			if (stmt instanceof JIfStmt) {
				
				classBranchCount.put(className, classBranchCount.get(className)+1);

                InvokeExpr incExpr = Jimple.v().newStaticInvokeExpr(
                		recordIf.makeRef(),
                		StringConstant.v(statementID));
                
                Stmt incIf = Jimple.v().newInvokeStmt(incExpr);
                
                units.insertBefore(incIf, stmt);
                
            }
			
			for (Iterator j = stmt.getTags().iterator(); j.hasNext(); ) {
				
				Tag tag = (Tag)j.next();
				
				if (tag instanceof LineNumberTag) {
					
					String lineNumber = String.valueOf(((LineNumberTag) tag).getLineNumber());
					
					if (!lineCount.containsKey(lineNumber)) {
					
						lineCount.put(lineNumber, 1);
						
						classLineCount.put(className, classLineCount.get(className)+1);
	
		                InvokeExpr incExpr = Jimple.v().newStaticInvokeExpr(
		                		increaseLineCounter.makeRef(),
		                		StringConstant.v(className),
		                		StringConstant.v(lineNumber));
		                
		                Stmt incIf = Jimple.v().newInvokeStmt(incExpr);
		                
		                units.insertBefore(incIf, stmt);
		                
					}
					
					// System.err.println(String.valueOf(lineNumber));
					
				}
				
			}
		
		}

		// Check if it is main method
		String signature = method.getSubSignature();
		boolean isMain = signature.equals("void main(java.lang.String[])");

		// report counter before program exit
		if (!isMain) {
			stmtIt = units.snapshotIterator();

			while (stmtIt.hasNext()) {
				Stmt stmt = (Stmt) stmtIt.next();

				if ((stmt instanceof ReturnStmt)
						|| (stmt instanceof ReturnVoidStmt)) {
					
					InvokeExpr reportExpr = Jimple.v().newStaticInvokeExpr(
							reportCounter.makeRef());

					Stmt reportStmt = Jimple.v().newInvokeStmt(reportExpr);

					units.insertBefore(reportStmt, stmt);
				}
			}
		}
		
		try {
			
			// Create folder for storing number of statement in each class
			File oDir = new File("scripts/numOfStatement/");
			oDir.mkdirs();
			
			File ofile = new File("scripts/numOfStatement/" + className + "_" + "statement_count.txt");
			
			BufferedWriter writer = new BufferedWriter(new FileWriter(ofile));
			
			writer.write(Integer.toString(classStatementCount.get(className)));
			
			writer.close();
			
        } catch (Exception e) {

        	System.err.println("Write statement count file error...");
        	
        }
		
		try {
			
			// Create folder for storing number of statement in each class
			File oDir = new File("scripts/numOfBranch/");
			oDir.mkdirs();
			
			File ofile = new File("scripts/numOfBranch/" + className + "_" + "branch_count.txt");
			
			BufferedWriter writer = new BufferedWriter(new FileWriter(ofile));
			
			writer.write(Integer.toString(classBranchCount.get(className)*2));
			
			writer.close();
			
        } catch (Exception e) {

        	System.err.println("Write branch count file error...");
        	
        }
		
		try {
			
			// Create folder for storing number of statement in each class
			File oDir = new File("scripts/numOfLine/");
			oDir.mkdirs();
			
			File ofile = new File("scripts/numOfLine/" + className + "_" + "line_count.txt");
			
			BufferedWriter writer = new BufferedWriter(new FileWriter(ofile));
			
			writer.write(Integer.toString(classLineCount.get(className)));
			
			writer.close();
			
        } catch (Exception e) {

        	System.err.println("Write line count file error...");
        	
        }
		
	}
	
	public static void saveJimpleCode() {
		
		BufferedWriter writer = null;
			
		try {
			
			// Export all statements with id
			File oDir = new File("scripts/statement_map/");
			oDir.mkdirs();
			
			File ofile = new File("scripts/statement_map/statement_map.txt");
			
			writer = new BufferedWriter(new FileWriter(ofile));
			
			for (Map.Entry<String, String> entry : statementJimple.entrySet()) {
				
				String statementID = entry.getKey();
			    String jimpleCode = entry.getValue();
			
			    writer.write(statementID + ":" + jimpleCode + "\r\n");
			
			}
			
        } catch (Exception e) {

        	System.err.println("Export Jimple error...");
        	
        }
	}
}
