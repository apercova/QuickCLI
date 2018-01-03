package net.apercova.quickcli.api;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Factory for {@link Command } creation
 * 
 * @author <a href="https://twitter.com/apercova" target="_blank">{@literal @}apercova</a> <a href="https://github.com/apercova" target="_blank">https://github.com/apercova</a>
 * @since 1.0
 *
 */
public class CommandFactory {
	
	protected static final String ARG_REQUIRED = "Argument %s is required.";
	protected static final String ARG_NOT_VALID = "%s is not a valid argument.";
	protected static final String NUM_ARG_NOT_VALID = "Argument %s is not valid. %s is not a valid number for type: %s %n";
	protected static final String TYPE_NOT_VALID = "Type %s is not a valid command type.";
	protected static final String VALUE_CONV_ERROR = "Value conversion error for argument %s. value: %s %n";
	protected static final String PROP_NAME ="name";
	protected static final String PROP_DESCRIPTION ="description";
	
	private CommandFactory() {
		super();
	}
	
	/**
	 * Creates a command of the provided {@link Class} type
	 * @param <T> Generic command type
	 * @param args CLI Arguments
	 * @param clazz Command type class
	 * @return Command Command instance
	 * @throws CLIArgumentException If an error occurs at command creation
	 */
	public static <T extends Command> T createCommand(String[] args, Class<T> clazz) 
			throws CLIArgumentException{
		T command = null;
		
		try {
			if(clazz.isAnnotationPresent(CLICommand.class)) {
				command = clazz.newInstance();
				synchronized (command) {
					read(command, args);
					verify(command);
				}
			}else {
				throw new CLIArgumentException(String.format(TYPE_NOT_VALID, clazz));
			}
		} catch (IllegalAccessException e) {
			throw new CLIArgumentException(String.format(TYPE_NOT_VALID, clazz), e);
		} catch (NoSuchFieldException e) {
			throw new CLIArgumentException(String.format(TYPE_NOT_VALID, clazz), e);
		} catch (InstantiationException e) {
			throw new CLIArgumentException(String.format(TYPE_NOT_VALID, clazz), e);
		}

		return command;
	}
		
	private static <T extends Command> void read(T command, String[] args) 
			throws CLIArgumentException, NoSuchFieldException, IllegalAccessException, InstantiationException  {
	
		//Command props
		readCommandProps(command);		
		
		//CLI Arguments
		List<String> argSet = new ArrayList<String>(Arrays.asList(args));
		
		//Declared argument map
		Map<String, CLIArgument> argMap = getDeclaredArgs(command) ;
		
		String alias = null;
		boolean lookArg = true;
		Field field = null;
		for(int i = 0; i< argSet.size(); i++) {
			
			String arg = argSet.get(i);
			if(lookArg) {
				if(isArgument(arg, argMap)) {
					lookArg = false;
					alias = arg;
					field = getAnnotatedField(argMap.get(arg), command);
					if(field != null) {
						field.setAccessible(true);
						if(boolean.class.equals(field.getType()) || Boolean.class.equals(field.getType())) {
							parseValue(argMap.get(arg), field, String.valueOf(Boolean.TRUE), command);							
							lookArg = true;
							alias = null;
							
						} else if(!argMap.get(arg).required()) {
							if((i+1)< argSet.size() && !isArgument( argSet.get((i+1)) , argMap)) {
								parseValue(argMap.get(arg), field, argSet.get((i+1)), command);
								i++;
								
							}else {		
								parseValue(argMap.get(arg), field, argMap.get(arg).value(), command);

							}
							lookArg = true;
							alias = null;
						}else {
							if((i+1)>= argSet.size()) {
								throw new IllegalArgumentException(
										String.format(ARG_REQUIRED, alias));
							}						
						}
					}
				}else {
					throw new IllegalArgumentException(
							String.format(ARG_NOT_VALID, arg));
				}
			}else {
				if(!isArgument(arg, argMap)) {
					field = getAnnotatedField(argMap.get(alias), command);
					if(field != null) {
						field.setAccessible(true);
						parseValue(argMap.get(alias), field, arg, command);
						lookArg = true;
						alias = null;
					}
				} else {
					throw new IllegalArgumentException(
							String.format(ARG_REQUIRED, alias));
				}
			}	
		}

	}
	
	private static <T extends Command> void parseValue(CLIArgument arg, Field field, String value, T command) 
			throws CLIArgumentException, IllegalAccessException, InstantiationException {
		if(!field.isAccessible()) {
			field.setAccessible(true);
		}		
		if(field.isAnnotationPresent(CLIDatatypeConverter.class)){
			CLIDatatypeConverter converter = field.getAnnotation(CLIDatatypeConverter.class);
			parseCustomValue(arg, converter, field, value, command);
        }else {
        	parsePrimitiveValue(arg, field, value, command);
        }
	}
	
	private static <T extends Command> void parseCustomValue(CLIArgument arg, CLIDatatypeConverter converter, Field field, String value, T command) 
			throws CLIArgumentException, IllegalAccessException, InstantiationException{
		try {
			Class<? extends DatatypeConverter<?>> converterClass = converter.value();
			Object converterImpl = converterClass.newInstance();
			field.set(command, converterClass.cast(converterImpl).parse(value));
		} catch (DatatypeConverterException e) {
			throw new CLIArgumentException(
					String.format(VALUE_CONV_ERROR, arg.name(), value), e);
		}
	}
	
	private static <T extends Command> void parsePrimitiveValue(CLIArgument arg, Field field, String value, T command) 
			throws CLIArgumentException, IllegalAccessException {
		
		if(String.class.equals(field.getType())) {
			field.set(command, value);
		}
		
		if(boolean.class.equals(field.getType()) || Boolean.class.equals(field.getType())) {
			field.set(command, Boolean.valueOf(value));
		}
		
		try {
			
			if(byte.class.equals(field.getType()) || Byte.class.equals(field.getType())) {
				field.set(command, Byte.valueOf(value));
			}
			
			if(short.class.equals(field.getType()) || Short.class.equals(field.getType())) {
				field.set(command, Short.valueOf(value));
			}
			
			if(int.class.equals(field.getType()) || Integer.class.equals(field.getType())) {
				field.set(command, Integer.valueOf(value));
			}
			
			if(long.class.equals(field.getType()) || Long.class.equals(field.getType())) {
				field.set(command, Long.valueOf(value));
			}
			
			if(float.class.equals(field.getType()) || Float.class.equals(field.getType())) {
				field.set(command, Float.valueOf(value));
			}
			
			if(double.class.equals(field.getType()) || Double.class.equals(field.getType())) {
				field.set(command, Double.valueOf(value));
			}
			
			if(BigInteger.class.equals(field.getType())) {
				field.set(command, new BigInteger(value));
			}
			
			if(BigDecimal.class.equals(field.getType())) {
				field.set(command, new BigDecimal(value));
			}
		} catch (NumberFormatException e) {
			throw new CLIArgumentException(
					String.format(NUM_ARG_NOT_VALID, arg.name(), value, field.getType().getName()), e);
		}

		
	}
	
	private static <T extends Command> void verify(T command) 
			throws CLIArgumentException, IllegalAccessException, InstantiationException {
		
		//Validating fields
		for(Field f: command.getClass().getDeclaredFields()) {
			if(f.isAnnotationPresent(CLIArgument.class)) {
				f.setAccessible(true);
				Object value = f.get(command);
				CLIArgument arg = f.getAnnotation(CLIArgument.class);
				
				if(value == null) {
					if(!arg.required()) {
						parseValue(arg, f, arg.value() , command);
					}else {
						throw new CLIArgumentException(
								String.format(ARG_REQUIRED, arg.name()));
					}
				}else {
					if(value instanceof String && 
						((String) value).length() == 0 && 
						arg.required())
					{
						
							throw new CLIArgumentException(
									String.format(ARG_REQUIRED, arg.name()));
						
					}
				}
			}
		}
	}
		
	private static <T extends Command> void readCommandProps(T command) 
		throws NoSuchFieldException, IllegalAccessException{
		
		if(command.getClass().isAnnotationPresent(CLICommand.class)) {
			CLICommand props = command.getClass().getAnnotation(CLICommand.class);
			Field sfield = null;
			if(command instanceof BaseCommand) {
				
				sfield = BaseCommand.class.getDeclaredField(PROP_NAME);
				sfield.setAccessible(true);
				sfield.set(command, props.value());
				
				sfield = BaseCommand.class.getDeclaredField(PROP_DESCRIPTION);
				sfield.setAccessible(true);
				sfield.set(command, props.description());
			}
		}

	}
	
	private static <T extends Command> Map<String, CLIArgument> getDeclaredArgs(T command){
		Map<String, CLIArgument> argMap = new HashMap<String, CLIArgument>();
		for(Field f: command.getClass().getDeclaredFields()) {
			if(f.isAnnotationPresent(CLIArgument.class)) {
				CLIArgument arg = f.getAnnotation(CLIArgument.class);  
				argMap.put(arg.name(), arg);
				for(String alias: arg.aliases()) {
					argMap.put(alias, arg);
				}
			}
		}
		return Collections.unmodifiableMap(argMap);
	}
	
	private static boolean isArgument(String alias, Map<String, CLIArgument> args) {
		return args.containsKey(alias);
	}
	
	private static <T extends Command> Field getAnnotatedField(CLIArgument argument, T command) {
		Field field = null;
		for(Field f: command.getClass().getDeclaredFields()) {
			if(f.isAnnotationPresent(CLIArgument.class)) {
				CLIArgument arg = f.getAnnotation(CLIArgument.class);
				if(arg.equals(argument)) {
					field = f;
					break;
				}
			}
		}
		return field;
	}
	
}
