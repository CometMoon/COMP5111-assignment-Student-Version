/* Usage: java MainDriver [soot-options] classname
 */

package comp5111.assignment;

/* import necessary soot packages */
import soot.*;
import soot.options.Options;
import java.io.File;

public class MainDriver {
	
	public static void main(String[] args) {

		/* check the arguments */
		if (args.length == 0) {
			
			System.err.println("Usage: java MainDriver [options] classname");
			System.exit(0);
			
		}
		
		/*Set the soot-classpath to include the helper class and class to analyze*/
		Options.v().set_soot_classpath(Scene.v().defaultClassPath()
			+ File.pathSeparator + "target/classes"
			+ File.pathSeparator + "target/classes/junit-4.12.jar"
		);
		
		/* add a phase to transformer pack by call Pack.add */
		Pack jtp = PackManager.v().getPack("jtp");
		
		jtp.add(new Transform("jtp.instrumenter", new InvokeInstrumenter()));
		
		for(String arg:args){
			
			System.out.println(arg);
			
		}
		
		soot.Main.main(args);
		
		InvokeInstrumenter.saveMarkedCode();
		
	}
	
}