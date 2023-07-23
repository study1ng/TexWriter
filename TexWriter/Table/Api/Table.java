package TexWriter.Table.Api;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;

import TexWriter.General.*;
import TexWriter.General.Collections.*;

public class Table {
    private TableReader reader;
    private TableDesign design;

    Table(String cellPattern, String filePath) throws NullPointerException, IOException {
        reader = new TableReader(cellPattern);
        reader.setCellPattern(cellPattern);
        reader.readFile(filePath);
        design = new TableDesign(reader.getColumnCount(), reader.getMaxRowCount());
    }

    Table(String cellPattern, String filePath, ArrayList<String> ignoredLinePatterns) throws NullPointerException, IOException {
        reader = new TableReader(cellPattern);
        reader.setCellPattern(cellPattern);
        reader.readFile(filePath);
        for (String pattern : ignoredLinePatterns) {
            reader.addIgnoredLinePattern(pattern);
        }
        design = new TableDesign(reader.getColumnCount(), reader.getMaxRowCount());
    }

    public void eraseIgnoredLinePattern(int index) {
        // TODO: Implement this
    }

    public void showIgnoredLinePattern() {
        // TODO: Implement this
    }

    public void changeCellPattern(String newCellPattern) {
        reader.setCellPattern(newCellPattern);
    }

    private boolean isInTable(int row, int column) {
        return row >= 0 && row < reader.getMaxRowCount() && column >= 0 && column < reader.getRowSizeAt(column);
    }

    public void changeCell(int row, int column, String data) throws IndexOutOfBoundsException {
        if (!isInTable(row, column)) {
            throw new IndexOutOfBoundsException();
        }
        reader.changeCell(row, column, data);
    }

    public String transpileTex() throws IOException {
        // TODO: use reader to get table data
        // TODO: use design to get table format
        ArrayList<ArrayList<String>> table = reader.getTable();
        String columnBorder = "";

        for (int i = 0; i < reader.getColumnCount(); i++) {
            // TODO: add column border use design
            var borderSegments = design.getColumnBorders(i);
            if (borderSegments.size() == 0) {
                continue;
            } else if (borderSegments.size() == 1) {
                columnBorder += "|c";
            } else {
                for (Pair<Coordinates> segment : borderSegments) {
                    // TODO: Not implemented, should fix;
                    columnBorder += "|" + segment;
                }
            }
        }
        return null;
    }

    // \begin{tag}
    //    content
    // \end{tag}
    // will tab automatically
    private String begin(String tag, String content) {
        content.replace("\n", "\n\t");
        return "\\begin{" + tag + "}\n\t" + content + "\n" + "\\end{" + tag + "}";
    }
}
