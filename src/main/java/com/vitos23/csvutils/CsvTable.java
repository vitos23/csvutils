package com.vitos23.csvutils;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import java.util.Arrays;

public class CsvTable {
    private String[] header;
    private final String[][] table;

    /**
     * Constructs a CsvTable instance with given header and content
     * @param header of table
     * @param table is the content of the csv table
     */
    public CsvTable(String[] header, String[][] table) {
        this.header = header;
        this.table = table == null ? new String[][]{} : table;
    }

    /**
     * Constructs a CsvTable instance without header with given content
     * @param table is the content of the csv table
     */
    public CsvTable(String[][] table) {
        this(null, table);
    }

    /**
     * Returns a number of columns in table
     * @return number of columns
     */
    public int getWidth() {
        if (table.length == 0) {
            return 0;
        }
        return table[0].length;
    }

    /**
     * Returns number of rows in table excluding header
     * @return number of rows
     */
    public int getHeight() {
        return table.length;
    }

    /**
     * Returns true if table header was specified, and it isn't equal to null
     * @return true if header != null
     */
    public boolean hasHeader() {
        return header != null;
    }

    /**
     * Set a new header with valid size to table. New header can be null.
     * @param header is new header
     * @throws IllegalArgumentException if new header is not null
     * and its width (number of columns) isn't equal to table width
     */
    public void setHeader(String[] header) {
        if (header != null && header.length != getWidth()) {
            throw new IllegalArgumentException(String.format(
                    "Specified header has %d columns while table width is %d", header.length, getWidth()
            ));
        }
        this.header = header;
    }

    private boolean checkHeaderCell(int c) {
        return header != null && 0 <= c && c < header.length;
    }

    /**
     * Returns value of header of column with given index. Numbering starts from 0.
     * @param col is index of column
     * @return value of column header
     * @throws IndexOutOfBoundsException if specified column doesn't exist or table doesn't have header
     */
    public String getColumnHeader(int col) {
        if (!checkHeaderCell(col)) {
            throw new IndexOutOfBoundsException("Column with index " +
                    col + " doesn't exist or table doesn't have header");
        }
        return header[col];
    }

    /**
     * Set a new value to header of column with given index. Numbering starts from 0.
     * @param col is index of column
     * @param val is new value of column header
     * @throws IndexOutOfBoundsException if specified column doesn't exist or table doesn't have header
     */
    public void setColumnHeader(int col, String val) {
        if (!checkHeaderCell(col)) {
            throw new IndexOutOfBoundsException("Column with index " +
                    col + " doesn't exist or table doesn't have header");
        }
        header[col] = val;
    }

    private void checkCell(int r, int c) {
        if (!(0 <= r && r < getHeight() && 0 <= c && c < getWidth())) {
            throw new IndexOutOfBoundsException("Invalid index: (" + r + "," + c + ")");
        }
    }

    /**
     * Returns value of cell with given row and column indices. Numbering starts from 0.
     * @param row is index of row of the cell
     * @param col is index of column of the cell
     * @return value of the cell
     * @throws IndexOutOfBoundsException if specified cell doesn't exist
     */
    public String getCell(int row, int col) {
        checkCell(row, col);
        return table[row][col];
    }


    /**
     * Sets a new value to cell with given row and column indices. Numbering starts from 0.
     * @param row is index of row of the cell
     * @param col is index of column of the cell
     * @param val is new value of the cell
     * @throws IndexOutOfBoundsException if specified cell doesn't exist
     */
    public void setCell(int row, int col, String val) {
        checkCell(row, col);
        table[row][col] = val;
    }

    private String escapeHtml(String s) {
        s = s.replace("&", "&amp;");
        s = s.replace("<", "&lt;");
        s = s.replace(">", "&gt;");
        return s;
    }

    /**
     * Returns Html representation of the table.
     * @return html representation of the table
     */
    public String toHtml() {
        StringBuilder res = new StringBuilder();
        res.append("<table>");
        if (header != null) {
            res.append("<thead><tr>");
            for (String val : header) {
                res.append("<th>").append(escapeHtml(val)).append("</th>");
            }
            res.append("</tr></thead>");
        }
        res.append("<tbody>");
        for (String[] row : table) {
            res.append("<tr>");
            for (String val : row) {
                res.append("<td>").append(escapeHtml(val)).append("</td>");
            }
            res.append("</tr>");
        }
        res.append("</tbody></table>");
        return res.toString();
    }

    private String[] getHeader() {
        String[] headers = new String[getWidth()];
        for (int i = 0; i < headers.length; i++) {
            if (!checkHeaderCell(i)) {
                headers[i] = "COL_" + (i + 1);
            } else {
                headers[i] = getColumnHeader(i);
            }
        }
        return headers;
    }

    /**
     * Converts the table into JsonArray of json objects (for each row) containing pairs "column header" - "value".
     * If column header doesn't exist it is replaced with "COL_%d" where %d is the index of column (starts from 1).
     * @return JsonArray of json objects for each row
     */
    public JsonArray toJson() {
        JsonArray res = new JsonArray();

        String[] headers = getHeader();

        for (String[] row : table) {
            JsonObject rowJson = new JsonObject();
            for (int i = 0; i < row.length; i++) {
                rowJson.add(headers[i], new JsonPrimitive(row[i]));
            }
            res.add(rowJson);
        }

        return res;
    }


    /**
     * Converts the table into JsonArray of arrays corresponding rows. The header won't be included.
     * @return JsonArray of arrays.
     */
    public JsonArray toJsonArrays() {
        JsonArray res = new JsonArray();

        for (String[] row : table) {
            JsonArray rowJson = new JsonArray();
            for (String val : row) {
                rowJson.add(val);
            }
            res.add(rowJson);
        }

        return res;
    }

    /**
     * Converts the table into JsonObjects containing pairs "column header" - "array of column values".
     * If column header doesn't exist it is replaced with "COL_%d" where %d is the index of column (starts from 1).
     * @return JsonObject of pairs "column header" - "array of column values"
     */
    public JsonObject toJsonColumnArray() {
        JsonObject res = new JsonObject();

        String[] headers = getHeader();
        for (int col = 0; col < getWidth(); col++) {
            JsonArray columnValues = new JsonArray();
            for (int row = 0; row < getHeight(); row++) {
                columnValues.add(table[row][col]);
            }
            res.add(headers[col], columnValues);
        }

        return res;
    }

    /**
     * Returns true if this table is equal to table given as a parameter.
     * Csv tables are equal if their headers and content are equal
     * @param obj - other object
     * @return true if arrays are equal
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        CsvTable table1 = (CsvTable) obj;
        return Arrays.equals(header, table1.header) && Arrays.deepEquals(table, table1.table);
    }
}
