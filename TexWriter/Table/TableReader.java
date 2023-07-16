package TexWriter.Table;

import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.regex.*;

import TexWriter.Exceptions.*;

class TableReader {
    private ArrayList<Pattern> ignoredLinePattern = null;
    private ArrayList<ArrayList<String>> Table;
    private Pattern cellPattern = null;

    TableReader() {
        this.ignoredLinePattern = new ArrayList<Pattern>();
    }

    void addIgnoredLinePattern(String pattern) {
        this.ignoredLinePattern.add(Pattern.compile(pattern));
    }

    void setCellPattern(String pattern) {
        this.cellPattern = Pattern.compile(pattern);
    }

    private boolean isValidLine(String line) throws IOException {
        if (line == null) {
            return false;
        }
        for (Pattern pattern : ignoredLinePattern) {
            if (pattern.matcher(line).matches()) {
                return true;
            }
        }
        return true;
    }

    private void readLine(String line) throws IOException {
        // FIXME: Auto-generated method, seems good but not sure
        ArrayList<String> row = new ArrayList<String>();
        Matcher matcher = cellPattern.matcher(line);
        while (matcher.find()) {
            row.add(matcher.group());
        }
        Table.add(row);
    }

    void ReadFile2(String path) throws IOException, NullPointerException {
        File file = new File(path);
        BufferedReader fileReader = new BufferedReader(new FileReader(file));
        Table = new ArrayList<ArrayList<String>>();
        while (true) {
            String line = fileReader.readLine();
            if (!isValidLine(line)) {
                break;
            }
            readLine(line);
        }
        fileReader.close();
    }

    ArrayList<ArrayList<String>> getTable() {
        return Table;
    }
}

class Commander {
    private ArrayList<String> response = null;
    private TableReader reader = null;

    /* ParseCommand */
    private ArrayList<String> ParseCommand(String command) throws ParserException {
        // TODO: Implement this
        return null;
    }

    /*
     * DoCommand
     * This method is used to do some commands to the table.
     */
    private ArrayList<String> DoCommand(String command, ArrayList<Integer> args) {
        // COMMANDS: COMMAND <args...> -> data add to responses;
        // IGNORE-LINE <line-number: int> -> null;
        // DELETE-CELL <row: int> <column: int> -> null;
        // DELETE-ROW <row: int> -> null;
        // DELETE-COLUMN <column: int> -> null;
        // RESIZE <row: int> <column: int> -> null;
        // COPY <row: int> <column: int> -> copied-count, copied-data;
        // TODO: Implement this
        ArrayList<String> responses = new ArrayList<String>();
        return responses;
    }

    ArrayList<String> DoCommand(String command) throws ParserException{
        ArrayList<String> parsed = ParseCommand(command);
        String commandName = parsed.get(0);
        ArrayList<Integer> args = new ArrayList<Integer>();
        for (int i = 1; i < parsed.size(); i++) {
            args.add(Integer.parseInt(parsed.get(i)));
        }
        return DoCommand(commandName, args);
}}