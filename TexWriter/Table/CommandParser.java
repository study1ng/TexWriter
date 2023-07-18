package TexWriter.Table;

import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import TexWriter.Exceptions.CommandNotMatchException;

class CommandParser {
    final static String COMMANDS = "IGNORE-LINE|DELETE-CELL|DELETE-ROW|DELETE-COLUMN|RESIZE|COPY";
    // command is start with a command name, and end with a semicolon.
    final static String PARSER = String.format("^\\b(?<COMMAND>%s)\\b((?<args>\\w+)\\b)*;$", COMMANDS);
    final static String INT_ARG = "\\d+";
    final static Map<String, String> MESSAGE = Map.ofEntries(
            Map.entry("IGNORE-LINE", "IGNORE-LINE <line-number: int>"),
            Map.entry("DELETE-CELL", "DELETE-CELL <row: int> <column: int>"),
            Map.entry("DELETE-ROW", "DELETE-ROW <row: int>"),
            Map.entry("DELETE-COLUMN", "DELETE-COLUMN <column: int>"),
            Map.entry("RESIZE", "RESIZE <row: int> <column: int>"),
            Map.entry("COPY", "COPY <row: int> <column: int>"));
    final static Map<String, ArgType[]> ARG_TYPES = Map.ofEntries(
            Map.entry("IGNORE-LINE", new ArgType[] { ArgType.INT }),
            Map.entry("DELETE-CELL", new ArgType[] { ArgType.INT, ArgType.INT }),
            Map.entry("DELETE-ROW", new ArgType[] { ArgType.INT }),
            Map.entry("DELETE-COLUMN", new ArgType[] { ArgType.INT }),
            Map.entry("RESIZE", new ArgType[] { ArgType.INT, ArgType.INT }),
            Map.entry("COPY", new ArgType[] { ArgType.INT, ArgType.INT }));

    enum ArgType {
        INT
    }

    // If args don't match the format, throw CommandNotMatchException
    private static boolean isType(String arg, CommandParser.ArgType type) {
        switch (type) {
            case INT:
                return (Objects.isNull(arg) || !Pattern.compile(INT_ARG).matcher(arg).matches());
        }
        return false;
    }

    private static ArrayList<String> parseArgs(Matcher matched, String commandName)
            throws CommandNotMatchException {
        ArrayList<String> args = new ArrayList<String>();
        for (CommandParser.ArgType type : ARG_TYPES.get(commandName)) {
            String arg = matched.group("args");
            if (!isType(commandName, type)) {
                throw new CommandNotMatchException(MESSAGE.get(commandName));
            }
            args.add(arg);
        }
        return args;
    }

    static ArrayList<String> parseCommand(String command) throws CommandNotMatchException {
        ArrayList<String> parsed = new ArrayList<String>();
        Matcher matcher = Pattern.compile(PARSER).matcher(command);
        if (Objects.isNull(matcher)) {
            throw new CommandNotMatchException();
        }
        final String commandName = matcher.group("COMMAND");
        // if no match
        if (Objects.isNull(commandName)) {
            throw new CommandNotMatchException();
        }
        parsed.add(commandName);
        parsed.addAll(parseArgs(matcher, commandName));
        return parsed;
    }

}