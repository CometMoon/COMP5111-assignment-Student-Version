package comp5111.assignment;
import soot.*;
import soot.jimple.*;
import soot.util.*;

import java.util.*;

public class InvokeInstrumenter extends BodyTransformer {

	static SootClass counterClass;
	static SootMethod increaseCounter, reportCounter;
	static int statementID = 1;

	static {
		counterClass = Scene.v().loadClassAndSupport("comp5111.assignment.MyCounter");
		increaseCounter = counterClass.getMethod("void increaseStatementCounter(int)");
		reportCounter = counterClass.getMethod("void reportStatementCounter()");
		Scene.v().setSootClassPath(null);
	}

	/*
	 * internalTransform goes through a method body and inserts counter
	 * instructions before each statement
	 */
	protected void internalTransform(Body body, String phase, Map options) {
		// body's method
		SootMethod method = body.getMethod();

		// debugging
		System.out.println("instrumenting method : " + method.getSignature());

		// get body's unit as a chain
		Chain units = body.getUnits();

		// get a snapshot iterator of the unit
		Iterator stmtIt = units.snapshotIterator();
		
		// iterating over each statement
		while (stmtIt.hasNext()) {

			System.err.println(statementID);
			
			// cast back to a statement.
			Stmt stmt = (Stmt) stmtIt.next();

			// call Chain.insertBefore() to insert instructions
			InvokeExpr incExpr = Jimple.v().newStaticInvokeExpr(
					increaseCounter.makeRef(), IntConstant.v(statementID));

			Stmt incStmt = Jimple.v().newInvokeStmt(incExpr);

			units.insertBefore(incStmt, stmt);
			
			statementID++;
			
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
	}
}
