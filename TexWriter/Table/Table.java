package TexWriter.Table;

import java.io.File;
import java.io.IOException;

public class Table {
    private TableReader reader;
    private TableDesign design;

    Table(String ignoredLinePattern, String cellPattern, String filePath) throws NullPointerException, IOException {
        reader = new TableReader();
        reader.addIgnoredLinePattern(ignoredLinePattern);
        reader.setCellPattern(cellPattern);
        reader.readFile(filePath);
        design = new TableDesign(reader.getColumnCount(), reader.getMaxRowCount());
    }

    public void eraseIgnoredLinePattern(int index) {
        // TODO: Implement this
    }

    public void showIgnoredLinePattern() {
        // TODO: Implement this
    }

    public void changeCellPattern(String newCellPattern) {
        // TODO: Implement this
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
        return null;
    }
}
