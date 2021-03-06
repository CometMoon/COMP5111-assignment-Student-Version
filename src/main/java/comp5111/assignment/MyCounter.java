package comp5111.assignment;

/* The counter class */
public class MyCounter {
	
	/* the counter, initialize to zero */
	private static int c[] = new int[1104];
	
	/**
	* increases the counter by <pre>howmany</pre>
	* @param howmany, the increment of the counter.
	*/
	public static synchronized void increaseStatementCounter(int howmany) {
		
		c += howmany;
		
	}
	
	/**
	* reports the counter content.
	*/
	public static synchronized void reportStatementCounter() {
		
		System.err.println("counter : " + c);
	
	}
}