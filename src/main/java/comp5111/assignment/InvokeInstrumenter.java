package comp5111.assignment;
import soot.*;
import soot.jimple.internal.*;
import soot.jimple.*;
import soot.util.*;

import java.io.*;
import java.util.*;

public class InvokeInstrumenter extends BodyTransformer {

	static SootClass counterClass;
	static SootMethod increaseStatementCounter, reportStatementCounter;
	static int statementID = 1;
	static HashMap<String, Integer> classStatementCount;

	static {
		counterClass = Scene.v().loadClassAndSupport("comp5111.assignment.MyCounter");
		increaseStatementCounter = counterClass.getMethod("void increaseStatementCounter(java.lang.String,int)");
		reportStatementCounter = counterClass.getMethod("void reportStatementCounter()");
		classStatementCount = new HashMap<>();
		Scene.v().setSootClassPath(null);
	}

	/*
	 * internalTransform goes through a method body and inserts counter
	 * instructions before each statement
	 */
	protected void internalTransform(Body body, String phase, Map options) {
		
		// body's method
		SootMethod method = body.getMethod();
		
		String className = method.getDeclaringClass().toString();

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

			if (!(stmt instanceof JIdentityStmt)) {
				
				if (!classStatementCount.containsKey(className)) {
					
					classStatementCount.put(className, 0);
		            
		        }
				
				classStatementCount.put(className, classStatementCount.get(className)+1);
				
				// call Chain.insertBefore() to insert instructions
				InvokeExpr incExpr = Jimple.v().newStaticInvokeExpr(
						increaseStatementCounter.makeRef(), StringConstant.v(className), IntConstant.v(statementID));
	
				Stmt incStmt = Jimple.v().newInvokeStmt(incExpr);
	
				units.insertBefore(incStmt, stmt);
				
				statementID++;
				
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
							reportStatementCounter.makeRef());

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

        	System.err.println("Write file error...");
        	
        }
		
	}
}
