package com.github.apercova.quickcli;

import com.github.apercova.quickcli.exception.ExecutionException;
import com.github.apercova.quickcli.annotation.CLIArgument;
import com.github.apercova.quickcli.annotation.CLICommand;
import java.io.Closeable;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * DefaultCommand base command.
 *
 * @author
 * <a href="https://twitter.com/apercova" target="_blank">{@literal @}apercova</a>
 * <a href="https://github.com/apercova" target="_blank">https://github.com/apercova</a>
 * @param <T> Command result type
 * @since 1.0
 *
 */
public abstract class Command<T> implements Executable<T>, Closeable {

    /**
     * Command logger
     */
    protected static final Logger LOGGER = Logger.getLogger(Command.class.getName());
    
    /**
     * Command name.
     */
    protected String name;
    /**
     * Command description.
     */
    protected String description;
    /**
     * Command {@link Locale}.
     */
    protected Locale locale;
    /**
     * Command {@link PrintWriter}.
     */
    protected PrintWriter writer;

    public Command() {
    }
    
    /**
     * Retrieve command's name.
     *
     * @return command's name.
     */
    public String getName() {
        return name;
    }

    /**
     * Retrieve command's description.
     *
     * @return command's description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Set provided {@link Writer} as the underlying {@link PrintWriter}.
     *
     * @param writer out writer.
     */
    public void setWriter(Writer writer) {
        this.writer = ((writer != null)
                ? (writer instanceof PrintWriter
                        ? (PrintWriter) writer
                        : new PrintWriter(writer, true))
                : new PrintWriter(System.out, true));
    }

    /**
     * Retrieve underlying {@link PrintStream}.
     *
     * @return underlying {@link PrintStream}.
     */
    public PrintWriter getWriter() {
        return writer;
    }

    /**
     * Set provided {@link Locale} as the underlying {@link Locale}.
     *
     * @param locale a {@link Locale}.
     */
    public void setLocale(Locale locale) {
        this.locale = locale == null ? Locale.getDefault() : locale;
    }

    /**
     * retrieve underlying {@link Locale}.
     *
     * @return underlying {@link Locale}.
     */
    public Locale getLocale() {
        return locale;
    }

    /**
     * Display auto-generated usage info.
     */
    public void printUsage() {
        if (getClass().isAnnotationPresent(CLICommand.class)) {
            CLICommand command = getClass().getAnnotation(CLICommand.class);
            if (command.value() != null && command.value().length() != 0) {
                writer.printf(locale, "[%s]: ", command.value());
            }
            if (command.description() != null && command.description().length() != 0) {
                writer.printf(locale, "%-100s", command.description());
            }
            writer.printf(locale, "%n");
        }
        writer.printf(locale, "%-5s%-15s%-25s %s%n", "", "option", "aliases", "usage");
        writer.printf(locale, "%-5s%-15s%-25s %s%n", "", "----------", "---------------", "-------------------------");
        for (Field f : getClass().getDeclaredFields()) {
            if (f.isAnnotationPresent(CLIArgument.class)) {
                CLIArgument arg = f.getAnnotation(CLIArgument.class);
                if (arg.aliases().length > 0) {
                    writer.printf(locale, "%-5s%-15s%-25s %s%n", "", arg.name(), Arrays.toString(arg.aliases()), arg.usage());
                } else {
                    writer.printf(locale, "%-5s%-40s %s%n", "", arg.name(), arg.usage());
                }

            }
        }
    }

    /**
     * implements command execution.Prints command usage as default
     * implementation.
     *
     * @throws ExecutionException If an error raised when executing command.
     */
    public T execute() throws ExecutionException {
        this.printUsage();
        return null;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName()).append(" [name=").append(name).append(", description=").append(description);

        for (Field f : getClass().getDeclaredFields()) {
            try {
                if (Modifier.isPublic(f.getModifiers())
                        || f.isAnnotationPresent(CLIArgument.class)) {
                    f.setAccessible(true);
                    sb.append(String.format(", %s=%s", f.getName(), f.get(this)));
                }
            } catch (IllegalAccessException e) {
                LOGGER.log(Level.FINER, e.getMessage(), e);
            } catch (IllegalArgumentException e) {
                LOGGER.log(Level.FINER, e.getMessage(), e);
            }
        }
        sb.append("]");
        return sb.toString();
    }

    public void close() throws IOException {
        if (writer != null) {
            writer.close();
        }
    }

}
