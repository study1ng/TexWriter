package TexWriter.Table;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.TreeSet;
import java.util.regex.*;

import TexWriter.Exceptions.*;
import TexWriter.Table.CommandParser;

class TableReader {
    /*
     * ParseCommand
     * This method is used to parse a command.
     * 
     * @command: The command to be parsed. assert command is trimmed.
     * 
     * @return: An ArrayList<String> which array[0] is the command name, others are
     * arguments.
     */

    private ArrayList<Pattern> ignoredLinePattern;
    // TODO: use some structure which will be sorted will make read faster but IDK
    private TreeSet<Integer> ignoredLineNumber;
    private ArrayList<ArrayList<String>> Table;
    private Pattern cellPattern = null;

    TableReader(String cellPattern) {
        if (cellPattern == null) {
            throw new IllegalArgumentException("Cell pattern should not be null");
        }
        ignoredLinePattern = new ArrayList<Pattern>();
        ignoredLineNumber = new TreeSet<Integer>();
        Table = new ArrayList<ArrayList<String>>();
        this.cellPattern = Pattern.compile(cellPattern);
    }

    void addIgnoredLinePattern(String pattern) {
        if (pattern == null) {
            return;
        }

        ignoredLinePattern.add(Pattern.compile(pattern));
    }

    void setCellPattern(String pattern) {
        cellPattern = Pattern.compile(pattern);
    }

    private boolean isIgnored(String line) throws IOException {
        for (Pattern pattern : ignoredLinePattern) {
            if (pattern.matcher(line).matches()) {
                return true;
            }
        }
        return false;
    }

    private void readLine(String line) throws IOException {
        // FIXME: Auto-generated method, seems good but not sure
        ArrayList<String> row = new ArrayList<String>();
        // cellPattern is not null when this method is called
        Matcher matcher = cellPattern.matcher(line);
        while (matcher.find()) {
            row.add(matcher.group());
        }
        Table.add(row);
    }

    void readFile(String path) throws IOException {
        if (path == null) {
            throw new IllegalArgumentException("Path should not be null");
        }
        File file = new File(path);
        BufferedReader fileReader = new BufferedReader(new FileReader(file));
        Table = new ArrayList<ArrayList<String>>();
        int lineCount = 0;
        while (true) {
            String line = fileReader.readLine();
            if (line == null) {
                break;
            }
            if (ignoredLineNumber.contains(lineCount)) {
                ++lineCount;
                continue;
            }
            if (isIgnored(line)) {
                ++lineCount;
                continue;
            }
            readLine(line);
            ++lineCount;
        }
        fileReader.close();
    }

    ArrayList<ArrayList<String>> getTable() {
        return Table;
    }

    // row and column will be in range
    void changeCell(int row, int column, String data) {
        Table.get(row).set(column, data);
    }

    int getColumnCount() {
        return Table.size();
    }

    int getMaxRowCount() {
        // row count will be 0 if table is empty
        if (Table.size() == 0) {
            return 0;
        }
        // FIXME: Auto-generated method, seems good but not sure
        return Table.stream().max((a, b) -> a.size() - b.size()).get().size();
    }

    int getRowSizeAt(int column) {
        return Table.get(column).size();
    }

    private void resize(int row, int column) {
        // TODO: Implement this
    }

    private ArrayList<String> doCommand(String commandName, ArrayList<Integer> args) {
        // COMMANDS: COMMAND <args...> -> data add to responses;
        // IGNORE-LINE <line-number: int> -> null;
        // DELETE-CELL <row: int> <column: int> -> null;
        // DELETE-ROW <row: int> -> null;
        // DELETE-COLUMN <column: int> -> null;
        // RESIZE <row: int> <column: int> -> null;
        // COPY <row: int> <column: int> -> copied-data;
        ArrayList<String> responses = new ArrayList<String>();
        switch (commandName) {
            case "IGNORE-LINE":
                ignoredLineNumber.add(args.get(0));
                break;
            case "DELETE-CELL":
                Table.get(args.get(0).intValue()).set(args.get(1).intValue(), null);
                break;
            case "DELETE-ROW":
                Table.remove(args.get(0).intValue());
                break;
            case "DELETE-COLUMN":
                for (ArrayList<String> row : Table) {
                    row.remove(args.get(0).intValue());
                }
                break;
            case "RESIZE":
                resize(args.get(0).intValue(), args.get(1).intValue());
                break;
            case "COPY":
                responses.add(Table.get(args.get(0).intValue()).get(args.get(1).intValue()));
                break;
            default:
                break;
        }
        return responses;
    }

    ArrayList<String> doCommand(String command) throws CommandNotMatchException {
        ArrayList<String> parsed = CommandParser.parseCommand(command);
        String commandName = parsed.get(0);
        ArrayList<Integer> args = new ArrayList<Integer>();
        for (int i = 1; i < parsed.size(); i++) {
            args.add(Integer.parseInt(parsed.get(i)));
        }
        return doCommand(commandName, args);
    }

}