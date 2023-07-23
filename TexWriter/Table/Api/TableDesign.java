package TexWriter.Table.Api;

import java.util.ArrayList;
import java.util.Stack;

import TexWriter.General.Coordinates;
import TexWriter.General.Collections.*;

class RGBColor {
    public int R, G, B;

    RGBColor(int r, int g, int b) {
        R = r;
        G = g;
        B = b;
    }
}

// Design the table's borders, format, color, etc.
// TODO: Implement this
class TableDesign {
    // Only Borders' dimension is (row + 1, col + 1), others' dimension is (row,
    // col)
    private ArrayList<ArrayList<Boolean>> Borders;
    private ArrayList<ArrayList<RGBColor>> Colors;
    private ArrayList<ArrayList<String>> Formats;
    // Groups[i][j] = j + i * column;
    private ArrayList<ArrayList<Integer>> Groups;
    int rowCount;
    int columnCount;

    TableDesign(int row, int column) {
        Borders = new ArrayList<ArrayList<Boolean>>((row + 1) * (column + 1));
        Colors = new ArrayList<ArrayList<RGBColor>>(row * column);
        Formats = new ArrayList<ArrayList<String>>(row * column);
        Groups = new ArrayList<ArrayList<Integer>>(row * column);
        for (int i = 0; i < row + 1; i++) {
            Borders.add(new ArrayList<Boolean>(column + 1));
            for (int j = 0; j < column + 1; j++) {
                Borders.get(i).add(false);
            }
        }
        for (int i = 0; i < row + 1; i++) {
            Colors.add(new ArrayList<RGBColor>(column));
            Formats.add(new ArrayList<String>(column));
            Groups.add(new ArrayList<Integer>(column));
            for (int j = 0; j < column; j++) {
                Colors.get(i).add(new RGBColor(0, 0, 0));
                Formats.get(i).add("");
                Groups.get(i).add(j + i * column);
            }
        }
    }

    void setBorder(int row, int column) throws IndexOutOfBoundsException {
        Borders.get(row).set(column, true);
    }

    void unsetBorder(int row, int column) throws IndexOutOfBoundsException {
        Borders.get(row).set(column, false);
    }

    Boolean getBorder(int row, int column) throws IndexOutOfBoundsException {
        return Borders.get(row).get(column);
    }

    void setColor(int row, int column, RGBColor color) throws IndexOutOfBoundsException {
        Colors.get(row).set(column, color);
    }

    Boolean getColor(int row, int column) throws IndexOutOfBoundsException {
        return Borders.get(row).get(column);
    }

    void setFormat(int row, int column, String format) throws IndexOutOfBoundsException {
        Formats.get(row).set(column, format);
    }

    String getFormat(int row, int column) throws IndexOutOfBoundsException {
        return Formats.get(row).get(column);
    }

    void Merge(int row, int column, int rowSpan, int colSpan) throws IndexOutOfBoundsException {
        for (int i = row; i < row + rowSpan; i++) {
            for (int j = column; j < column + colSpan; j++) {
                Groups.get(i).set(j, Groups.get(row).get(column));
            }
        }
    }

    private boolean isInRange(int row, int column) {
        return row >= 0 && row < this.rowCount && column >= 0 && column < this.columnCount;
    }

    private boolean isInRange(Coordinates c) {
        return isInRange(c.y, c.x);
    }

    // expect dRow > 0
    private void addNewRow(int dRow) {
        if (dRow < 0) {
            throw new IllegalArgumentException("dRow must be positive");
        }
        this.rowCount += dRow;
        for (int i = rowCount - dRow; i < rowCount; i++) {
            Borders.add(new ArrayList<Boolean>(columnCount + 1));
            Colors.add(new ArrayList<RGBColor>(columnCount));
            Formats.add(new ArrayList<String>(columnCount));
            Groups.add(new ArrayList<Integer>(columnCount));
            for (int j = 0; j < columnCount + 1; j++) {
                Borders.get(i).add(false);
            }
            for (int j = 0; j < columnCount; j++) {
                Colors.get(i).add(new RGBColor(0, 0, 0));
                Formats.get(i).add("");
                Groups.get(i).add(j + i * columnCount);
            }
        }
    }

    private void addNewColumn(int dColumn) {
        if (dColumn < 0) {
            throw new IllegalArgumentException("dColumn must be positive");
        }
        this.columnCount += dColumn;
        for (int j = columnCount - dColumn; j < columnCount; ++j) {
            for (int i = 0; i < rowCount + 1; ++i) {
                Borders.get(i).add(false);
            }
            for (int i = 0; i < rowCount; ++i) {
                Colors.get(i).add(new RGBColor(0, 0, 0));
                Formats.get(i).add("");
                Groups.get(i).add(j + i * columnCount);
            }
        }
    }

    // expect dRow < 0
    private void reduceRow(int dRow) {
        if (dRow > 0) {
            throw new IllegalArgumentException("dRow must be negative");
        }
        this.rowCount += dRow;
        for (int i = rowCount - dRow - 1; i >= rowCount; --i) {
            Borders.remove(i + 1);
            Colors.remove(i);
            Formats.remove(i);
            Groups.remove(i);
        }
    }

    private void reduceColumn(int dColumn) {
        if (dColumn > 0) {
            throw new IllegalArgumentException("dColumn must be negative");
        }
        this.columnCount += dColumn;
        for (int i = 0; i < rowCount + 1; ++i) {
            for (int j = columnCount - dColumn - 1; j >= columnCount; --j) {
                Borders.get(i).remove(j + 1);
                Colors.get(i).remove(j);
                Formats.get(i).remove(j);
                Groups.get(i).remove(j);
            }
        }
    }

    void expand(int dRow, int dColumn) {
        if (dRow > 0) {
            addNewRow(dRow);
        }
        if (dColumn > 0) {
            addNewColumn(dColumn);
        }
        if (dRow < 0) {
            reduceRow(dRow);
        }
        if (dColumn < 0) {
            reduceColumn(dColumn);
        }
    }

    void changeSize(int row, int column) {
        expand(row - this.rowCount, column - this.columnCount);
    }

    ArrayList<Coordinates> getSameGroupCell(int row, int column) {
        Stack<Coordinates> stack = new Stack<Coordinates>();
        ArrayList<Coordinates> result = new ArrayList<Coordinates>();
        Coordinates current = new Coordinates(row, column);
        stack.push(current);
        while (!stack.isEmpty()) {
            current = stack.pop();
            result.add(current);
            if (isInRange(row, column)) {
                stack.push(new Coordinates(current.x - 1, current.y));
            }
            if (current.x < this.rowCount - 1
                    && Groups.get(current.x + 1).get(current.y) == Groups.get(current.x).get(current.y)) {
                stack.push(new Coordinates(current.x + 1, current.y));
            }
            if (current.y > 0 && Groups.get(current.x).get(current.y - 1) == Groups.get(current.x).get(current.y)) {
                stack.push(new Coordinates(current.x, current.y - 1));
            }
            if (current.y < this.columnCount - 1
                    && Groups.get(current.x).get(current.y + 1) == Groups.get(current.x).get(current.y)) {
                stack.push(new Coordinates(current.x, current.y + 1));
            }
        }
        return result;
    }

    private Pair<Coordinates> getPartiallyRowBorder(Coordinates from) {
        if (!isInRange(from)) {
            throw new IndexOutOfBoundsException();
        }
        int row = from.y;
        int column = from.x;

        while (column < this.columnCount && Borders.get(row).get(column)) {
            column++;
        }
        return new Pair<Coordinates>(from, new Coordinates(row, column));
    }
    private Pair<Coordinates> getPartiallyColumnBorder(Coordinates from) {
        if (!isInRange(from)) {
            throw new IndexOutOfBoundsException();
        }
        int row = from.y;
        int column = from.x;

        while (row < this.rowCount && Borders.get(row).get(column)) {
            row++;
        }
        return new Pair<Coordinates>(from, new Coordinates(row, column));
    }

    ArrayList<Pair<Coordinates>> getRowBorders(int row)
    {
        ArrayList<Pair<Coordinates>> result = new ArrayList<Pair<Coordinates>>();
        int current = 0;
        while (current < this.columnCount)
        {
            if (Borders.get(row).get(current))
            {
                Pair<Coordinates> border = getPartiallyRowBorder(new Coordinates(row, current));
                result.add(border);
                current = border.second().x;
            }
            else
            {
                current++;
            }
        }
        return result;
    }

    ArrayList<Pair<Coordinates>> getColumnBorders(int column)
    {
        ArrayList<Pair<Coordinates>> result = new ArrayList<Pair<Coordinates>>();
        int current = 0;
        while (current < this.rowCount)
        {
            if (Borders.get(current).get(column))
            {
                Pair<Coordinates> border = getPartiallyColumnBorder(new Coordinates(current, column));
                result.add(border);
                current = border.second().y;
            }
            else
            {
                current++;
            }
        }
        return result;
    }
}
