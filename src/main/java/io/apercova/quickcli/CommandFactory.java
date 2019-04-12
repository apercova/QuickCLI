package io.apercova.quickcli;

import io.apercova.quickcli.annotation.CLIArgument;
import io.apercova.quickcli.annotation.CLICommand;
import io.apercova.quickcli.annotation.CLIDatatypeConverter;
import io.apercova.quickcli.exception.CLIArgumentException;
import io.apercova.quickcli.exception.DatatypeConverterException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

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
     * @param <T> Generic command type.
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
     * @param <T> Generic command type.
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
     * @param <T> Generic command type.
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
     * @param <T> Generic command type.
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
     * @param <T> Generic command type.
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
     * @param <T> Generic command type.
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
     * @param <T> Generic command type.
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
     * @param <T> Generic command type.
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
                T command = clazz.getDeclaredConstructor().newInstance();
                command.setLocale(locale);
                command.setWriter(writer);

                synchronized (command) {
                    read(command, args, messages);
                    verify(command, messages);
                }
                return command;
            } else {
                throw new CLIArgumentException(
                        MessageFormat.format(messages.getString("type.invalid"), clazz.getName())
                );
            }
        } catch (ReflectiveOperationException e) {
            throw new CLIArgumentException(
                    MessageFormat.format(messages.getString("type.invalid"), clazz.getName()),
                    e
            );
        }
    }

    /**
     * Read and set command's properties.
     *
     * @param command {@link Command} instance.
     * @param args Command's arguments.
     * @param messages {@link ResourceBundle} messages.
     * @throws CLIArgumentException
     * @throws NoSuchFieldException
     * @throws ReflectiveOperationException
     */
    private static <T extends Command<?>> void read(T command, String[] args, ResourceBundle messages)
            throws CLIArgumentException, ReflectiveOperationException {

        //Command properties
        readCommandProps(command);

        //CLI Arguments
        List<String> argSet = new ArrayList<String>(Arrays.asList(args));

        //Declared argument map
        Map<String, CLIArgument> argMap = getDeclaredArgs(command);

        String alias = null;
        boolean lookArg = true;
        for (int i = 0; i < argSet.size(); i++) {

            String arg = argSet.get(i);
            if (lookArg) {
                if (isArgument(arg, argMap)) {
                    lookArg = false;
                    alias = arg;
                    Field field = getAnnotatedField(argMap.get(arg), command);
                    if (field != null) {
                        field.setAccessible(true);
                        if (boolean.class.equals(field.getType()) || Boolean.class.equals(field.getType())) {
                            parseValue(argMap.get(arg), field, String.valueOf(Boolean.TRUE), command, messages);
                            lookArg = true;
                            alias = null;

                        } else if (!argMap.get(arg).required()) {
                            if ((i + 1) < argSet.size() && !isArgument(argSet.get((i + 1)), argMap)) {
                                parseValue(argMap.get(arg), field, argSet.get((i + 1)), command, messages);
                                i++;

                            } else {
                                parseValue(argMap.get(arg), field, argMap.get(arg).value(), command, messages);

                            }
                            lookArg = true;
                            alias = null;
                        } else {
                            if ((i + 1) >= argSet.size()) {
                                throw new CLIArgumentException(
                                        MessageFormat.format(messages.getString("arg.required"), alias)
                                );
                            }
                        }
                    }
                } else {
                    throw new CLIArgumentException(
                            MessageFormat.format(messages.getString("arg.invalid"), arg)
                    );
                }
            } else {
                if (!isArgument(arg, argMap)) {
                    Field field = getAnnotatedField(argMap.get(alias), command);
                    if (field != null) {
                        field.setAccessible(true);
                        parseValue(argMap.get(alias), field, arg, command, messages);
                        lookArg = true;
                        alias = null;
                    }
                } else {
                    throw new CLIArgumentException(
                            MessageFormat.format(messages.getString("arg.required"), alias)
                    );
                }
            }
        }

    }

    /**
     * Verify command's required arguments.
     *
     * @param command {@link Command} instance.
     * @param messages {@link ResourceBundle} messages.
     * @throws CLIArgumentException
     * @throws ReflectiveOperationException
     */
    private static <T extends Command<?>> void verify(T command, ResourceBundle messages)
            throws CLIArgumentException, ReflectiveOperationException {

        //Validating fields
        for (Field f : command.getClass().getDeclaredFields()) {
            if (f.isAnnotationPresent(CLIArgument.class)) {
                f.setAccessible(true);
                Object value = f.get(command);
                CLIArgument arg = f.getAnnotation(CLIArgument.class);

                if (value == null) {
                    if (!arg.required()) {
                        parseValue(arg, f, arg.value(), command, messages);
                    } else {
                        throw new CLIArgumentException(
                                MessageFormat.format(messages.getString("arg.required"), arg.name())
                        );
                    }
                } else {
                    if (value instanceof String
                            && ((String) value).length() == 0
                            && arg.required()) {
                        throw new CLIArgumentException(
                                MessageFormat.format(messages.getString("arg.required"), arg.name())
                        );

                    }
                }
            }
        }
    }

    /**
     * Parse a command argument value.
     *
     * @param arg Command argument.
     * @param field {@link Field} for storing parsed value.
     * @param value Argument value as string.
     * @param command {@link Command} instance.
     * @param messages {@link ResourceBundle} messages.
     * @throws CLIArgumentException
     * @throws ReflectiveOperationException
     */
    private static <T extends Command<?>> void parseValue(CLIArgument arg, Field field, String value, T command, ResourceBundle messages)
            throws CLIArgumentException, ReflectiveOperationException {
        if (!field.isAccessible()) {
            field.setAccessible(true);
        }
        if (field.isAnnotationPresent(CLIDatatypeConverter.class)) {
            CLIDatatypeConverter converter = field.getAnnotation(CLIDatatypeConverter.class);
            parseCustomValue(arg, converter, field, value, command, messages);
        } else {
            parsePrimitiveValue(arg, field, value, command, messages);
        }
    }

    /**
     * Parse a custom-type command argument value.
     *
     * @param arg Command argument.
     * @param converter {@link Field} for storing parsed value.
     * @param field {@link Field} for storing parsed value.
     * @param value Argument value as string.
     * @param command {@link Command} instance.
     * @param messages {@link ResourceBundle} messages.
     * @throws CLIArgumentException
     * @throws ReflectiveOperationException
     */
    private static <T extends Command<?>> void parseCustomValue(CLIArgument arg, CLIDatatypeConverter converter, Field field, String value, T command, ResourceBundle messages)
            throws CLIArgumentException, ReflectiveOperationException {

        try {
            Class<? extends DatatypeConverter<?>> converterClass = converter.value();
            Object converterImpl = converterClass.getDeclaredConstructor().newInstance();
            field.set(command, converterClass.cast(converterImpl).parse(value));
        } catch (DatatypeConverterException e) {
            throw new CLIArgumentException(
                    MessageFormat.format(messages.getString("arg.conversion.error"), arg.name(), value),
                    e
            );
        }

    }

    /**
     * Parse a primitive command argument value.
     *
     * @param arg Command argument.
     * @param field {@link Field} for storing parsed value.
     * @param value Argument value as string.
     * @param command {@link Command} instance.
     * @param messages {@link ResourceBundle} messages.
     * @throws CLIArgumentException
     * @throws ReflectiveOperationException
     */
    private static <T extends Command<?>> void parsePrimitiveValue(CLIArgument arg, Field field, String value, T command, ResourceBundle messages)
            throws CLIArgumentException, ReflectiveOperationException {

        if (String.class.equals(field.getType())) {
            field.set(command, value);
        }

        if (boolean.class.equals(field.getType()) || Boolean.class.equals(field.getType())) {
            field.set(command, Boolean.valueOf(value));
        }

        try {

            if (byte.class.equals(field.getType()) || Byte.class.equals(field.getType())) {
                field.set(command, Byte.valueOf(value));
            }

            if (short.class.equals(field.getType()) || Short.class.equals(field.getType())) {
                field.set(command, Short.valueOf(value));
            }

            if (int.class.equals(field.getType()) || Integer.class.equals(field.getType())) {
                field.set(command, Integer.valueOf(value));
            }

            if (long.class.equals(field.getType()) || Long.class.equals(field.getType())) {
                field.set(command, Long.valueOf(value));
            }

            if (float.class.equals(field.getType()) || Float.class.equals(field.getType())) {
                field.set(command, Float.valueOf(value));
            }

            if (double.class.equals(field.getType()) || Double.class.equals(field.getType())) {
                field.set(command, Double.valueOf(value));
            }

            if (BigInteger.class.equals(field.getType())) {
                field.set(command, new BigInteger(value));
            }

            if (BigDecimal.class.equals(field.getType())) {
                field.set(command, new BigDecimal(value));
            }
        } catch (NumberFormatException e) {
            throw new CLIArgumentException(
                    MessageFormat.format(messages.getString("arg.number.invalid"), value, arg.name(), field.getType().getName()),
                    e
            );
        }

    }

    /**
     * Read and set command's properties.
     *
     * @param command {@link Command} Instance.
     * @throws NoSuchFieldException
     * @throws ReflectiveOperationException
     */
    private static <T extends Command<?>> void readCommandProps(T command)
            throws NoSuchFieldException, ReflectiveOperationException {

        CLICommand props = command.getClass().getAnnotation(CLICommand.class);
        if (command instanceof Command) {
            Field sfield = Command.class.getDeclaredField("name");
            sfield.setAccessible(true);
            sfield.set(command, props.value());

            sfield = Command.class.getDeclaredField("description");
            sfield.setAccessible(true);
            sfield.set(command, props.description());
        }

    }

    /**
     * Retrieve Command's declared {@link CLIArgument} map.
     *
     * @param command {@link Command} instance.
     * @return Command's declared {@link CLIArgument} map.
     */
    private static <T extends Command<?>> Map<String, CLIArgument> getDeclaredArgs(T command) {
        Map<String, CLIArgument> argMap = new HashMap<String, CLIArgument>();
        for (Field f : command.getClass().getDeclaredFields()) {
            if (f.isAnnotationPresent(CLIArgument.class)) {
                CLIArgument arg = f.getAnnotation(CLIArgument.class);
                argMap.put(arg.name(), arg);
                for (String alias : arg.aliases()) {
                    argMap.put(alias, arg);
                }
            }
        }
        return Collections.unmodifiableMap(argMap);
    }

    /**
     * Determine if given alias is a valid {@link CLIArgument}
     *
     * @param alias Test alias.
     * @param args {@link Command} arguments
     * @return <code>true</code> if given alias is a valid {@link CLIArgument}.
     * <code>false</code> other ways.
     */
    private static boolean isArgument(String alias, Map<String, CLIArgument> args) {
        return args.containsKey(alias);
    }

    /**
     * Return the field that has been annotated with {@link CLIArgument}.
     *
     * @param argument {@link CLIArgument} annotation.
     * @param command {@link Command} instance.
     * @return {@link} Field that has been annotated with {@link CLIArgument}.
     */
    private static <T extends Command<?>> Field getAnnotatedField(CLIArgument argument, T command) {
        Field field = null;
        for (Field f : command.getClass().getDeclaredFields()) {
            if (f.isAnnotationPresent(CLIArgument.class)) {
                CLIArgument arg = f.getAnnotation(CLIArgument.class);
                if (arg.equals(argument)) {
                    field = f;
                    break;
                }
            }
        }
        return field;
    }

}
