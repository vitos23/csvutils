package com.vitos23.csvutils;

import com.vitos23.csvutils.exceptions.IncorrectInputException;

import java.util.ArrayList;

public class CsvParser {
    /**
     * <p>Returns a CsvTable instance that is representation of parsed csv table.
     * Csv table should follow RFC 4180</p>
     * @return CsvTable instance
     * @param source is a string representation of csv table to parse
     * @param hasHeader if the first row of csb table is its header
     * @param delimiter is a string that separates cells in row
     * @throws IncorrectInputException if source table is incorrect meaning it doesn't follow RFC 4180
     */
    public static CsvTable parse(String source, boolean hasHeader, String delimiter) {
        return new CsvParserHelper(source, hasHeader, delimiter).parse();
    }

    /**
     * <p>Returns a CsvTable instance that is representation of parsed csv table.
     * Csv table should follow RFC 4180. A comma (,) is used as a separator</p>
     * @return CsvTable instance
     * @param source is a string representation of csv table to parse
     * @param hasHeader if the first row of csb table is its header
     * @throws IncorrectInputException if source table is incorrect meaning it doesn't follow RFC 4180
     */
    public static CsvTable parse(String source, boolean hasHeader) {
        return parse(source, hasHeader, ",");
    }

    private static class CsvParserHelper extends BaseParser {
        private final String delimiter;
        private final boolean hasHeader;

        public CsvParserHelper(String source, boolean hasHeader, String delimiter) {
            super(source);
            this.hasHeader = hasHeader;
            this.delimiter = delimiter;
        }

        public CsvTable parse() {
            String[] header = null;
            int colCnt = -1;
            if (hasHeader) {
                header = parseRow();
                colCnt = header.length;
            }
            ArrayList<String[]> table = new ArrayList<>();
            while (hasNext()) {
                String[] row = parseRow();
                table.add(row);
                if (colCnt == -1) {
                    colCnt = row.length;
                } else if (colCnt != row.length) {
                    throw new IncorrectInputException("Rows have different number of columns");
                }
            }
            return new CsvTable(header, table.toArray(String[][]::new));
        }

        private String[] parseRow() {
            ArrayList<String> row = new ArrayList<>();
            while(!testEndOfLine()) {
                row.add(parseCell());
                if (!testEndOfLine()) {
                    take(delimiter);
                }
            }
            takeEndOfLine();
            return row.toArray(String[]::new);
        }

        private String parseCell() {
            String res;
            if (take('\"')) {
                res = parseValue(true);
                if (!take('\"')) {
                    throw new IncorrectInputException(
                            String.format("Missing closing quote: Expected '\"' but found '%s'", getCurrentChar())
                    );
                }
            } else {
                res = parseValue(false);
                if (take('\"')) {
                    throw new IncorrectInputException("Missing opening quote");
                }
            }
            return res;
        }

        private boolean endOfValue(boolean escaped) {
            return test('\"') && !test("\"\"") || test(delimiter) || !escaped && testEndOfLine();
        }

        private String parseValue(boolean escaped) {
            StringBuilder res = new StringBuilder();
            while (!endOfValue(escaped)) {
                if (take("\"\"")) {
                    res.append('\"');
                } else {
                    res.append(next());
                }
            }
            return res.toString();
        }
    }
}
