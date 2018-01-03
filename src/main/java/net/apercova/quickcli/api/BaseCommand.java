package net.apercova.quickcli.api;

import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.util.Arrays;

/**
 * Base abstract command
 * 
 * @author <a href="https://twitter.com/apercova" target="_blank">{@literal @}apercova</a> <a href="https://github.com/apercova" target="_blank">https://github.com/apercova</a>
 * @since 1.0
 *
 */
public abstract class BaseCommand implements Command{
	
	protected String name;
	protected String description;
	protected PrintWriter out;
	
	public BaseCommand() {
		super();
		this.out = new PrintWriter(System.out,true);
	}
	
	/**
	 * Retrieves command name.
	 * @return Command name.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Defines output printwriter
	 */
	public void setPrintWriter(PrintWriter out) {
		this.out = out;
	}

	/**
	 * Displays Annotation-based usage info 
	 */
	public void printUsage() {
		if(getClass().isAnnotationPresent(CLICommand.class)) {
			CLICommand command = getClass().getAnnotation(CLICommand.class);
			if(command.value()!= null && command.value().length() != 0) {
				out.printf("[%s]: ",command.value());
			}
			if(command.description() != null && command.description().length() != 0) {
				out.printf("%-100s",command.description());
			}
			out.printf("%n");
		}
		out.printf("%-5s%-15s%-25s %s%n","", "option", "aliases", "usage");
		out.printf("%-5s%-15s%-25s %s%n","", "----------", "---------------", "-------------------------");
		for(Field f: getClass().getDeclaredFields()) {
			if(f.isAnnotationPresent(CLIArgument.class)) {
				CLIArgument arg = f.getAnnotation(CLIArgument.class);
				if(arg.aliases().length > 0) {
					out.printf("%-5s%-15s%-25s %s%n","", arg.name(), Arrays.toString(arg.aliases()), arg.usage());
				}else {
					out.printf("%-5s%-40s %s%n","", arg.name(), arg.usage());
				}
				
			}
		}
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getClass().getSimpleName() + " [name=" + name + ", description=" + description);
		
		for(Field f: getClass().getDeclaredFields()) {
			try {
				f.setAccessible(true);
				sb.append(String.format(", %s=%s", f.getName(), f.get(this)));
			} catch (Exception e) {
				e.printStackTrace();
				continue;
			}
		}
		sb.append("]");
		return sb.toString();
	}
	
}
