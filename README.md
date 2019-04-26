## QuickCLI
  
[![Build Status](https://travis-ci.org/apercova/QuickCLI.svg?branch=master)](https://travis-ci.org/apercova/QuickCLI)
  
### Eficient POJO-based parsing for java CLI arguments

> Check out some examples [here](https://github.com/apercova/QuickCLI-examples)!  
> Also try [jchecksum](https://github.com/apercova/jchecksum). A QuickCLI command implementation for hashing calculation using JVM MessageDigest.

### Quick example of task command that retrieves a formated Date.
> Standalone command has to be invoked as follows:  
>`$GetDate [-f format]` where `-f` must be an optional `java.util.DateFormat` valid format pattern.
```bash
package com.github.apercova.quickcli.examples.command;

import java.util.Locale;
import com.github.apercova.quickcli.Command;
import com.github.apercova.quickcli.CommandFactory;
import com.github.apercova.quickcli.annotation.CLIArgument;
import com.github.apercova.quickcli.annotation.CLICommand;
import com.github.apercova.quickcli.exception.CLIArgumentException;
import com.github.apercova.quickcli.exception.ExecutionException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Example of task command that retrieves a formated Date. Format and Locale are
 * parameterized as args. Local getters are important to retrieve instance
 * values, not parent ones.
 *
 * @author
 * <a href="https://twitter.com/apercova" target="_blank">{@literal @}apercova</a>
 * <a href="https://github.com/apercova" target="_blank">https://github.com/apercova</a>
 */
@CLICommand(value = "currdate", description = "Retrieves current date.")
public class GetDate extends Command<String> {

    @CLIArgument(name = "-f", value = "yyyy-MM-dd'T'hh:mm:ss.SSS zZ")
    private String format;

    @Override
    public String execute() throws ExecutionException {
        SimpleDateFormat sdf = new SimpleDateFormat(format, locale);
        return sdf.format(Calendar.getInstance().getTime());
    }

    public static void main(String[] args) throws CLIArgumentException, ExecutionException {
        /*Emulating CLI user arguments*/
        args = new String[]{"-f", "EEEE, dd MMM yyyy HH:mm:ss z"};
        
        /*Getting a GetDate command instance*/
        GetDate command = CommandFactory.create(args, GetDate.class, Locale.FRENCH);
        
        /*Prints out French localized date*/
        System.out.println(command.execute());
    }
}

```
