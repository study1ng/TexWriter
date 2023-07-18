package TexWriter.Table;

import java.util.ArrayList;
import java.util.Stack;

import javax.swing.GroupLayout.Group;

import java.awt.Point;

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
    // Only Borders' dimension is (row + 1, col + 1), others' dimension is (row, col)
    private ArrayList<ArrayList<Boolean>> Borders;
    private ArrayList<ArrayList<RGBColor>> Colors;
    private ArrayList<ArrayList<String>> Formats;
    private ArrayList<ArrayList<Integer>> Groups;
    int row;
    int column;

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
        for (int i = 0; i < row + 1; i++)
        {
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
    ArrayList<Point> getSameGroupCell(int row, int column)
    {
        Stack<Point> stack = new Stack<Point>();
        // TODO: Will use DFS to find all the cells in the same group as (row, column)
        return null;
    }
}
