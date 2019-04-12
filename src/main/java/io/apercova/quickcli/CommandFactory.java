package io.apercova.quickcli;

import io.apercova.quickcli.annotation.CLICommand;
import io.apercova.quickcli.exception.CLIArgumentException;
import io.apercova.quickcli.exception.ReflectiveOperationException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Factory for {@link Command } creation.
 *
 * @author
 * <a href="https://twitter.com/apercova" target="_blank">{@literal @}apercova</a>
 * <a href="https://github.com/apercova" target="_blank">https://github.com/apercova</a>
 * @since 1.0
 *
 */
public final class CommandFactory {

    private static final String MESSAGE_BOUNDLE = "io.apercova.quickcli.i18n.messages";

    private CommandFactory() {
        super();
    }

    /**
     * Creates a command of the provided {@link Class} type.
     *
     * @param <T> Command type.
     * @param args CLI Arguments.
     * @param clazz Command type class.
     * @return Command {@link Command} instance.
     * @throws CLIArgumentException If any error occurs at command creation.
     */
    public static <T extends Command<?>> T create(String[] args, Class<T> clazz)
            throws CLIArgumentException {
        return create(args, clazz, System.out, Charset.defaultCharset(), Locale.getDefault());
    }

    /**
     * Creates a command of the provided {@link Class} type.
     *
     * @param <T> Command type.
     * @param args CLI Arguments.
     * @param clazz Command type class.
     * @param locale Output locale.
     * @return Command {@link Command} instance.
     * @throws CLIArgumentException If an error occurs at command creation.
     */
    public static <T extends Command<?>> T create(String[] args, Class<T> clazz, Locale locale)
            throws CLIArgumentException {
        return create(args, clazz, System.out, Charset.defaultCharset(), locale);
    }

    /**
     * Creates a command of the provided {@link Class} type.
     *
     * @param <T> Command type.
     * @param args CLI Arguments.
     * @param clazz Command type class.
     * @param out Output stream.
     * @return Command {@link Command} instance.
     * @throws CLIArgumentException If any error occurs at command creation.
     */
    public static <T extends Command<?>> T create(String[] args, Class<T> clazz, OutputStream out)
            throws CLIArgumentException {
        return create(args, clazz, out, Charset.defaultCharset(), Locale.getDefault());
    }

    /**
     * Creates a command of the provided {@link Class} type.
     *
     * @param <T> Command type.
     * @param args CLI Arguments.
     * @param clazz Command type class.
     * @param out Output stream.
     * @param locale Output locale.
     * @return Command {@link Command} instance.
     * @throws CLIArgumentException If any error occurs at command creation.
     */
    public static <T extends Command<?>> T create(String[] args, Class<T> clazz, OutputStream out, Locale locale)
            throws CLIArgumentException {
        return create(args, clazz, out, Charset.defaultCharset(), locale);
    }

    /**
     * Creates a command of the provided {@link Class} type.
     *
     * @param <T> Command type.
     * @param args CLI Arguments.
     * @param clazz Command type class.
     * @param out Output stream.
     * @param cs Output character set.
     * @return Command {@link Command} instance.
     * @throws CLIArgumentException If any error occurs at command creation.
     */
    public static <T extends Command<?>> T create(String[] args, Class<T> clazz, OutputStream out, Charset cs)
            throws CLIArgumentException {
        return create(args, clazz, out, cs, Locale.getDefault());
    }

    /**
     * Creates a command of the provided {@link Class} type.
     *
     * @param <T> Command type.
     * @param args CLI Arguments.
     * @param clazz Command type class.
     * @param out Output stream.
     * @param cs Output character set.
     * @param locale Output locale.
     * @return Command {@link Command} instance.
     * @throws CLIArgumentException If any error occurs at command creation.
     */
    public static <T extends Command<?>> T create(String[] args, Class<T> clazz, OutputStream out, Charset cs, Locale locale)
            throws CLIArgumentException {
        Writer writer = new OutputStreamWriter(
                (out != null ? out : System.out),
                (cs != null ? cs : Charset.defaultCharset())
        );
        return create(args, clazz, writer, locale);
    }

    /**
     * Creates a command of the provided {@link Class} type.
     *
     * @param <T> Command type.
     * @param args CLI Arguments.
     * @param clazz Command type class.
     * @param writer Output writer.
     * @return Command {@link Command} instance.
     * @throws CLIArgumentException If any error occurs at command creation.
     */
    public static <T extends Command<?>> T create(String[] args, Class<T> clazz, Writer writer)
            throws CLIArgumentException {
        return create(args, clazz, writer, Locale.getDefault());
    }

    /**
     * Creates a command of the provided {@link Class} type.
     *
     * @param <T> Command type.
     * @param args CLI Arguments.
     * @param clazz Command type class.
     * @param writer Output writer.
     * @param locale Output locale.
     * @return Command Command instance.
     * @throws CLIArgumentException If any error occurs at command creation.
     */
    public static <T extends Command<?>> T create(String[] args, Class<T> clazz, Writer writer, Locale locale)
            throws CLIArgumentException {
        locale = locale == null ? Locale.getDefault() : locale;
        ResourceBundle messages = ResourceBundle.getBundle(MESSAGE_BOUNDLE, locale);

        try {
            if (clazz.isAnnotationPresent(CLICommand.class)) {
                T command = clazz.newInstance();
                command.setLocale(locale);
                command.setWriter(writer);

                synchronized (command) {
                    CommandParser.parse(command, args, messages);
                    CommandParser.validate(command, messages);
                }
                return command;
            } else {
                throw new CLIArgumentException(MessageFormat.format(messages.getString("type.invalid"), clazz.getName()));
            }
        } catch (ReflectiveOperationException ex) {
            throw new CLIArgumentException(MessageFormat.format(messages.getString("type.invalid"), clazz.getName()), ex);
        } catch (InstantiationException ex) {
            Throwable cause = new ReflectiveOperationException(ex);
            Logger.getLogger(CommandFactory.class.getName()).log(Level.SEVERE, ex.getMessage(), cause);
            throw new CLIArgumentException(MessageFormat.format(messages.getString("type.invalid"), clazz.getName()), cause);
        } catch (IllegalAccessException ex) {
            Throwable cause = new ReflectiveOperationException(ex);
            Logger.getLogger(CommandFactory.class.getName()).log(Level.SEVERE, ex.getMessage(), cause);
            throw new CLIArgumentException(MessageFormat.format(messages.getString("type.invalid"), clazz.getName()), cause);
        }
    }
}
