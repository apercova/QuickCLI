package net.apercova.quickcli;

import java.io.PrintWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Locale;

/**
 * DefaultCommand base command
 * 
 * @author <a href="https://twitter.com/apercova" target="_blank">{@literal @}apercova</a> <a href="https://github.com/apercova" target="_blank">https://github.com/apercova</a>
 * @since 1.0
 *
 */
public abstract class DefaultCommand implements Command{
	
	protected String name;
	protected String description;
	protected PrintWriter writer;
	protected Charset charset;
	protected Locale locale;
		
	public String getName() {
		return name;
	}
	public void setWriter(Writer writer) {
		this.writer =  ((writer != null) 
				? (writer instanceof PrintWriter 
						? (PrintWriter) writer
						:new PrintWriter(writer, true)
					) 
				: new PrintWriter(System.out, true));
	}
	public PrintWriter getWriter() {
		return writer;
	}
	public void setLocale(Locale locale) {
		this.locale = locale != null ? locale : Locale.getDefault();
	}
	public Locale getLocale() {
		return locale;
	}
	public void printUsage() {
		if(getClass().isAnnotationPresent(CLICommand.class)) {
			CLICommand command = getClass().getAnnotation(CLICommand.class);
			if(command.value()!= null && command.value().length() != 0) {
				writer.printf(locale, "[%s]: ",command.value());
			}
			if(command.description() != null && command.description().length() != 0) {
				writer.printf(locale, "%-100s",command.description());
			}
			writer.printf(locale, "%n");
		}
		writer.printf(locale, "%-5s%-15s%-25s %s%n","", "option", "aliases", "usage");
		writer.printf(locale, "%-5s%-15s%-25s %s%n","", "----------", "---------------", "-------------------------");
		for(Field f: getClass().getDeclaredFields()) {
			if(f.isAnnotationPresent(CLIArgument.class)) {
				CLIArgument arg = f.getAnnotation(CLIArgument.class);
				if(arg.aliases().length > 0) {
					writer.printf(locale, "%-5s%-15s%-25s %s%n","", arg.name(), Arrays.toString(arg.aliases()), arg.usage());
				}else {
					writer.printf(locale, "%-5s%-40s %s%n","", arg.name(), arg.usage());
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
