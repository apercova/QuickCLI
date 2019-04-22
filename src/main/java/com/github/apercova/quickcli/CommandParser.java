package com.github.apercova.quickcli;

import com.github.apercova.quickcli.annotation.CLIArgument;
import com.github.apercova.quickcli.annotation.CLICommand;
import com.github.apercova.quickcli.annotation.CLIDatatypeConverter;
import com.github.apercova.quickcli.exception.CLIArgumentException;
import com.github.apercova.quickcli.exception.DatatypeConverterException;
import com.github.apercova.quickcli.exception.ReflectiveOperationException;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Command parser.
 *
 * @author
 * <a href="https://twitter.com/apercova" target="_blank">{@literal @}apercova</a>
 * <a href="https://github.com/apercova" target="_blank">https://github.com/apercova</a>
 * @param <T> Command
 * @since 1.0
 *
 */
public abstract class CommandParser<T extends Command<?>> {

    /**
     * Parse command line arguments an initialize a command instance.
     *
     * @param <T> Command type.
     * @param command {@link Command} instance.
     * @param args Command's arguments.
     * @param messages {@link ResourceBundle} messages.
     * @return Parsed command instance.
     * @throws CLIArgumentException If an exception occurs when parsing a
     * command.
     * @throws ReflectiveOperationException If a java reflection-related
     * exception occurs.
     */
    public static <T extends Command<?>> T parse(T command, String[] args, ResourceBundle messages)
            throws CLIArgumentException, ReflectiveOperationException {

        //Command properties
        readProperties(command);

        //CLI Arguments
        List<String> argSet = new ArrayList<String>(Arrays.asList(args));

        //Declared argument map
        Map<String, CLIArgument> argMap = readArguments(command);

        String alias = null;
        boolean lookArg = true;
        for (int i = 0; i < argSet.size(); i++) {

            String arg = argSet.get(i);
            if (lookArg) {
                if (isArgument(arg, argMap)) {
                    lookArg = false;
                    alias = arg;
                    Field field = getArgumentField(argMap.get(arg), command);
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
                                throw new CLIArgumentException(MessageFormat.format(messages.getString("arg.required"), alias));
                            }
                        }
                    }
                } else {
                    throw new CLIArgumentException(MessageFormat.format(messages.getString("arg.invalid"), arg));
                }
            } else {
                if (!isArgument(arg, argMap)) {
                    Field field = getArgumentField(argMap.get(alias), command);
                    if (field != null) {
                        field.setAccessible(true);
                        parseValue(argMap.get(alias), field, arg, command, messages);
                        lookArg = true;
                        alias = null;
                    }
                } else {
                    throw new CLIArgumentException(MessageFormat.format(messages.getString("arg.required"), alias));
                }
            }
        }

        return command;

    }

    /**
     * validate command arguments.
     *
     * @param <T> Command type.
     * @param command {@link Command} instance.
     * @param messages {@link ResourceBundle} messages.
     * @throws CLIArgumentException If an exception occurs when parsing a
     * command.
     * @throws ReflectiveOperationException If a java reflection-related
     * exception occurs.
     */
    public static <T extends Command<?>> void validate(T command, ResourceBundle messages)
            throws CLIArgumentException, ReflectiveOperationException {

        //Validating fields
        for (Field f : command.getClass().getDeclaredFields()) {
            if (f.isAnnotationPresent(CLIArgument.class)) {
                try {
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
                } catch (IllegalArgumentException ex) {
                    Logger.getLogger(CommandFactory.class.getName()).log(Level.FINE, ex.getMessage(), ex);
                    throw new ReflectiveOperationException(ex);
                } catch (IllegalAccessException ex) {
                    Logger.getLogger(CommandFactory.class.getName()).log(Level.FINE, ex.getMessage(), ex);
                    throw new ReflectiveOperationException(ex);
                }
            }
        }
    }

    /**
     * Read and set command's properties.
     *
     * @param <T> Command type.
     * @param command {@link Command} Instance.
     * @throws ReflectiveOperationException If a java reflection-related
     * exception occurs.
     */
    protected static <T extends Command<?>> void readProperties(T command)
            throws ReflectiveOperationException {

        CLICommand props = command.getClass().getAnnotation(CLICommand.class);
        if (command instanceof Command) {
            try {
                Field sfield = Command.class.getDeclaredField("name");
                sfield.setAccessible(true);
                sfield.set(command, props.value());

                sfield = Command.class.getDeclaredField("description");
                sfield.setAccessible(true);
                sfield.set(command, props.description());
            } catch (NoSuchFieldException ex) {
                Logger.getLogger(CommandFactory.class.getName()).log(Level.FINE, ex.getMessage(), ex);
                throw new ReflectiveOperationException(ex);
            } catch (IllegalAccessException ex) {
                Logger.getLogger(CommandFactory.class.getName()).log(Level.FINE, ex.getMessage(), ex);
                throw new ReflectiveOperationException(ex);
            }
        }

    }

    /**
     * Retrieve Command's {@link CLIArgument} map.
     *
     * @param <T> Command type.
     * @param command {@link Command} instance.
     * @return Command's {@link CLIArgument} map.
     */
    protected static <T extends Command<?>> Map<String, CLIArgument> readArguments(T command) {
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
    protected static boolean isArgument(String alias, Map<String, CLIArgument> args) {
        return args.containsKey(alias);
    }

    /**
     * Return the field that has been annotated with {@link CLIArgument}.
     *
     * @param <T> Command type.
     * @param argument {@link CLIArgument} annotation.
     * @param command {@link Command} instance.
     * @return Field that has been annotated with {@link CLIArgument}.
     */
    protected static <T extends Command<?>> Field getArgumentField(CLIArgument argument, T command) {
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

    /**
     * Parse a command argument value.
     *
     * @param <T> Command type.
     * @param arg Command argument.
     * @param field {@link Field} for storing parsed value.
     * @param value Argument value as string.
     * @param command {@link Command} instance.
     * @param messages {@link ResourceBundle} messages.
     * @throws CLIArgumentException If an exception occurs when parsing a
     * command.
     * @throws ReflectiveOperationException If a java reflection-related
     * exception occurs.
     */
    protected static <T extends Command<?>> void parseValue(CLIArgument arg, Field field, String value, T command, ResourceBundle messages)
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
     * Parse a primitive command argument value.
     *
     * @param <T> Command type.
     * @param arg Command argument.
     * @param field {@link Field} for storing parsed value.
     * @param value Argument value as string.
     * @param command {@link Command} instance.
     * @param messages {@link ResourceBundle} messages.
     * @throws CLIArgumentException If an exception occurs when parsing a
     * command.
     * @throws ReflectiveOperationException If a java reflection-related
     * exception occurs.
     */
    protected static <T extends Command<?>> void parsePrimitiveValue(CLIArgument arg, Field field, String value, T command, ResourceBundle messages)
            throws CLIArgumentException, ReflectiveOperationException {

        try {
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

        } catch (IllegalArgumentException ex) {
            Logger.getLogger(CommandFactory.class.getName()).log(Level.FINE, ex.getMessage(), ex);
            throw new ReflectiveOperationException(ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(CommandFactory.class.getName()).log(Level.FINE, ex.getMessage(), ex);
            throw new ReflectiveOperationException(ex);
        }
    }

    /**
     * Parse a custom-type command argument value.
     *
     * @param <T> Command type.
     * @param arg Command argument.
     * @param converter Converter for parsed value.
     * @param field {@link Field} for storing parsed value.
     * @param value Argument value as string.
     * @param command {@link Command} instance.
     * @param messages {@link ResourceBundle} messages.
     * @throws CLIArgumentException If an exception occurs when parsing a
     * command.
     * @throws ReflectiveOperationException If a java reflection-related
     * exception occurs.
     */
    protected static <T extends Command<?>> void parseCustomValue(CLIArgument arg, CLIDatatypeConverter converter, Field field, String value, T command, ResourceBundle messages)
            throws CLIArgumentException, ReflectiveOperationException {

        try {
            Class<? extends DatatypeConverter<?>> converterClass = converter.value();
            Object converterImpl = converterClass.newInstance();
            field.set(command, converterClass.cast(converterImpl).parse(value));
        } catch (DatatypeConverterException e) {
            throw new CLIArgumentException(
                    MessageFormat.format(messages.getString("arg.conversion.error"), arg.name(), value),
                    e
            );
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(CommandFactory.class.getName()).log(Level.FINE, ex.getMessage(), ex);
            throw new ReflectiveOperationException(ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(CommandFactory.class.getName()).log(Level.FINE, ex.getMessage(), ex);
            throw new ReflectiveOperationException(ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(CommandFactory.class.getName()).log(Level.FINE, ex.getMessage(), ex);
            throw new ReflectiveOperationException(ex);
        }

    }
}
